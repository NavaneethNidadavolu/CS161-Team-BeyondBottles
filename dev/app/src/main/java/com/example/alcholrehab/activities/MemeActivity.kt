package com.example.alcholrehab.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.MemesAdapter
import com.example.alcholrehab.models.MemeModel

class MemeActivity : BaseActivity() {

    var listBlogs : ArrayList<MemeModel> = ArrayList()
    var memeElement : MemeModel? = null

    private lateinit var rv_memes : RecyclerView
    private lateinit var toolbarLeaderboard : Toolbar

    private var mMemesAdapter : MemesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memes)

        addMemeData()
        rv_memes = findViewById(R.id.rv_memes)
        mMemesAdapter = MemesAdapter()
        rv_memes.layoutManager = LinearLayoutManager(this)
        rv_memes.adapter = mMemesAdapter

        mMemesAdapter!!.setData(listBlogs)

        toolbarLeaderboard = findViewById(R.id.tbMemes)
        setSupportActionBar(toolbarLeaderboard)
        toolbarLeaderboard.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun addMemeData(){
//        memeElement = MemeModel(0, "Alcohol Use in the Unites States", R.drawable.al1)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(1, "Alcohol Related Deaths in United States", R.drawable.al2)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(2, "Alcohol Use Disorder in the Unites States", R.drawable.al3)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(3, "Alcohol Use Disorder in the Unites States Adoloscents", R.drawable.al4)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(4, "Binge Drinking in the Unites States", R.drawable.al5)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(5, "US children living with Alcohol Use", R.drawable.al6)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(6, "Economic burden of alcohol use", R.drawable.al7)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(7, "Drinking or Going clubbing?", R.drawable.meme1)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(8, "Drink Lemonade", R.drawable.meme2)
//        listBlogs.add(memeElement!!)
//
//        memeElement = MemeModel(9, "I don't drink alcohol", R.drawable.meme3)
//        listBlogs.add(memeElement!!)
    }
}
