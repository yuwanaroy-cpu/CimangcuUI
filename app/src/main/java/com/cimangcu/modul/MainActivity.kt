package com.cimangcu.modul

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    private val OVERLAY_PERMISSION_REQ_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cimangcu)

        val prefs = getSharedPreferences("CimangcuPrefs", Context.MODE_PRIVATE)

        // Inisialisasi Elemen UI
        val swRefresh = findViewById<SwitchCompat>(R.id.swRefresh)
        val tvRefreshMs = findViewById<TextView>(R.id.tvRefreshMs)
        val sbRefresh = findViewById<SeekBar>(R.id.sbRefresh)

        val swWaktuOtomatis = findViewById<SwitchCompat>(R.id.swWaktuOtomatis)
        val swWaktuManual = findViewById<SwitchCompat>(R.id.swWaktuManual)
        val tvWaktuMenit = findViewById<TextView>(R.id.tvWaktuMenit)
        val sbWaktuManual = findViewById<SeekBar>(R.id.sbWaktuManual)

        val swAutobidSemua = findViewById<SwitchCompat>(R.id.swAutobidSemua)
        val swAutobidSortir = findViewById<SwitchCompat>(R.id.swAutobidSortir)
        val swAutobidKurir = findViewById<SwitchCompat>(R.id.swAutobidKurir)
        val swTawarJarak = findViewById<SwitchCompat>(R.id.swTawarJarak)

        val etHrgMin = findViewById<EditText>(R.id.etHrgMin)
        val etHrgMaks = findViewById<EditText>(R.id.etHrgMaks)
        val etJrkMaks = findViewById<EditText>(R.id.etJrkMaks)

        val swNaikkanHarga = findViewById<SwitchCompat>(R.id.swNaikkanHarga)
        val tvPilihan = findViewById<TextView>(R.id.tvPilihan)
        val sbPilihan = findViewById<SeekBar>(R.id.sbPilihan)

        val btnMengambang = findViewById<Button>(R.id.btnMengambang)
        val swHidupkan = findViewById<SwitchCompat>(R.id.swHidupkan)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        // Memuat Pengaturan yang Tersimpan (SharedPreferences)
        swRefresh.isChecked = prefs.getBoolean("sw_refresh", false)
        sbRefresh.progress = prefs.getInt("refresh_ms", 300)
        tvRefreshMs.text = "${sbRefresh.progress} ms"

        swWaktuOtomatis.isChecked = prefs.getBoolean("sw_waktu_otomatis", false)
        swWaktuManual.isChecked = prefs.getBoolean("sw_waktu_manual", false)
        sbWaktuManual.progress = prefs.getInt("waktu_manual", 0)
        tvWaktuMenit.text = "${sbWaktuManual.progress} menit"

        swAutobidSemua.isChecked = prefs.getBoolean("sw_autobid_semua", false)
        swAutobidSortir.isChecked = prefs.getBoolean("sw_autobid_sortir", false)
        swAutobidKurir.isChecked = prefs.getBoolean("sw_autobid_kurir", false)
        swTawarJarak.isChecked = prefs.getBoolean("sw_tawar_jarak", false)

        etHrgMin.setText(prefs.getString("hrg_min", "8000"))
        etHrgMaks.setText(prefs.getString("hrg_maks", "100000"))
        etJrkMaks.setText(prefs.getString("jrk_maks", "1000"))

        swNaikkanHarga.isChecked = prefs.getBoolean("sw_naikkan_harga", false)
        sbPilihan.progress = prefs.getInt("pilihan_val", 0)
        tvPilihan.text = "Pilihan ${sbPilihan.progress}"

        swHidupkan.isChecked = prefs.getBoolean("is_active", false)

        // Listener SeekBar
        sbRefresh.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) { tvRefreshMs.text = "$p ms" }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        sbWaktuManual.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) { tvWaktuMenit.text = "$p menit" }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        sbPilihan.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) { tvPilihan.text = "Pilihan $p" }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        // Listener Tombol Tampilkan Mengambang
        btnMengambang.setOnClickListener {
            checkOverlayPermission()
        }

        // Listener Tombol SIMPAN
        btnSimpan.setOnClickListener {
            val editor = prefs.edit()
            editor.putBoolean("sw_refresh", swRefresh.isChecked)
            editor.putInt("refresh_ms", sbRefresh.progress)
            editor.putBoolean("sw_waktu_otomatis", swWaktuOtomatis.isChecked)
            editor.putBoolean("sw_waktu_manual", swWaktuManual.isChecked)
            editor.putInt("waktu_manual", sbWaktuManual.progress)
            editor.putBoolean("sw_autobid_semua", swAutobidSemua.isChecked)
            editor.putBoolean("sw_autobid_sortir", swAutobidSortir.isChecked)
            editor.putBoolean("sw_autobid_kurir", swAutobidKurir.isChecked)
            editor.putBoolean("sw_tawar_jarak", swTawarJarak.isChecked)
            editor.putString("hrg_min", etHrgMin.text.toString())
            editor.putString("hrg_maks", etHrgMaks.text.toString())
            editor.putString("jrk_maks", etJrkMaks.text.toString())
            editor.putBoolean("sw_naikkan_harga", swNaikkanHarga.isChecked)
            editor.putInt("pilihan_val", sbPilihan.progress)
            editor.putBoolean("is_active", swHidupkan.isChecked)
            editor.apply()

            Toast.makeText(this, "Pengaturan Cimangcu Modul Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        } else {
            startFloatingService()
        }
    }

    private fun startFloatingService() {
        startService(Intent(this, FloatingService::class.java))
        Toast.makeText(this, "Tampilkan Mengambang Diaktifkan", Toast.LENGTH_SHORT).show()
    }
}
