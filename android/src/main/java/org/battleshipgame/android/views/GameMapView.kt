package org.battleshipgame.android.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import org.battleshipgame.android.R
import kotlin.math.floor
import kotlin.math.roundToInt

class GameMapView(ctx: Context, attrs: AttributeSet) : View(ctx, attrs) {
    var active = false
    var shotListener: ShotListener? = null

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = ResourcesCompat.getColor(resources, R.color.inputBackgroundDefault, context.theme)
        val bounds = Rect(0, 0, width, height)
        canvas.drawRect(bounds, paint)

        val step = width / 10.0f

        paint.style = Paint.Style.STROKE
        paint.color = ResourcesCompat.getColor(resources, R.color.linesColorDefault, context.theme)
        paint.strokeWidth = step / 10;

        for(i in 0..11) {
            canvas.drawLine(i * step, 0.0f, i * step, height.toFloat(), paint)
            canvas.drawLine(0.0f, i * step, width.toFloat(), i * step, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (active && event.action == MotionEvent.ACTION_DOWN) {
            val step = width / 10.0f
            val ox = floor(event.x / step).roundToInt()
            val oy = floor(event.y / step).roundToInt()
            shotListener?.onShot(ox, oy)
            return true
        }
        return super.onTouchEvent(event)
    }
}