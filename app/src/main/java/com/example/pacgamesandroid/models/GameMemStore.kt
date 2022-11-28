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

    override fun findByName(string: String):GameModel {
        var found= GameModel()
        if (games.size!=0) {
            for (g in games) {
                if (string.uppercase() == g.title.uppercase()) {
                    found = g
                }
            }
        }
        return found
    }

    override fun findAllNames(): ArrayList<String> {
        var nameList = ArrayList<String>()
        for (g in games){
            nameList.add(g.title)
        }
        return nameList
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
            foundGame.image = game.image
            logAll()
        }
    }

    override fun delete(game: GameModel) {
        var foundGame: GameModel? = games.find { p -> p.id == game.id }
        games.remove(foundGame)
    }

    fun logAll() {
        games.forEach{ i("$it") }
    }
}