package com.ekenya.rnd.cargillcoop.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillcoop.ui.CoopMainFragment
import com.ekenya.rnd.cargillcoop.ui.CoopMainViewModel
import com.ekenya.rnd.cargillcoop.ui.coopbuyerlist.CoopBuyerDetailsListFragment
import com.ekenya.rnd.cargillcoop.ui.evalue.ApproveEvalueRequestFragment
import com.ekenya.rnd.cargillcoop.ui.evalue.EvalueBookingFragment
import com.ekenya.rnd.cargillcoop.ui.evalue.EvalueRequestListFragment
import com.ekenya.rnd.cargillcoop.ui.fundrequest.BuyerRequestApprovalFragment
import com.ekenya.rnd.cargillcoop.ui.fundrequest.FundRequestsByBuyerFragment
import com.ekenya.rnd.cargillcoop.ui.recenttransaction.CoopRecentTransactionFragment
import com.ekenya.rnd.cargillcoop.ui.recenttransaction.GraphAnalysisFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CargillCoopFragmentModule {

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contributeFirstFragment(): CoopMainFragment

    @Module
    abstract class CoopMainViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(CoopMainViewModel::class)
        abstract fun bindMainViewModel(viewModel: CoopMainViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_CoopBuyerDetailsListFragment(): CoopBuyerDetailsListFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_ApproveEvalueRequestFragment(): ApproveEvalueRequestFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_EvalueBookingFragment(): EvalueBookingFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_EvalueRequestListFragment(): EvalueRequestListFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_BuyerRequestApprovalFragment(): BuyerRequestApprovalFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_FundRequestsByBuyerFragment(): FundRequestsByBuyerFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_CoopRecentTransactionFragment(): CoopRecentTransactionFragment

    @ContributesAndroidInjector(modules = [CoopMainViewModelModule::class])
    abstract fun contribute_GraphAnalysisFragment(): GraphAnalysisFragment
}
