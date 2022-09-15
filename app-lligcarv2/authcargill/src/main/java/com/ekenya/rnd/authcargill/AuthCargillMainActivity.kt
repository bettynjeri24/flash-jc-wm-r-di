package com.ekenya.rnd.authcargill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekenya.rnd.authcargill.databinding.ActivityAuthCargillBinding

class AuthCargillMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthCargillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthCargillBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}