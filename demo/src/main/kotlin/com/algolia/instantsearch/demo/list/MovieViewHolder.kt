package com.algolia.instantsearch.demo.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.product.Product
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*


class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(movie: Product) {
        view.itemTitle.text = view.resources.getString(R.string.product_title).format(movie.name)
//        view.itemSubtitle.text = movie.genre.sorted().joinToString { it }

//        Glide.with(view)
//            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
//            .centerCrop()
//            .into(view.itemImage)
    }
}