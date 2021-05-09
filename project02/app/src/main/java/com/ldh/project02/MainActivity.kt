package com.ldh.project02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }

    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }

    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.num1),
            findViewById(R.id.num2),
            findViewById(R.id.num3),
            findViewById(R.id.num4),
            findViewById(R.id.num5),
            findViewById(R.id.num6)
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initAddButton()
        initClearButton()
        initRunButton()
    }

    private fun initNumberPicker() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 45
        numberPicker.wrapSelectorWheel = false
    }

    private fun initAddButton() {
        addButton.setOnClickListener {

            if(didRun) {
                Toast.makeText(this, "초기화 후 이용해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()
            setBackgroundColor(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            didRun = false
            pickNumberSet.clear()

            numberTextViewList.forEach {
                it.isVisible = false
            }
        }

    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            didRun = true

            val list = getRandomNumber()

            list.forEachIndexed { index, number ->
                val tv = numberTextViewList[index]
                tv.isVisible = true
                tv.text = number.toString()

                setBackgroundColor(number, tv)
            }
        }
    }

    private fun getRandomNumber(): List<Int> {
        val list = mutableListOf<Int>().apply {
            for(i in 1..45) {
                if(!pickNumberSet.contains(i)) add(i)
            }
        }

        list.shuffle()

        val newList = pickNumberSet.toList() + list.subList(0, 6 - pickNumberSet.size)

        return newList.sorted()
    }

    private fun setBackgroundColor(number: Int, tv: TextView) {
        when(number) {
            in 1..10 -> tv.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> tv.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> tv.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> tv.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            else -> tv.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }


}