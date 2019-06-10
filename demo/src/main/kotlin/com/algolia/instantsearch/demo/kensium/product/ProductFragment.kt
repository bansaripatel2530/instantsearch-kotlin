package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.index.IndexSegmentViewSpinner
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_activity.*
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel!!

        if (arguments  != null) {
            val category = arguments!!.getString("categoryLvl0")!!
            val filter = Filter.Facet(Kensium.categoryLvl0, category)
            viewModel.filterState.notify {
                clear()
                add(Kensium.groupIDCategoryLvl0, filter)
            }
        }


        viewModel.products.observe(this, Observer { hits -> viewModel.adapterProduct.submitList(hits) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val sortByView = IndexSegmentViewSpinner(spinner, adapterSortBy)

        viewModel.indexSegmentViewModel.connectView(sortByView, viewModel.presenterSortBy)
        viewModel.searchBoxViewModel.connectView(SearchBoxViewAppCompat(activity?.searchView!!))
        productList.let {
            it.adapter = viewModel.adapterProduct
            it.itemAnimator = null
        }

        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}