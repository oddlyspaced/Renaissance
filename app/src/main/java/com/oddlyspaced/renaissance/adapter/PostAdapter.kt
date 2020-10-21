package com.oddlyspaced.renaissance.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.Post
import com.oddlyspaced.renaissance.util.SavedPostDatabaseManager
import com.oddlyspaced.renaissance.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.item_post.view.imgPost
import kotlinx.android.synthetic.main.item_post.view.txPostExtra
import kotlinx.android.synthetic.main.item_post.view.txPostTime
import kotlinx.android.synthetic.main.item_post.view.txPostTitle
import kotlinx.android.synthetic.main.layout_post_expanded.view.*

class PostAdapter(private val list: ArrayList<Post>, private val feedType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MIXED = 1
        const val TYPE_SINGLE = 2
        const val TYPE_QUICK = 3
        const val LAYOUT_COMPACT = 11
        const val LAYOUT_EXPANDED = 12
    }

    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var savedPostDatabaseManager: SavedPostDatabaseManager

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
        val cardSource: CardView = itemView.cvPostSource
        val sourceText: TextView = itemView.txPostSource
        val cardSaved: CardView = itemView.cvSave
        val imageSaved: ImageView = itemView.imgSave
    }

    class ViewHolderQuick(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txPostTitle
        val time: TextView = itemView.txPostTime
        val image: ImageView = itemView.imgPost
        val extra: TextView = itemView.txPostExtra
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        sharedPreferenceManager = SharedPreferenceManager(parent.context)
        savedPostDatabaseManager = SavedPostDatabaseManager(parent.context)
        return when (feedType) {
            TYPE_QUICK -> ViewHolderQuick(LayoutInflater.from(parent.context).inflate(R.layout.item_quick_bit, parent, false))
            else -> return when (sharedPreferenceManager.getLayoutStyle()) {
                LAYOUT_COMPACT -> ViewHolderCompact(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
                LAYOUT_EXPANDED -> ViewHolderExpanded(LayoutInflater.from(parent.context).inflate(R.layout.layout_post_expanded, parent, false))
                else -> throw Exception("Unknown type of post layout!")
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(hol: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (feedType) {
            TYPE_QUICK -> {
                val holder = hol as ViewHolderQuick
                holder.title.text = item.title
                holder.time.text = item.created
                Glide.with(holder.itemView.context).load(item.thumbnail).into(holder.image)
                val authorText = "By ${item.user}"
                holder.extra.text = authorText
                return
            }
        }
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
                holder.sourceText.text = item.anchor_text
                holder.cardSource.setOnClickListener {
                    Toast.makeText(hol.itemView.context, "Load webview here", Toast.LENGTH_SHORT).show()
                }
                holder.imageSaved.setImageDrawable(ContextCompat.getDrawable(hol.itemView.context,  if (savedPostDatabaseManager.checkPostSaved(item)) R.drawable.ic_saved else R.drawable.ic_save))
                holder.cardSaved.setOnClickListener {
                    if (savedPostDatabaseManager.checkPostSaved(item)) {
                        savedPostDatabaseManager.deleteSavedPost(item)
                    }
                    else {
                        savedPostDatabaseManager.addSavedPost(item)
                    }
                    holder.imageSaved.setImageDrawable(ContextCompat.getDrawable(hol.itemView.context,  if (savedPostDatabaseManager.checkPostSaved(item)) R.drawable.ic_saved else R.drawable.ic_save))
                }
            }
        }
    }

}