package com.example.joyrun.planpage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyrun.R
import com.example.joyrun.adapter.RunningEventAdapter
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.databinding.FragmentPlanBinding
import com.example.joyrun.db.RunningEventDatabase
import com.example.joyrun.planpage.viewmodel.PlanViewModel
import com.example.joyrun.planpage.viewmodel.PlanViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.ZoneId

class PlanFragment : Fragment() {
    private lateinit var binding: FragmentPlanBinding
    private lateinit var adapter: RunningEventAdapter
    private lateinit var viewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RunningEventAdapter() { runningEvent ->
            showDeleteDialog(runningEvent)
        }
        binding.rvRunningEvents.adapter = adapter
        binding.rvRunningEvents.layoutManager = LinearLayoutManager(requireContext())

        val dao = RunningEventDatabase.Companion.getInstance(requireContext()).runningEventDao()
        val factory = PlanViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[PlanViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.allRunningEvents.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.calenderPlan.setOnCalendarChangedListener { year, month, localDate, dateChangeBehavior ->
            val zoneId = ZoneId.systemDefault()
            val startOfDay = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val endOfDay = localDate.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

            viewModel.loadEventsByDate(startOfDay, endOfDay)
        }
    }

    private fun showDeleteDialog(event: RunningEvent) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
            .setTitle("删除记录")
            .setMessage("确定要删除这条运动记录吗？")
            .setPositiveButton("确定") { _, _ ->
                viewModel.deleteEvent(event)
            }.setNegativeButton("取消", null)
            .show()
    }
}