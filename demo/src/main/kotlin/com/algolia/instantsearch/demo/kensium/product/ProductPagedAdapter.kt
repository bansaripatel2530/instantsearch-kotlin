package com.algolia.instantsearch.demo.kensium.product

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.demo.list.bind
import com.algolia.instantsearch.demo.list.createProductViewHolder

class ProductPagedAdapter : PagedListAdapter<Product, ProductViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return createProductViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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