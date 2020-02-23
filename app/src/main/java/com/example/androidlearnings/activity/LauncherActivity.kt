package com.example.androidlearnings.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnings.R
import com.example.androidlearnings.pojo.TestPojo

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        var testPojo=TestPojo("title", "description", 1)
        testPojo.title
        testPojo.description
        testPojo.priority
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.tvMVVM -> {
                startActivity(Intent(this, FetchDataActivity::class.java))
            }
            R.id.tvActivityLC -> {
                startActivity(Intent(this, ActivityLifeCycle::class.java))
            }
        }
    }
}