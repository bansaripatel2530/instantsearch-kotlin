package com.algolia.instantsearch.demo.kensium.product

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate


class ProductAdapter : ListAdapter<Product, ProductViewHolder>(ProductDiffUtil), HitsView<Product> {



    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Product>) {
        submitList(hits)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.list_item_large))
    }

}
