package com.algolia.instantsearch.demo.kensium.product

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable


@Serializable
data class Product(
    val name: String,
    override val objectID: ObjectID
): Indexable