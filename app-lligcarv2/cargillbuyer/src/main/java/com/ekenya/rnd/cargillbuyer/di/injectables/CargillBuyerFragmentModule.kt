package com.ekenya.rnd.cargillbuyer.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerCargillViewModel
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.BuyerHomeFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers.BuyerPayFarmerFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers.FarmerDetailsBottomSheetFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.farmers.ListOfFarmersFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.ffpendingpayments.FFPendingPaymentListFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.ffpendingpayments.FfPendingPaymentDetailsFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.fundrequest.BuyerFundsRequestedListFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.fundrequest.BuyerRequestFundFragment
import com.ekenya.rnd.cargillbuyer.ui.buyerprofile.latestTransactions.BuyerRecentTransactionsFragment
import com.ekenya.rnd.cargillbuyer.ui.tabs.TabBuyerFundsRequestedListFragment
import com.ekenya.rnd.cargillbuyer.ui.tabs.TabBuyerRecentTranactionFragment
import com.ekenya.rnd.cargillbuyer.utils.BuyerSuccessfulFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CargillBuyerFragmentModule {

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_BuyerSuccessfulFragment(): BuyerSuccessfulFragment

    @Module
    abstract class CargillBuyerViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(BuyerCargillViewModel::class)
        abstract fun bindMainViewModel(viewModel: BuyerCargillViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contributeFirstFragment(): BuyerHomeFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_TabBuyerRecentTranactionFragment(): TabBuyerRecentTranactionFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_FundsRequestedListFragment(): TabBuyerFundsRequestedListFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_BuyerFundsRequestedListFragment(): BuyerFundsRequestedListFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_ListOfFarmersFragment(): ListOfFarmersFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_FFPendingPaymentListFragment(): FFPendingPaymentListFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_BuyerRequestFundFragment(): BuyerRequestFundFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_FfPendingPaymentDetailsFragment(): FfPendingPaymentDetailsFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_BuyerRecentTranactionFragment(): BuyerRecentTransactionsFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_FarmerDetailsBottomSheetFragment(): FarmerDetailsBottomSheetFragment

    @ContributesAndroidInjector(modules = [CargillBuyerViewModelModule::class])
    abstract fun contribute_BuyerPayFarmerFragment(): BuyerPayFarmerFragment
}
