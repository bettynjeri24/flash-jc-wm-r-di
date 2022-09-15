package io.eclectics.cargilldigital

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import io.eclectics.cargilldigital.databinding.ActivityMainBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.qrcode.QRCodeGenerator
import io.eclectics.cargilldigital.smsservice.AppSignatureHelper
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility

class MainClass : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var navControler: NavController
    //  lateinit var  navHostFragment:NavHostFragment
    // supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nav = binding.mainLayoutToolbar
        navControler = findNavController(R.id.nav_host_fragment_content_main)
        //setContentView(R.layout.activity_main)

        val appSignatureHelper = AppSignatureHelper(this)
        appSignatureHelper.appSignatures

        setProfileQRCode()

        binding.btnLogout.setOnClickListener {
            profileLogout()
            /* val intentLogin = Intent(this, MainActivity::class.java)
             intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
             intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             startActivity(intentLogin)
             this.finish()*/
        }
        // binding.btnLogout.
    }

    private fun setProfileQRCode() {
        try{
            var userJson  = UtilPreference().getUserData(this)
            if(!userJson.isNullOrBlank()) {
                var userData: UserDetailsObj = NetworkUtility.jsonResponse(userJson)
                var phoneNumber = userData.phoneNumber
                var coopId = userData.cooperativeId
                var userId = userData.userId
                var userIndex = "${userData.userIndex}"
                var name = "${userData.firstName} ${userData.lastName}"
                var stringToGenerate = "$userId~$userIndex~$phoneNumber~$coopId~$name"
                var qrBitmap = QRCodeGenerator.generateQr(stringToGenerate)
                binding.profileQRCode.setImageBitmap(qrBitmap)
                binding.tvName.text = "${userData.firstName} ${userData.lastName}"
                binding.tvAccPhoneNumber.text = userData.phoneNumber
                binding.tvCompanyId.text = userData.providedUserId
                // var section = userData.getSection()
                //binding.tvSection.text = section.sectionName
                //binding.tvUserLocation.text = userData.
                //depending on the profile show user account details
            }

        }catch (ex:Exception){}
    }


    fun setToolbarTitle(title:String,description:String){
        val toolBar =  binding.mainLayoutToolbar
        binding.mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            navigationMgmt()
        }
        //layoutToolbar.visibility = View.VISIBLE

    }
    fun hideToolbar(){
        val toolBar =  binding.mainLayoutToolbar.toolbar
        toolBar.visibility = View.GONE

    }
    fun openDrawer(){
        setProfileQRCode()
        binding.drawerLayout.openDrawer(GravityCompat.START)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateOnBackPress()

    }

    private fun navigateOnBackPress() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            // var currentFragment2 = navHostFragment!!.childFragmentManager.fragments[1]
            LoggerHelper.loggerError("currentFragment",currentFragment.toString())
            var currentProfileDashboard = UtilPreference().getActiveprofile(this)
            when {
                currentFragment.toString().startsWith("PinFragment") -> {
                    LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                    currentFragment!!.findNavController().popBackStack(currentProfileDashboard, false)
                }
                /* currentFragment.toString().startsWith("CooperativeFragment") -> {
                     LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                     currentFragment!!.findNavController().navigate(R.id.action_logout)
                 }
                 currentFragment.toString().startsWith("AgentFragment") -> {
                     LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                     currentFragment!!.findNavController().navigate(R.id.action_logout)
                 }*/
                /* currentFragment.toString().startsWith("PinFragment") -> {
                     LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                     currentFragment!!.findNavController().popBackStack(currentProfileDashboard, false)
                 }*/
            }
        }catch (ex:Exception){}
    }

    fun navigationMgmt() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
/*  binding.mainLayoutToolbar.toolbarCancel.setOnClickListener {
     //  var currentFragment = nav_host_fragment.childFragmentManager.fragments
     var currentFragment =
         navControler.childFragmentManager.primaryNavigationFragment
 }*/
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            LoggerHelper.loggerError("currentFragment",currentFragment.toString())
/* when {
     currentFragment.toString().startsWith("MakeSalesMenu") -> {
         LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
         currentFragment!!.findNavController().popBackStack(R.id.nav_setLanguage, false)
     }
 }*/
            var currentProfileDashboard = UtilPreference().getActiveprofile(this)
            LoggerHelper.loggerError("currentDashboard",currentProfileDashboard.toString())
            var testDestination = R.id.nav_agentProfile
//currentFragment!!.findNavController().popBackStack(currentProfileDashboard, false)
            currentFragment!!.findNavController().navigate(R.id.action_dashboard)

        }catch (ex:Exception){
            LoggerHelper.loggerError("naverror","error ${ex.message}")

        }            }

    fun navigateToAccounts(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            navHostFragment!!.findNavController().navigate(R.id.nav_selectAccount)
        }catch (ex:Exception){}
    }
    fun profileLogout(){
        binding.drawerLayout.close()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
        var currentProfileDashboard = UtilPreference().getActiveprofile(this)
        currentFragment!!.findNavController().navigate(R.id.action_logout)//currentProfileDashboard
//currentFragment!!.findNavController().
/* val manager: FragmentManager = this.supportFragmentManager
val trans: FragmentTransaction = manager.beginTransaction()
trans.remove(currentFragment!!)
trans.commit()
manager.popBackStack(R.id.nav_loginAccountFragment, 1)*/
    }

    /* setSupportActionBar(binding.toolbar)

     val navController = findNavController(R.id.nav_host_fragment_content_main)
     appBarConfiguration = AppBarConfiguration(navController.graph)
     setupActionBarWithNavController(navController, appBarConfiguration)

     binding.fab.setOnClickListener { view ->
         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             .setAction("Action", null).show()
     }
 }

 override fun onCreateOptionsMenu(menu: Menu): Boolean {
     // Inflate the menu; this adds items to the action bar if it is present.
     menuInflater.inflate(R.menu.menu_main, menu)
     return true
 }

 override fun onOptionsItemSelected(item: MenuItem): Boolean {
     // Handle action bar item clicks here. The action bar will
     // automatically handle clicks on the Home/Up button, so long
     // as you specify a parent activity in AndroidManifest.xml.
     return when (item.itemId) {
         R.id.action_settings -> true
         else -> super.onOptionsItemSelected(item)
     }
 }

 override fun onSupportNavigateUp(): Boolean {
     val navController = findNavController(R.id.nav_host_fragment_content_main)
     return navController.navigateUp(appBarConfiguration)
             || super.onSupportNavigateUp()
 }*/
}