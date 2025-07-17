package com.example.joyrun.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.joyrun.R
import com.example.joyrun.adapter.SportVPAdapter
import com.example.joyrun.custom.LoopWheelDialogFragment
import com.example.joyrun.databinding.FragmentSportBinding
import com.example.joyrun.viewmodel.SportViewModel


class SportFragment : Fragment() {
    private lateinit var viewModel: SportViewModel
    private var _binding: FragmentSportBinding? = null
    private val binding get() = _binding!!
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                Log.d("权限请求", "${it.key} = ${it.value}")
                it.value
            }
            if (granted) {
                checkLocationPermission()
            } else {
                Toast.makeText(context, "定位权限被拒绝", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this).get(SportViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDistance.setOnClickListener {
            val dialog = LoopWheelDialogFragment(
                onDistanceSelected = {
                    viewModel.targetDistance = it
                    binding.tvDistance.text = "目标距离 ${it} 公里"
                }, viewModel.targetDistance
            )
            dialog.show(parentFragmentManager, "LoopWheelDialog")
        }

        binding.navigationSportType.setItemSelected(R.id.run_outdoor, true)
        binding.navigationSportType.setOnItemSelectedListener { id ->
            when (id) {
                R.id.run_outdoor -> {
                    binding.vpSport.setCurrentItem(0, true)
                }

                R.id.run_indoor -> {
                    binding.vpSport.setCurrentItem(1, true)
                }
            }
        }
        binding.vpSport.adapter = SportVPAdapter(requireActivity())
        binding.vpSport.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navigationSportType.setItemSelected(
                    when (position) {
                        0 -> R.id.run_outdoor
                        else -> R.id.run_indoor
                    }, true
                )
            }
        })
        binding.vpSport.offscreenPageLimit =1

        binding.imgSport.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        val foregroundPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val foregroundGranted = foregroundPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (!foregroundGranted) {
            // 第一步：申请前台定位权限
            locationPermissionLauncher.launch(foregroundPermissions)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 第二步：已获得前台权限，单独申请后台权限
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            } else {
                val intent = Intent(context, CountDownAndRunActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(context, CountDownAndRunActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}