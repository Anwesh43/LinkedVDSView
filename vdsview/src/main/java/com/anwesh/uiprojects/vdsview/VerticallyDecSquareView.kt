package com.anwesh.uiprojects.vdsview

/**
 * Created by anweshmishra on 18/08/18.
 */

import android.view.View
import android.content.Context
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RectF

val nodes : Int = 5

class VerticallyDecSquareView(ctx : Context) : View(ctx) {

    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}