package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureSearchView
import com.algolia.instantsearch.demo.configureSearcher
import com.algolia.instantsearch.demo.configureToolbar
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.kensium_activity.*


class KensiumActivity : AppCompatActivity() {

    private var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)


        val searchBoxViewModel = SearchBoxViewModel()
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.connectSearcher(viewModel!!.searcher)
        searchBoxViewModel.onQueryChanged += {
            viewModel!!.product
                    .value?.dataSource?.invalidate()
        }

        configureToolbar(toolbar)
        configureSearcher(viewModel!!.searcher)
        configureSearchView(searchView, getString(R.string.search_movies))


    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.searcher.cancel()

    }
}