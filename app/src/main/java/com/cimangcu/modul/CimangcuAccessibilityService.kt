package com.cimangcu.modul

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class CimangcuAccessibilityService : AccessibilityService() {

    companion object {
        var instance: CimangcuAccessibilityService? = null
        var isServiceActive: Boolean = false
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        isServiceActive = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Jika status OFF, jangan jalankan logika auto-click/otomasi sama sekali
        if (!isServiceActive) return

        // Logika otomasi kamu berjalan di sini
    }

    override fun onInterrupt() {
        isServiceActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        isServiceActive = false
    }

    // Fungsi untuk mensimulasikan klik otomatis pada koordinat X, Y
    fun autoClick(x: Float, y: Float) {
        if (!isServiceActive) return

        val path = Path().apply {
            moveTo(x, y)
        }
        val builder = GestureDescription.Builder()
        val gestureDescription = builder
            .addStroke(GestureDescription.StrokeDescription(path, 0, 50))
            .build()

        dispatchGesture(gestureDescription, null, null)
    }

    // Fungsi untuk mematikan service secara mandiri
    fun stopServiceSelf() {
        isServiceActive = false
        disableSelf() // Mematikan AccessibilityService dari dalam aplikasi
    }
}
