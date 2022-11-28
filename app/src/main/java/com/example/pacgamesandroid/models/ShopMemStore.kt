package com.example.pacgamesandroid.models

import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber.i



class ShopMemStore : ShopStore , AppCompatActivity(){

    val shops = ArrayList<ShopModel>()
    var location = arrayOf(
        Location(53.286029, -6.24168, 15f), Location( 53.22027,-6.6596, 15f), Location(53.45375, -6.21923, 15f), Location(52.35314, -7.70071, 15f), Location(52.26016, -7.10993, 15f),
        Location(52.25998, -7.11081, 15f)
    )

    var shopNames = arrayOf("Dundrum","Naas","Swords","Newbridge","Clonmel","Waterford")

    override fun findAll(): List<ShopModel> {
        return shops
    }



    override fun create(){

        var i = 0

        for (s in location.indices) {
            val shop = ShopModel()
            shop.id = getId()
            shop.title = shopNames[i]
            i += 1
            shop.games
            shop.coordinates = location[s].lat.toString() + " " + location[s].lng.toString()
            shops.add(shop)
        }
        logAll()
    }

    override fun update(shop: ShopModel) {
        var foundShop: ShopModel? = shops.find { p -> p.id == shop.id }
        if (foundShop != null) {
            logAll()
        }
    }

    fun logAll() {
        shops.forEach{ i("$it") }
    }
}