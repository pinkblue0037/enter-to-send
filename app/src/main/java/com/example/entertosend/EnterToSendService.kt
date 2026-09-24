package com.example.entertosend

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import java.util.ArrayDeque

class EnterToSendService : AccessibilityService() {

    companion object {
        private const val TAG = "EnterToSend"
        private const val CHATGPT_PACKAGE = "com.openai.chatgpt"

        private val SEND_LABELS = listOf(
            "send",
            "send message",
            "submit",
            "보내기",
            "메시지 보내기",
            "전송"
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "SERVICE ON")
        Toast.makeText(this, "SERVICE ON", Toast.LENGTH_SHORT).show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() {
        Log.w(TAG, "SERVICE INTERRUPTED")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return false
        if (event.keyCode != KeyEvent.KEYCODE_ENTER && event.keyCode != KeyEvent.KEYCODE_NUMPAD_ENTER) return false
        if (event.isShiftPressed) return false

        Log.i(TAG, "ENTER DETECTED")
        Toast.makeText(this, "ENTER DETECTED", Toast.LENGTH_SHORT).show()

        val root = rootInActiveWindow
        if (root == null) {
            Log.w(TAG, "NO ROOT WINDOW")
            Toast.makeText(this, "NO ROOT WINDOW", Toast.LENGTH_SHORT).show()
            return false
        }

        val activePackage = root.packageName?.toString().orEmpty()
        Log.i(TAG, "ACTIVE PACKAGE=$activePackage")

        if (activePackage != CHATGPT_PACKAGE) {
            Toast.makeText(this, "NOT CHATGPT: $activePackage", Toast.LENGTH_SHORT).show()
            return false
        }

        val result = findAndClickSend(root)
        if (!result) {
            Log.w(TAG, "SEND NOT FOUND")
            Toast.makeText(this, "SEND NOT FOUND", Toast.LENGTH_SHORT).show()
        }
        return result
    }

    private fun findAndClickSend(root: AccessibilityNodeInfo): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (looksLikeSendButton(node)) {
                Log.i(TAG, "SEND FOUND")
                Toast.makeText(this, "SEND FOUND", Toast.LENGTH_SHORT).show()
                if (clickNodeOrParent(node)) {
                    Log.i(TAG, "SEND CLICKED")
                    Toast.makeText(this, "SEND CLICKED", Toast.LENGTH_SHORT).show()
                    return true
                }
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let(queue::addLast)
            }
        }
        return false
    }

    private fun looksLikeSendButton(node: AccessibilityNodeInfo): Boolean {
        val text = node.text?.toString()?.trim()?.lowercase().orEmpty()
        val desc = node.contentDescription?.toString()?.trim()?.lowercase().orEmpty()
        val viewId = node.viewIdResourceName?.lowercase().orEmpty()

        val labelMatched = SEND_LABELS.any { label ->
            text == label || desc == label || text.contains(label) || desc.contains(label)
        }

        val idMatched = viewId.contains("send") || viewId.contains("submit")

        return (labelMatched || idMatched) && (node.isClickable || node.parent?.isClickable == true)
    }

    private fun clickNodeOrParent(node: AccessibilityNodeInfo): Boolean {
        if (node.isClickable && node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
            return true
        }

        var parent = node.parent
        repeat(4) {
            val p = parent ?: return false
            if (p.isClickable && p.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                return true
            }
            parent = p.parent
        }
        return false
    }
}
