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
import com.example.pacgamesandroid.models.GameMemStore
import com.example.pacgamesandroid.models.GameModel
import com.example.pacgamesandroid.models.Location
import com.example.pacgamesandroid.models.ShopModel
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

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.layoutManager = layoutManager
        binding.recyclerView2.adapter = ShopEditAdapter(app.shops.findAll())

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

//            game.location = binding.autoCompleteTextView.text.toString()
            shop.games.add(gameStore.findByName(binding.gameBox.text.toString()))
            println(shop.games.size)
            println("===========")
            shop.games[shop.games.size-1].quantity = binding.quantiyInput.text.toString().toInt()
            println(shop.games[shop.games.size-1].quantity)
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


