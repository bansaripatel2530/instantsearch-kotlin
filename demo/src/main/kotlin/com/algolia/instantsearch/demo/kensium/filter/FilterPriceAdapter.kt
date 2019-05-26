package com.algolia.instantsearch.demo.kensium.filter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.search.model.filter.Filter


class FilterPriceAdapter(val presenter: FilterPresenter) :
    ListAdapter<SelectableItem<Filter.Numeric>, FilterPriceViewHolder>(DiffUtilItem()),
    FilterListView<Filter.Numeric> {

    override var onClick: ((Filter.Numeric) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterPriceViewHolder {
        return FilterPriceViewHolder(parent.inflate(R.layout.list_item_selectable))
    }

    override fun onBindViewHolder(holder: FilterPriceViewHolder, position: Int) {
        val (filter, selected) = getItem(position)

        holder.bind(presenter(filter), selected, View.OnClickListener { onClick?.invoke(filter) })
    }

    override fun setSelectableItems(selectableItems: List<SelectableItem<Filter.Numeric>>) {
        submitList(selectableItems)
    }

    private class DiffUtilItem: DiffUtil.ItemCallback<SelectableItem<Filter.Numeric>>() {

        override fun areItemsTheSame(oldItem: SelectableItem<Filter.Numeric>, newItem: SelectableItem<Filter.Numeric>): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: SelectableItem<Filter.Numeric>, newItem: SelectableItem<Filter.Numeric>): Boolean {
            return oldItem == newItem
        }
    }
}