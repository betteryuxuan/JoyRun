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
        strokeWidth = 8f
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
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

        // 边距
        val padding = 40f
        val mapWidth = bounds.maxLng - bounds.minLng
        val mapHeight = bounds.maxLat - bounds.minLat

        // 计算缩放比例
        val scaleX = (width - 2 * padding) / mapWidth
        val scaleY = (height - 2 * padding) / mapHeight
        val scale = minOf(scaleX, scaleY)

        // 偏移用于让路径居中
        val offsetX = padding
        val offsetY = padding

        // 把经纬度转换为屏幕坐标
        val screenPoints = pathPoints.map {
            val x = (it.longitude - bounds.minLng) * scale + offsetX
            val y = (bounds.maxLat - it.latitude) * scale + offsetY  // 纬度反着来，越大越靠上
            PointF(x.toFloat(), y.toFloat())
        }

        val path = Path()
        path.moveTo(screenPoints[0].x, screenPoints[0].y)

        for (i in 1 until screenPoints.size - 1) {
            val current = screenPoints[i]
            val next = screenPoints[i + 1]

            // 控制点为当前点，目标点为当前和下一个点的中点
            // 这里控制点 = 当前点，终点 = 当前与下一点的中点
            val midX = (current.x + next.x) / 2
            val midY = (current.y + next.y) / 2

            // x1,y1 为控制点，x2,y2 为目标点
            path.quadTo(current.x, current.y, midX, midY)
        }

        // 处理最后两个点
        val secondLast = screenPoints[screenPoints.size - 2]
        val last = screenPoints.last()
        path.quadTo(secondLast.x, secondLast.y, last.x, last.y)

        // 绘制平滑路径
        canvas.drawPath(path, pathPaint)

        // 绘制起点
        val start = screenPoints.first()
        canvas.drawCircle(start.x, start.y, 12f, startPaint)

        // 绘制终点
        canvas.drawCircle(last.x, last.y, 12f, endPaint)
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
