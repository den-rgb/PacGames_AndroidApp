package com.example.pacgamesandroid.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacgamesandroid.adapters.ShopAdapter
import com.example.pacgamesandroid.adapters.ShopListener
import com.example.pacgamesandroid.databinding.ActivityGameListBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.ShopListModel
import com.example.pacgamesandroid.models.ShopModel
import com.example.pacgamesandroid.models.getId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ShopListActivity : AppCompatActivity(), ShopListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
//    var lastShop = ShopModel()
    val id = getId().toString()
    lateinit var list : ArrayList<ShopModel>
    var shoplist = ShopListModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        list = arrayListOf()
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val user = auth.currentUser!!
//        if (app.shops.shops.size == 0){
//            app.shops.create()
//        }


        val docRefShops = db.collection("shopList").document(user.uid)
        docRefShops.get().addOnSuccessListener { documentSnapshot ->
            val shopList = documentSnapshot.toObject<ShopListModel>()
//
            binding.recyclerView.adapter = ShopAdapter(shopList!!.shops, this)
        }


        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing  =false
        }




    }




    override fun onShopClick(shop: ShopModel) {
        val launcherIntent = Intent(this, ShopEditActivity::class.java)
        launcherIntent.putExtra("shop_edit", shop)

        getClickResult.launch(launcherIntent)
//        lastShop = shop
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,list.size)
            }
        }

}

