package com.example.pacgamesandroid.models



interface ShopStore {
    fun findAll(): List<ShopModel>
    fun create()
    fun update(shop: ShopModel)
}