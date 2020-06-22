package com.algolia.instantsearch.demo.filter.facet


import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.helper.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_radio_selectable.view.*


class CategoryListVIewHolderImpl(view: View) : FacetListViewHolder(view) {

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.rdselectableItemIcon.isChecked = selected
        view.rdselectableItemName.text = facet.highlightedOrNull?.let {
            HighlightTokenizer(preTag = "<b>", postTag = "</b>")(it).toSpannedString()
        } ?: facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return CategoryListVIewHolderImpl(parent.inflate(R.layout.list_item_radio_selectable))
        }
    }
}