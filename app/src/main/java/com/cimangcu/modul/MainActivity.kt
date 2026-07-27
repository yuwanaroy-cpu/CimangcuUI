package com.cimangcu.modul

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cimangcu)

        // Inisialisasi Komponen Layout
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

        // Event Listener untuk Slider Refresh (ms)
        sbRefresh.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvRefreshMs.text = "$progress ms"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Event Listener untuk Slider Waktu Manual (menit)
        sbWaktuManual.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvWaktuMenit.text = "$progress menit"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Event Listener untuk Slider Pilihan
        sbPilihan.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvPilihan.text = "Pilihan $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Event Listener Tombol SIMPAN
        btnSimpan.setOnClickListener {
            Toast.makeText(this, "Pengaturan Cimangcu Modul Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}
