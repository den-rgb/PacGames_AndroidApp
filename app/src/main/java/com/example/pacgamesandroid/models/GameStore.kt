package com.example.pacgamesandroid.models

interface GameStore {
    fun findAll(): List<GameModel>
    fun findAllNames(): ArrayList<String>
    fun findByName(string: String): GameModel
    fun create(game: GameModel)
    fun update(game: GameModel)
    fun delete(game: GameModel)
}