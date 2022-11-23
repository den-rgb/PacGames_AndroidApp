package com.example.pacgamesandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pacgamesandroid.databinding.CardGameBinding
import com.example.pacgamesandroid.models.GameModel
import com.squareup.picasso.Picasso

interface GameListener {
    fun onGameClick(game: GameModel)
}
class GameAdapter constructor(private var games: List<GameModel>, private val listener: GameListener) :
    RecyclerView.Adapter<GameAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGameBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val game = games[holder.adapterPosition]
        holder.bind(game, listener)
    }

    override fun getItemCount(): Int = games.size

    class MainHolder(private val binding : CardGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameModel, listener: GameListener) {
            binding.gameTitle.text = game.title
            binding.description.text = game.description
            Picasso.get().load(game.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onGameClick(game) }
        }
    }
}