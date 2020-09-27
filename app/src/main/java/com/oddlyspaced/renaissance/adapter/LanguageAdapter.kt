package com.oddlyspaced.renaissance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oddlyspaced.renaissance.R
import com.oddlyspaced.renaissance.modal.Language
import com.oddlyspaced.renaissance.modal.LanguageOption
import kotlinx.android.synthetic.main.item_language.view.*

class LanguageAdapter(private val list: ArrayList<Array<LanguageOption>>): RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardLeft: CardView = itemView.cvLanguageLeft
        val cardRight: CardView = itemView.cvLanguageRight
        val textLeft: TextView = itemView.txLanguageLeft
        val textRight: TextView = itemView.txLanguageRight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false))
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
        holder.textLeft.text = item[0].language.language
        holder.cardLeft.setOnClickListener {
            item[0].isSelected = !item[0].isSelected
            unselectAll(item[0])
            holder.cardLeft.setCardBackgroundColor(ContextCompat.getColor(context, if (item[0].isSelected) R.color.colorCardDark else R.color.colorCardLight))
            holder.textLeft.setTextColor(ContextCompat.getColor(context, if(item[0].isSelected) R.color.colorTextCard else R.color.colorText))
        }
        if (item.size > 1) {
            holder.cardRight.visibility = View.VISIBLE
            holder.cardRight.setCardBackgroundColor(ContextCompat.getColor(context, if (item[1].isSelected) R.color.colorCardDark else R.color.colorCardLight))
            holder.textRight.setTextColor(ContextCompat.getColor(context, if(item[1].isSelected) R.color.colorTextCard else R.color.colorText))
            holder.textRight.text = item[1].language.language
            holder.cardRight.setOnClickListener {
                item[1].isSelected = !item[1].isSelected
                unselectAll(item[1])
                holder.cardRight.setCardBackgroundColor(ContextCompat.getColor(context, if (item[1].isSelected) R.color.colorCardDark else R.color.colorCardLight))
                holder.textRight.setTextColor(ContextCompat.getColor(context, if(item[1].isSelected) R.color.colorTextCard else R.color.colorText))
            }
        }
    }

    private fun unselectAll(exception: LanguageOption) {
        list.forEach {
            if (it[0] != exception) {
                it[0].isSelected = false
            }
            if (it.size > 1) {
                if (it[1] != exception) {
                    it[1].isSelected = false
                }
            }
        }
        notifyDataSetChanged()
    }

}