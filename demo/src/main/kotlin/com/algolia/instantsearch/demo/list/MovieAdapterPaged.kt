package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductAdapter
import com.algolia.search.model.response.ResponseSearch


class MovieAdapterPaged : PagedListAdapter<Product, MovieViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return createMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        bind(holder, getItem(position))
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<Product>() {

            override fun areItemsTheSame(
                    oldItem: Product,
                    newItem: Product
            ): Boolean {
                return oldItem.objectID == newItem.objectID
            }

            override fun areContentsTheSame(
                    oldItem: Product,
                    newItem: Product
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}