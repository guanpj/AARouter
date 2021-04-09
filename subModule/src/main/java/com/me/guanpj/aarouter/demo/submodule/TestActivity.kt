package com.me.guanpj.aarouter.demo.submodule

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.me.guanpj.aarouter.annotations.Destination

@Destination(url = "router://sub-test", description = "test page")
class TestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setBackgroundColor(Color.WHITE)
        textView.setTextColor(Color.BLACK)
        textView.textSize = 16f
        textView.gravity = Gravity.CENTER

        setContentView(textView)

        val name = intent.getStringExtra("name")
        val message = intent.getStringExtra("message")

        textView.text = "TestActivity -> $name : $message"
    }
}