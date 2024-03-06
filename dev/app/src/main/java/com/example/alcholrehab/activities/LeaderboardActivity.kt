package com.example.alcholrehab.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.BlogAdapter
import com.example.alcholrehab.adapters.LeaderboardAdapter
import com.example.alcholrehab.models.BlogItem
import com.example.alcholrehab.models.LeadershipModel
import com.google.android.material.imageview.ShapeableImageView

class LeaderboardActivity : BaseActivity(){

    var listBlogs : ArrayList<LeadershipModel> = ArrayList()
    var leaderElement : LeadershipModel? = null

    private lateinit var toolbarLeaderboard : Toolbar
    private lateinit var rvLeaders : RecyclerView
    private var mLeaderAdapter : LeaderboardAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        addLeadershipData()
        rvLeaders = findViewById(R.id.rv_leaderboard)
        rvLeaders.layoutManager = LinearLayoutManager(this)

        mLeaderAdapter = LeaderboardAdapter()

        toolbarLeaderboard = findViewById(R.id.tbLeaderboard)
        setSupportActionBar(toolbarLeaderboard)


        toolbarLeaderboard.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        mLeaderAdapter!!.setData(listBlogs)
    }

    fun addLeadershipData(){
        leaderElement = LeadershipModel(0, "John Smith", "Rank 1", "Score : 1123")
        listBlogs.add(0, leaderElement!!)

        leaderElement = LeadershipModel(0, "Williams Brown", "Rank 2", "Score : 987")
        listBlogs.add(1, leaderElement!!)

        leaderElement = LeadershipModel(0, "Jones Garcia", "Rank 3", "Score : 874")
        listBlogs.add(2, leaderElement!!)

        leaderElement = LeadershipModel(0, "Davis Miller", "Rank 4", "Score : 677")
        listBlogs.add(3, leaderElement!!)

        leaderElement = LeadershipModel(0, "John Hernandez", "Rank 5", "Score : 674")
        listBlogs.add(4, leaderElement!!)

        leaderElement = LeadershipModel(0, "Anderson Thomas", "Rank 6", "Score : 597")
        listBlogs.add(5, leaderElement!!)

        leaderElement = LeadershipModel(0, "Taylor Moore", "Rank 7", "Score : 574")
        listBlogs.add(6, leaderElement!!)

        leaderElement = LeadershipModel(0, "Martin Lee", "Rank 8", "Score : 540")
        listBlogs.add(7, leaderElement!!)

        leaderElement = LeadershipModel(0, "Thompson White", "Rank 9", "Score : 530")
        listBlogs.add(8, leaderElement!!)

        leaderElement = LeadershipModel(0, "Scott Torres", "Rank 10", "Score : 487")
        listBlogs.add(9, leaderElement!!)


    }
}