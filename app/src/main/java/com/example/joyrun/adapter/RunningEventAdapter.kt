package com.example.joyrun.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.maps2d.model.LatLng
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.custom.PathPreviewView
import com.example.joyrun.databinding.ItemRunningEventBinding

class RunningEventAdapter :
    ListAdapter<RunningEvent, RunningEventAdapter.ViewHolder>(DiffCallback()) {

    private lateinit var pathPreview: PathPreviewView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRunningEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRunningEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: RunningEvent) {
            binding.event = event
            binding.ppvPlan.setPath(event.pathPoints)
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RunningEvent>() {
        override fun areItemsTheSame(oldItem: RunningEvent, newItem: RunningEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RunningEvent, newItem: RunningEvent): Boolean {
            return oldItem == newItem
        }
    }
}
