package com.algolia.instantsearch.demo.kensium.subcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_subcate.*

class SubCategoryFragment : Fragment() {

    var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel
        val filter1 = Filter.Facet(Kensium.categoryLvl0,"Barber Tools")
//        viewModel?.filterState?.notify {
//            add(Kensium.groupIDCategoryLvl0,filter1)
//        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_subcate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnShampoo.setOnClickListener {
            val bundle = bundleOf("categoryLvl0" to "Blades")
            val filter1 = Filter.Facet(Kensium.categoryLvl0,"Spa Tools")
//            val filter2 = Filter.Facet(Kensium.categoryLvl0,"Heated Styling Tools")

            viewModel!!.filterState.notify {
                clear()
                add(Kensium.groupIDCategoryLvl0,filter1)
            }
            findNavController().navigate(R.id.action_subCategoryFragment_to_fragmentProduct, bundle)
            viewModel!!.searcher.searchAsync()

        }
    }
}