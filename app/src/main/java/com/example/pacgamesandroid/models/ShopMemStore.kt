package com.example.pacgamesandroid.models

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.scan
import timber.log.Timber.i



class ShopMemStore : ShopStore , AppCompatActivity(){
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    val shops = ArrayList<ShopModel>()
    var sL = ShopListModel()

    var location = arrayOf(
        Location(53.286029, -6.24168, 15f), Location( 53.22027,-6.6596, 15f), Location(53.45375, -6.21923, 15f), Location(52.35314, -7.70071, 15f), Location(52.26016, -7.10993, 15f),
    )
    var counter = 0

    var shopNames = arrayOf("Dundrum","Naas","Swords","Clonmel","Waterford")

    override fun findAll(): List<ShopModel> {
        return shops
    }



    override fun create(){
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        var i = 0
        val docRefShops = db.collection("shopList").document(auth.currentUser!!.uid)
        db.collection("shopList").document(auth.currentUser!!.uid).set(ShopListModel(arrayListOf()))
        for (s in location.indices) {
            val shop = ShopModel()
            shop.id = getId()
            shop.title = shopNames[i]
            i += 1
            shop.games
            shop.coordinates = location[s].lat.toString()+"°" + " " + location[s].lng.toString()+"°"
            shops.add(shop)
            sL.shops.add(shop.copy())
            println("shopList model: ${sL.shops}")
            docRefShops.update("shops", FieldValue.arrayUnion(shop.copy()))
//            val docRefShops = db.collection("shops").document(shop.id.toString())
//            docRefShops.get().addOnSuccessListener { documentSnapshot ->
//                val activeShop = documentSnapshot.toObject<ShopModel>()
//            }
//            db.collection("shops").document(shop.id.toString()).set(shop)
//            docRefShops.update("games", FieldValue.arrayUnion(shop.copy()))
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