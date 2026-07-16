package com.cimangcu.ui
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cimangcu)
        val swAutoRefresh = findViewById<Switch>(R.id.swAutoRefresh)
        val sbRefresh = findViewById<SeekBar>(R.id.sbRefresh)
        val tvRefreshValue = findViewById<TextView>(R.id.tvRefreshValue)
        val btnSave = findViewById<Button>(R.id.btnSave)
        sbRefresh.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvRefreshValue.text = "$progress ms"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        btnSave.setOnClickListener {
            Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show()
        }
    }
}
