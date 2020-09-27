package com.oddlyspaced.renaissance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(private val list: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.time.text = item.created
        Picasso.get().load(item.thumbnail).into(holder.image)
    }

}