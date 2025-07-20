package com.example.joyrun.chatpage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.joyrun.R
import com.example.joyrun.adapter.MsgAdapter
import com.example.joyrun.chatpage.viewmodel.ChatViewModel
import com.example.joyrun.chatpage.viewmodel.ChatViewModelFactory
import com.example.joyrun.databinding.FragmentChatBinding
import com.example.joyrun.utils.SoftHideKeyBoardUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: MsgAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SoftHideKeyBoardUtil.assistActivity(requireActivity())
        val factory = ChatViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())
        binding.msgRecyclerView.layoutManager = layoutManager
        adapter = MsgAdapter(mutableListOf())
        binding.msgRecyclerView.adapter = adapter

        viewModel.msgList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
            binding.msgRecyclerView.post {
                layoutManager.scrollToPosition(it.size - 1)
            }

        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                showToast(msg)
                viewModel.clearError()
            }
        }

        binding.imgSend.setOnClickListener {
            val content = binding.etText.text.toString().trim()
            if (content.isNotEmpty()) {
                binding.etText.setText("")
                viewModel.sendUserMessage(content)
            }
        }
        binding.fabNew.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
                .setTitle("开启新对话")
                .setMessage("确定要开启一个新的对话吗？当前对话内容将被清空哦～")
                .setPositiveButton("确定") { _, _ ->
                    viewModel.openNewChat()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveToLocal()
    }

}
