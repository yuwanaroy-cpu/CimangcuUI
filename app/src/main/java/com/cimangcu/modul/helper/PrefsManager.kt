package com.cimangcu.modul.helper

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "cimangcu_prefs"
        private const val KEY_MIN_HARGA = "min_harga"
        private const val KEY_MAX_HARGA = "max_harga"
        private const val KEY_MAX_JARAK = "max_jarak"
        private const val KEY_MIN_HARGA_PER_KM = "min_harga_per_km"
        private const val KEY_AREA_BLOKIR = "area_blokir"
        private const val KEY_DELAY_COCOL = "delay_cocol"
        private const val KEY_IS_ACTIVE = "is_active"
    }

    fun saveSettings(
        minHarga: Double,
        maxHarga: Double,
        maxJarak: Double,
        minHargaPerKm: Double,
        areaBlokir: String,
        delayCocol: Int,
        isActive: Boolean
    ) {
        prefs.edit().apply {
            putFloat(KEY_MIN_HARGA, minHarga.toFloat())
            putFloat(KEY_MAX_HARGA, maxHarga.toFloat())
            putFloat(KEY_MAX_JARAK, maxJarak.toFloat())
            putFloat(KEY_MIN_HARGA_PER_KM, minHargaPerKm.toFloat())
            putString(KEY_AREA_BLOKIR, areaBlokir)
            putInt(KEY_DELAY_COCOL, delayCocol)
            putBoolean(KEY_IS_ACTIVE, isActive)
            apply()
        }
    }

    fun getMinHarga(): Double = prefs.getFloat(KEY_MIN_HARGA, 10000f).toDouble()
    fun getMaxHarga(): Double = prefs.getFloat(KEY_MAX_HARGA, 100000f).toDouble()
    fun getMaxJarak(): Double = prefs.getFloat(KEY_MAX_JARAK, 10.0f).toDouble()
    fun getMinHargaPerKm(): Double = prefs.getFloat(KEY_MIN_HARGA_PER_KM, 4000f).toDouble()
    fun getDelayCocol(): Int = prefs.getInt(KEY_DELAY_COCOL, 0)
    fun isActive(): Boolean = prefs.getBoolean(KEY_IS_ACTIVE, false)

    fun getListAreaBlokir(): List<String> {
        val rawText = prefs.getString(KEY_AREA_BLOKIR, "Cibinong, Ciluar") ?: ""
        return rawText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    fun getRawAreaBlokir(): String = prefs.getString(KEY_AREA_BLOKIR, "Cibinong, Ciluar") ?: ""
}
