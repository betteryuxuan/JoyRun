package com.example.joyrun.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyrun.DAO.RunningEventDatabase
import com.example.joyrun.R
import com.example.joyrun.databinding.FragmentPlanBinding
import com.example.joyrun.viewmodel.CountDownViewModel
import com.example.joyrun.viewmodel.CountDownViewModelFactory
import com.example.joyrun.viewmodel.PlanViewModel
import com.example.joyrun.viewmodel.PlanViewModelFactory

class PlanFragment : Fragment() {
    private lateinit var binding: FragmentPlanBinding
    private lateinit var adapter: RunningEventAdapter
    private lateinit var viewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RunningEventAdapter()
        binding.rvRunningEvents.adapter = adapter
        binding.rvRunningEvents.layoutManager = LinearLayoutManager(requireContext())

        val dao = RunningEventDatabase.getInstance(requireContext()).runningEventDao()
        val factory = PlanViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[PlanViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.allRunningEvents.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
