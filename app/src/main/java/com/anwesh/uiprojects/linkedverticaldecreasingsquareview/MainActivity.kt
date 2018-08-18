package com.anwesh.uiprojects.linkedverticaldecreasingsquareview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.vdsview.VerticallyDecSquareView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VerticallyDecSquareView.create(this)
    }
}
