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



class GameListActivity : AppCompatActivity(), GameListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GameAdapter(app.games.findAll(), this)
        binding.toolbar.title = title
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

    override fun onDelete(game: GameModel){
        app.games.delete(game)
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

