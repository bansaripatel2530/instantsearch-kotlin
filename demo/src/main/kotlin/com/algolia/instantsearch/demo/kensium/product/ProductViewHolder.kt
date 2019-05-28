package com.algolia.instantsearch.demo.kensium.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_selectable.view.*


class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.selectableItemName.text = product.name
        view.selectableItemSubtitle.text = product.price.getObject("USD").getPrimitive("default_formated").content
        view.selectableItemSubtitle.visibility = View.VISIBLE
    }
}