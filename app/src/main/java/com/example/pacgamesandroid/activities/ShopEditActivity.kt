package com.example.pacgamesandroid.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.pacgamesandroid.adapters.ShopEditAdapter
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding

import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber.i


class ShopEditActivity : AppCompatActivity() {
    private lateinit var binding: CardShopeditBinding
    private lateinit var binding2: ActivityShopEditBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var gameStore = GameMemStore()
    var shopStore = ShopMemStore()
    var shop = ShopModel()
    var game = GameModel()
    lateinit var app: MainApp
    var edit = false
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = CardShopeditBinding.inflate(layoutInflater)
        binding2 = ActivityShopEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        app = application as MainApp
        db = Firebase.firestore
        println(shopStore.shops.size)
        var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.layoutManager = layoutManager
        val docRefShops = db.collection("shops").document(chosen!!.id.toString())
        docRefShops.get().addOnSuccessListener { documentSnapshot ->
            val activeShop = documentSnapshot.toObject<ShopModel>()
            binding.recyclerView2.adapter = ShopEditAdapter(activeShop)
        }


        var shopList = app.shops.shops
        var index = app.shops.shops.indexOf(chosen)
        binding.locationText.text = shopList[index].title
//            val shop_loc = resources.getStringArray()
//            val locAdapter = ArrayAdapter(this, R.layout.dropdown_item, shop_loc)
//            binding.autoCompleteTextView.setAdapter(locAdapter)
        //ArrayAdapter<GameModel>() gameAdapter = new ArrayAdapter<GameModel>(this, R.layout.dropdown_item, shop.games)
        val user = auth.currentUser!!

        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            if (activeUser != null) {
                val gameAdapter =
                    ArrayAdapter(this, com.example.pacgamesandroid.R.layout.dropdown_item, app.games.findAllNames(activeUser.games))
                binding.gameBox.setAdapter(gameAdapter)
            }
        }




        i("Main Activity started...")



        binding.locationText.setOnClickListener {
            var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
            var index = app.shops.shops.indexOf(chosen)
                val launcherIntent = Intent(this, MapActivity::class.java)
                    .putExtra("location", app.shops.location[index])
                mapIntentLauncher.launch(launcherIntent)

        }


        binding.addGame.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            db = Firebase.firestore
            var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
            var shopList = app.shops.shops
            var index = shopList.indexOf(chosen)
            var chosenGame = app.games.findByName(binding.gameBox.text.toString())
            var recent = GameModel()
            var gamePicked = binding.gameBox.text.toString()
            var quant = binding.quantiyInput.text.toString()

            if (shopList[index].games.size!=0) {
                for (i in shopList[index].games) {
                    if (i.id == chosenGame.id){
                        if ( gamePicked.uppercase()!="CHOOSE GAME") {
                            recent = i
                            println("2 pos: ${shopList[index].games.indexOf(recent)} ------ id: ${recent.id}")
                            break
                        }
                    }else{
                        if ( gamePicked.uppercase()!="CHOOSE GAME" ) {
//
                            shopList[index].games.add(chosenGame.copy())

                            for (j in shopList[index].games) {
                                if (j == chosenGame) {
                                    recent = j
                                    println("1 pos: ${shopList[index].games.indexOf(recent)} ------ id: ${recent.id}")
                                    break
                                }
                            }
                        }

                    }
                }
            }else{
                if ( gamePicked.uppercase()!="CHOOSE GAME") {

//                    db.collection("shopGames").document(chosenGame.id.toString()).set(chosenGame.copy())
                    shopList[index].games.add(chosenGame.copy())
                    for (j in shopList[index].games) {
                        if (j == chosenGame) {
                            recent = j
                            println("3 pos: ${shopList[index].games.indexOf(recent)} ------ id: ${recent.id}")
                        }
                    }
                }
            }
            if ( gamePicked.uppercase()!="CHOOSE GAME") {
                println("shop games: ${shopList[index].games}")

                recent.quantity = binding.quantiyInput.text.toString().toInt()
                recent.title = binding.gameBox.text.toString()
                recent.genre = chosenGame.genre
                recent.price = "€" + chosenGame.price
                recent.id = chosenGame.id
                val docRef2 = db.collection("shops").document(chosen!!.id.toString())
                docRef2.get().addOnSuccessListener {
                    docRef2.update("shopGames",FieldValue.arrayUnion(recent.copy()))
                }

                (binding.recyclerView2.adapter)?.notifyItemRangeChanged(0, shopList[index].games.size)
            }
            setResult(RESULT_OK)

            finish()
        }
        registerMapCallback()

        binding.ToShop.setOnClickListener {
            finish()
        }

    }



    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }



}


