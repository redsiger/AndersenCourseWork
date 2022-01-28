package com.example.androidschool.andersencoursework.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)

//        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
//            val insetsCompat = toWindowInsetsCompat(windowInsets, view)
//            viewBinding.activityMainBottomNav.isGone = insetsCompat.isVisible(ime())
//            view.onApplyWindowInsets(windowInsets)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}