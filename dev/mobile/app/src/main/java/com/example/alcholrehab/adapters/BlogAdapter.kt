package com.example.alcholrehab.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import java.io.Serializable
import android.view.LayoutInflater
import com.example.alcholrehab.models.BlogItem

class BlogAdapter : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>(), Serializable {

    private var blogList = emptyList<BlogItem>()

    class BlogViewHolder (ItemView : View) : RecyclerView.ViewHolder(ItemView) {
        val headerBlog : TextView = itemView.findViewById(R.id.headerBlog)
        val contentBlog : TextView = itemView.findViewById(R.id.contentBlog)
        val readMore : TextView = itemView.findViewById(R.id.readMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vh_blog, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val currentItem = blogList[position]
        holder.headerBlog.text = currentItem.contentHeader
        holder.contentBlog.text = currentItem.content
        holder.readMore.movementMethod = LinkMovementMethod.getInstance()
        holder.readMore.setLinkTextColor(Color.BLUE)
        holder.readMore.makeHyperLink(currentItem.link)
    }

    fun setData(questionsLi: List<BlogItem>){
        blogList = questionsLi
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    fun TextView.makeHyperLink(url: String) {
        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml("<a href='${url}'>${text}</a>", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml("<a href='${url}'>${text}</a>")
        }
        movementMethod = LinkMovementMethod.getInstance()
        isClickable = true
    }

    override fun getItemCount(): Int {
        return blogList.size
    }
}