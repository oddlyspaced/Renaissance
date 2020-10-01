package com.oddlyspaced.renaissance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.Post
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.imgPost
import kotlinx.android.synthetic.main.item_post.view.txPostTime
import kotlinx.android.synthetic.main.item_post.view.txPostTitle

class PostAdapter(private val list: ArrayList<Post>, private val feedType: Int): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    companion object {
        const val TYPE_MIXED = 1
        const val TYPE_SINGLE = 2
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val extra: TextView = itemView.txPostExtra
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.time.text = item.created
        Glide.with(holder.itemView.context).load(item.thumbnail).into(holder.image)
        val authorText = "By ${item.user}"
        when (feedType) {
            TYPE_SINGLE -> {
                holder.extra.text = authorText
            }
            TYPE_MIXED -> {
                holder.extra.text = item.category
            }
        }
    }

}