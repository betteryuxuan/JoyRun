package com.example.joyrun.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.amap.api.maps2d.model.LatLng
import androidx.core.graphics.toColorInt

class PathPreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // 路径点（经纬度）
    private var pathPoints: List<LatLng> = emptyList()

    // 轨迹画笔
    private val pathPaint = Paint().apply {
        color = "#57aaf7".toColorInt()
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    // 起点画笔
    private val startPaint = Paint().apply {
        color = "#2BC18B".toColorInt()
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // 终点画笔
    private val endPaint = Paint().apply {
        color = "#CC3333".toColorInt()
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // 设置路径点，调用后刷新视图
    fun setPath(points: List<LatLng>) {
        pathPoints = points
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pathPoints.size < 2) return

        // 获取经纬度边界，用于缩放到视图范围
        val bounds = getBounds(pathPoints)

        val padding = 40f // 给路径加边距
        val mapWidth = bounds.maxLng - bounds.minLng
        val mapHeight = bounds.maxLat - bounds.minLat

        val scaleX = (width - 2 * padding) / mapWidth
        val scaleY = (height - 2 * padding) / mapHeight
        val scale = minOf(scaleX, scaleY)

        // 偏移用于让路径居中
        val offsetX = padding
        val offsetY = padding

        val screenPoints = pathPoints.map {
            val x = (it.longitude - bounds.minLng) * scale + offsetX
            val y = (bounds.maxLat - it.latitude) * scale + offsetY  // 注意纬度是反的（越大越靠上）
            PointF(x.toFloat(), y.toFloat())
        }

        // 绘制轨迹路径
        for (i in 0 until screenPoints.size - 1) {
            val start = screenPoints[i]
            val end = screenPoints[i + 1]
            canvas.drawLine(start.x, start.y, end.x, end.y, pathPaint)
        }

        // 绘制起点
        val start = screenPoints.first()
        canvas.drawCircle(start.x, start.y, 12f, startPaint)

        // 绘制终点
        val end = screenPoints.last()
        canvas.drawCircle(end.x, end.y, 12f, endPaint)
    }

    // 获取路径的经纬度边界
    private fun getBounds(points: List<LatLng>): Bounds {
        var minLat = Double.MAX_VALUE
        var maxLat = -Double.MAX_VALUE
        var minLng = Double.MAX_VALUE
        var maxLng = -Double.MAX_VALUE
        for (p in points) {
            if (p.latitude < minLat) minLat = p.latitude
            if (p.latitude > maxLat) maxLat = p.latitude
            if (p.longitude < minLng) minLng = p.longitude
            if (p.longitude > maxLng) maxLng = p.longitude
        }
        return Bounds(minLat, maxLat, minLng, maxLng)
    }

    // 辅助数据类，用于边界计算
    private data class Bounds(
        val minLat: Double,
        val maxLat: Double,
        val minLng: Double,
        val maxLng: Double
    )
}
