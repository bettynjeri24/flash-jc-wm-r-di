package com.ekenya.rnd.onboarding.di.injectables
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.dashboard.viewmodels.MainViewModel
import com.ekenya.rnd.dashboard.view.MobileWalletFragment
import com.ekenya.rnd.dashboard.view.TopUpWalletFragment
import com.ekenya.rnd.dashboard.view.*
import com.ekenya.rnd.onboarding.ui.*
import com.ekenya.rnd.walletbaseapp.tollo.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
abstract class TourismFragmentModule {
    @ContributesAndroidInjector()
    abstract fun contributeSavingsFragment(): SavingsFragment
    @ContributesAndroidInjector()
    abstract fun contributeOrangeAirtimeFragment(): BuyAirtimeFragment
    @ContributesAndroidInjector()
    abstract fun contributeSendtoWallet(): SendToWalletFragment
    @ContributesAndroidInjector()
    abstract fun contributeAbsaRepaymentsFragment(): AbsaRepaymentsFragment
    @ContributesAndroidInjector()
    abstract fun contributeTransactionsFragment(): TransactionsFragment
    @ContributesAndroidInjector()
    abstract fun contributeEnterCardFragment(): EnterCardFragment
    @ContributesAndroidInjector()
    abstract fun contributeSendtoBank(): SendToBankFragment
    @ContributesAndroidInjector()
    abstract fun contributeTopUpWalletFragment(): TopUpWalletFragment
    @ContributesAndroidInjector()
    abstract fun contributeSendtoMobile(): SendToMobileFragment
    @ContributesAndroidInjector()
    abstract fun contributePaymerchant(): PayMerchantFragment
    @ContributesAndroidInjector()
    abstract fun contributePSendMoneyFragment(): SendMoneyFragment
    @ContributesAndroidInjector()
    abstract fun contributeWithdrawFragment(): WithdrawFragment
    @ContributesAndroidInjector()
    abstract fun contributeBillPaymentsFragment(): BillPaymentsFragment
    @ContributesAndroidInjector()
    abstract fun contributeMobileWalletFragment(): MobileWalletFragment







    @Module
    abstract class TourismFirstFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
    }

    @ContributesAndroidInjector()
    abstract fun contributeHomeFragment(): HomeFragment


  /*  @Module
    abstract class WalletHomeFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(HomeViewModel::class)
        abstract fun bindHomeModel(viewModel: HomeViewModel): ViewModel
    }*/



    @ContributesAndroidInjector()
    abstract fun contributeSignUpFragment(): SignUpFragment


    @ContributesAndroidInjector(/*modules = [WalletManualVerificationFragmentModule::class]*/)
    abstract fun contributeManualVerificationFragment(): ManualVerificationFragment

   /* @Module
    abstract class WalletManualVerificationFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ManualVerificationViewModel::class)
        abstract fun bindManualVerificationViewModel(viewModel: ManualVerificationViewModel): ViewModel
    }*/

    @ContributesAndroidInjector(/*modules = [WalletCapturePhotosFragmentModule::class]*/)
    abstract fun contributeCapturePhotosFragment(): CapturePhotosFragment

   /* @Module
    abstract class WalletCapturePhotosFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(CapturePhotosViewModel::class)
        abstract fun bindCapturePhotosViewModel(viewModel: CapturePhotosViewModel): ViewModel
    }*/

    @ContributesAndroidInjector(modules = [WalletTakeSelfieFragmentModule::class])
    abstract fun contributeTakeSelfieFragment(): TakeSelfieFragment

    @Module
    abstract class WalletTakeSelfieFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(TakeSelfieViewModel::class)
        abstract fun bindTakeSelfieViewModel(viewModel: TakeSelfieViewModel): ViewModel
    }

    @ContributesAndroidInjector(/*modules = [WalletVerifyIdentityFragmentModule::class]*/)
    abstract fun contributeVerifyIdentityFragment(): VerifyIdentityFragment

    /*@Module
    abstract class WalletVerifyIdentityFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(VerifyIdentityViewModel::class)
        abstract fun bindVerifyIdentityViewModel(viewModel: VerifyIdentityViewModel): ViewModel
    }*/


    @ContributesAndroidInjector(/*modules = [WalletFinalVerificationFragmentModule::class]*/)
    abstract fun contributeFinalVerification(): FinalDetailsVerificationFragment

   /* @Module
    abstract class WalletFinalVerificationFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(FinalDetailsVerificationViewModel::class)
        abstract fun bindVerifyIdentityViewModel(viewModel: FinalDetailsVerificationViewModel): ViewModel
    }*/

    @ContributesAndroidInjector(modules = [WalletResetPasswordFragmentFragmentModule::class])
    abstract fun contributeResetPasswordFragment(): ResetPasswordFragment

   @ContributesAndroidInjector(modules = [WalletResetPasswordFragmentFragmentModule::class])
    abstract fun contributeResetAccountLookUpFragment(): AccountLookUpFragment

    @Module
    abstract class WalletResetPasswordFragmentFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ResetPasswordViewModel::class)
        abstract fun bindResetPasswordViewModel(viewModel: ResetPasswordViewModel): ViewModel
    }

/*




    @ContributesAndroidInjector(modules = [WalletScanIdFragmentModule::class])
    abstract fun contributeScanIdFragment(): ScanIdFragment

    @Module
    abstract class WalletScanIdFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ScanIdViewModel::class)
        abstract fun bindScanIdViewModel(viewModel: ScanIdFragment): ViewModel
    }*/
}
