package com.example.joyrun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.joyrun.R
import com.example.joyrun.bean.Msg

class MsgAdapter(private var msgList: MutableList<Msg>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_RECEIVED = Msg.TYPE_RECEIVED
        private const val VIEW_TYPE_SENT = Msg.TYPE_SENT
    }

    override fun getItemCount(): Int = msgList.size

    override fun getItemViewType(position: Int): Int {
        return when (msgList[position].type) {
            Msg.TYPE_RECEIVED -> VIEW_TYPE_RECEIVED
            Msg.TYPE_SENT -> VIEW_TYPE_SENT
            else -> VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val view = inflater.inflate(R.layout.item_msg_right, parent, false)
                SentViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_msg_left, parent, false)
                ReceivedViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is ReceivedViewHolder -> holder.bind(msg)
            is SentViewHolder -> holder.bind(msg)
        }
    }

    fun updateData(newList: List<Msg>) {
        msgList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun addMessage(msg: Msg) {
        msgList.add(msg)
        notifyItemInserted(msgList.size - 1)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val leftLayout: LinearLayout = itemView.findViewById(R.id.item_message_left)
        private val leftMsg: TextView = itemView.findViewById(R.id.left_message)
        private val leftTime: TextView = itemView.findViewById(R.id.left_time)

        fun bind(msg: Msg) {
            leftLayout.visibility = View.VISIBLE
            leftMsg.text = msg.content
            leftTime.text = msg.time
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rightLayout: LinearLayout = itemView.findViewById(R.id.item_message_right)
        private val rightMsg: TextView = itemView.findViewById(R.id.right_message)
        private val rightTime: TextView = itemView.findViewById(R.id.right_time)

        fun bind(msg: Msg) {
            rightLayout.visibility = View.VISIBLE
            rightMsg.text = msg.content
            rightTime.text = msg.time
        }
    }
}
