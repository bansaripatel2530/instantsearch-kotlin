package com.algolia.instantsearch.demo.kensium.subcategory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class SubCategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(facet: Facet, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.selectableItemName.text = facet.value
    }
}