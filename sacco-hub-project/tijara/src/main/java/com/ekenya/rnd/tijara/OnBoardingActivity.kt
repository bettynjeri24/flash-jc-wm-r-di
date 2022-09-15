package com.ekenya.rnd.tijara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekenya.rnd.tijara.databinding.ActivityOnboardBinding


class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      setTheme(R.style.AppTheme)
       binding= ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}