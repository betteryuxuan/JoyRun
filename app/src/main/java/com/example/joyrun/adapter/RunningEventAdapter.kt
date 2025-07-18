package com.example.joyrun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.databinding.ItemRunningEventBinding

class RunningEventAdapter(private val onItemLongClick: (RunningEvent) -> Unit) :
    ListAdapter<RunningEvent, RunningEventAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRunningEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemLongClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemRunningEventBinding,
        private val onItemLongClick: (RunningEvent) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: RunningEvent) {
            binding.event = event
            binding.ppvPlan.setPath(event.pathPoints)
            binding.executePendingBindings()

            binding.root.setOnLongClickListener {
                onItemLongClick(event)
                true
            }
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
