package com.anwesh.uiprojects.vdsview

/**
 * Created by anweshmishra on 18/08/18.
 */

import android.app.Activity
import android.view.View
import android.content.Context
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF

val nodes : Int = 5

fun Canvas.drawVDSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size = gap / 2
    val wSize : Float = size / nodes
    val sc1 : Float = Math.min(0.5f, scale) * 2
    val sc2 : Float = Math.min(0.5f, Math.max(0f, scale - 0.5f)) * 2
    paint.color = Color.parseColor("#4CAF50")
    save()
    translate(i * gap + gap/2 + gap * sc1, h/2)
    val y : Float = -size / 2
    val x : Float = -size/2 + wSize * i
    drawRect(RectF(x, y + size * sc2, x + wSize, y + size), paint)
    for (j in 1..(nodes - 1 - i)) {
        val rx : Float = x + wSize * j
        drawRect(RectF(rx, y, rx + wSize, y + size), paint)
    }
    restore()
}

class VerticallyDecSquareView(ctx : Context) : View(ctx) {

    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    var onAnimationListener : OnAnimationListener? = null

    fun addOnAnimationListener(onComplete : (Int) -> Unit, onReset : (Int) -> Unit) {
        onAnimationListener = OnAnimationListener(onComplete, onReset)
    }

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += dir * 0.05f
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class VDSNode(var i : Int, val state : State = State()) {

        private var next : VDSNode? = null
        private var prev : VDSNode? = null

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawVDSNode(i, state.scale, paint)
        }

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = VDSNode(i + 1)
                next?.prev = this
            }
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : VDSNode {
            var curr : VDSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }

    }

    class LinkedVDS(var i : Int) {

        private var curr : VDSNode = VDSNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : VerticallyDecSquareView) {

        private val animator : Animator = Animator(view)
        private val linkedVDS : LinkedVDS = LinkedVDS(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#BDBDBD"))
            linkedVDS.draw(canvas, paint)
            animator.animate {
                linkedVDS.update{i, scl ->
                    animator.stop()
                    when (scl) {
                        0f -> view.onAnimationListener?.onReset?.invoke(i)
                        1f -> view.onAnimationListener?.onComplete?.invoke(i)
                    }
                }
            }
        }

        fun handleTap() {
            linkedVDS.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : VerticallyDecSquareView {
            val view : VerticallyDecSquareView = VerticallyDecSquareView(activity)
            activity.setContentView(view)
            return view
        }
    }

    data class OnAnimationListener(var onComplete : (Int) -> Unit, var onReset : (Int) -> Unit)
}