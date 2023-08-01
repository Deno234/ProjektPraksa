package com.example.aplikacijazaprognozuvremena

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.aplikacijazaprognozuvremena.databinding.ActivityMainBinding
import com.example.aplikacijazaprognozuvremena.homeview.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
    }
}