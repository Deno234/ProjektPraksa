package com.example.aplikacijazaprognozuvremena.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.getBackgroundColor

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val rootView = findViewById<FragmentContainerView>(R.id.fragmentContainer)
        val backgroundColor = getBackgroundColor()
        rootView.setBackgroundResource(backgroundColor)
    }
}