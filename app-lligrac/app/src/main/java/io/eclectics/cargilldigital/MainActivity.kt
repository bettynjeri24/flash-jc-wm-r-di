package io.eclectics.cargilldigital

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.databinding.ActivityMainBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.qrcode.QRCodeGenerator
import io.eclectics.cargilldigital.smsservice.AppSignatureHelper
import io.eclectics.cargilldigital.utils.LogOutTimerUtil
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , LogOutTimerUtil.LogOutListener, LifecycleObserver{
    private lateinit var binding: ActivityMainBinding
    lateinit var navControler: NavController
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

        }
        binding.cardPrivacynSecurityCard.setOnClickListener {
            navToDestinationParam(R.id.nav_privacynSecurity)
            closeDrawer()

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
                binding.tvPrivacynsecurity.text = this.resources.getString(R.string.privacy_n_security)
                binding.tvfarmNoTitle.text = this.resources.getString(R.string.farmer_id)
                binding.tvScanTitle.text = this.resources.getString(R.string.scan_qr_code)
                binding.tvAccPhoneNumber.text = userData.phoneNumber
                binding.tvCompanyId.text = userData.providedUserId
                 var section = userData.getSection()
                binding.tvSectionName.text = section.sectionName
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
        binding.navView.visibility = View.VISIBLE
        binding.navViewConstraint.visibility = View.VISIBLE
       // binding.drawerLayout.visibility = View.VISIBLE
        binding.drawerLayout.openDrawer(GravityCompat.START)


    }
    fun closeDrawer(){
        setProfileQRCode()
        binding.navView.visibility = View.GONE
        binding.navViewConstraint.visibility = View.GONE
        // binding.drawerLayout.visibility = View.VISIBLE
        binding.drawerLayout.close()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        //navigateOnBackPress()

    }
    private fun navToDestinationParam( destination:Int){

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            var userJson = UtilPreference().getUserData(this)
            var userData:UserDetailsObj = NetworkUtility.jsonResponse(userJson)
            var phoneNumber = userData.phoneNumber
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var bundle = Bundle()
            bundle.putString("phone",phoneNumber)
            currentFragment!!.findNavController().navigate(destination,bundle)
        }catch (ex:java.lang.Exception){
        }
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
               /* currentFragment.toString().startsWith("PinFragment") -> {
                    LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                    currentFragment!!.findNavController().popBackStack(currentProfileDashboard, false)
                }*/
                currentFragment.toString().startsWith("FarmerHomeFragment") -> {
                    LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
                    currentFragment!!.findNavController().popBackStack(R.id.nav_loginAccountFragment, false)
                }
                /*else->{
                    currentFragment!!.findNavController().navigateUp()
                }*/
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

    /**
     * Navigaet to workmanager ussd
     */
    fun navigateWorkManagerUSSDFrag(
        ussdCode: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>
    ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            currentLiveData = respLiveData
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var bundle = Bundle()
            bundle.putString("ussdcode",ussdCode)
            currentFragment!!.findNavController().navigate(R.id.nav_ussdWorkManagerFrag,bundle)
        }catch (ex:java.lang.Exception){
        }        }

    //navigate to ussd
    fun navigateUSSDFrag(
        ussdCode: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>
    ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            currentLiveData = respLiveData
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var bundle = Bundle()
            bundle.putString("ussdcode",ussdCode)
            currentFragment!!.findNavController().navigate(R.id.nav_ussdFragment,bundle)
        }catch (ex:java.lang.Exception){
        }        }

    //navigate to dashboard from successful ussd call
    fun navigateUSSDDashboard( ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var currentProfileDashboard = UtilPreference().getActiveprofile(this)
            currentFragment!!.findNavController().navigate(currentProfileDashboard)

        }catch (ex:java.lang.Exception){
            LoggerHelper.loggerError("error","eoor ${ex.message}")
        }        }

    //navigate to dashboard from successful ussd call
    fun navigateUSSDWorkmanager( ) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
            var currentProfileDashboard = UtilPreference().getActiveprofile(this)
            currentFragment!!.findNavController().navigateUp()//navigatup
            LoggerHelper.loggerError("navigatehere","ussd navigation")
        }catch (ex:java.lang.Exception){
            LoggerHelper.loggerError("error","eoor ${ex.message}")
        }        }

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
            var currentProfileDashboard = UtilPreference().getActiveprofile(this)
            LoggerHelper.loggerError("currentDashboard",currentProfileDashboard.toString())
 when {
     currentFragment.toString().startsWith("FarmerHomeFragment") -> {
         LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
         currentFragment!!.findNavController().popBackStack(R.id.nav_loginAccountFragment, false)
     }
    /* currentFragment.toString().startsWith("PinFragment") -> {
         LoggerHelper.loggerError("inMakeSale", "Make sale menu $currentFragment")
         currentFragment!!.findNavController().popBackStack(currentProfileDashboard, false)
     }*/
     //
     else ->{
         currentFragment!!.findNavController().navigate(currentProfileDashboard)
         // currentFragment!!.findNavController().navigate(R.id.action_dashboard)

     }
 }



        }catch (ex:Exception){
            LoggerHelper.loggerError("naverror","error ${ex.message}")

        }            }

    fun navigateToAccounts(){
        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        try{
            navHostFragment!!.findNavController().navigate(R.id.nav_selectAccount)
        }catch (ex:Exception){}*/
    }
    fun profileLogout(){
        binding.drawerLayout.close()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        var currentFragment = navHostFragment!!.childFragmentManager.primaryNavigationFragment
        var currentProfileDashboard = UtilPreference().getActiveprofile(this)
        LoggerHelper.loggerError("currentFag","fragment $currentFragment")
        when{
            currentFragment.toString().startsWith("SplashFragment") -> {
                closeDrawer()
            }
            currentFragment.toString().startsWith("LoginAccountFragment") -> {
                closeDrawer()
            }
            currentFragment.toString().startsWith("LanguageFragment")||currentFragment.toString().startsWith("UserPhoneLookup")||
                    currentFragment.toString().startsWith("CompanyIDLookup")||currentFragment.toString().startsWith("OnboardingOtp")||
                    currentFragment.toString().startsWith("SetAccountPin")||currentFragment.toString().startsWith("UssdWorkManagerFrag")||
                    currentFragment.toString().startsWith("UssdFragment")-> {
                closeDrawer()
                //nav_onboardingOtp  nav_providerIdLookup  nav_phoneLookupFragment  nav_confirmationAddChannel  UssdWorkManagerFrag
            }
            else ->{
                currentFragment!!.findNavController().navigate(R.id.action_logout)
            }

        }//LoginAccountFragment
       //currentProfileDashboard
        //currentFragment!!.findNavController().
        /* val manager: FragmentManager = this.supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(currentFragment!!)
        trans.commit()
        manager.popBackStack(R.id.nav_loginAccountFragment, 1)*/
    }

    fun updateCurrentLiveData(response: String,isSuccess:Boolean){
        if(isSuccess) {
            currentLiveData.value = ViewModelWrapper.response(response)
        }else{
            currentLiveData.value = ViewModelWrapper.error(response)
        }
    }

    companion object{
        lateinit var currentLiveData: MediatorLiveData<ViewModelWrapper<String>>
    }




    override fun onStart() {
        super.onStart()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Timber.e("OnStart () &&& Starting timer")
        // register observer
       // ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Timber.e("User interacting with screen")
    }


    override fun onPause() {
        /*this.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )*/
        super.onPause()
    }

    override fun doLogout() {
        /*PreferencesUtils().setPrefLoggedIn(false)
        PreferencesUtils().setPrefLinkedAccounts(null)
        PreferencesUtils().setPrefEnabledServices(null)*/
        CoroutineScope(Dispatchers.Main).launch {
            LogOutTimerUtil.stopLogoutTimer()
            profileLogout()
        }
        /*val intentLogin = Intent(this, MainActivity::class.java)
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentLogin)
        this.finish()*/
    }
}