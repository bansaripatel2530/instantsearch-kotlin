package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductAdapter
import com.algolia.search.model.response.ResponseSearch


class MovieAdapterPaged : PagedListAdapter<Product, MovieViewHolder>(ProductAdapter.diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return createMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        bind(holder, getItem(position))
    }
}