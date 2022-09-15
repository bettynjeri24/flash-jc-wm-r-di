package com.ekenya.rnd.tijara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ekenya.rnd.common.abstractions.BaseActivity
import kotlinx.android.synthetic.main.activity_tijara_splash.*
import javax.inject.Inject

class TijaraSplashActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(com.ekenya.rnd.baseapp.R.style.SplashScreenTheme)
        setContentView(R.layout.activity_tijara_splash)
        anim_intro.alpha = 0f
        anim_intro.animate().setDuration(2000).alpha(1f).withEndAction {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}