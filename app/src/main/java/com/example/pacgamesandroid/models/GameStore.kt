package com.example.pacgamesandroid.models

interface GameStore {
    fun findAll(): List<GameModel>
    fun findAllNames(game: ArrayList<GameModel>): ArrayList<String>
    fun findByName(string: String): GameModel
    fun create(game: GameModel) : GameModel
    fun update(game: GameModel) : GameModel
    fun delete(game: GameModel)
}