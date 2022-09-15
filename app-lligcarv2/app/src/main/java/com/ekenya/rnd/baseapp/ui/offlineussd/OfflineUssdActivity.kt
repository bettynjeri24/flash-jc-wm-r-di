package com.ekenya.rnd.baseapp.ui.offlineussd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ekenya.rnd.baseapp.R
import com.ekenya.rnd.baseapp.databinding.ActivityOfflineUssdBinding

class OfflineUssdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOfflineUssdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOfflineUssdBinding.inflate(layoutInflater)
        setContentView(binding.root)

   /*     if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.offlineUssdMainLayout, OfflineUssdFragment.newInstance())
                .commitNow()
        }*/
    }
}