package com.cimangcu.modul.helper

import android.view.accessibility.AccessibilityNodeInfo
import java.util.regex.Pattern

object ScreenScraperHelper {

    /**
     * Mengambil semua teks yang terlihat di layar secara rekursif
     */
    fun extractAllTextFromNode(node: AccessibilityNodeInfo?): List<String> {
        val textList = mutableListOf<String>()
        if (node == null) return textList

        // Jika node memiliki teks, simpan ke list
        node.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let {
            textList.add(it)
        }

        // Rekursi untuk mengecek anak-anak node (children)
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            textList.addAll(extractAllTextFromNode(child))
        }

        return textList
    }

    /**
     * Mengambil nilai Nominal Harga (Rp) dari teks layar
     * Contoh yang cocok: "Rp25.000", "Rp 25.000", "25.000", "Rp14.000,-"
     */
    fun parseHargaFromScreen(rootNode: AccessibilityNodeInfo?): Double? {
        val allTexts = extractAllTextFromNode(rootNode)
        
        // Regex untuk mencari format rupiah atau angka puluhan ribu
        val priceRegex = Pattern.compile("(?:Rp\\.?\\s*)?(\\d{1,3}(?:[\\.,]\\d{3})+)")

        for (text in allTexts) {
            val matcher = priceRegex.matcher(text)
            if (matcher.find()) {
                val rawNumber = matcher.group(1)
                // Bersihkan titik/koma separator ribuan
                val cleanNumber = rawNumber?.replace(".", "")?.replace(",", "")
                val parsedPrice = cleanNumber?.toDoubleOrNull()
                
                // Menyaring nominal wajar orderan (misal minimal Rp 5.000)
                if (parsedPrice != null && parsedPrice >= 5000) {
                    return parsedPrice
                }
            }
        }
        return null
    }

    /**
     * Mengambil Nilai Jarak (dalam KM) dari teks layar
     * Contoh yang cocok: "4.2 km", "4,2 KM", "10 km", "0.8km"
     */
    fun parseJarakFromScreen(rootNode: AccessibilityNodeInfo?): Double? {
        val allTexts = extractAllTextFromNode(rootNode)
        
        // Regex untuk mencari format angka yang diikuti "km"
        val distanceRegex = Pattern.compile("(\\d+(?:[\\.,]\\d+)?)\\s*km", Pattern.CASE_INSENSITIVE)

        for (text in allTexts) {
            val matcher = distanceRegex.matcher(text)
            if (matcher.find()) {
                val rawDistance = matcher.group(1)?.replace(",", ".")
                val parsedDistance = rawDistance?.toDoubleOrNull()
                if (parsedDistance != null) {
                    return parsedDistance
                }
            }
        }
        return null
    }

    /**
     * Mengambil String Alamat atau teks rute dari layar
     */
    fun parseAlamatFromScreen(rootNode: AccessibilityNodeInfo?): String {
        val allTexts = extractAllTextFromNode(rootNode)
        
        // Menggabungkan baris teks yang mengandung kata kunci jalan/lokasi
        val addressKeywords = listOf("jl", "jalan", "gang", "gg", "kec", "kab", "rt", "rw", "no")
        
        val matchedAddresses = allTexts.filter { text ->
            addressKeywords.any { keyword -> text.lowercase().contains(keyword) }
        }

        return matchedAddresses.joinToString(" - ")
    }
}
