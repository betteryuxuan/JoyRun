package com.example.joyrun.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progress = 0
    private var targetProgress = 100

    private val strokeWidth = 60f
    private val bgColor = Color.parseColor("#e7dfea") // 背景圆环
    private val progressColor = Color.parseColor("#665490") // 高亮圆环
    private val textColor = progressColor

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = bgColor
        strokeWidth = this@CircleProgressView.strokeWidth
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = progressColor
        strokeWidth = this@CircleProgressView.strokeWidth
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textSize = 90f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EFEAFA") // 底部圆角矩形背景
    }

    private val labelTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    fun setProgress(value: Int) {
        progress = value
        invalidate()
    }

    fun setTargetProgress(value: Int) {
        targetProgress = value
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (minOf(width, height) - strokeWidth) / 2f - 30f

        val rect = RectF(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius
        )

        // 背景圆环（全圆）
        canvas.drawArc(rect, 0f, 360f, false, bgPaint)

        // 目标进度圆弧（浅色）
        val targetSweepAngle = 360f * targetProgress / 100f
        val targetPaint = Paint(progressPaint).apply {
            color = Color.parseColor("#99C1B8E4") // 浅紫色，透明度约60%
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(rect, -90f, targetSweepAngle, false, targetPaint)

        // 当前进度圆弧（深色）
        val sweepAngle = 360f * progress / 100f
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)

        // 中间百分比文字（当前进度）
        val progressText = "$progress%"
        val textBaseline = centerY - ((textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(progressText, centerX, textBaseline, textPaint)

        // 底部 label
        val labelText = "预期 $targetProgress%"
        val labelPaddingH = 32f
        val labelPaddingV = 16f
        val labelTextWidth = labelTextPaint.measureText(labelText)
        val labelOffset = 100f
        val labelRect = RectF(
            centerX - labelTextWidth / 2 - labelPaddingH,
            centerY + labelOffset,
            centerX + labelTextWidth / 2 + labelPaddingH,
            centerY + labelOffset + labelTextPaint.textSize + labelPaddingV
        )

        canvas.drawRoundRect(labelRect, 60f, 60f, labelPaint)

        val labelFontMetrics = labelTextPaint.fontMetrics
        val labelBaseline = labelRect.centerY() - (labelFontMetrics.ascent + labelFontMetrics.descent) / 2
        canvas.drawText(labelText, centerX, labelBaseline, labelTextPaint)
    }


}
