package com.example.pdf

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private var isFirstAnimation = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val translateScale: Animation = AnimationUtils.loadAnimation(this, R.anim.translate_scale)
        val imageView: ImageView = findViewById(R.id.header_icon)
        translateScale.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {    }
            override fun onAnimationEnd(animation: Animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation()

                        val intentH = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intentH)
                        finish()
                }
                isFirstAnimation = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        imageView.startAnimation(translateScale)

    }




}