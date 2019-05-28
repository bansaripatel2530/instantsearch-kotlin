package com.algolia.instantsearch.demo.kensium.product

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class Product(
    val name: String,
    val price: JsonObject,
    override val objectID: ObjectID
): Indexable