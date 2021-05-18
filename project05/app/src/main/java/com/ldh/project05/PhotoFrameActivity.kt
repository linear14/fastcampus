package com.ldh.project05

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ldh.project05.databinding.ActivityPhotoFrameBinding
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhotoFrameBinding

    private val photoList = mutableListOf<Uri>()
    private var currentPos = 0
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPhotoUriFromIntent()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    private fun getPhotoUriFromIntent() {
        val photoSize = intent.getIntExtra("photoListSize", 0)
        for(i in 0 until photoSize) {
            intent.getStringExtra("image${i}")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {

            if(!photoList.isNullOrEmpty()) {
                runOnUiThread {
                    binding.apply {
                        val nextPos = if(currentPos + 1 == photoList.size) 0 else currentPos + 1

                        ivBackgroundPhoto.setImageURI(photoList[currentPos])

                        ivPhoto.alpha = 0f
                        ivPhoto.setImageURI(photoList[nextPos])
                        ivPhoto.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .start()

                        currentPos = nextPos
                    }
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


}