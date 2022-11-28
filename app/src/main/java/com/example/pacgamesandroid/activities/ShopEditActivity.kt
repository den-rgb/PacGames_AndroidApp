package com.example.pacgamesandroid.activities


import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.adapters.ShopAdapter
import com.example.pacgamesandroid.adapters.ShopEditAdapter
import com.example.pacgamesandroid.databinding.ActivityMainBinding
import com.example.pacgamesandroid.databinding.CardShopBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding

import com.example.pacgamesandroid.helpers.showImagePicker
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber
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
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.layoutManager = layoutManager
        binding.recyclerView2.adapter = ShopEditAdapter(app.shops.findAll())
        var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
        var shopList = app.shops.shops
        var index = app.shops.shops.indexOf(chosen)
        binding.locationText.text = shopList[index].title
//            val shop_loc = resources.getStringArray()
//            val locAdapter = ArrayAdapter(this, R.layout.dropdown_item, shop_loc)
//            binding.autoCompleteTextView.setAdapter(locAdapter)
        //ArrayAdapter<GameModel>() gameAdapter = new ArrayAdapter<GameModel>(this, R.layout.dropdown_item, shop.games)

        val gameAdapter = ArrayAdapter(this, R.layout.dropdown_item, app.games.findAllNames())
        binding.gameBox.setAdapter(gameAdapter)



        i("Main Activity started...")



//        binding.shopLocation.setOnClickListener {
//            for (i in location) {
//                val launcherIntent = Intent(this, MapActivity::class.java)
//                    .putExtra("location", i)
//                mapIntentLauncher.launch(launcherIntent)
//            }
//        }


        binding.addGame.setOnClickListener {
            var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
            var shopList = app.shops.shops
            var index = app.shops.shops.indexOf(chosen)
            var chosenGame = gameStore.findByName(binding.gameBox.text.toString())
//            println("index ${index} chosen ${chosen}")
//            for (i in app.shops.shops){
//            println("shopList ${i}")}

            shopList[index].games.add(chosenGame)
////            game.location = binding.autoCompleteTextView.text.toString()
//            println("shop size $shopList[index].games[shopList[index].games.size].quantity= binding.quantiyInput.text.toString().toInt(){app.shops.shops[index].games.size}")
//            println("===========")
//            print("listSize ${shopList[index].games[shopList[index].games.size-1]}")
            var recent = shopList[index].games[shopList[index].games.size-1]
            recent.quantity= binding.quantiyInput.text.toString().toInt()
            recent.title = binding.gameBox.text.toString()
            recent.genre = chosenGame.genre
            recent.price = chosenGame.price
            recent.id = chosenGame.id
            recent.image = chosenGame.image
//            println("quantity ${app.shops.shops[index].games[shopStore.shops[index].games.size-1].quantity}")
            setResult(RESULT_OK)
            finish()
        }



        registerMapCallback()
    }



    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView2.adapter)?.
                notifyItemRangeChanged(0,app.shops.findAll().size)
            }
        }


}


