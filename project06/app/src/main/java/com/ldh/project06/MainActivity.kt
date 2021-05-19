package com.ldh.project06

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private val tvRemainedMinute: TextView by lazy {
        findViewById(R.id.tv_minute)
    }

    private val tvRemainedSecond: TextView by lazy {
        findViewById(R.id.tv_second)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekbar)
    }

    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 사용자가 실제로 프로그레스바를 업데이트 했을때만, 반영되도록
                // (updateSeekBar 함수의 seekBar.progress로 변경된 경우에는 적용 안되도록)
                if(fromUser) {
                    updateRemain(progress * 60 * 1000L)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                countDownTimer?.cancel()
                countDownTimer = null
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar != null) {
                    createCountDownTimer(seekBar.progress * 60 * 1000L)
                }
            }

        })
    }

    private fun createCountDownTimer(initTimeInMillis: Long) {
        countDownTimer = object: CountDownTimer(initTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemain(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemain(0)
                updateSeekBar(0)
            }

        }.start()
    }

    private fun updateRemain(remainedMillis: Long) {
        val remainSeconds = remainedMillis / 1000

        tvRemainedMinute.text = "%02d".format(remainSeconds / 60)
        tvRemainedSecond.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainedMillis: Long) {
        seekBar.progress = (remainedMillis / 1000 / 60).toInt()
    }
}