package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductViewHolder

fun RecyclerView.Adapter<ProductViewHolder>.createProductViewHolder(parent: ViewGroup): ProductViewHolder {
    return ProductViewHolder(parent.inflate(R.layout.list_item_large))
}

fun RecyclerView.Adapter<MoviewNewViewHolder>.createMovieNewViewHolder(parent: ViewGroup): MoviewNewViewHolder {
    return MoviewNewViewHolder(parent.inflate(R.layout.list_item_large))
}

fun RecyclerView.Adapter<MovieViewHolder>.bind(holder: MoviewNewViewHolder, item: Movie?) {
    if (item != null) holder.bind(item)
}

fun RecyclerView.Adapter<ProductViewHolder>.bind(holder: ProductViewHolder, item: Product?) {
    if (item != null) holder.bind(item)
}

object DiffUtilMovie : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.objectID == newItem.objectID
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}