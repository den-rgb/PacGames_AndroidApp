package com.example.pacgamesandroid.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityGameListBinding
import com.example.pacgamesandroid.databinding.ActivityMainBinding
import com.example.pacgamesandroid.helpers.showImagePicker
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.GameModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber
import timber.log.Timber.i


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var game = GameModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Main Activity started...")

        if (intent.hasExtra("game_edit")) {
            edit = true
            game = intent.extras?.getParcelable("game_edit")!!
            binding.gameTitle.setText(game.title)
            binding.description.setText(game.description)
            binding.btnAdd.setText(R.string.save_game)
            Picasso.get()
                .load(game.image)
                .into(binding.gameImage)
            if (game.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_game_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            game.title = binding.gameTitle.text.toString()
            game.description = binding.description.text.toString()
            if (game.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.games.update(game.copy())
                } else {
                    app.games.create(game.copy())
                }
            }
            i("add Button Pressed: $game")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()

        binding.shopLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerMapCallback()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            game.image = result.data!!.data!!
                            Picasso.get()
                                .load(game.image)
                                .into(binding.gameImage)
                            binding.chooseImage.setText(R.string.change_game_image)
                        }// end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}