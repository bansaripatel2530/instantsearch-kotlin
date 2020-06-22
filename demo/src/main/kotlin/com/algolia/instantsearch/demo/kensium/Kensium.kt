package com.algolia.instantsearch.demo.kensium

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel


object Kensium  {
    val price = Attribute("price.USD.default")
    val brand = Attribute("brand")
    val categoryLvl0 = Attribute("categories.level0")
    val categoryLvl00 = Attribute("[categories.level0][0]")
    val categoryLvl1 = Attribute("categories.level1")
    val categoryLvl2 = Attribute("categories.level2")

    //comment kensium
    val groupIDCategoryLvl0 = FilterGroupID(categoryLvl0,FilterOperator.Or)
    val groupIDCategoryLvl1 = FilterGroupID(categoryLvl1,FilterOperator.Or)
    val groupIDCategoryLvl2 = FilterGroupID(categoryLvl2,FilterOperator.Or)

//    val groupIDCategoryLvl1 = FilterGroupID(categoryLvl1, FilterOperator.Or)
    val groupIDBrand = FilterGroupID(brand,FilterOperator.Or)
    val groupIDPrice = FilterGroupID(price,FilterOperator.And)

    private val applicationID = ApplicationID("HDLGLOLEHS")
    private val apiKey = APIKey("75025d6975e4c9a012de7163cde4e42d")
    private val client = ClientSearch(applicationID, apiKey, LogLevel.ALL)
    val indexName = IndexName("bbprod_default_products")
    private val indexNamePriceAsc = IndexName("bbprod_default_products_price_default_desc")
    private val indexNamePriceDes = IndexName("bbprod_default_products_price_default_asc")
    private val indexNameNewestFirst = IndexName("bbprod_default_products_created_at_desc")
    private val indexNameAsc = IndexName("bbprod_default_products_name_asc")
    private val indexNameDes = IndexName("bbprod_default_products_name_desc")
    val index = client.initIndex(indexName)
    val index1 = client.initIndex((indexNamePriceDes))
    val index2 = client.initIndex((indexNamePriceAsc))
    val index3 = client.initIndex((indexNameNewestFirst))
    val index4 = client.initIndex((indexNameAsc))
    val index5 = client.initIndex((indexNameDes))

    val indexPriceAsc = client.initIndex(indexNamePriceAsc)
    val indexPriceDes = client.initIndex(indexNamePriceDes)
    val indexNewestFirst = client.initIndex(indexNameNewestFirst)
    val indexNameAscName = client.initIndex(indexNameAsc)
    val indexNameDesName = client.initIndex(indexNameDes)

}