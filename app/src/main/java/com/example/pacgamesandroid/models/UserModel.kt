package com.example.pacgamesandroid.models

//data class UserModel(var id: String = "",
//                     var name: String = "",
//                     var email: String = "",
//                     var games: ArrayList<GameModel>)

class UserModel {
    var id: String = ""
        private set
    var name: String = ""
        private set
    var email: String = ""
        private set
    var games: ArrayList<GameModel> = arrayListOf()


    constructor(id:String, name: String, email: String, games: ArrayList<GameModel>) {
        this.id = id
        this.name = name
        this.email = email
        this.games = games
    }

    //Add this
    constructor() {}
}

