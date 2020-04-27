package com.algolia.instantsearch.demo.filter.facet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.inflate
import com.algolia.instantsearch.helper.filter.facet.FacetListItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.search.model.search.Facet


class FacetListAdapter :
    ListAdapter<FacetListItem, FacetListViewHolder>(diffUtil),
    FacetListView {

    override var onClick: ((Facet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacetListViewHolder {
        return FacetListViewHolder(parent.inflate(R.layout.list_item_selectable))
    }

    override fun onBindViewHolder(holder: FacetListViewHolder, position: Int) {
        val (facet, selected) = getItem(position)

        holder.bind(facet, selected, View.OnClickListener { onClick?.invoke(facet) })
    }



    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<FacetListItem>() {

            override fun areItemsTheSame(
                oldItem: FacetListItem,
                newItem: FacetListItem
            ): Boolean {
                return oldItem::class == newItem::class
            }

            override fun areContentsTheSame(
                oldItem: FacetListItem,
                newItem: FacetListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun setSelectableItems(selectableItems: List<SelectableItem<Facet>>) {
        submitList(selectableItems)
    }
}