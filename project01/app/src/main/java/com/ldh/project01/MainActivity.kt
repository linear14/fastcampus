package com.ldh.project01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputHeight: EditText = findViewById(R.id.input_height)
        val inputWeight: EditText = findViewById(R.id.input_weight)
        val resultButton: Button = findViewById(R.id.btn_result)

        resultButton.setOnClickListener {
            if(inputHeight.text.isNullOrEmpty() || inputWeight.text.isNullOrEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height: Int = inputHeight.text.toString().toInt()
            val weight: Int = inputWeight.text.toString().toInt()

            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("height", height)
                putExtra("weight", weight)
            }
            startActivity(intent)
        }
    }
}