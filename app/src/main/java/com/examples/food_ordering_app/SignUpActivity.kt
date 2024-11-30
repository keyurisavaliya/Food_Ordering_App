package com.examples.food_ordering_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.examples.food_ordering_app.databinding.ActivitySignUpBinding
import com.examples.food_ordering_app.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password1:String
    private lateinit var username:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.examples.food_ordering_app.R.string.default_web_client_id)).requestEmail().build()
        //initialize Firebase auth
        auth = Firebase.auth
        //init firebase database
        database = Firebase.database.reference
        //init google signup
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        binding.createAccount.setOnClickListener {
            username = binding.userName.text.toString()
            email = binding.emailAddress.text.toString().trim()
            password1 = binding.password.text.toString().trim()

            if(email.isEmpty() || password1.isEmpty() || username.isEmpty()){
                Toast.makeText(this, "Enter All Details.", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password1)
            }
        }

        binding.alreadyhavebutton.setOnClickListener{
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.googleButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }
    //launcher for google sign in
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account:GoogleSignInAccount? = task.result
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

    private fun createAccount(email: String, password1: String) {
        auth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount:Failure",task.exception)
            }
        }
    }

    private fun saveUserData() {
        //retrive data of user to firebase
        username = binding.userName.text.toString()
        email = binding.emailAddress.text.toString().trim()
        password1 = binding.password.text.toString().trim()

        val user = UserModel(username,email,password1)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}
