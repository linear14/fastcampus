package com.ldh.project06

import android.annotation.SuppressLint
import android.media.SoundPool
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

    private val soundPool = SoundPool.Builder().build()

    private var countDownTimer: CountDownTimer? = null
    private var tickSound: Int? = null
    private var bellSound: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onResume() {
        super.onResume()

        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()

        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        soundPool.release()
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
                stopCountDown()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar != null) {
                    if(seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }

        })
    }

    private fun initSounds() {
        tickSound = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSound = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun createCountDownTimer(initTimeInMillis: Long)
        = object: CountDownTimer(initTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemain(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }.start()


    private fun startCountDown() {
        countDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)

        tickSound?.let {
            soundPool.play(it, 1f, 1f, 0, -1, 1f)
        }
    }

    private fun stopCountDown() {
        countDownTimer?.cancel()
        countDownTimer = null
        soundPool.autoPause()
    }

    private fun completeCountDown() {
        updateRemain(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSound?.let {
            soundPool.play(it, 1f, 1f, 0, 0, 1f)
        }
    }

    private fun updateRemain(remainedMillis: Long) {
        val remainSeconds = remainedMillis / 1000

        tvRemainedMinute.text = "%02d`".format(remainSeconds / 60)
        tvRemainedSecond.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainedMillis: Long) {
        seekBar.progress = (remainedMillis / 1000 / 60).toInt()
    }
}