package com.example.pacgamesandroid.models

interface GameStore {
    fun findAll(): List<GameModel>
    fun create(game: GameModel)
    fun update(game: GameModel)
}