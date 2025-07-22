package com.example.joyrun.personalpage.view

import android.content.Context
import android.icu.text.Transliterator
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.R
import com.example.joyrun.databinding.FragmentPersonalBinding
import com.example.joyrun.db.RunningEventDatabase
import com.example.joyrun.personalpage.viewmodel.PersonalModelFactory
import com.example.joyrun.personalpage.viewmodel.PersonalViewModel
import com.example.joyrun.utils.FormatUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PersonalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalBinding
    private lateinit var viewModel: PersonalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalBinding.inflate(inflater)
        val dao = RunningEventDatabase.getInstance(requireContext()).runningEventDao()
        val factory = PersonalModelFactory(dao, requireContext())
        viewModel = ViewModelProvider(this, factory)[PersonalViewModel::class.java]
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addKeyboardVisibilityListener { isVisible ->
            if (isVisible) {
                binding.imgPersonalAvatar.visibility = View.GONE
            } else {
                binding.imgPersonalAvatar.visibility = View.VISIBLE
            }
        }
        // 总里程
        viewModel.totalDistance.observe(viewLifecycleOwner) {
            binding.tvTotalDistance.text = FormatUtils.formatDistanceWithoutKm(it)
        }
        // 今日里程
        viewModel.todayDistance.observe(viewLifecycleOwner) {
            binding.tvTodayDistance.text = FormatUtils.formatDistanceWithoutKm(it)
        }
        // 年里程
        viewModel.yearDistance.observe(viewLifecycleOwner) { distance ->
            val target = viewModel.targetYearDistance.value ?: 1200
            updateYearData(distance, target)
        }
        viewModel.targetYearDistance.observe(viewLifecycleOwner) { target ->
            val distance = viewModel.yearDistance.value ?: 120f
            updateYearData(distance, target)
        }

        // 月里程
        viewModel.monthDistance.observe(viewLifecycleOwner) { distance ->
            val target = viewModel.targetMonthDistance.value ?: 120
            updateMonthData(distance, target)
        }
        viewModel.targetMonthDistance.observe(viewLifecycleOwner) { target ->
            val distance = viewModel.monthDistance.value ?: 0f
            updateMonthData(distance, target)
        }

        // 用户名
        viewModel.userName.observe(viewLifecycleOwner) {
            updateUserName(it)
        }

        binding.tvUsername.setOnClickListener {
            val editText = EditText(context).apply {
                hint = "请输入姓名"
                inputType = InputType.TYPE_CLASS_TEXT
                filters = arrayOf(InputFilter.LengthFilter(10))
            }

            MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
                .setTitle("修改用户名")
                .setView(editText)
                .setPositiveButton("确定") { _, _ ->
                    val input = editText.text.toString().trim()
                    if (input.isBlank()) {
                        Toast.makeText(requireContext(), "请输入有效的姓名", Toast.LENGTH_SHORT)
                            .show()
                        return@setPositiveButton
                    }
                    viewModel.saveUserName(input)
                }
                .setNegativeButton("取消", null)
                .show()
        }

        binding.cvYearChange.setOnClickListener {
            val editText = EditText(context).apply {
                hint = "请输入 0~5000km 内目标"
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(4))
            }

            MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
                .setTitle("请输入年度目标")
                .setView(editText)
                .setPositiveButton("确定") { dialog, _ ->
                    val input = editText.text.toString()
                    val target = input.toIntOrNull()

                    if (target == null) {
                        Toast.makeText(requireContext(), "请输入有效的数字", Toast.LENGTH_SHORT)
                            .show()
                    } else if (target < 1) {
                        Toast.makeText(requireContext(), "目标不能小于1", Toast.LENGTH_SHORT).show()
                    } else if (target > 5000) {
                        Toast.makeText(requireContext(), "目标不能大于5000", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.saveTargetYearDistance(target)
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }

        binding.cvMonthChange.setOnClickListener {
            val editText = EditText(context).apply {
                hint = "请输入 0~1000km 内目标"
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(4))
            }

            MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialog)
                .setTitle("请输入月度目标")
                .setView(editText)
                .setPositiveButton("确定") { dialog, which ->
                    val input = editText.text.toString()
                    val target = input.toIntOrNull()

                    if (target == null) {
                        Toast.makeText(requireContext(), "请输入有效的数字", Toast.LENGTH_SHORT)
                            .show()
                    } else if (target < 1) {
                        Toast.makeText(requireContext(), "目标不能小于1", Toast.LENGTH_SHORT).show()
                    } else if (target > 1000) {
                        Toast.makeText(requireContext(), "目标不能大于1000", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.saveTargetMonthDistance(target)
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    private fun updateUserName(input: String) {
        val transliterator = Transliterator.getInstance("Han-Latin/Names; Latin-ASCII")
        val pinyin = transliterator.transliterate(input)

        val parts = pinyin.split(" ").filter { it.isNotBlank() }

        val formattedName = if (parts.size >= 2) {
            val lastName = parts[0].replaceFirstChar { it.uppercase() }
            val givenName = parts.subList(1, parts.size)
                .joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
            "$givenName\n$lastName"
        } else {
            input
        }
        binding.tvUsername.text = formattedName
    }

    private fun updateYearData(distance: Float, target: Int) {
        binding.tvYearCompletedDistance.text =
            FormatUtils.formatDistanceByIntWithoutKm(distance)
        binding.tvYearTargetDistance.text = target.toString()
        if (distance != 0f) {
            val progress = ((distance / 1000f) / target * 100).toInt()

            val yearProgress = FormatUtils.getCurrentYearPercent()
            val expectDistance = (target * yearProgress / 100).toInt()
            binding.tvYearExpectDistance.text = expectDistance.toString()
            binding.progressYearPlan.setProgress(progress)
            binding.progressYearPlan.setTargetProgress(yearProgress.toInt())
        } else {
            binding.progressYearPlan.setProgress(0)
            binding.progressYearPlan.setTargetProgress(0)
        }
    }

    private fun updateMonthData(monthDistance: Float, target: Int) {
        binding.tvMonthCompletedDistance.text =
            FormatUtils.formatDistanceByIntWithoutKm(monthDistance)
        binding.tvMonthTargetDistance.text =
            buildString {
                append(target)
                append(" km")
            }
        if (monthDistance != 0f) {
            val progress = ((monthDistance / 1000f) / target * 100).toInt()
            binding.progressMonthPlan.setProgress(progress, true)
            binding.tvMonthCompleted.text =
                buildString {
                    append("已完成 ")
                    append(progress)
                    append("%")
                }
        } else {
            binding.progressMonthPlan.setProgress(0, true)

        }
    }

    fun Fragment.addKeyboardVisibilityListener(onChanged: (Boolean) -> Unit) {
        val rootView = requireActivity().findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height
            val isKeyboardVisible = heightDiff > 200.dpToPx(requireContext())
            onChanged(isKeyboardVisible)
        }
    }

    fun Int.dpToPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()


}