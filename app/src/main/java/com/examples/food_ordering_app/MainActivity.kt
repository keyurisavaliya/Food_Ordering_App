 package com.examples.food_ordering_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.examples.food_ordering_app.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

 class MainActivity : AppCompatActivity() {
     private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomnav.setupWithNavController(NavController)

    }
}