package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.selectable.SelectableSegmentViewSpinner
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_product.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


//class ProductFragment : Fragment() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            val category = arguments!!.getString("categoryLvl0")!!
//            val filter = Filter.Facet(Kensium.categoryLvl0, category)
//
//            viewModel?.searcher.let {
//                /**
//                 * When we enter the screen, first we clear the FilterState.
//                 * Then we add the "categoryLvl0" that was passed as an argument.
//                 */
//                it!!.filterState.notify {
//
//                    clear()
//                    add(Kensium.groupIDCategoryLvl0, filter)
//                }
//            }
//
//        }
//
//        viewModel?.searcher!!.onResponseChanged += {
//            viewModel?.product!!.value?.dataSource?.invalidate()
//
//
//        }
//
//        viewModel?.product!!.observe(this, Observer {
//            viewModel?.adapterProduct!!.submitList(it)
//
//
//        })
//
//
////        viewModel?.product!!.observe(this, Observer { hits ->{
////        } })
//
//
//        viewModel?.indexSegmentViewModel!!.onSelectedComputed += {
//            //            Log.e("tag","onSelectedComputed")
//            viewModel?.product!!.value?.dataSource?.invalidate()
//
//        }
//    }
//
//    private fun setSearchView() {
////        viewModel?.searcher?.let { searchBoxViewModel!!.connectSearcher(it) }
////        viewModel?.searcher?.let { (activity as KensiumActivity).configureSearcher(it) }
//        viewModel?.adapterProduct?.let { (activity as KensiumActivity).configureRecyclerView(productList, it) }
//        viewModel?.searcher!!.search()
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.kensium_product, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
//        val sortByView = SelectableSegmentViewSpinner(spinner, adapterSortBy)
//
//        viewModel?.indexSegmentViewModel!!.connectView(sortByView, viewModel?.presenterSortBy!!)
//
//        productList.adapter = viewModel?.adapterProduct
////        setSearchView()
//        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
//    }
//}


class ProductFragment : Fragment() {

    private var check = 0
    var viewModel: KensiumViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel

        val category = arguments!!.getString("categoryLvl0")!!
        val filter = Filter.Facet(Kensium.categoryLvl0, category)

        viewModel?.searcher.let {
            Log.e("TAG","searcher")
            /**
             * When we enter the screen, first we clear the FilterState.
             * Then we add the "categoryLvl0" that was passed as an argument.
             */
            it?.filterState?.notify {
                clear()
                add(Kensium.groupIDCategoryLvl0, filter)
            }
        }
        viewModel?.searcher?.onResponseChanged?.plusAssign({
            Log.e("TAG","onResponseChanged")
            viewModel?.products?.value?.dataSource?.invalidate()
        })
        viewModel?.indexSegmentViewModel?.onSelectedComputed?.plusAssign {
            if(check++ > 1 ){
                Log.e("TAG","onSelectedComputed")
                viewModel?.products?.value?.dataSource?.invalidate()
            }

        }

        viewModel?.products?.observe(activity!!, Observer {
            if(isVisible){
                Log.e("TAG","before-->product")
                viewModel?.adapterProduct?.submitList(it)
                Log.e("TAG","after-->product")
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val sortByView = SelectableSegmentViewSpinner(spinner, adapterSortBy)

        viewModel?.indexSegmentViewModel?.connectView(sortByView, viewModel?.presenterSortBy!!)

        productList.let {
            it.adapter = viewModel?.adapterProduct
            it.layoutManager = LinearLayoutManager(context)
        }

        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}