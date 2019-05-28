package com.algolia.instantsearch.demo.kensium.filter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_selectable.view.*


class FilterPriceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(text: String, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemName.text = text
        view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }
}