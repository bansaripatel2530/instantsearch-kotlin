package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter


class MovieAdapter : ListAdapter<Movie, MoviewNewViewHolder>(DiffUtilMovie) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviewNewViewHolder {
        return createMovieNewViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MoviewNewViewHolder, position: Int) {
//        bind(holder, getItem(position))
    }
}