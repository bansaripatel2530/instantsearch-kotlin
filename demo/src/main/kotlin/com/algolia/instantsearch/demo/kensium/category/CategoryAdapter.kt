package com.algolia.instantsearch.demo.kensium.category

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.search.model.search.Facet


class CategoryAdapter : ListAdapter<Facet, CategoryViewHolder>(diffUtil) {

    var onClick: ((Facet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(parent.inflate(R.layout.list_item_selectable))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val facet = getItem(position)

        holder.bind(facet, View.OnClickListener { onClick?.invoke(facet) })
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<Facet>() {

            override fun areItemsTheSame(
                oldItem: Facet,
                newItem: Facet
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: Facet,
                newItem: Facet
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}