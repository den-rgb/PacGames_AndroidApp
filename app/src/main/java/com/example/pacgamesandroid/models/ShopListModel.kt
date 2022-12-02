package com.example.pacgamesandroid.models

//data class ShopListModel(var shops: ArrayList<ShopModel> = arrayListOf()){
//
//
//
//}

class ShopListModel {
    var shops: ArrayList<ShopModel> = arrayListOf()


    constructor(shops: ArrayList<ShopModel>) {
        this.shops = shops
    }

    //Add this
    constructor() {}
}

