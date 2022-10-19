package com.ngockieubao.orderapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.ngockieubao.orderapp.R
import com.ngockieubao.orderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.actNavHost)
        return navController.navigateUp()
    }
}