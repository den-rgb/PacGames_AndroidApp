package com.example.pacgamesandroid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityFindUserBinding
import com.example.pacgamesandroid.databinding.ActivityLogInActiviyBinding
import com.example.pacgamesandroid.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LogInActiviy : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLogInActiviyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_activiy)

        binding = ActivityLogInActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        app = application as MainApp

        binding.signUpText.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        binding.LogInBut.setOnClickListener {
            var email = binding.emailLog.text.toString()
            var pass = binding.passwordLog.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, GameListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Oops Something went wrong! User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Make sure all the fields are filled in!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goBack1.setOnClickListener {
            val intent = Intent(this, GameListActivity::class.java)
            startActivity(intent)
        }

    }


}