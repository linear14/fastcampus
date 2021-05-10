package com.ldh.project03

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    val inputDiary: EditText by lazy {
        findViewById<EditText>(R.id.input_diary)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        initDiary()
    }

    private fun initDiary() {
        val memo = getSharedPreferences("diary_db", Context.MODE_PRIVATE).getString("detail", "")
        inputDiary.setText(memo)

        val saveMemoRunnable = Runnable {
            getSharedPreferences("diary_db", Context.MODE_PRIVATE).edit(true) {
                Log.d("Handler Test", "$inputDiary")
                putString("detail", inputDiary.text.toString())
            }
        }

        inputDiary.addTextChangedListener {
            handler.removeCallbacks(saveMemoRunnable)
            handler.postDelayed(saveMemoRunnable, 500)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Handler Test", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Handler Test", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Handler Test", "onDestroy()")
    }
}