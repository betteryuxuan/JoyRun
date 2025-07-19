package com.example.joyrun.custom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.joyrun.R
import com.weigan.loopview.LoopView

class LoopWheelDialogFragment(
    private val onDistanceSelected: (Float) -> Unit, val currentDistance: Float
) : DialogFragment() {

    private val floatList = mutableListOf(
        0.5f, 1.0f, 1.5f, 2.0f, 2.5f,
        3.0f, 3.5f, 4.0f, 4.5f, 5.0f,
        5.5f, 6.0f, 6.5f, 7.0f, 7.5f,
        8.0f, 8.5f, 9.0f, 10.0f, 10.5f,
        11.0f, 11.5f, 12.0f, 12.5f, 13.0f,
        13.5f, 14.0f, 14.5f, 15.0f
    )

    private val stringList = floatList.map { String.format("%.1f 公里", it) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_loop_wheel, null)

        val loopView = dialogView.findViewById<LoopView>(R.id.loopView)
        val confirmBtn = dialogView.findViewById<TextView>(R.id.btnConfirm)

        loopView.setItems(stringList)
        loopView.setInitPosition(stringList.indexOf("$currentDistance 公里"))
        loopView.setNotLoop()
        loopView.setTextSize(20F)

        confirmBtn.setOnClickListener {
            val index = loopView.selectedItem
            onDistanceSelected(floatList[index])
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
    }
}
