package com.algolia.instantsearch.demo.filter.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.search.model.filter.Filter


class FacetListAdapter<T: Filter> :
    ListAdapter<SelectableItem<T>, FacetListViewHolder>(DiffUtilItem()),
    FilterListView<T> {

    override var onSelection: Callback<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacetListViewHolder {
        return FacetListViewHolder(parent.inflate(R.layout.list_item_selectable))
    }

    override fun onBindViewHolder(holder: FacetListViewHolder, position: Int) {
        val (filter, selected) = getItem(position)
        holder.bind(FilterPresenterImpl()(filter), selected, View.OnClickListener { onSelection?.invoke(filter) })
    }

    override fun setItems(items: List<SelectableItem<T>>) {
        submitList(items)
    }

    private class DiffUtilItem<T: Filter>: DiffUtil.ItemCallback<SelectableItem<T>>() {

        override fun areItemsTheSame(oldItem: SelectableItem<T>, newItem: SelectableItem<T>): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: SelectableItem<T>, newItem: SelectableItem<T>): Boolean {
            return oldItem == newItem
        }
    }
}