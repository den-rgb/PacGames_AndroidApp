package com.example.pacgamesandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pacgamesandroid.databinding.CardShopBinding
import com.example.pacgamesandroid.databinding.CardShopeditBinding
import com.example.pacgamesandroid.databinding.ActivityShopEditBinding
import com.example.pacgamesandroid.models.ShopModel



interface ShopEditListener {
    fun onShopClick(shop: ShopModel)
}
class ShopEditAdapter constructor(private var shops: List<ShopModel>, private val listener: ShopEditListener) :
    RecyclerView.Adapter<ShopEditAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = ActivityShopEditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val shop = shops[holder.adapterPosition]
        holder.bind(shop)
    }

    override fun getItemCount(): Int = shops.size

    class MainHolder(private val binding : ActivityShopEditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: ShopModel) {

//            for (g in shop.games!!) {
//                binding.gameTitle.text = g.title
//                binding.price.text = g.price
//                binding.gameGenre.text = g.genre
//                binding.quantity.text = "20"
//            }
        }
    }
}


