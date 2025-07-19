package com.example.joyrun.homepage.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.joyrun.custom.LoopWheelDialogFragment
import com.example.joyrun.databinding.FragmentSportIndoorBinding

class SportIndoorFragment : Fragment() {
    private lateinit var binding: FragmentSportIndoorBinding
    private var currentDistance: Float = 5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSportIndoorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDistance.setOnClickListener {
            val dialog = LoopWheelDialogFragment(
                onDistanceSelected = {
                    currentDistance = it
                    binding.tvDistance.text = "目标距离 ${it} 公里"
                }, currentDistance
            )
            dialog.show(parentFragmentManager, "LoopWheelDialog")
        }
    }
    fun getDistance(): Float {
        return currentDistance
    }

}