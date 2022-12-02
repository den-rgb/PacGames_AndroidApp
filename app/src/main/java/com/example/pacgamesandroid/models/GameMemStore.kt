package com.example.pacgamesandroid.models

import timber.log.Timber.i
import kotlin.random.Random
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking

var lastId = 0

internal fun getId(): Int {
    return Random.nextInt(10000)
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
                if (string.uppercase() == g.title.uppercase()) {      ///////////possibly creates new game
                    return g
                }
            }
        }
        return found

    }

    override fun findAllNames(game: ArrayList<GameModel>): ArrayList<String> {
        var nameList = ArrayList<String>()
        for (g in game){
            nameList.add(g.title)
        }
        return nameList
    }

    override fun create(game: GameModel): GameModel {
        game.id = getId()
        games.add(game)
        println("games size: ${games.size}")
        logAll()
        return games[games.indexOf(game)]
    }

    override fun update(game: GameModel): GameModel {
        var foundGame: GameModel? = games.find { p -> p.id == game.id }
        if (foundGame != null) {
            foundGame.title = game.title
            foundGame.price = game.price
            foundGame.genre = game.genre
            foundGame.image = game.image
            logAll()
        }
        return foundGame!!
    }

    override fun delete(game: GameModel)  {
        var foundGame: GameModel? = games.find { p -> p.id == game.id }
        games.remove(foundGame)
    }

    fun logAll() {
        games.forEach{ i("$it") }
    }
}