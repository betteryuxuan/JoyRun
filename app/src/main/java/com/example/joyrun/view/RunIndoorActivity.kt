package com.example.joyrun.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.DAO.RunningEventDatabase
import com.example.joyrun.R
import com.example.joyrun.databinding.ActivityRunIndoorBinding
import com.example.joyrun.utils.FormatUtils
import com.example.joyrun.viewmodel.RunIndoorViewModel
import com.example.joyrun.viewmodel.RunIndoorViewModelFactory
import kotlin.math.sqrt

class RunIndoorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunIndoorBinding
    private lateinit var viewModel: RunIndoorViewModel
    private var isRunningStarted = false

    private val countdownNumbers = listOf("3", "2", "1", "开始")
    private var index = 0
    private val handler = Handler(Looper.getMainLooper())
    private val countdownRunnable = object : Runnable {
        override fun run() {
            if (index < countdownNumbers.size) {
                binding.tvCountdown.text = countdownNumbers[index]
                playCountdownAnimation(binding.tvCountdown)
                index++
                handler.postDelayed(this, 1000)
            } else {
                onCountdownFinished()
            }
        }
    }
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private var lastStepTime = 0L

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val acceleration = sqrt(x * x + y * y + z * z)

            val now = System.currentTimeMillis()
            if (acceleration > 12f && now - lastStepTime > 300) {
                lastStepTime = now
                viewModel.onStepDetected()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRunIndoorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = RunningEventDatabase.getInstance(this).runningEventDao()
        val factory = RunIndoorViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[RunIndoorViewModel::class.java]
        binding.lifecycleOwner = this

        val distance = intent.getFloatExtra("distance", 0f)
        binding.targetDistance.setText(String.format("目标距离: %.2f 公里", distance))

        binding.countdownOverlay.visibility = View.VISIBLE
        binding.rootRunindoor.visibility = View.GONE
        handler.post(countdownRunnable)

        viewModel.currentPace.observe(this) {
            binding.tvIndoorPace.text = it
        }
        viewModel.duration.observe(this) {
            binding.tvIndoorTime.text = FormatUtils.formatDurationWithoutHour(it)
        }
        viewModel.runningEvent.observe(this) {
            binding.tvIndoorTotalDistance.text = "%.2f".format(it.totalDistance / 1000f)
        }

        binding.imgIndoorPauseContinue.setOnClickListener { view ->
            if (!isRunningStarted) {
                scaleAnimator(view)
                return@setOnClickListener
            }
            if (viewModel.isRunning) {
                viewModel.pauseRunning()
                binding.imgIndoorPauseContinue.setImageResource(R.drawable.ic_continue)
            } else {
                viewModel.continueRunning()
                binding.imgIndoorPauseContinue.setImageResource(R.drawable.ic_pause)
            }
            scaleAnimator(view)
        }

        binding.imgIndoorFinish.setOnClickListener { view ->
            if (!isRunningStarted) {
                scaleAnimator(view)
                return@setOnClickListener
            }
            if (viewModel.stopRunning()) {
                Toast.makeText(this, "运动结束", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "运动时间或距离过短，未保存", Toast.LENGTH_SHORT).show()
            }
            scaleAnimator(view)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        sensorManager.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(sensorListener)
    }

    private fun scaleAnimator(view: View) {
        view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
            view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }

    private fun playCountdownAnimation(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f)
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
            duration = 600
            start()
        }
    }

    private fun onCountdownFinished() {
        Toast.makeText(this, "开始运动！", Toast.LENGTH_SHORT).show()
        ObjectAnimator.ofFloat(binding.tvCountdown, "alpha", 1f, 0f).apply {
            duration = 200
            start()
        }
        handler.postDelayed({
            binding.countdownOverlay.visibility = View.GONE
            binding.rootRunindoor.visibility = View.VISIBLE
        }, 200)
        viewModel.startRunning()
        isRunningStarted = true
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(countdownRunnable)
    }

    override fun onBackPressed() {
        // 禁止返回
    }
}
