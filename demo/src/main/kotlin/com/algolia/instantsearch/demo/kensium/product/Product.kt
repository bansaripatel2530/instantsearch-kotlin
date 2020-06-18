package com.algolia.instantsearch.demo.kensium.product

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


//data class Product(
//    val name: String
////    val price: JsonObject,
////    val thumbnail_url:String,
////    val sku:String,
////    override val objectID: ObjectID
//): Indexable

@Serializable
data class Product(
    val name: String,
    val price: JsonObject,
    val thumbnail_url:String,
    val sku:String,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable,Highlightable {

    @Transient
    public val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("name"))
}