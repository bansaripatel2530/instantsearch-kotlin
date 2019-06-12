package com.algolia.instantsearch.demo.kensium.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import kotlinx.android.synthetic.main.kensium_filter.*


class FilterFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)


        viewModel.brandCount.observe(this, Observer {
            if(isVisible){
                if(it == -1){
                    tvEmptyBrand.visibility = View.VISIBLE
                    brandFilterList.visibility = View.INVISIBLE
                }else{
                    tvEmptyBrand.visibility = View.GONE
                    brandFilterList.visibility = View.VISIBLE

                }
            }

        })

        viewModel.cateCount.observe(this, Observer {
            if(isVisible){
                if(it == -1){
                    tvEmptyCat.visibility = View.VISIBLE
                    categoryFilterList.visibility = View.INVISIBLE
                }else{
                    tvEmptyCat.visibility = View.GONE
                    categoryFilterList.visibility = View.VISIBLE

                }
            }

        })

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
            it.adapter = viewModel.adapterBrand
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        categoryFilterList.let {
            it.adapter = viewModel.adapterCategoryLvl1
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        priceFilterList.let {
            it.adapter = viewModel.adapterPrice
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        buttonClear.setOnClickListener {
            /**
             * This will clear only price, brand and categoryLvl0.
             * It will not clear categoryLvl0.
             */
            shared.filterState.notify {
                clear(Kensium.groupIDCategoryLvl1)
                clear(Kensium.groupIDPrice)
                clear(Kensium.groupIDBrand)
            }

            viewModel.brandCount.postValue(1)
            viewModel.cateCount.postValue(1)
        }
        buttonSubmit.setOnClickListener {
            viewModel.products.value?.dataSource?.invalidate()
            findNavController().popBackStack()
        }
    }
}