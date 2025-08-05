package com.example.joyrun.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt

class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progress = 0
    private var targetProgress = 100

    // 圆环的宽度
    private val strokeWidth = 65f
    private val bgColor = "#e7dfea".toColorInt() // 背景圆环
    private val progressColor = "#665490".toColorInt() // 高亮圆环
    private val textColor = progressColor

    // 背景圆环画笔
    private val bgPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = bgColor
        strokeWidth = this@CircleProgressView.strokeWidth
        isAntiAlias = true
    }

    // 已完成进度圆环画笔
    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = progressColor
        strokeWidth = this@CircleProgressView.strokeWidth
        isAntiAlias = true
    }

    // 当前进度文字画笔
    private val textPaint = Paint().apply {
        color = textColor
        textSize = 100f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    // 圆角矩形背景
    private val labelPaint = Paint().apply {
        color = "#EFEAFA".toColorInt()
        isAntiAlias = true
    }

    // 预期进度文字画笔
    private val labelTextPaint = Paint().apply {
        color = textColor
        textSize = 50f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
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

        // 目标进度圆弧
        val targetSweepAngle = 360f * targetProgress / 100f
        val targetPaint = Paint(progressPaint).apply {
            color = "#99C1B8E4".toColorInt()
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(rect, -90f, targetSweepAngle, false, targetPaint)

        // 当前进度圆弧
        val sweepAngle = 360f * progress / 100f
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)

        // 当前进度百分比文字
        val progressText = "$progress%"
        // 保证文字居中，下面这一步用中心的位置加上文字一半高度（文字的距离中心位置上面和下面的差/2）
        val textBaseline = centerY - ((textPaint.descent() + textPaint.ascent()) / 2)
        // drawText的y是文字基线，是文字的中心（这里我在向上偏点，下面还有文字，做到视觉居中）
        canvas.drawText(progressText, centerX, textBaseline - 25f, textPaint)

        // 底部预期的背景
        val labelText = "预期 $targetProgress%"
        val labelPaddingH = 32f
        val labelPaddingV = 32f
        val labelTextWidth = labelTextPaint.measureText(labelText)

        val labelOffset = 70f // 距离圆心的垂直偏移，向上的margin
        val labelRect = RectF(
            centerX - labelTextWidth / 2 - labelPaddingH,
            centerY + labelOffset,
            centerX + labelTextWidth / 2 + labelPaddingH,
            centerY + labelOffset + labelTextPaint.textSize + labelPaddingV
        )

        canvas.drawRoundRect(labelRect, 60f, 60f, labelPaint)

        // 预期进度文字
        val labelFontMetrics = labelTextPaint.fontMetrics
        val labelBaseline =
            labelRect.centerY() - (labelFontMetrics.ascent + labelFontMetrics.descent) / 2
        canvas.drawText(labelText, centerX, labelBaseline, labelTextPaint)
    }
}
