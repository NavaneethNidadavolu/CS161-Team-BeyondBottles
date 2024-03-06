package com.example.alcholrehab.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.MainAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

open class BaseActivity  : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{



    lateinit var frameLayout: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView : NavigationView
    lateinit var navHeaderImg : ShapeableImageView
    lateinit var username : TextView
    lateinit var email : TextView
    lateinit var images : IntArray
    lateinit var sessionId : String
    var welcomeText : Int = 0

    val sharedPrefFile = "signInPrefs"

    private lateinit var recyclerviewMain : RecyclerView
    private lateinit var progressbarAnim : LottieAnimationView
    private lateinit var networkErrorAnim : LottieAnimationView


    override fun setContentView(layoutResID: Int) {

        drawerLayout = layoutInflater.inflate(R.layout.activity_base, null) as DrawerLayout
        frameLayout = drawerLayout.findViewById(R.id.drawer_frame) as FrameLayout
        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(drawerLayout)

        navView = findViewById(R.id.nav_view)
        navHeaderImg =  navView.getHeaderView(0).findViewById(R.id.navHeaderImg)

        username = navView.getHeaderView(0).findViewById(R.id.txtUsername)
        email = navView.getHeaderView(0).findViewById(R.id.txtEmail)

        val sharedPref = this.getSharedPreferences("signInPrefs", Context.MODE_PRIVATE)
        username.text = sharedPref.getString("user_name", "")
        email.text = sharedPref.getString("email", "")
        welcomeText = sharedPref.getInt("welcome_text", 0)

        //Nav view
        navView.bringToFront()
        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Alcholrehab)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when {
            id === R.id.nav_before_rehab -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, BlogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("clicked", 1)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
            id === R.id.nav_during_rehab -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, BlogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("clicked", 2)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
            id === R.id.nav_after_rehab -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, BlogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("clicked", 3)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
            id === R.id.nav_family_help -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, BlogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("clicked", 4)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
            id === R.id.nav_leaderboard -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, LeaderboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
            id === R.id.nav_memes -> {
                drawerLayout.closeDrawer(GravityCompat.START, false)
                val intent = Intent(this, MemeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                this.startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
}