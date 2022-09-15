package com.ekenya.lamparam.activities.main

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.ActivityLamparamBinding
import com.ekenya.lamparam.utilities.PreferenceProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class LampMainActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mainNavController: NavController
    private lateinit var binding: ActivityLamparamBinding
    lateinit var mainComponent: LampMainComponent
    private var destination = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLamparamBinding.inflate(layoutInflater)

        val extras = intent.extras!!.getString("source");
        if (extras != null) {
            destination = extras
        }

        setContentView(binding.root)
        //navView = findViewById(R.id.nav_view)
        supportActionBar?.hide()
        mainNavController = findNavController(R.id.navHostFragmentRemittance)
        if(destination == "cashPickup")
        {
            mainNavController.setGraph(R.navigation.lamparam_navigation)
           // mainNavController.navigate(R.id.nav_receiveMoneyMenu)

        }
        else if(destination == "sendMoney")
        {
            mainNavController.navigate(R.id.nav_sendMoney)
            mainNavController.setGraph(R.navigation.lamparam_navigation_send_money)

        }

        //navigateToTarget()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.nav_paymerchant,
                R.id.nav_walletStmt
            )
        )

        binding.btnBack.setOnClickListener {
            //  var currentFragment = nav_host_fragment.childFragmentManager.fragments
            /*val currentFragment = mainNavController.childFragmentManager.primaryNavigationFragment
            currentFragment!!.findNavController().popBackStack()*/
        }

        //navigate to profile menu on cancel
        binding.imgHomeMenu.setOnClickListener {
            //var navDestination = DataClass().getDestinationMenu(this)
            //val currentFragment = binding.nav_host_fragment.childFragmentManager.primaryNavigationFragment
            //if (currentFragment == SelectSparePart)
            //currentFragment!!.findNavController()
            //   .popBackStack(R.id.navigation_home, false)//.navigate(navDestination)
        }

        val pref = PreferenceProvider(this)

        val c = Calendar.getInstance()
        val timeOfDay = c[Calendar.HOUR_OF_DAY]

        val timeGreeting: String = when {
            timeOfDay < 13 -> getString(R.string.str_good_morning, "Peter")
            timeOfDay < 20 -> getString(R.string.str_good_afternoon, "Peter")
            else -> getString(R.string.str_good_evening, "Peter")
        }

        binding.tvMainMenuGreeting.text = timeGreeting

        //
        if (resources.configuration.locale.language == "pt") {
            binding.ivChangeLanguage.text = "🇵🇹"
        } else if (resources.configuration.locale.language == "fr") {
            binding.ivChangeLanguage.text = "🇫🇷"
        } else {
            binding.ivChangeLanguage.text = "🇬🇧"
        }

        binding.ivChangeLanguage.setOnClickListener {
            var list = resources.getStringArray(R.array.language_array_titles)
            val adapter =
                ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, list)

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.change_language))
            builder.setAdapter(adapter) { dialog, pos ->
                val current = resources.configuration.locale
                var lang = resources.getStringArray(R.array.language_array_values)[pos]
                var locale = Locale(lang)
                if (locale.language != current.language || locale.country != current.country) {
                    //mApp.setLocale(lng)
                    //
                    Locale.setDefault(locale)
                    val config: Configuration = resources.getConfiguration()
                    config.setLocale(locale)
                    resources.updateConfiguration(config, resources.getDisplayMetrics())
                    //
                    onConfigurationChanged(config)
                }
            }.setNegativeButton(R.string.btn_cancel, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    //
                }
            }).setPositiveButton(R.string.btn_okay, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    //
                }
            })
            //
            builder.create().show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }

    private fun navigateToTarget() {

        if(destination == "cashPickup")
        {
            mainNavController.navigate(R.id.nav_receiveMoneyMenu)

        }
        else if(destination == "sendMoney")
        {
            mainNavController.navigate(R.id.nav_sendMoney)
        }
    }



//    fun showMenuDashboard(title: String) {
//        navView.visibility = View.GONE
//        binding.tvTitleExplanation.visibility = View.GONE
//        binding.layoutIcon.visibility = View.GONE
//        binding.layoutToolbar.visibility = View.GONE
//        binding.tvMenuTitle.visibility = View.GONE
//        binding.tvMenuTitle.text = title
//        binding.tvMainMenuGreeting.visibility = View.GONE
//        navView.visibility = View.GONE
//    }
//
//    fun showToolbar(title: String, titleExplanation: String) {
//        navView.visibility = View.GONE
//        binding.layoutIcon.visibility = View.GONE
//        binding.tvTitleExplanation.visibility = View.GONE
//        binding.layoutToolbar.visibility = View.GONE
//        binding.tvActionTitle.text = title
//        binding.tvTitleExplanation.text = titleExplanation
//        navView.visibility = View.GONE
//    }
//
//    fun hideActionBar() {
//        navView.visibility = View.GONE
//        binding.layoutIcon.visibility = View.GONE
//        binding.tvTitleExplanation.visibility = View.GONE
//        binding.layoutToolbar.visibility = View.GONE
//        navView.visibility = View.GONE
//
//    }
//
//    fun hideBottonNav() {
//        navView.visibility = View.GONE
//        binding.tvTitleExplanation.visibility = View.GONE
//        binding.layoutIcon.visibility = View.GONE
//        binding.layoutToolbar.visibility = View.GONE
//        binding.tvMenuTitle.visibility = View.GONE
//        binding.tvMainMenuGreeting.visibility = View.GONE
//    }

     fun onBackClick(imgView: ImageView, rootView: View) {
        imgView.setOnClickListener { rootView.findNavController().navigateUp() }
    }


}