package com.example.pacgamesandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.GameModel
import com.example.pacgamesandroid.models.ShopModel




class ShopEditAdapter(private var shop: ShopModel?) :
    RecyclerView.Adapter<ShopEditAdapter.MainHolder>() {
    lateinit var app: MainApp



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = ActivityShopEditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val shop = shop?.games?.get(holder.adapterPosition)
        holder.bind(shop)
    }

    override fun getItemCount(): Int {


        return shop!!.games.size
    }


    class MainHolder(private val binding : ActivityShopEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var app: MainApp
        fun bind(game: GameModel?) {
            if (game != null) {
                binding.gameTitle.text = game.title
                binding.gameEditPrice.text = game.price
                binding.genre.text = game.genre
                binding.gameQuantity.text = game.quantity.toString()
            }


        }
    }
}


