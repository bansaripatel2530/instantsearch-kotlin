package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.kensium_activity.*


class KensiumActivity : AppCompatActivity() {



        var searchBoxViewModel : SearchBoxViewModel?=null
        var searchBoxView:SearchBoxViewAppCompat ?= null
        var viewModel: KensiumViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)
//
//        searchBoxViewModel = SearchBoxViewModel()
//        searchBoxView = SearchBoxViewAppCompat(searchView)
//        searchBoxViewModel!!.connectView(searchBoxView!!)
//        searchBoxViewModel!!.connectSearcher(viewModel!!.searcher)

////
//        configureToolbar(toolbar)
//        configureSearcher(viewModel!!.searcher)
//        configureSearchView(searchView, getString(R.string.search_movies))
//        viewModel!!.searcher.search()

//        configureSearchView(searchView, getString(R.string.search_demos))
//        configureSearchBox(searchView, viewModel!!.searcher)
//        viewModel!!.searcher.search()
    }



    override fun onPause() {
        super.onPause()
    }
}