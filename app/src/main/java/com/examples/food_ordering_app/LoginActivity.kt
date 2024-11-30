package com.examples.food_ordering_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.examples.food_ordering_app.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var googleSignInClient:GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //init firebase auth
        auth = Firebase.auth
        //init firebase database
        database = Firebase.database.reference
        //init google sign in
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        //login with email and password
        binding.loginButton.setOnClickListener{

            //get data from text field
            email = binding.EmailAddress.text.toString().trim()
            password = binding.Password.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Enter all details to proceed", Toast.LENGTH_SHORT).show()
            }else{
                createUser()
            }
        }

        binding.donthavebutton.setOnClickListener{
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }

        binding.googlebutton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
    //launcher for google sign in
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Sign-In Successfull", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUser() {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if(task.isSuccessful){
                val user = auth.currentUser
                updateUi(user)
            }else{
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    private fun updateUi(user: FirebaseUser?) {
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
        finish()
    }
}