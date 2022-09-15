package com.ekenya.rnd.cargillfarmer.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.MoreRecentTransactionFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.dashboard.FarmerHomeFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer.SendMoneyAccountListFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.fundtransfer.TransferToTelcoFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.manageaccounts.FarmerAddBeneficiaryFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.manageaccounts.FarmerLinkedAccountFragment
import com.ekenya.rnd.cargillfarmer.ui.farmerprofile.statements.FarmerMiniStatementCargillFragment
import com.ekenya.rnd.cargillfarmer.ui.otp.OTPFragment
import com.ekenya.rnd.cargillfarmer.utils.base.FarmerSuccessfulFragment
import com.ekenya.rnd.common.successful.SuccessfulFragmentCommon
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentCargillModule {
    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_SuccessfulFragment():
        SuccessfulFragmentCommon

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_BaseSuccessfulFragment():
        FarmerSuccessfulFragment

    /*  @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
      abstract fun contribute_CommonAuthFragment():
              CommonAuthFragment*/

    @Module
    abstract class CargillBaseViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(FarmerViewModel::class)
        abstract fun bindHomeViewModel(viewModel: FarmerViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_FarmerHomeFragment(): FarmerHomeFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_SendMoneyAccountListFragment(): SendMoneyAccountListFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_TransferToTelcoFragment(): TransferToTelcoFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_MoreRecentTransactionFragment(): MoreRecentTransactionFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_FarmerLinkedAccountFragment(): FarmerLinkedAccountFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_FarmerAddBeneficiaryFragment(): FarmerAddBeneficiaryFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_OTPFragment(): OTPFragment

    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contribute_FarmerMiniStatementCargillFragment(): FarmerMiniStatementCargillFragment
}
