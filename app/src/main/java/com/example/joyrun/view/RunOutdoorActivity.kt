package com.example.joyrun.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapsInitializer
import com.amap.api.maps2d.model.MyLocationStyle
import com.amap.api.maps2d.model.PolylineOptions
import com.example.joyrun.DAO.RunningEventDatabase
import com.example.joyrun.R
import com.example.joyrun.databinding.ActivityRunOutdoorBinding
import com.example.joyrun.utils.FormatUtils
import com.example.joyrun.viewmodel.RunOutdoorViewModel
import com.example.joyrun.viewmodel.RunOutdoorViewModelFactory

class RunOutdoorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunOutdoorBinding
    private lateinit var viewModel: RunOutdoorViewModel
    private lateinit var aMap: AMap
    private var isRunningStarted = false // 用于按钮控制

    //倒计时处理
    private val countdownNumbers = listOf("3", "2", "1", "开始")
    private var index = 0
    private val handler = Handler(Looper.getMainLooper())
    private val countdownRunnable = object : Runnable {
        override fun run() {
            if (index < countdownNumbers.size) {
                binding.tvRun.text = countdownNumbers[index]
                playAnimation(binding.tvRun)
                index++
                handler.postDelayed(this, 1000)
            } else {
                onCountdownFinished()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRunOutdoorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = RunningEventDatabase.getInstance(this).runningEventDao()
        val factory = RunOutdoorViewModelFactory(dao)

        viewModel = ViewModelProvider(this, factory)[RunOutdoorViewModel::class.java]
        binding.lifecycleOwner = this

        binding.mapRun.onCreate(savedInstanceState)
        initMap()
        handler.post(countdownRunnable)
        viewModel.currentPace.observe(
            this, { pace ->
                binding.tvPace.text = pace
//                Log.d("location", "实时速度: $pace min/km")
            })
        viewModel.duration.observe(
            this, { it ->
                binding.tvTotalDuration.text = FormatUtils.formatDurationWithoutHour(it)
            })
        binding.imgPauseContinue.setOnClickListener { view ->
            if (!isRunningStarted) {
                scaleAnimator(view)
                return@setOnClickListener
            }
            if (viewModel.isRunning) {
                viewModel.pauseRunning()
                binding.imgPauseContinue.setImageResource(R.drawable.ic_continue)
            } else {
                viewModel.continueRunning()
                binding.imgPauseContinue.setImageResource(R.drawable.ic_pause)
            }
            scaleAnimator(view)
        }
        binding.imgFinish.setOnClickListener { view ->
            if (!isRunningStarted) {
                scaleAnimator(view)
                return@setOnClickListener
            }
            if (!isRunningStarted) return@setOnClickListener
            if (viewModel.stopRunning()) {
                Toast.makeText(this, "运动结束", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "运动时间或距离过短，记录不保存", Toast.LENGTH_SHORT).show()
            }
            scaleAnimator(view)
            finish()
        }
        viewModel.runningEvent.observe(
            this, { runningEvent ->
                binding.tvDistance.text = "%.2f".format(runningEvent.totalDistance / 1000f)
                // 绘制路径
                if (runningEvent.pathPoints.size >= 2) {
                    val polylineOptions =
                        PolylineOptions().addAll(runningEvent.pathPoints).width(10f)
                            .color(0xFF24C68A.toInt())
                    aMap.addPolyline(polylineOptions)
                }
//                Log.d("location", "路径:{$runningEvent.pathPoints}")
//                Log.d(
//                    "location",
//                    "累计距离: ${runningEvent.totalDistance} 米 "
//                )
            })
    }

    private fun scaleAnimator(view: View) {
        val scaleDownUp = AnimatorSet().apply {
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.9f)
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.9f)
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1f)
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1f)

            playSequentially(AnimatorSet().apply {
                playTogether(scaleDownX, scaleDownY)
                duration = 100
            }, AnimatorSet().apply {
                playTogether(scaleUpX, scaleUpY)
                duration = 100
            })
        }
        scaleDownUp.start()

    }

    private fun initMap() {
        aMap = binding.mapRun.map
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        myLocationStyle.interval(3000)
        aMap.setMyLocationStyle(myLocationStyle)
        aMap.isMyLocationEnabled = true
        MapsInitializer.getUpdateDataActiveEnable()
        aMap.setOnMapLoadedListener {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        }
    }

    private val scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f)
    private val scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f)
    private val alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
    private fun playAnimation(view: android.view.View) {
        ObjectAnimator.ofPropertyValuesHolder(view, scaleXHolder, scaleYHolder, alphaHolder).apply {
            duration = 600
            start()
        }
    }

    private fun onCountdownFinished() {
        Toast.makeText(this, "开始运动！", Toast.LENGTH_SHORT).show()
        ObjectAnimator.ofFloat(binding.tvRun, "alpha", 1f, 0f).apply {
            duration = 200
            start()
        }
        handler.postDelayed({
            binding.tvRun.visibility = android.view.View.GONE
        }, 200)
        startRunning()
        isRunningStarted = true
    }

    private lateinit var locationClient: AMapLocationClient
    private lateinit var locationOption: AMapLocationClientOption
    private fun startRunning() {
        locationClient = AMapLocationClient(applicationContext)
        locationOption = AMapLocationClientOption().apply {
            locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Sport
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = false
            isMockEnable = true
            interval = 3000
        }
        locationClient.setLocationOption(locationOption)
        aMap.setOnMyLocationChangeListener {
//            Toast.makeText(this, "定位: ${it.latitude}, ${it.longitude}", Toast.LENGTH_SHORT).show()
//            Log.d("location", "location: {${it.latitude}, ${it.longitude}}")
        }

        locationClient.setLocationListener { amapLocation ->
            if (amapLocation != null && amapLocation.errorCode == 0) {
                viewModel.handleLocationUpdate(amapLocation)

            } else {
//                Toast.makeText(this, "定位失败: ${amapLocation?.errorInfo}", Toast.LENGTH_SHORT).show()
                Log.e(
                    "location",
                    "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo()
                );
            }
        }

        locationClient.startLocation()
        viewModel.startRunning()
    }

    override fun onResume() {
        super.onResume()
        binding.mapRun.onResume()
    }

    override fun onBackPressed() {
        // 禁止返回键
    }

    override fun onPause() {
        super.onPause()
        binding.mapRun.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapRun.onDestroy()
        handler.removeCallbacks(countdownRunnable)
        if (::locationClient.isInitialized) {
            locationClient.stopLocation()
            locationClient.onDestroy()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapRun.onSaveInstanceState(outState)
    }
}
