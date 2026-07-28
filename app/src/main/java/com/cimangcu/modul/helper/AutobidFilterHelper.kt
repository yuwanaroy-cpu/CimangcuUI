package com.cimangcu.modul.helper

import android.content.Context

class AutobidFilterHelper(private val context: Context) {

    fun isOrderValidByPriceAndDistance(
        totalHargaOrder: Double,
        jarakKm: Double,
        minHarga: Double,
        maxHarga: Double,
        maxJarak: Double,
        hargaPerKmMinimum: Double
    ): Boolean {
        if (totalHargaOrder < minHarga || totalHargaOrder > maxHarga) return false
        if (jarakKm > maxJarak) return false

        val hargaPerKmOrder = if (jarakKm > 0) totalHargaOrder / jarakKm else 0.0
        if (hargaPerKmOrder < hargaPerKmMinimum) return false

        return true
    }

    fun isAlamatDiblokir(alamatTujuan: String, listAreaBlokir: List<String>): Boolean {
        val alamatLower = alamatTujuan.lowercase()
        return listAreaBlokir.any { area ->
            area.isNotEmpty() && alamatLower.contains(area.lowercase())
        }
    }
}
