package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.selectable.SelectableSegmentViewSpinner
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = arguments!!.getString("categoryLvl0")!!
        val filter = Filter.Facet(Kensium.categoryLvl0, category)
//        val searchBoxViewModel = SearchBoxViewModel()
//        val searchView1 = activity?.findViewById<SearchView>(R.id.searchView)
//        val searchBoxView = SearchBoxViewAppCompat(searchView1!!)
//        searchBoxViewModel.connectView(searchBoxView)
//        searchBoxViewModel.connectSearcher(viewModel.searcher)
//        searchBoxViewModel.onQueryChanged += {
//            viewModel.product.value?.dataSource?.invalidate()
//        }
        viewModel = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)
        viewModel.searcher.let {
            /**
             * When we enter the screen, first we clear the FilterState.
             * Then we add the "categoryLvl0" that was passed as an argument.
             */
            it.filterState.notify {
                clear()
                add(Kensium.groupIDCategoryLvl0, filter)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val sortByView = SelectableSegmentViewSpinner(spinner, adapterSortBy)

        viewModel.indexSegmentViewModel.connectView(sortByView, viewModel.presenterSortBy)

        productList.adapter = viewModel.adapterProduct

        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}