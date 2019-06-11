package com.algolia.instantsearch.demo.kensium.subcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import kotlinx.android.synthetic.main.kensium_category.*
import kotlinx.android.synthetic.main.kensium_subcate.*

class SubCategoryFragment : Fragment() {

    var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel



    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_subcate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnShampoo.setOnClickListener {
            val  bundle =  bundleOf("categoryLvl0" to "Shampoo")
            findNavController().navigate(R.id.action_subCategoryFragment_to_fragmentProduct,bundle)
        }
    }
}