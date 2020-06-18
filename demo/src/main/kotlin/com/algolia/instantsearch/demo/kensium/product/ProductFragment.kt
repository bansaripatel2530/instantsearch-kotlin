package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.searcher.connectView
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.android.sortby.SortByViewAutocomplete
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.helper.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.helper.hierarchical.connectView
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.connectView
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.onResponseChangedThenUpdateNbHits
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel
    private val indexes = mapOf(
        0 to Kensium.index,
        1 to Kensium.index1,
        2 to Kensium.index2,
        3 to Kensium.index3,
        4 to Kensium.index4,
        5 to Kensium.index5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel!!
        if (arguments  != null) {
            val category = arguments!!.getString("categoryLvl0")!!
//            val filter = Filter.Facet(Kensium.categoryLvl0, category)
//            val filter1 = Filter.Facet(Kensium.categoryLvl0,category)
//            val filter1 = Filter.Facet(Kensium.categoryLvl2,"Hair /// Shampoo & Conditioner /// Shampoo")
            val filter1 = Filter.Facet(Kensium.categoryLvl0,"Heated Styling Tools")
//            val filter2 = Filter.Facet(Kensium.categoryLvl0,"Heated Styling Tools")

            viewModel.filterState.notify {
                add(Kensium.groupIDCategoryLvl0,filter1)
            }
        }


//        viewModel.product.observe(this, Observer {
//            if(isVisible){
//                if(it.isEmpty()){
//                    tvEmpty.visibility = View.VISIBLE
//                    productList.visibility = View.GONE
//                }else{
//                    tvEmpty.visibility = View.GONE
//                    productList.visibility = View.VISIBLE
//                }
//                productList.smoothScrollToPosition(0)
//            }
//            Log.e("results",it.toString())
//            viewModel.adapterProductOne.submitList(it)
//        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList.let {
            it.adapter = viewModel.adapterProductOne
            it.itemAnimator = null
        }

        val sortBy = SortByConnector(indexes, viewModel.searcher, selected = 0)
        val connection = ConnectionHandler(sortBy)
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val autoView = SortByViewAutocomplete(autoCompleteTextView, adapter)
        connection += viewModel.searcher.connectFilterState(viewModel.filterState)

        connection += sortBy.connectView(autoView) { index ->
            when (index) {
                Kensium.index -> "Relevance"
                Kensium.index1 -> "Lowest Price"
                Kensium.index2 -> "Highest Price"
                Kensium.index3 -> "Newest First"
                Kensium.index4 -> "Name Asc"
                Kensium.index5 -> "Name Desc"
                else -> index.indexName.raw
            }
        }
        connection += viewModel.searcher.connectHitsView(viewModel.adapterProductOne) { response ->
            response.hits.deserialize(Product.serializer())
        }

        viewModel.searcher.searchAsync()



        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}