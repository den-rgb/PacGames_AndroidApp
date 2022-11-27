//package com.example.pacgamesandroid.adapters
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.pacgamesandroid.databinding.CardShopBinding
//import com.example.pacgamesandroid.models.ShopModel
//
//
//
//interface ShopListener {
//    fun onShopClick(shop: ShopModel)
//}
//class ShopAdapter constructor(private var shops: List<ShopModel>, private val listener: ShopListener) :
//    RecyclerView.Adapter<ShopAdapter.MainHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
//        val binding = CardShopBinding
//            .inflate(LayoutInflater.from(parent.context), parent, false)
//        return MainHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MainHolder, position: Int) {
//        val shop = shops[holder.adapterPosition]
//        holder.bind(shop, listener)
//    }
//
//    override fun getItemCount(): Int = shops.size
//
//    class MainHolder(private val binding : CardShopBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(shop: ShopModel, listener: ShopListener) {
//            binding.shopTitle.text = shop.title
//            binding.description.text = shop.description
//            binding.root.setOnClickListener { listener.onShopClick(shop) }
//        }
//    }
//}