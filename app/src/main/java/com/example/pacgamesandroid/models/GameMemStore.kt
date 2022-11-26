package com.example.pacgamesandroid.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GameMemStore : GameStore {

    val games = ArrayList<GameModel>()

    override fun findAll(): List<GameModel> {
        return games
    }

    override fun create(game: GameModel) {
        game.id = getId()
        games.add(game)
        logAll()
    }

    override fun update(game: GameModel) {
        var foundGame: GameModel? = games.find { p -> p.id == game.id }
        if (foundGame != null) {
            foundGame.title = game.title
            foundGame.price = game.price
            foundGame.genre = game.genre
            foundGame.location = game.location
            foundGame.image = game.image
            logAll()
        }
    }

    fun logAll() {
        games.forEach{ i("$it") }
    }
}