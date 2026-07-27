package com.cimangcu.modul.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import java.io.IOException
import java.util.Locale

class AutobidFilterHelper(private val context: Context) {

    // ==========================================
    // 1. PERHITUNGAN HARGA PER-KM
    // ==========================================
    
    /**
     * Menghitung rekomendasi harga per km berdasarkan persentase penyesuaian.
     */
    fun getRekomendasiHargaPerKm(basePrice: Double, percentage: Double): Double {
        return basePrice * (1.0 + (percentage / 100.0))
    }

    /**
     * Validasi apakah orderan sesuai dengan batas minimum & maksimum yang ditentukan user.
     */
    fun isOrderValidByPriceAndDistance(
        totalHargaOrder: Double,
        jarakKm: Double,
        minHarga: Double,
        maxHarga: Double,
        maxJarak: Double,
        hargaPerKmMinimum: Double
    ): Boolean {
        // Cek range harga
        if (totalHargaOrder < minHarga || totalHargaOrder > maxHarga) return false
        
        // Cek jarak maksimal
        if (jarakKm > maxJarak) return false
        
        // Cek kelayakan harga per KM
        val realHargaPerKm = totalHargaOrder / jarakKm
        if (realHargaPerKm < hargaPerKmMinimum) return false

        return true
    }

    // ==========================================
    // 2. FILTER LOKASI & GEO-FENCING
    // ==========================================

    /**
     * Mengambil lokasi terakhir perangkat.
     */
    fun getLastKnownLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null

        for (provider in providers) {
            try {
                val location = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                    bestLocation = location
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
        return bestLocation
    }

    /**
     * Mendapatkan detail alamat (Kota, Kecamatan, Kelurahan) dari koordinat lokasi.
     */
    fun getDetailAlamat(location: Location): Array<String>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                arrayOf(
                    address.countryName ?: "",   // Index 0: Negara
                    address.locality ?: "",      // Index 1: Kota/Kabupaten
                    address.subLocality ?: "",   // Index 2: Kecamatan/Kelurahan
                    address.adminArea ?: ""      // Index 3: Provinsi
                )
            } else null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Memeriksa apakah alamat orderan masuk dalam daftar "Blokir Area".
     */
    fun isAlamatDiblokir(alamatOrder: String, daftarAlamatBlokir: List<String>): Boolean {
        for (blokir in daftarAlamatBlokir) {
            if (alamatOrder.contains(blokir, ignoreCase = true)) {
                return true // Alamat cocok dengan area yang diblokir
            }
        }
        return false
    }

    // ==========================================
    // 3. SELEKSI STRATEGI HARGA
    // ==========================================

    enum class PriceOption {
        LOWEST, SECOND_LOWEST, MID, THIRD_HIGHEST, HIGH
    }

    /**
     * Memilih harga target jika ada beberapa pilihan variasi harga orderan.
     */
    fun selectTargetPrice(availablePrices: List<Double>, option: PriceOption): Double? {
        if (availablePrices.isEmpty()) return null
        val sortedPrices = availablePrices.sorted()

        return when (option) {
            PriceOption.LOWEST -> sortedPrices.first()
            PriceOption.SECOND_LOWEST -> if (sortedPrices.size > 1) sortedPrices[1] else sortedPrices.first()
            PriceOption.MID -> sortedPrices[sortedPrices.size / 2]
            PriceOption.THIRD_HIGHEST -> if (sortedPrices.size >= 3) sortedPrices[sortedPrices.size - 3] else sortedPrices.last()
            PriceOption.HIGH -> sortedPrices.last()
        }
    }
}
