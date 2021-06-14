package com.ldh.project07

import android.content.Context
import android.util.AttributeSet

class RecordButton(context: Context, attrs: AttributeSet): androidx.appcompat.widget.AppCompatImageButton(context, attrs) {

    fun updateIconWithState(state: State) {
        when(state) {
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_record)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_stop)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop)
            }
        }
    }
}