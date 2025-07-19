package com.example.joyrun.chatpage.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyrun.adapter.MsgAdapter
import com.example.joyrun.bean.Msg
import com.example.joyrun.chatpage.ChatViewModel
import com.example.joyrun.databinding.FragmentChatBinding
import com.example.joyrun.utils.SoftHideKeyBoardUtil

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: MsgAdapter
    private val msgList: MutableList<Msg> by lazy {
        mutableListOf<Msg>().apply {
            add(Msg("Hello, this is a test message.", "10:30", Msg.TYPE_RECEIVED))
        }
    }
    private var isRequestInProgress = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        SoftHideKeyBoardUtil.assistActivity(requireActivity())
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())
        if (msgList.size > 3)
            layoutManager.setStackFromEnd(true)
        adapter = MsgAdapter(msgList)
        binding.msgRecyclerView.layoutManager = layoutManager
        binding.msgRecyclerView.adapter = adapter

        binding.imgSend.setOnClickListener({ v ->
            val content: String? = binding.etText.getText().toString()
            if (vailInput()) {
                binding.etText.setText("")
                isRequestInProgress = true
                viewModel.sendMessage(content)
            } else {
                showToast("正在回答中")
            }
        })
    }

    fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun vailInput(): Boolean {
        return !(msgList.get(msgList.size - 1).type === Msg.TYPE_THINKING ||
                msgList.get(msgList.size - 1).type === Msg.TYPE_SENT)
    }

    fun stopRequest() {
        isRequestInProgress = false
    }

}