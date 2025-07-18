package com.example.joyrun.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amap.api.maps2d.model.LatLng
import com.example.joyrun.databinding.FragmentPathPreviewBinding

class PathPreviewFragment : Fragment() {
    private lateinit var binding: FragmentPathPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPathPreviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ppv.setPath(listOf(LatLng(34.154805,108.905664), LatLng(34.154855,108.905666), LatLng (34.154907,108.905669),  LatLng(34.154956,108.905672), LatLng(34.154956,108.905673), LatLng (34.154957,108.90567), LatLng(34.154956,108.905669), LatLng (34.154957,108.905671), LatLng (34.154955,108.905673),  LatLng(34.154956,108.905673), LatLng (34.154992,108.905634), LatLng (34.155013,108.905616)))
    }

}