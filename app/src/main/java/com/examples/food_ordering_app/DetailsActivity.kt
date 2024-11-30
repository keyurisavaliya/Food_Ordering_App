package com.examples.food_ordering_app

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.examples.food_ordering_app.databinding.ActivityDetailsBinding
import com.examples.food_ordering_app.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding :ActivityDetailsBinding
    private var foodName: String?=null
    private var foodImage: String?=null
    private var foodDescription: String?=null
    private var foodIngredients: String?=null
    private var foodPrice: String?=null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailfoodname.text = foodName
            descriptionTextview.text = foodDescription
            ingredientsTextview.text = foodIngredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
        }

        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addItemButton.setOnClickListener {
            addItemCart()
        }
    }

    private fun addItemCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        //create cart items object
        val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)

        //save cart item to database
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added To Cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item Not Added", Toast.LENGTH_SHORT).show()
        }
    }
}