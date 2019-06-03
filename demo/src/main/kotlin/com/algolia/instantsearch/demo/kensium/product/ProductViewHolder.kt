package com.algolia.instantsearch.demo.kensium.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*
import kotlinx.android.synthetic.main.list_item_selectable.view.*
import kotlinx.serialization.json.JsonObject


class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.productTitle.text = view.resources.getString(R.string.product_title).format(product.name)
        view.productPrice.text = (product.price.get("USD") as JsonObject)["default_formated"].toString().replace("\"","")
        Glide.with(view)
                .load("https:${product.thumbnail_url}").placeholder(android.R.drawable.ic_media_play)
                .centerCrop()
                .into(view.productImage)
    }
}