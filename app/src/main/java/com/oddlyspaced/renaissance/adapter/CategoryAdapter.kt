package com.oddlyspaced.renaissance.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.CategoryOption
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(private val list: ArrayList<Array<CategoryOption>>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardLeft: CardView = itemView.cvCategoryLeft
        val cardRight: CardView = itemView.cvCategoryRight
        val textLeft: TextView = itemView.txCategoryLeft
        val textRight: TextView = itemView.txCategoryRight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isEmpty() || list[0].isEmpty())
            return
        val item = list[position]
        holder.cardLeft.setCardBackgroundColor(ContextCompat.getColor(context, if (item[0].isSelected) R.color.colorCardDark else R.color.colorCardLight))
        holder.textLeft.setTextColor(ContextCompat.getColor(context, if(item[0].isSelected) R.color.colorTextCard else R.color.colorText))
        holder.textLeft.text = item[0].category.title
        holder.cardLeft.setOnClickListener {
            item[0].isSelected = !item[0].isSelected
            holder.cardLeft.setCardBackgroundColor(ContextCompat.getColor(context, if (item[0].isSelected) R.color.colorCardDark else R.color.colorCardLight))
            holder.textLeft.setTextColor(ContextCompat.getColor(context, if(item[0].isSelected) R.color.colorTextCard else R.color.colorText))
        }
        if (item.size > 1) {
            holder.cardRight.visibility = View.VISIBLE
            holder.cardRight.setCardBackgroundColor(ContextCompat.getColor(context, if (item[1].isSelected) R.color.colorCardDark else R.color.colorCardLight))
            holder.textRight.setTextColor(ContextCompat.getColor(context, if(item[1].isSelected) R.color.colorTextCard else R.color.colorText))
            holder.textRight.text = item[1].category.title
            holder.cardRight.setOnClickListener {
                item[1].isSelected = !item[1].isSelected
                holder.cardRight.setCardBackgroundColor(ContextCompat.getColor(context, if (item[1].isSelected) R.color.colorCardDark else R.color.colorCardLight))
                holder.textRight.setTextColor(ContextCompat.getColor(context, if(item[1].isSelected) R.color.colorTextCard else R.color.colorText))
            }
        }
    }

}