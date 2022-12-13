package com.example.pacgamesandroid.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityLogInActiviyBinding
import com.example.pacgamesandroid.main.MainApp
import com.example.pacgamesandroid.models.UserModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LogInActiviy : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLogInActiviyBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_activiy)

        binding = ActivityLogInActiviyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Firebase.firestore
        auth = Firebase.auth
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()// getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso)

        binding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        binding.googleSignInButton.setColorScheme(1)

        binding.googleSignInButton.setOnClickListener{
            signInGoogle()
        }

    }
    private  fun signInGoogle(){

        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }


    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toString())
                val intent = Intent(this, GameListActivity::class.java)
                val user = auth.currentUser!!
                println("user uid2: ${user.uid}")
                SavedPreference.getUsername(this)
                    ?.let { SavedPreference.getEmail(this)?.let { it1 -> UserModel(user.uid, it, it1, arrayListOf()) } }
                    ?.let { db.collection("users").document(user.uid).set(it) }
                println("name: ${SavedPreference.getUsername(this)} + email: ${SavedPreference.getEmail(this)}")
                startActivity(intent)
                finish()
            }
        }
    }







}