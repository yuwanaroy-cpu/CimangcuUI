package com.cimangcu.modul

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
import com.cimangcu.modul.helper.PrefsManager
import com.cimangcu.modul.service.FloatingWidgetService

class MainActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager

    private lateinit var inputMinHarga: EditText
    private lateinit var inputMaxHarga: EditText
    private lateinit var inputMaxJarak: EditText
    private lateinit var inputHargaPerKM: EditText
    private lateinit var inputAlamatBlokir: EditText
    private lateinit var tvDelayLabel: TextView
    private lateinit var seekBarDelay: SeekBar
    private lateinit var btnMengambang: Button
    private lateinit var swHidupkan: SwitchCompat
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate()
        setContentView(R.layout.activity_main)

        prefsManager = PrefsManager(this)

        // Binding UI
        inputMinHarga = findViewById(R.id.inputMinHarga)
        inputMaxHarga = findViewById(R.id.inputMaxHarga)
        inputMaxJarak = findViewById(R.id.inputMaxJarak)
        inputHargaPerKM = findViewById(R.id.inputHargaPerKM)
        inputAlamatBlokir = findViewById(R.id.inputAlamatBlokir)
        tvDelayLabel = findViewById(R.id.tvDelayLabel)
        seekBarDelay = findViewById(R.id.seekBarDelay)
        btnMengambang = findViewById(R.id.btnMengambang)
        swHidupkan = findViewById(R.id.swHidupkan)
        btnSimpan = findViewById(R.id.btnSimpan)

        loadSavedValues()

        // Handler SeekBar / Slider Delay
        seekBarDelay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDelayLabel.text = "Delay Cocol: $progress detik"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Handler Tombol Mengambang
        btnMengambang.setOnClickListener {
            checkAndStartOverlay()
        }

        // Handler Simpan
        btnSimpan.setOnClickListener {
            saveValues()
        }
    }

    private fun loadSavedValues() {
        inputMinHarga.setText(prefsManager.getMinHarga().toInt().toString())
        inputMaxHarga.setText(prefsManager.getMaxHarga().toInt().toString())
        inputMaxJarak.setText(prefsManager.getMaxJarak().toString())
        inputHargaPerKM.setText(prefsManager.getMinHargaPerKm().toInt().toString())
        inputAlamatBlokir.setText(prefsManager.getRawAreaBlokir())
        
        val savedDelay = prefsManager.getDelayCocol()
        seekBarDelay.progress = savedDelay
        tvDelayLabel.text = "Delay Cocol: $savedDelay detik"

        swHidupkan.isChecked = prefsManager.isActive()
    }

    private fun saveValues() {
        val minHarga = inputMinHarga.text.toString().toDoubleOrNull() ?: 0.0
        val maxHarga = inputMaxHarga.text.toString().toDoubleOrNull() ?: 0.0
        val maxJarak = inputMaxJarak.text.toString().toDoubleOrNull() ?: 0.0
        val minHargaPerKm = inputHargaPerKM.text.toString().toDoubleOrNull() ?: 0.0
        val areaBlokir = inputAlamatBlokir.text.toString()
        val delayCocol = seekBarDelay.progress
        val isActive = swHidupkan.isChecked

        prefsManager.saveSettings(minHarga, maxHarga, maxJarak, minHargaPerKm, areaBlokir, delayCocol, isActive)
        Toast.makeText(this, "Pengaturan Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
    }

    private fun checkAndStartOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            Toast.makeText(this, "Izinkan aplikasi tampil di atas aplikasi lain", Toast.LENGTH_LONG).show()
        } else {
            startService(Intent(this, FloatingWidgetService::class.java))
        }
    }
}
