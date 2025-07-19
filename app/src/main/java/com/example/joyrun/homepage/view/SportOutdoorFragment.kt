package com.example.joyrun.homepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapsInitializer
import com.amap.api.maps2d.model.MyLocationStyle
import com.example.joyrun.databinding.FragmentSportOutdoorBinding


class SportOutdoorFragment : Fragment() {
    private lateinit var binding: FragmentSportOutdoorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSportOutdoorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sportMap.onCreate(savedInstanceState)

        val aMap = binding.sportMap.map
        // 打开时进行定位
        val myLoactionStyle = MyLocationStyle()
        myLoactionStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        myLoactionStyle.interval(2000)
        aMap.setMyLocationStyle(myLoactionStyle)
        aMap.isMyLocationEnabled = true
        MapsInitializer.getUpdateDataActiveEnable()
        aMap.setOnMapLoadedListener {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        }
        aMap.uiSettings.isScrollGesturesEnabled = false      // 禁止滑动
        aMap.uiSettings.isZoomGesturesEnabled = false        // 禁止缩放

    }

    override fun onResume() {
        super.onResume()
        binding.sportMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.sportMap.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.sportMap.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.sportMap.onSaveInstanceState(outState)
    }


}