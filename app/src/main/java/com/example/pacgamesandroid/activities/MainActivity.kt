package com.example.pacgamesandroid.activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityMainBinding

import kotlinx.coroutines.tasks.await
import com.example.pacgamesandroid.helpers.showImagePicker
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.GameMemStore
import com.example.pacgamesandroid.models.GameModel
import com.example.pacgamesandroid.models.Location
import com.example.pacgamesandroid.models.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import timber.log.Timber.i


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var game = GameModel()
    var gameStore = GameMemStore()
    lateinit var app: MainApp
    var edit = false
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val shop_loc = resources.getStringArray(R.array.shop_locations)
//        val locAdapter = ArrayAdapter(this, R.layout.dropdown_item, shop_loc)
//        binding.autoCompleteTextView.setAdapter(locAdapter)

        val game_genre = resources.getStringArray(R.array.game_genres)
        val genreAdapter = ArrayAdapter(this, R.layout.dropdown_item, game_genre)
        binding.genreBox.setAdapter(genreAdapter)

        auth = FirebaseAuth.getInstance()
        app = application as MainApp
        db = Firebase.firestore

        val user = auth.currentUser!!

        val docRef = db.collection("users").document(user.uid)


        i("Main Activity started...")

        if (intent.hasExtra("game_edit")) {
            edit = true
            game = intent.extras?.getParcelable("game_edit")!!
            binding.gameTitle.setText(game.title)
            binding.price.setText(game.price)

            Picasso.get()
                .load(game.image)
                .into(binding.gameImage)
            if (game.image.toUri() != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_game_image)
            }
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()

//        binding.shopLocation.setOnClickListener {
//            for (i in location) {
//                val launcherIntent = Intent(this, MapActivity::class.java)
//                    .putExtra("location", i)
//                mapIntentLauncher.launch(launcherIntent)
//            }
//        }

        binding.gameAdd.setOnClickListener {
            game.title = binding.gameTitle.text.toString()
            game.price = binding.price.text.toString()
            game.genre = binding.genreBox.text.toString()
            if (game.title.isEmpty() || game.price.isEmpty() || game.genre.uppercase()=="CHOOSE GENRE") {
                    Snackbar.make(it,R.string.enter_game_title, Snackbar.LENGTH_LONG)
                        .show()
            } else {
                if (edit) {
                    docRef.get().addOnSuccessListener { documentSnapshot  ->
                        val activeUser = documentSnapshot.toObject<UserModel>()
                        if (activeUser != null) {
                            app.games.update(game.copy())
                            docRef.update("games",FieldValue.arrayUnion(game.copy()))
                        }else{
                            Toast.makeText(this, "Please log in to update", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    docRef.get().addOnSuccessListener { documentSnapshot ->
                        val activeUser = documentSnapshot.toObject<UserModel>()
                        if (activeUser != null) {
                            app.games.create(game.copy())
                            docRef.update("games", FieldValue.arrayUnion(game.copy()))
                        }
                    }
                }
            }
            i("add Button Pressed: $game")
            setResult(RESULT_OK)
            finish()


        }



        binding.itemCancel.setOnClickListener {
            finish()
        }



        registerMapCallback()
    }

    fun addGame() = runBlocking {
        delay(10000)

    }



    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            game.image = result.data!!.data.toString()
                            Picasso.get()
                                .load(game.image.toUri())
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


}