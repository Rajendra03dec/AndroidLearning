package com.example.androidlearnings.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnings.R
import com.example.androidlearnings.fragment.FragmentOne

class ActivityLifeCycle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actvity_life_cycle)
        Log.d(localClassName, "onCreate called")

        var fm=supportFragmentManager
        var ft=fm.beginTransaction()

        ft.add(R.id.fragmentContainer, FragmentOne())
        ft.commit()
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

    fun onClick(view: View) {
        when (view.id) {
            R.id.tvStartActivity -> {
                startActivity(Intent(this, ActivityB::class.java))
            }
        }
    }
}
