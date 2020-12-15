package com.stock.manager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.stock.manager.R

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut : Long = 3000 // 1sec
    lateinit var splashHead: LinearLayout
    lateinit var splashBottom: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashHead = findViewById(R.id.splash_head)
        splashBottom = findViewById(R.id.dbs)

        splashHead.animation = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)
        splashBottom.animation = AnimationUtils.loadAnimation(this,R.anim.up_anim)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this@SplashActivity,MainActivity::class.java))

            // close this activity
            finish()
        }, splashTimeOut)

    }
}