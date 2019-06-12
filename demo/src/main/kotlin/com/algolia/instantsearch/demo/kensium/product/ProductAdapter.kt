package com.algolia.instantsearch.demo.kensium.product

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate


class ProductAdapter : PagedListAdapter<Product, ProductViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)

        if (product != null) holder.bind(product)

        holder.itemView.setOnClickListener {
            Log.e("Click-->",product?.name)
        }
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