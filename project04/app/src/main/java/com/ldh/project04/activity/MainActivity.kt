package com.ldh.project04.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.ldh.project04.R
import com.ldh.project04.data.AppDatabase
import com.ldh.project04.data.model.History
import com.ldh.project04.isNumber

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById(R.id.tv_expression)
    }

    private val resultTextView: TextView by lazy {
        findViewById(R.id.tv_result)
    }

    private val historyLayout: ConstraintLayout by lazy {
        findViewById(R.id.layout_history)
    }

    private val historyRowLayout: LinearLayout by lazy {
        findViewById(R.id.layout_history_row)
    }

    lateinit var db: AppDatabase

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "FastCampusProject4DB"
        ).build()
    }

    fun buttonClicked(v: View) {
        val num = (v as TextView).text.toString()
        handleNumberButtonClicked(num)
    }

    private fun handleNumberButtonClicked(num: String) {

        if(isOperator) {
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText = expressionTextView.text.split(" ")

        if(expressionText.isNotEmpty() && expressionText.last().length > 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(expressionText.last().isEmpty() && num == "0") {
            Toast.makeText(this, "맨 앞에 0이 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(num)
        resultTextView.text = calculateExpression()
    }


    fun signButtonClicked(v: View) {
        val sign = (v as TextView).text.toString()
        handleSignButtonClicked(sign)
    }

    private fun handleSignButtonClicked(sign: String) {
        val expressionText = expressionTextView.text.split(" ")

        if(expressionText.isEmpty()) {
            Toast.makeText(this, "연산자는 맨 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        when {
            isOperator -> {
                expressionTextView.text = expressionTextView.text.toString().dropLast(1) + sign
            }

            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            else -> {
                expressionTextView.append(" $sign")
            }
        }

        val sb = SpannableStringBuilder(expressionTextView.text)
        sb.setSpan(
            ForegroundColorSpan(getColor(R.color.textGreen)),
            expressionTextView.text.length - 1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = sb

        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(v: View) {
        val expressionTexts = expressionTextView.text.split(" ")

        if(expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        if(expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(!expressionTexts[0].isNumber() || !expressionTexts[2].isNumber()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        resultTextView.text = ""
        expressionTextView.text = resultText

        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if(!hasOperator || expressionTexts.size != 3) {
            return ""
        }

        if(!expressionTexts[0].isNumber() || !expressionTexts[2].isNumber()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()

        return when(expressionTexts[1]) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "X" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v: View) {
        historyLayout.visibility = View.VISIBLE
        historyRowLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val layout = LayoutInflater.from(this).inflate(R.layout.item_history, null, false).apply {
                        findViewById<TextView>(R.id.tv_row_expression).text = it.expression
                        findViewById<TextView>(R.id.tv_row_result).text = "= ${it.result}"
                    }
                    historyRowLayout.addView(layout)
                }
            }
        }).start()

    }


    fun closeHistoryButtonClicked(v: View) {
        historyLayout.visibility = View.GONE
    }

    fun historyClearButtonClicked(v: View) {
        historyRowLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().deleteAllHistory()
        }).start()
    }
}