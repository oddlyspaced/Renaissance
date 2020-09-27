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
import kotlinx.android.synthetic.main.item_post.view.imgPost
import kotlinx.android.synthetic.main.item_post.view.txPostTime
import kotlinx.android.synthetic.main.item_post.view.txPostTitle
import kotlinx.android.synthetic.main.item_post_author.view.*

class PostAdapter(private val list: ArrayList<Post>, private val feedType: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MIXED = 1
        const val TYPE_SINGLE = 2
    }

    class ViewHolderSingle(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val author: TextView = itemView.txPostAuthor
    }

    class ViewHolderMixed(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val category: TextView = itemView.txPostCategory
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(feedType) {
            TYPE_SINGLE -> ViewHolderSingle(LayoutInflater.from(parent.context).inflate(R.layout.item_post_author, parent, false))
            TYPE_MIXED -> ViewHolderMixed(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
            else -> throw Exception("Unknown feed type provided!")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(hol: RecyclerView.ViewHolder, position: Int) {
        when (feedType) {
            TYPE_SINGLE -> {
                val holder = hol as ViewHolderSingle
                val item = list[position]
                holder.title.text = item.title
                holder.time.text = item.created
                Picasso.get().load(item.thumbnail).into(holder.image)
                val authorText = "By ${item.user}"
                holder.author.text = authorText
            }
            TYPE_MIXED -> {
                val holder = hol as ViewHolderMixed
                val item = list[position]
                holder.title.text = item.title
                holder.time.text = item.created
                Picasso.get().load(item.thumbnail).into(holder.image)
                holder.category.text = item.category
            }
        }
    }

    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

}