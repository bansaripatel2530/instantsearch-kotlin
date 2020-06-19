package com.algolia.instantsearch.demo.kensium.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.filter.facet.HierarchicalAdapter
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.helper.hierarchical.connectView
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.highlight
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_filter.*
import java.util.*


class FilterFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel
    private  var connection : ConnectionHandler? = null
    val adapter = HierarchicalAdapter()
    val viewNumeric = FilterListAdapter<Filter.Numeric>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)
        Log.e("Min",""+viewModel.priceAdapter.currentList)
        val priceListOne = FilterListConnector.Numeric(
            viewModel.priceFilterList!!
            , viewModel.filterState, groupID = viewModel.groupPrice)

        connection = ConnectionHandler(
            viewModel.brandList,
            priceListOne,
            viewModel.categoryList,
            viewModel.searcher.connectFilterState(viewModel.filterState)
        )


        connection!! += viewModel.brandList.connectView(viewModel.brandAdapter,viewModel.brandPresenter)
        connection!! += priceListOne.connectView(viewNumeric)
        connection!! += viewModel.categoryList.connectView(viewModel.categoryAdapter,viewModel.categoryPresenter)

//        connection!! += viewModel.hierarchical.connectView(adapter, HierarchicalPresenterImpl(viewModel.separator))

    }

  private fun onClearAllThenClearFilters(
       filterState: FilterState,
       filtersClearAll: View,
       connection: ConnectionHandler
    ) {
        val connector = FilterClearConnector(filterState,
            groupIDs = listOf(Kensium.groupIDCategoryLvl0),
            mode = ClearMode.Except)
        connection += connector
        connection += connector.connectView(FilterClearViewImpl(filtersClearAll))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClearAllThenClearFilters(viewModel.filterState,filtersClearAll,connection!!)
        viewModel.filterState.filters.subscribe {
            Log.e("Tag",""+it)
        }

//        viewModel.brandCount.observe(this, Observer {
//            Log.e("Brand",""+it)
//            if(isVisible){
//                if(it == -1){
//                    tvEmptyBrand.visibility = View.VISIBLE
//                    brandFilterList.visibility = View.INVISIBLE
//                }else{
//                    tvEmptyBrand.visibility = View.GONE
//                    brandFilterList.visibility = View.VISIBLE
//
//                }
//            }
//
//        })
//
//        viewModel.cateCount.observe(this, Observer {
//            if(isVisible){
//                if(it == -1){
//                    tvEmptyCat.visibility = View.VISIBLE
//                    categoryFilterList.visibility = View.INVISIBLE
//                }else{
//                    tvEmptyCat.visibility = View.GONE
//                    categoryFilterList.visibility = View.VISIBLE
//
//                }
//            }
//
//        })

//        viewModel.searcher.onResponseChanged += {
//            if(isVisible){
//                if(viewModel.adapterBrand.itemCount == 0 ){
//                    tvEmptyBrand.visibility = View.VISIBLE
//                }else{
//                    tvEmptyBrand.visibility = View.GONE
//                }
//                if(viewModel.adapterCategoryLvl1.itemCount == 0 ){
//                    tvEmptyCat.visibility = View.VISIBLE
//                }else{
//                    tvEmptyCat.visibility = View.GONE
//                }
//                if(viewModel.adapterPrice.itemCount == 0 ){
//                    tvEmptyPrice.visibility = View.VISIBLE
//                }else{
//                    tvEmptyPrice.visibility = View.GONE
//                }
//            }
//
//        }



        brandFilterList.let {
            it.adapter = viewModel.brandAdapter
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        categoryFilterList.let {
            it.adapter = viewModel.categoryAdapter
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        priceFilterList.let {
            it.adapter = viewNumeric
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }

        buttonSubmit.setOnClickListener {
            viewModel.product.value?.dataSource?.invalidate()
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection!!.disconnect()
    }
}