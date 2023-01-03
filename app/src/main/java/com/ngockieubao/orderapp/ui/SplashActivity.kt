package com.ngockieubao.orderapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ngockieubao.orderapp.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start MainActivity
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        // Close splash activity
        finish()
    }
}