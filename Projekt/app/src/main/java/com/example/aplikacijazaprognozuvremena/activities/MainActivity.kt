package com.example.aplikacijazaprognozuvremena.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.aplikacijazaprognozuvremena.databinding.ActivityMainBinding
import com.example.aplikacijazaprognozuvremena.publicfunctions.getBackgroundColor

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        val backgroundColor = getBackgroundColor()
        binding.fragmentContainer.setBackgroundResource(backgroundColor)
    }
}