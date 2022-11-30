package com.example.pacgamesandroid.activities


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.pacgamesandroid.adapters.ShopEditAdapter
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding

import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = CardShopeditBinding.inflate(layoutInflater)
        binding2 = ActivityShopEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        println(shopStore.shops.size)
        var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.layoutManager = layoutManager
        binding.recyclerView2.adapter = ShopEditAdapter(chosen)

        var shopList = app.shops.shops
        var index = app.shops.shops.indexOf(chosen)
        binding.locationText.text = shopList[index].title
//            val shop_loc = resources.getStringArray()
//            val locAdapter = ArrayAdapter(this, R.layout.dropdown_item, shop_loc)
//            binding.autoCompleteTextView.setAdapter(locAdapter)
        //ArrayAdapter<GameModel>() gameAdapter = new ArrayAdapter<GameModel>(this, R.layout.dropdown_item, shop.games)

        val gameAdapter = ArrayAdapter(this, com.example.pacgamesandroid.R.layout.dropdown_item, app.games.findAllNames())
        binding.gameBox.setAdapter(gameAdapter)




        i("Main Activity started...")



        binding.locationText.setOnClickListener {
            var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
            var index = app.shops.shops.indexOf(chosen)
                val launcherIntent = Intent(this, MapActivity::class.java)
                    .putExtra("location", app.shops.location[index])
                mapIntentLauncher.launch(launcherIntent)

        }


        binding.addGame.setOnClickListener {
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
                recent.image = chosenGame.image
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


