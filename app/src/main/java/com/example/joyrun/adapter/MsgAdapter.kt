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
    RecyclerView.Adapter<MsgAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_msg, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val msg = msgList[position]

        // 设置第一个 item 的顶部 margin
        val layoutParams = holder.itemView.layoutParams as? ViewGroup.MarginLayoutParams
        if (layoutParams != null) {
            if (position == 0) {
                layoutParams.topMargin = dpToPx(holder.itemView.context, 35)
            } else {
                layoutParams.topMargin = 0
            }
            holder.itemView.layoutParams = layoutParams
        }

        when (msg.type) {
            Msg.TYPE_RECEIVED -> {
                holder.leftLayout.visibility = View.VISIBLE
                holder.rightLayout.visibility = View.GONE
                holder.thinkingLayout.visibility = View.GONE
                holder.leftMsg.text = msg.content
                holder.leftTime.text = msg.time
            }

            Msg.TYPE_SENT -> {
                holder.leftLayout.visibility = View.GONE
                holder.rightLayout.visibility = View.VISIBLE
                holder.thinkingLayout.visibility = View.GONE
                holder.rightMsg.text = msg.content
                holder.rightTime.text = msg.time
            }

            Msg.TYPE_THINKING -> {
                holder.leftLayout.visibility = View.GONE
                holder.rightLayout.visibility = View.GONE
                holder.thinkingLayout.visibility = View.VISIBLE
            }
        }
    }

    fun dpToPx(context: android.content.Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun getItemCount(): Int = msgList.size

    fun setMsgList(msgList: MutableList<Msg>) {
        this.msgList = msgList
    }

    fun addMessage(msg: Msg) {
        msgList.add(msg)
    }

    fun updateData(newList: List<Msg>) {
        msgList = newList.toMutableList()
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftLayout: LinearLayout = itemView.findViewById(R.id.item_message_left)
        val rightLayout: LinearLayout = itemView.findViewById(R.id.item_message_right)
        val thinkingLayout: LinearLayout = itemView.findViewById(R.id.item_message_thinking)
        val leftMsg: TextView = itemView.findViewById(R.id.left_message)
        val rightMsg: TextView = itemView.findViewById(R.id.right_message)
        val leftTime: TextView = itemView.findViewById(R.id.left_time)
        val rightTime: TextView = itemView.findViewById(R.id.right_time)
    }
}
