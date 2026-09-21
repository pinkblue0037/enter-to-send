package com.example.entertosend

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.ArrayDeque

class EnterToSendService : AccessibilityService() {

    companion object {
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

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return false
        if (event.keyCode != KeyEvent.KEYCODE_ENTER && event.keyCode != KeyEvent.KEYCODE_NUMPAD_ENTER) return false
        if (event.isShiftPressed) return false

        val root = rootInActiveWindow ?: return false
        val activePackage = root.packageName?.toString() ?: return false
        if (activePackage != CHATGPT_PACKAGE) return false

        return findAndClickSend(root)
    }

    private fun findAndClickSend(root: AccessibilityNodeInfo): Boolean {
        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (looksLikeSendButton(node) && clickNodeOrParent(node)) {
                return true
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
