package com.example.androidschool.andersencoursework.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidschool.andersencoursework.ui.MainActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(
            this,
            MainActivity::class.java
        ))
        finish()
    }
}