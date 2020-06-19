package com.algolia.instantsearch.demo.kensium.product

import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductViewHolder
import com.algolia.instantsearch.helper.android.inflate

class ProductAdapterOne :HitsView<Product>, PagedListAdapter<Product, ProductViewHolder>(ProductDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }



    object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {

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

    override fun setHits(hits: List<Product>) {
        submitList(hits as PagedList<Product>)
        notifyDataSetChanged()
    }
}