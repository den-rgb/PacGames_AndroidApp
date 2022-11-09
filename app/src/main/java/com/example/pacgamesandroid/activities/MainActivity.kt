package com.example.pacgamesandroid.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pacgamesandroid.databinding.ActivityMainBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.GameModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var game = GameModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Game Activity started...")
        binding.btnAdd.setOnClickListener() {
            game.title = binding.gameTitle.text.toString()
            game.description = binding.description.text.toString()
            if (game.title.isNotEmpty()) {
                app.games.add(game.copy())
                i("add Button Pressed: $game")
                for (i in app.games.indices)
                { i("Game[$i]:${app.games[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}