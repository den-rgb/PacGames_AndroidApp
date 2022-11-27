//package com.example.pacgamesandroid.activities
//
//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.pacgamesandroid.R
//
//import com.example.pacgamesandroid.main.MainApp
//import com.example.pacgamesandroid.adapters.ShopAdapter
//import com.example.pacgamesandroid.adapters.ShopListener
//import com.example.pacgamesandroid.databinding.ActivityGameListBinding
//import com.example.pacgamesandroid.models.ShopModel
//
//
//
//class ShopListActivity : AppCompatActivity(), ShopListener {
//    lateinit var app: MainApp
//    private lateinit var binding: ActivityGameListBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityGameListBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        app = application as MainApp
//
//        val layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = ShopAdapter(app.shops.findAll(), this)
//        binding.toolbar.title = title
//        setSupportActionBar(binding.toolbar)
//
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.item_add -> {
//                val launcherIntent = Intent(this, MainActivity::class.java)
//                getResult.launch(launcherIntent)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private val getResult =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                (binding.recyclerView.adapter)?.
//                notifyItemRangeChanged(0,app.shops.findAll().size)
//            }
//        }
//
//    override fun onShopClick(shop: ShopModel) {
//        val launcherIntent = Intent(this, MainActivity::class.java)
//        launcherIntent.putExtra("shop_edit", shop)
//        getClickResult.launch(launcherIntent)
//    }
//
//    private val getClickResult =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                (binding.recyclerView.adapter)?.
//                notifyItemRangeChanged(0,app.shops.findAll().size)
//            }
//        }
//
//}
//
