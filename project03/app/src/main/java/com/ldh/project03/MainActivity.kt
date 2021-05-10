package com.ldh.project03

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    val picker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_1).apply {
            minValue = 0
            maxValue = 9
        }
    }

    val picker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_2).apply {
            minValue = 0
            maxValue = 9
        }
    }

    val picker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker_3).apply {
            minValue = 0
            maxValue = 9
        }
    }

    val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.btn_open)
    }

    val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.btn_change_password)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initOpenButton()
        initChangePasswordButton()
    }

    private fun initNumberPicker() {
        picker1
        picker2
        picker3
    }

    private fun initOpenButton() {
        openButton.setOnClickListener {
            if(!changePasswordMode) {
                val pref = getSharedPreferences("diary_db", Context.MODE_PRIVATE)
                val password = pref.getString("password", "000")

                val passwordInput = "${picker1.value}${picker2.value}${picker3.value}"

                if(password == passwordInput) {
                    startActivity(Intent(this, DiaryActivity::class.java))
                } else {
                    showErrorPopup()
                }
            }
        }
    }

    private fun initChangePasswordButton() {
        changePasswordButton.setOnClickListener {

            val pref = getSharedPreferences("diary_db", Context.MODE_PRIVATE)

            if(changePasswordMode) {

                pref.edit(true) {
                    putString("password", getSettledPassword())
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                val password = pref.getString("password", "000")
                val passwordInput = getSettledPassword()

                if(password == passwordInput) {
                    Toast.makeText(this, "비밀번호 변경 후 다시 눌러주세요", Toast.LENGTH_SHORT).show()
                    changePasswordMode = true
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showErrorPopup()
                }
            }

        }
    }

    private fun showErrorPopup() {
        AlertDialog.Builder(this)
            .setTitle("비밀번호 오류")
            .setMessage("잘못된 비밀번호가 입력되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }

    private fun getSettledPassword(): String = "${picker1.value}${picker2.value}${picker3.value}"

}