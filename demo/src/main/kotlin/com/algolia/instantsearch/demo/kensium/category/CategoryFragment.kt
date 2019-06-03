package com.algolia.instantsearch.demo.kensium.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.kensium_category.*


class CategoryFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel
    private var isChange:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)

        viewModel.adapterCategoryLvl0.apply {
            onClick = { facet ->
                isChange = true
                val bundle =  bundleOf("categoryLvl0" to facet.value)

                findNavController().navigate(R.id.navigateToFragmentProduct, bundle)
            }
        }
        activity?.searchView?.setOnQueryTextListener(object :SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    if(!isChange){
                        isChange = true
                        findNavController().navigate(R.id.navigateToFragmentProduct)
                    }
                }
                return true
            }

        })
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryList.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = viewModel.adapterCategoryLvl0
        }
    }
}