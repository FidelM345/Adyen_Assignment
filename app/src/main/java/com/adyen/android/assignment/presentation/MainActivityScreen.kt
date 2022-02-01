package com.adyen.android.assignment.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adyen.android.assignment.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivityScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}