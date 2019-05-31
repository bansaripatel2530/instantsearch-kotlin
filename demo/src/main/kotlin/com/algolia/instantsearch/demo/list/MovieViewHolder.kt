package com.algolia.instantsearch.demo.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.product.Product
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*
import kotlinx.serialization.json.JsonObject


class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.productTitle.text = view.resources.getString(R.string.product_title).format(product.name)
        view.productPrice.text = (product.price.get("USD") as JsonObject)["default_formated"].toString().replace("\"","")
        Glide.with(view)
            .load("http:${product.thumbnail_url}").placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(view.productImage)
    }



}