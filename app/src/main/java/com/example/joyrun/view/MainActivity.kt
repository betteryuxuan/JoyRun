package com.example.joyrun.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.joyrun.adapter.MainVPAdapter
import com.example.joyrun.R
import com.example.joyrun.databinding.ActivityMainBinding
import com.example.joyrun.viewmodel.MainViewModel

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
}