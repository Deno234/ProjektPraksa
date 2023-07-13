package com.example.aplikacijazaprognozuvremena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikacijazaprognozuvremena.databinding.ActivityMainBinding
import com.example.aplikacijazaprognozuvremena.databinding.ActivitySplashScreenBinding

private lateinit var binding: ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutSplashScreen.alpha = 0f
        binding.layoutSplashScreen.animate().setDuration(1500).alpha(1f).withEndAction {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}