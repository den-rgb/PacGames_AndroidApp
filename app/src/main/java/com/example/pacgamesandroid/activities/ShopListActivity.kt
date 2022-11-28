package com.example.pacgamesandroid.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacgamesandroid.R

import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.adapters.ShopAdapter
import com.example.pacgamesandroid.adapters.ShopListener
import com.example.pacgamesandroid.databinding.ActivityGameListBinding
import com.example.pacgamesandroid.databinding.CardShopBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding
import com.example.pacgamesandroid.models.Location
import com.example.pacgamesandroid.models.ShopModel



class ShopListActivity : AppCompatActivity(), ShopListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ShopAdapter(app.shops.findAll(), this)

        if (app.shops.shops.size == 0) app.shops.create()

    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.shops.findAll().size)
            }
        }

    override fun onShopClick(shop: ShopModel) {
        val launcherIntent = Intent(this, ShopEditActivity::class.java)
        launcherIntent.putExtra("shop_edit", shop)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.shops.findAll().size)
            }
        }

}

