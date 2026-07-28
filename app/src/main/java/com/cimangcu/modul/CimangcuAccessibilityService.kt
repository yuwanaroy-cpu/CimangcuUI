package com.cimangcu.modul

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.cimangcu.modul.helper.AutobidFilterHelper
import com.cimangcu.modul.helper.PrefsManager
import com.cimangcu.modul.helper.ScreenScraperHelper

class CimangcuAccessibilityService : AccessibilityService() {

    private lateinit var filterHelper: AutobidFilterHelper
    private lateinit var prefsManager: PrefsManager

    override fun onCreate() {
        super.onCreate()
        filterHelper = AutobidFilterHelper(applicationContext)
        prefsManager = PrefsManager(applicationContext)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!prefsManager.isActive()) return

        val rootNode = rootInActiveWindow ?: return

        // 1. Scraping data asli dari layar menggunakan ScreenScraperHelper
        val totalHarga = ScreenScraperHelper.parseHargaFromScreen(rootNode) ?: return
        val totalJarak = ScreenScraperHelper.parseJarakFromScreen(rootNode) ?: return
        val alamatTujuan = ScreenScraperHelper.parseAlamatFromScreen(rootNode)

        // 2. Ambil preferensi pengguna
        val minHarga = prefsManager.getMinHarga()
        val maxHarga = prefsManager.getMaxHarga()
        val maxJarak = prefsManager.getMaxJarak()
        val targetHargaPerKm = prefsManager.getMinHargaPerKm()
        val listAreaBlokir = prefsManager.getListAreaBlokir()
        val delayMillis = prefsManager.getDelayCocol() * 1000L

        // 3. Cek Blokir Area
        if (filterHelper.isAlamatDiblokir(alamatTujuan, listAreaBlokir)) return

        // 4. Validasi Kelayakan Order
        val isValid = filterHelper.isOrderValidByPriceAndDistance(
            totalHargaOrder = totalHarga,
            jarakKm = totalJarak,
            minHarga = minHarga,
            maxHarga = maxHarga,
            maxJarak = maxJarak,
            hargaPerKmMinimum = targetHargaPerKm
        )

        // 5. Eksekusi Auto-Click jika lolos filter
        if (isValid) {
            Handler(Looper.getMainLooper()).postDelayed({
                performAutoClick(rootNode)
            }, delayMillis)
        }
    }

    override fun onInterrupt() {}

    private fun performAutoClick(node: AccessibilityNodeInfo) {
        // Fungsi pencarian tombol "Terima", "Cocol", "Accept", dsb.
        val targetTextList = listOf("Terima", "Cocol", "Accept", "Terima Order")
        
        for (text in targetTextList) {
            val nodes = node.findAccessibilityNodeInfosByText(text)
            if (!nodes.isNullOrEmpty()) {
                for (item in nodes) {
                    if (item.isClickable) {
                        item.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        return
                    } else {
                        // Jika parent-nya yang bisa diklik
                        item.parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            }
        }
    }
}
