package com.example.joyrun.homepage.view

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.joyrun.R
import com.example.joyrun.adapter.MainVPAdapter
import com.example.joyrun.databinding.ActivityMainBinding
import com.example.joyrun.homepage.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this //让 LiveData 能自动更新界面

        val vpAdapter = MainVPAdapter(this)
        binding.mainVp.adapter = vpAdapter
        binding.bottomNavigation.setupWithViewPager2(binding.mainVp)
        binding.mainVp.isUserInputEnabled = false

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            val v = getCurrentFocus()
            // 判断当前焦点是否为EditText
            if (v is EditText) {
                // 将outRect设置为视图根视图坐标空间中该视图的非剪裁区域的坐标,即 EditText 在屏幕上的可见区域
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                // 触摸点不在 EditText 的可见区域
                if (!outRect.contains(ev.getRawX().toInt(), ev.getRawY().toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}