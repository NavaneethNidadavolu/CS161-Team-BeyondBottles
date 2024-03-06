package com.example.alcholrehab.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.MainAdapter
import com.example.alcholrehab.models.QuestionsData
import com.example.alcholrehab.network.BaseProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import retrofit2.Response
import android.net.ConnectivityManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity(){

    private lateinit var menuIcon: ImageView
    private lateinit var recyclerviewMain : RecyclerView
    private lateinit var progressbarAnim : LottieAnimationView
    private lateinit var networkErrorAnim : LottieAnimationView
    private lateinit var createNewQuestion : FloatingActionButton


    var mAdapter : MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressbarAnim = findViewById(R.id.progressbar_anim)
        networkErrorAnim = findViewById(R.id.networkErrorAnim)
        createNewQuestion = findViewById(R.id.createNewQuestion)

        navView.setCheckedItem(R.id.nav_feed)

        progressbarAnim.isVisible = true
        networkErrorAnim.isVisible = false

        recyclerviewMain = findViewById(R.id.recyclerview_main)
        mAdapter = MainAdapter()
        recyclerviewMain.adapter = mAdapter
        recyclerviewMain.layoutManager = LinearLayoutManager(this)

        var response : Response<List<QuestionsData>>

        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        if (mWifi!!.isConnected) {

            lifecycleScope.launch {
                    response = getFeedData()
                    if (response.isSuccessful) {
                        progressbarAnim.isVisible = false
                        Log.v("feed data", response.body().toString())
                        response.body()?.let { mAdapter!!.setData(it) }
                    } else {
                        progressbarAnim.isVisible = false
                        networkErrorAnim.isVisible = true
                    }
            }
        }else {
            progressbarAnim.isVisible = false
            networkErrorAnim.isVisible = true
        }

        menuIcon = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        createNewQuestion.setOnClickListener {
            val showQueDetailed = Intent(applicationContext, PostQuestionActivity::class.java)
            this.startActivity(showQueDetailed)
        }
    }

    private suspend fun getFeedData()
            = BaseProvider.api.getFeedData()
}