package com.example.joyrun.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.joyrun.homepage.view.SportIndoorFragment
import com.example.joyrun.homepage.view.SportOutdoorFragment

class SportVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        SportOutdoorFragment(),
        SportIndoorFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getIndoorDistance(): Float {
        return (fragments[1] as SportIndoorFragment).getDistance()
    }

}