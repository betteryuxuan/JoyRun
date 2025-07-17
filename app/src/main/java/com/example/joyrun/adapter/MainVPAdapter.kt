package com.example.joyrun.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.joyrun.view.PathPreviewFragment
import com.example.joyrun.view.PlanFragment
import com.example.joyrun.view.SportFragment

class MainVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        SportFragment(),
        PlanFragment(),
        PathPreviewFragment(),
        PlanFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}