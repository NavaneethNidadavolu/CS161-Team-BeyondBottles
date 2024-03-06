package com.example.alcholrehab.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity

class MainEmptyActivity : AppCompatActivity() {
    private var activityIntent: Intent? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = this.getSharedPreferences("signInPrefs", Context.MODE_PRIVATE)
        activityIntent = if (sharedPref.getString("user_name","default") != "default") {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}