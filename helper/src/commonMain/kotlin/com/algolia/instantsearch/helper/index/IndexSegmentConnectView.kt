package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView
import com.algolia.search.client.Index


public fun IndexSegmentViewModel.connectView(
    view: SelectableSegmentView<Int, String>,
    presenter: (Index) -> String
) {
    view.setItems(items.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}