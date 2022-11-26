package com.example.pacgamesandroid.models



interface ShopStore {
    fun findAll(): List<ShopModel>
    fun create(shop: ShopModel)
    fun update(shop: ShopModel)
}