package com.example.pacgamesandroid.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacgamesandroid.R

import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.adapters.GameAdapter
import com.example.pacgamesandroid.adapters.GameListener
import com.example.pacgamesandroid.databinding.ActivityGameListBinding

import com.example.pacgamesandroid.models.GameModel
import com.example.pacgamesandroid.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class GameListActivity : AppCompatActivity(), GameListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        app = application as MainApp
        db = Firebase.firestore

        val user = auth.currentUser!!

        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            binding.toolbar.title = "WELCOME " + activeUser!!.name.uppercase()
            val layoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = GameAdapter(activeUser.games, this)
        }

        setSupportActionBar(binding.toolbar)


    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MainActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.go_shops -> {
                val launcherIntent = Intent(this, ShopListActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_user -> {
                val launcherIntent = Intent(this, LogInActiviy::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.games.findAll().size)
            }
        }

    override fun onGameClick(game: GameModel) {
        val launcherIntent = Intent(this, MainActivity::class.java)
        launcherIntent.putExtra("game_edit", game)
        getClickResult.launch(launcherIntent)
    }

    override fun onDelete(game: GameModel) {
        db = Firebase.firestore

        val user = auth.currentUser!!

        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            if (activeUser != null) {
                app.games.delete(game)
                activeUser.games = app.games.games
                db.collection("users").document(user.uid).set(activeUser.games)
            }
        }

        overridePendingTransition(0, 0);
        finish()
        overridePendingTransition(0, 0);
        startActivity(getIntent())
        overridePendingTransition(0, 0);
    }



    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.games.findAll().size)
            }
        }



}