package com.example.pacgamesandroid.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Filterable
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.adapters.GameAdapter
import com.example.pacgamesandroid.adapters.GameListener
import com.example.pacgamesandroid.databinding.ActivityGameListBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.GameModel
import com.example.pacgamesandroid.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.concurrent.CountDownLatch


class GameListActivity : AppCompatActivity(),GameListener, Filterable {
    lateinit var app: MainApp
    private lateinit var binding: ActivityGameListBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding = ActivityGameListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        app = application as MainApp
        db = Firebase.firestore


        if (auth.currentUser!=null) {
            val user = auth.currentUser!!
            println("user uid: ${user.uid}")
            val docRef = db.collection("users").document(user.uid)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val activeUser = documentSnapshot.toObject<UserModel>()
                    println("username: ${activeUser!!.name}")
                    binding.toolbar.title = "WELCOME " + activeUser.name.uppercase()
                    val layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.layoutManager = layoutManager
                    binding.recyclerView.adapter = GameAdapter(activeUser.games, this)
                } else {
                    Timber.i("doc doesnt exist")
                    binding.toolbar.title = title
                    binding.recyclerView.adapter = GameAdapter(app.games.games, this)
                }
            }
        }else{
            binding.toolbar.title = title
            binding.recyclerView.adapter = GameAdapter(app.games.games, this)
        }

        setSupportActionBar(binding.toolbar)


        binding.swipeRefresh.setOnRefreshListener {
            val user = auth.currentUser!!
            val docRef = db.collection("users").document(user.uid)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val activeUser = documentSnapshot.toObject<UserModel>()
                binding.toolbar.title = "WELCOME " + activeUser!!.name.uppercase()
                val layoutManager = LinearLayoutManager(this)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = GameAdapter(activeUser.games, this)
                binding.swipeRefresh.isRefreshing = false
            }
        }



    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val search : androidx.appcompat.widget.SearchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter.filter(newText)
                return true
            }

        })
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
        val docRefUser = db.collection("users").document(user.uid)

        docRefUser.get().addOnSuccessListener { documentSnapshot ->
            val activeUser = documentSnapshot.toObject<UserModel>()
            if (activeUser != null) {
                app.games.create(game.copy())
                docRefUser.update("games", FieldValue.arrayRemove(game))
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

    override fun getFilter(): android.widget.Filter {
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser!!
        val docRefUser = db.collection("users").document(user.uid)
        var filter = object : android.widget.Filter() {
            var filterResults = FilterResults()
            val latch = CountDownLatch(1)

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint == null || constraint.isEmpty()) {
                    latch.countDown()
                    return filterResults
                } else {
                    val searchChar = constraint.toString().toUpperCase()
                    docRefUser.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val documentSnapshot = task.result
                            val activeUser = documentSnapshot.toObject<UserModel>()
                            if (activeUser != null) {
                                println("1: ${activeUser.games}")
                                val filteredGames = activeUser.games.filter { it.title.toUpperCase().contains(searchChar) }
                                println("2: ${filteredGames}")
                                filterResults.values = filteredGames
                                filterResults.count = filteredGames.size
                                println("3: ${filterResults.values}")
                            }
                        }
                        latch.countDown()
                    }
                }
                latch.await()
                println("4: ${filterResults.values}")
                return filterResults
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // Update the list being displayed in the UI with the filtered results
                val filteredGames = results?.values as? List<GameModel>
                println("5: ${filteredGames}")
                if (filteredGames != null) {
                    // Update the list in the UI
                    (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,filteredGames.size)
                    val layoutManager = LinearLayoutManager(this@GameListActivity)
                    binding.recyclerView.layoutManager = layoutManager
                    binding.recyclerView.adapter = GameAdapter(filteredGames,this@GameListActivity)
                }
            }

        }
        return filter
    }


}


