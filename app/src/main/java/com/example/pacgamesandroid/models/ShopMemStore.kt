package com.example.pacShopsandroid.models

import com.example.pacgamesandroid.models.ShopModel
import com.example.pacgamesandroid.models.ShopStore
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ShopMemStore : ShopStore {

    val shops = ArrayList<ShopModel>()

    override fun findAll(): List<ShopModel> {
        return shops
    }

    override fun create(shop: ShopModel) {
        shop.id = getId()
        shops.add(shop)
        logAll()
    }

    override fun update(Shop: ShopModel) {
        var foundShop: ShopModel? = shops.find { p -> p.id == Shop.id }
        if (foundShop != null) {
            foundShop.title = Shop.title
            foundShop.description = Shop.description
            foundShop.image = Shop.image
            logAll()
        }
    }

    fun logAll() {
        shops.forEach{ i("$it") }
    }
}