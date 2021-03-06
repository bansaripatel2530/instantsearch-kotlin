package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.category.CategoryFragment
import com.algolia.instantsearch.demo.kensium.subcategory.SubCategoryFragment
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import kotlinx.android.synthetic.main.kensium_activity.*


class KensiumActivity : AppCompatActivity() {
    var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)
        val searchBoxView = SearchBoxViewAppCompat(searchView!!)
        viewModel!!.searchBoxViewModel.connectView(searchBoxView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

//        searchView?.setOnQueryTextFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//                when (navHostFragment?.childFragmentManager?.fragments!![0].javaClass.canonicalName) {
//                    CategoryFragment::class.java.canonicalName -> {
//                        viewModel!!.filterState.notify {
//                            clear()
//                        }
//                        findNavController(navHostFragment.childFragmentManager.fragments[0]).navigate(R.id.action_fragmentCategory_to_fragmentProduct)
//                    }
//
//                }
//            }
//        }


        val closeButton = searchBoxView.searchView.findViewById(com.algolia.instantsearch.demo.R.id.search_close_btn) as ImageView
        val editText = searchBoxView.searchView.findViewById(com.algolia.instantsearch.demo.R.id.search_src_text) as EditText
        closeButton.setOnClickListener {
            when (navHostFragment?.childFragmentManager?.fragments!![0].javaClass.canonicalName) {
                CategoryFragment::class.java.canonicalName -> {
                    viewModel!!.filterState.notify {
                        clear()
                    }
                }

//                SubCategoryFragment::class.java.canonicalName -> {
//                }

            }

            editText.text.clear()
            searchBoxView.onQueryChanged?.invoke("")

        }


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBoxView.onQueryChanged?.invoke(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length > 3) {
                    searchBoxView.onQueryChanged?.invoke(newText)
                    when (navHostFragment?.childFragmentManager?.fragments!![0].javaClass.canonicalName) {
                        CategoryFragment::class.java.canonicalName -> {
                            viewModel!!.filterState.notify {
                                clear()
                            }
                            findNavController(navHostFragment.childFragmentManager.fragments[0]).navigate(R.id.action_fragmentCategory_to_fragmentProduct)
                        }

                        SubCategoryFragment::class.java.canonicalName -> {
                            findNavController(navHostFragment.childFragmentManager.fragments[0]).navigate(R.id.action_subCategoryFragment_to_fragmentProduct)
                        }

                    }
                }
                return false
            }

        })


    }


}