package com.oddlyspaced.renaissance.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.item_post.view.imgPost
import kotlinx.android.synthetic.main.item_post.view.txPostExtra
import kotlinx.android.synthetic.main.item_post.view.txPostTime
import kotlinx.android.synthetic.main.item_post.view.txPostTitle
import kotlinx.android.synthetic.main.layout_post_expanded.view.*
import org.xmlpull.v1.XmlPullParser

class PostAdapter(private val list: ArrayList<Post>, private val feedType: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MIXED = 1
        const val TYPE_SINGLE = 2
        const val LAYOUT_COMPACT = 3
        const val LAYOUT_EXPANDED = 4
    }

    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    class ViewHolderCompact(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val extra: TextView = itemView.txPostExtra
    }

    class ViewHolderExpanded(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val extra: TextView = itemView.txPostExtra
        val content: TextView = itemView.txPostContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        sharedPreferenceManager = SharedPreferenceManager(parent.context)
        return when (sharedPreferenceManager.getLayoutStyle()) {
            LAYOUT_COMPACT -> ViewHolderCompact(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
            LAYOUT_EXPANDED -> ViewHolderExpanded(LayoutInflater.from(parent.context).inflate(R.layout.layout_post_expanded, parent, false))
            else -> throw Exception("Unknown type of post layout!")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(hol: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (sharedPreferenceManager.getLayoutStyle()) {
            LAYOUT_COMPACT -> {
                val holder = hol as ViewHolderCompact
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
            LAYOUT_EXPANDED -> {
                val holder = hol as ViewHolderExpanded
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
                holder.content.text = Html.fromHtml(item.content)
            }
        }
    }

}