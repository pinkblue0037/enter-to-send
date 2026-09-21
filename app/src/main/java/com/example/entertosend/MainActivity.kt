package com.example.entertosend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (24 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(pad, pad * 2, pad, pad)
            setBackgroundColor(Color.WHITE)
        }

        root.addView(TextView(this).apply {
            text = "Enter to Send"
            textSize = 28f
            setTextColor(Color.BLACK)
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            bottomMargin = pad
        })

        root.addView(TextView(this).apply {
            text = "ChatGPT 앱에서\n\nEnter  → 메시지 전송\nShift + Enter → 줄바꿈\n\n아래 버튼을 눌러 접근성 설정에서 ‘Enter to Send’를 켜세요."
            textSize = 18f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            bottomMargin = pad
        })

        root.addView(Button(this).apply {
            text = "접근성 설정 열기"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setContentView(root)
    }
}
