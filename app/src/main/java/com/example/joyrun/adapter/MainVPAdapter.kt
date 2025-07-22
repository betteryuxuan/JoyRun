package com.example.joyrun.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.joyrun.chatpage.view.ChatFragment
import com.example.joyrun.planpage.view.PlanFragment
import com.example.joyrun.homepage.view.SportFragment
import com.example.joyrun.personalpage.view.PersonalFragment

class MainVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        SportFragment(),
        PlanFragment(),
        ChatFragment(),
        PersonalFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}