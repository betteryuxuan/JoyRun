package com.example.joyrun

import android.app.Application
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps2d.MapsInitializer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化高德地图
//        MapsInitializer.setPrivacyState(this, true, true)
//        AMapLocationClient.setPrivacyState(this, true, true)
    }
}