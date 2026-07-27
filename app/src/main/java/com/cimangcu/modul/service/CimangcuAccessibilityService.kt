package com.cimangcu.modul.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.cimangcu.modul.helper.AutobidFilterHelper
import com.cimangcu.modul.helper.PrefsManager

class CimangcuAccessibilityService : AccessibilityService() {

    private lateinit var filterHelper: AutobidFilterHelper
    private lateinit var prefsManager: PrefsManager

    override fun onCreate() {
        super.onCreate()
        filterHelper = AutobidFilterHelper(applicationContext)
        prefsManager = PrefsManager(applicationContext)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Cek Sakelar Utama (Hidupkan)
        if (!prefsManager.isActive()) return

        val rootNode = rootInActiveWindow ?: return

        val totalHarga = parseHargaFromScreen(rootNode) ?: return
        val totalJarak = parseJarakFromScreen(rootNode) ?: return
        val alamatTujuan = parseAlamatFromScreen(rootNode)

        val minHarga = prefsManager.getMinHarga()
        val maxHarga = prefsManager.getMaxHarga()
        val maxJarak = prefsManager.getMaxJarak()
        val targetHargaPerKm = prefsManager.getMinHargaPerKm()
        val listAreaBlokir = prefsManager.getListAreaBlokir()
        val delayMillis = prefsManager.getDelayCocol() * 1000L

        if (filterHelper.isAlamatDiblokir(alamatTujuan, listAreaBlokir)) return

        val isValid = filterHelper.isOrderValidByPriceAndDistance(
            totalHargaOrder = totalHarga,
            jarakKm = totalJarak,
            minHarga = minHarga,
            maxHarga = maxHarga,
            maxJarak = maxJarak,
            hargaPerKmMinimum = targetHargaPerKm
        )

        if (isValid) {
            // Berikan delay sesuai slider sebelum klik tombol terima
            Handler(Looper.getMainLooper()).postDelayed({
                performAutoClick(rootNode)
            }, delayMillis)
        }
    }

    override fun onInterrupt() {}

    private fun parseHargaFromScreen(node: AccessibilityNodeInfo): Double? = 25000.0
    private fun parseJarakFromScreen(node: AccessibilityNodeInfo): Double? = 4.2
    private fun parseAlamatFromScreen(node: AccessibilityNodeInfo): String = "Jl. Raya Ciluar, Bogor"
    private fun performAutoClick(node: AccessibilityNodeInfo) {}
}
