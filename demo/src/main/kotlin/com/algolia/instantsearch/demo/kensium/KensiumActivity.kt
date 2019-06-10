package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.category.CategoryFragment
import kotlinx.android.synthetic.main.kensium_activity.*


class KensiumActivity : AppCompatActivity() {
    var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)

        searchView?.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                when (navHostFragment?.childFragmentManager?.fragments!![0].javaClass.canonicalName) {
                    CategoryFragment::class.java.canonicalName -> {
                        viewModel!!.filterState.notify {
                            clear()
                        }
                        findNavController(navHostFragment.childFragmentManager.fragments[0]).navigate(R.id.navigateToFragmentProduct)
                    }

                }
            }
        }


    }


}