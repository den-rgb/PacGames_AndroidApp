package com.example.pacgamesandroid.models

import com.example.pacgamesandroid.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber.i
import kotlin.random.Random
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking

var lastId = 0

internal fun getId(): Int {
    return Random.nextInt(10000)
}
private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
class GameMemStore : GameStore {

    val games = ArrayList<GameModel>()

    override fun findAll(): List<GameModel> {
        return games
    }

    override fun findByName(string: String):GameModel {
        var found= GameModel()
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
//        val docRefShops = db.collection("shopList").document(user.uid)
//        docRefShops.get().addOnSuccessListener { documentSnapshot ->
//            var chosen = intent.extras?.getParcelable<ShopModel>("shop_edit")
//            val shopList = documentSnapshot.toObject<ShopListModel>()
//            val shop = documentSnapshot.toObject<ShopModel>()
        val docRef = db.collection("users").document(auth.currentUser!!.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            if (activeUser!!.games.size != 0) {
                for (g in activeUser.games) {
                    if (string.uppercase() == g.title.uppercase()) {
                        found = g
                    }
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