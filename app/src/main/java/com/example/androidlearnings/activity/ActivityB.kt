package com.example.androidlearnings.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.androidlearnings.R

class ActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        Log.d(localClassName, "onCreate called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(localClassName, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(localClassName, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(localClassName, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(localClassName, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(localClassName, "onDestroy called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(localClassName, "onRestart called")
    }
}
