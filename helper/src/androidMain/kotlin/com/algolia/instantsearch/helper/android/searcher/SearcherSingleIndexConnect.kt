package com.algolia.instantsearch.helper.android.searcher

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.helper.searcher.Searcher


fun Searcher.connectSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            setQuery(newText)
            search()
            return true
        }
    })
}