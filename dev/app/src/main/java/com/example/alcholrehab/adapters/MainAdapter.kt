package com.example.alcholrehab.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.activities.QuestionDetailActivity
import com.example.alcholrehab.models.QuestionsData
import com.google.android.material.imageview.ShapeableImageView
import java.io.Serializable

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>(), Serializable {

    lateinit var images : IntArray
    private var questionsList = emptyList<QuestionsData>()

    class MainViewHolder (ItemView : View) : RecyclerView.ViewHolder(ItemView) {
        val textUserName : TextView = itemView.findViewById(R.id.textUserName)
        val textQuestionBody : TextView = itemView.findViewById(R.id.textQuestionBody)
        val likesCount : TextView = itemView.findViewById(R.id.likesCount)
        val repliesCount : TextView = itemView.findViewById(R.id.repliesCount)
        val textCreatedAt : TextView = itemView.findViewById(R.id.textCreatedAt)
        val ivUserAvatar : ShapeableImageView = itemView.findViewById(R.id.ivUserAvatar)
        val layoutQuestion : TextView = itemView.findViewById(R.id.textQuestionBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_sample, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = questionsList[position]
        holder.textUserName.text = currentItem.username + ","
        holder.textQuestionBody.text = currentItem.question
        holder.likesCount.text = currentItem.upvotes.toString()
        loadImages(holder.ivUserAvatar)
        if(currentItem.comments < 0){
            holder.repliesCount.text = '0'.toString()
        }else{
            holder.repliesCount.text = currentItem.comments.toString()
        }

        holder.textCreatedAt.text = currentItem.time

        holder.layoutQuestion.setOnClickListener {
            val context = holder.itemView.context
            val showQueDetailed = Intent(context, QuestionDetailActivity::class.java)
            showQueDetailed.putExtra("adapterPosition", position)
            showQueDetailed.putExtra("username", currentItem.username)
            showQueDetailed.putExtra("questionid", currentItem.id)
            showQueDetailed.putExtra("question", currentItem.question)
            showQueDetailed.putExtra("upvotes", currentItem.upvotes)
            showQueDetailed.putExtra("comments", currentItem.comments)
            showQueDetailed.putExtra("time", currentItem.time)
            context.startActivity(showQueDetailed)
        }
    }

    override fun getItemCount(): Int {
        return  questionsList.size
    }

    fun setData(questionsLi: List<QuestionsData>){
        questionsList = questionsLi
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