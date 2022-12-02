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
import com.example.pacgamesandroid.R

import com.example.pacgamesandroid.adapters.ShopEditAdapter
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding


import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
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
    var game = GameModel()
    lateinit var app: MainApp
    var edit = false
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var list = ArrayList<GameModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    list = arrayListOf()
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

        val user = auth.currentUser!!
        val docRefShops = db.collection("shopList").document(user.uid)
        docRefShops.get().addOnSuccessListener { documentSnapshot ->
            val activeShop = documentSnapshot.toObject<ShopModel>()
            binding.recyclerView2.adapter = ShopEditAdapter(activeShop)
            val shopList = documentSnapshot.toObject<ShopListModel>()
            var index = shopList!!.shops.indexOf(chosen)

            binding.locationText.text = shopList.shops[index].title
            println("index: ${index} + chosen: ${chosen} \n" +
                    "shops ${shopList}")
        }

//            val shop_loc = resources.getStringArray()
//            val locAdapter = ArrayAdapter(this, R.layout.dropdown_item, shop_loc)
//            binding.autoCompleteTextView.setAdapter(locAdapter)
        //ArrayAdapter<GameModel>() gameAdapter = new ArrayAdapter<GameModel>(this, R.layout.dropdown_item, shop.games)


        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            if (activeUser != null) {
                val gameAdapter =
                    ArrayAdapter(this, R.layout.dropdown_item, app.games.findAllNames(activeUser.games))
                binding.gameBox.setAdapter(gameAdapter)
                list = activeUser.games
            }
        }




        i("Main Activity started...")



        binding.locationText.setOnClickListener {
            val user = auth.currentUser!!
            val docRefShops = db.collection("shopList").document(user.uid)
            docRefShops.get().addOnSuccessListener { documentSnapshot ->
                var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
                val shopList = documentSnapshot.toObject<ShopListModel>()
                var index = shopList!!.shops.indexOf(chosen)
                val launcherIntent = Intent(this, MapActivity::class.java)
                    .putExtra("location", app.shops.location[index])
                mapIntentLauncher.launch(launcherIntent)
            }

        }


        binding.addGame.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            db = Firebase.firestore
            val user = auth.currentUser!!
            val docRefShops = db.collection("shopList").document(user.uid)
            docRefShops.get().addOnSuccessListener { documentSnapshot ->
                var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
                val shopList = documentSnapshot.toObject<ShopListModel>()
                val shop = documentSnapshot.toObject<ShopModel>()
                var index = shopList!!.shops.indexOf(chosen)
                var recent = GameModel()
                var gamePicked = binding.gameBox.text.toString()
                var quant = binding.quantiyInput.text.toString()
                var chosenGame = GameModel()

                    println("active user games: ${list}")
                    if (list.size != 0) {
                        for (g in list) {
                            if ( gamePicked.uppercase()== g.title.uppercase()) {
                                chosenGame = g
                            }
                        }
                    }




                if (shopList.shops[index].games.size != 0) {
                    for (i in shopList.shops[index].games) {
                        if (i.id == chosenGame.id) {
                            if (gamePicked.uppercase() != "CHOOSE GAME") {
                                recent = i
                                //println("2 pos: ${shopList.shops[index].games.indexOf(recent)} ------ id: ${recent.id}")
                                break
                            }
                        } else {
                            if (gamePicked.uppercase() != "CHOOSE GAME") {
//
                                shopList.shops[index].games.add(chosenGame.copy())

                                for (j in shopList.shops[index].games) {
                                    if (j == chosenGame) {
                                        recent = j
                                        //println("1 pos: ${shopList.shops[index].games.indexOf(recent)} ------ id: ${recent.id}")
                                        break
                                    }
                                }
                            }

                        }
                    }
                } else {
                    if (gamePicked.uppercase() != "CHOOSE GAME") {
                        chosenGame.quantity = binding.quantiyInput.text.toString().toInt()
                        chosenGame.price = "€" + chosenGame.price
//                    db.collection("shopGames").document(chosenGame.id.toString()).set(chosenGame.copy())
                        shopList.shops[index].games.add(chosenGame.copy())
                        println("shop chosen: ${shopList.shops[index]} + game chosen: ${chosenGame.copy()}")
                        for (j in shopList.shops[index].games) {
                            if (j == chosenGame) {
                                recent = j
                                //println("3 pos: ${shopList.shops[index].games.indexOf(recent)} ------ id: ${recent.id}")
                            }
                        }
                    }
                }
                if (gamePicked.uppercase() != "CHOOSE GAME") {

                    recent.quantity = binding.quantiyInput.text.toString().toInt()
                    recent.title = binding.gameBox.text.toString()
                    recent.genre = chosenGame.genre
                    recent.price = "€" + chosenGame.price
                    recent.id = chosenGame.id
                    println("recent: ${recent} + chosen: ${chosenGame}")
                    println("shop games: ${shopList.shops[index].games}")
                    val updates = hashMapOf<String, Any>(
                        "shops" to FieldValue.delete()
                    )
                    docRefShops.update(updates).addOnCompleteListener {
                        for (i in shopList.shops) {
                            docRefShops.update("shops", FieldValue.arrayUnion(i)).addOnSuccessListener {  }

                        }
                        (binding.recyclerView2.adapter)?.notifyItemRangeChanged(0, shopList.shops[index].games.size)
                        setResult(RESULT_OK)

                        finish()

                    }
                    //docRefShops.update(FieldPath.of("shops.${index}.games"), FieldValue.arrayUnion(recent.copy()))

                }

            }

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


