package com.example.alcholrehab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.models.BlogItem
import com.example.alcholrehab.models.LeadershipModel
import com.google.android.material.imageview.ShapeableImageView
import java.io.Serializable

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>(), Serializable{

    private var blogList = emptyList<LeadershipModel>()
    lateinit var images : IntArray

    class LeaderboardViewHolder (ItemView : View) : RecyclerView.ViewHolder(ItemView) {
        val textUserName : TextView = itemView.findViewById(R.id.textUserName)
        val ivUserAvatar : ShapeableImageView = itemView.findViewById(R.id.ivUserAvatar)
        val textRank : TextView = itemView.findViewById(R.id.textRank)
        val textScore : TextView = itemView.findViewById(R.id.textScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vh_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val currentItem = blogList[position]
        holder.textUserName.text = currentItem.username
        holder.textRank.text = currentItem.rank
        holder.textScore.text = currentItem.score
        loadImages(holder.ivUserAvatar)
    }

    override fun getItemCount(): Int
    {
        return blogList.size
    }

    fun setData(questionsLi: List<LeadershipModel>){
        blogList = questionsLi
        notifyDataSetChanged()
    }

    private fun loadImages(imageView: ShapeableImageView){
        images = intArrayOf(
            R.drawable.artboard_1, R.drawable.artboard_10, R.drawable.artboard_11,
            R.drawable.artboard_12, R.drawable.artboard_13, R.drawable.artboard_14,
            R.drawable.artboard_15, R.drawable.artboard_16, R.drawable.artboard_17,
            R.drawable.artboard_18, R.drawable.artboard_19, R.drawable.artboard_20,
            R.drawable.artboard_21, R.drawable.artboard_22, R.drawable.artboard_23,
            R.drawable.artboard_24, R.drawable.artboard_25, R.drawable.artboard_26,
            R.drawable.artboard_27, R.drawable.artboard_28, R.drawable.artboard_29,
            R.drawable.artboard_30, R.drawable.artboard_31, R.drawable.artboard_32,
            R.drawable.artboard_33, R.drawable.artboard_34, R.drawable.artboard_35,
            R.drawable.artboard_36, R.drawable.artboard_37, R.drawable.artboard_38,
            R.drawable.artboard_39, R.drawable.artboard_40, R.drawable.artboard_41,
            R.drawable.artboard_42, R.drawable.artboard_43, R.drawable.artboard_44,
        )

        val imageId = (Math.random() * images.size).toInt()
        imageView.setImageResource(images[imageId])
    }


}