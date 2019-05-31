package com.algolia.instantsearch.demo.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter


class MovieAdapter : ListAdapter<Movie, MovieViewHolder>(DiffUtilMovie) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return createMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        bind(holder, getItem(position))
    }
}