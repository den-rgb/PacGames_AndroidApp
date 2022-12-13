package com.example.pacgamesandroid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityFindUserBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.ShopListModel
import com.example.pacgamesandroid.models.ShopModel
import com.example.pacgamesandroid.models.UserModel
import com.example.pacgamesandroid.models.getId
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CreateUserActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFindUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_user)
        binding = ActivityFindUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        app = application as MainApp





        binding.SignUpBut.setOnClickListener {
            var name = binding.name.text.toString()
            var email = binding.email.text.toString()
            var pass = binding.passwordSign.text.toString()
            var confirm = binding.confirmSign.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                    if (it.isSuccessful){
                        val user = auth.currentUser!!
                        if (pass == confirm) {
                            db.collection("users").document(user.uid).set(UserModel(user.uid,name,email, arrayListOf()))
                            app.shops.create()
                            println("shoplist size ${app.shopList.shops}")



                            val intent = Intent(this, LogInActiviy::class.java)
                            startActivity(intent)
                        }else Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_SHORT).show()
            }
        }

        binding.goBack2.setOnClickListener {
            val intent = Intent(this, LogInActiviy::class.java)
            startActivity(intent)
        }
    }


}