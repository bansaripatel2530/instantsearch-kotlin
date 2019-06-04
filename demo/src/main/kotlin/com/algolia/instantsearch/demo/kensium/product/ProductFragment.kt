package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.selectable.SelectableSegmentViewSpinner
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_facet_list.*
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val category = arguments!!.getString("categoryLvl0")!!
            val filter = Filter.Facet(Kensium.categoryLvl0, category)

            KensiumActivity.viewModel?.searcher.let {
                /**
                 * When we enter the screen, first we clear the FilterState.
                 * Then we add the "categoryLvl0" that was passed as an argument.
                 */
                it!!.filterState.notify {
                    clear()
                    add(Kensium.groupIDCategoryLvl0, filter)
                }
            }

        }

        KensiumActivity.viewModel?.searcher!!.onResponseChanged += {
            KensiumActivity.viewModel?.product!!.value?.dataSource?.invalidate()
        }




        KensiumActivity.viewModel?.product!!.observe(this, Observer { hits -> KensiumActivity.viewModel?.adapterProduct!!.submitList(hits) })


        KensiumActivity.viewModel?.indexSegmentViewModel!!.onSelectedComputed += {
            //            Log.e("tag","onSelectedComputed")
            KensiumActivity.viewModel?.product!!.value?.dataSource?.invalidate()

        }
    }

    private fun setSearchView() {
//        KensiumActivity.viewModel?.searcher?.let { KensiumActivity.searchBoxViewModel!!.connectSearcher(it) }
//        KensiumActivity.viewModel?.searcher?.let { (activity as KensiumActivity).configureSearcher(it) }
        KensiumActivity.viewModel?.adapterProduct?.let { (activity as KensiumActivity).configureRecyclerView(productList, it) }
        KensiumActivity.viewModel?.searcher!!.search()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val sortByView = SelectableSegmentViewSpinner(spinner, adapterSortBy)

        KensiumActivity.viewModel?.indexSegmentViewModel!!.connectView(sortByView, KensiumActivity.viewModel?.presenterSortBy!!)

        productList.adapter = KensiumActivity.viewModel?.adapterProduct
        setSearchView()
        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}