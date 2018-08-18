package com.anwesh.uiprojects.linkedverticaldecreasingsquareview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anwesh.uiprojects.vdsview.VerticallyDecSquareView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : VerticallyDecSquareView = VerticallyDecSquareView.create(this)
        fullScreen()
        view.addOnAnimationListener({createToast("animation number ${it} completed")}, {createToast("animation number ${it} is reset")})
    }

    fun createToast(msg :String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}