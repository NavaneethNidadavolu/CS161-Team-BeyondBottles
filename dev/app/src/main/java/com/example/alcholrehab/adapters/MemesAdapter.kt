package com.example.alcholrehab.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.models.MemeModel
import com.google.android.material.imageview.ShapeableImageView
import java.io.Serializable
import java.util.ArrayList

class MemesAdapter : RecyclerView.Adapter<MemesAdapter.MemesViewHolder>(), Serializable {

    class MemesViewHolder (ItemView : View) : RecyclerView.ViewHolder(ItemView) {

        val textReplierName : TextView = itemView.findViewById(R.id.textReplierName)
        val textRepliedOn : TextView = itemView.findViewById(R.id.textRepliedOn)
        val textReply : TextView = itemView.findViewById(R.id.textReply)
        val userImage : ShapeableImageView = itemView.findViewById(R.id.ivUserAvatar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemesViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MemesViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun setData(listBlogs: ArrayList<MemeModel>) {
        TODO("Not yet implemented")
    }
}