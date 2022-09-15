package com.ekenya.rnd.tijara.di.injectables


import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.tijara.ui.auth.changepassword.*
import com.ekenya.rnd.tijara.ui.auth.login.ChangeFirstPinFragment
import com.ekenya.rnd.tijara.ui.auth.login.ChangeFirstPinViewModel
import com.ekenya.rnd.tijara.ui.auth.login.LoginPinFragment
import com.ekenya.rnd.tijara.ui.auth.login.LoginViewModel
import com.ekenya.rnd.tijara.ui.auth.onboarding.*
import com.ekenya.rnd.tijara.ui.auth.registration.*
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.*
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.*
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime.BuyAirtimeViewmodel
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest.ATMCardFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest.ChequeFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest.CreateStandingOrderFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest.MemberShipCardFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink.*
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.SendMoneySuccessFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.internaltransfer.*
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.mobilemoneytransfer.MobileMoneyTFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares.ShareFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares.SharePhoneLookupFragment
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares.ShareViewModel
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup.*
import com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails.*
import com.ekenya.rnd.tijara.ui.homepage.loan.LoanSuccessFragment
import com.ekenya.rnd.tijara.ui.homepage.loan.getloan.*
import com.ekenya.rnd.tijara.ui.homepage.loan.payloan.LoanRepaymentFragment
import com.ekenya.rnd.tijara.ui.homepage.statement.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TijaraFragmentModule {

    @ContributesAndroidInjector(modules = [TijaraChangeFirstPinFragmentModule::class])
    abstract fun contributeChangeFirstPinFragment(): ChangeFirstPinFragment

    @Module
    abstract class TijaraChangeFirstPinFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ChangeFirstPinViewModel::class)
        abstract fun bindChangeFirstPinViewmodel(viewModel: ChangeFirstPinViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [DeviceVerificationFragmentModule::class])
    abstract fun contributeDeviceVerificationFragment(): DeviceVerificationFragment

    @Module
    abstract class DeviceVerificationFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(DeviceVerificationViewModel::class)
        abstract fun bindPhoneActivateViewModel(viewModel: DeviceVerificationViewModel): ViewModel

    }


    @ContributesAndroidInjector(modules = [TijaraLoginPinFragmentModule::class])
    abstract fun contributeLoginPinFragment(): LoginPinFragment

    @Module
    abstract class TijaraLoginPinFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraCountryFragmentModule::class])
    abstract fun contributeCountryFragment(): CountryFragment

    @Module
    abstract class TijaraCountryFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(CountryViewmodel::class)
        abstract fun bindCountryViewmodel(viewModel: CountryViewmodel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraNewSaccoFragmenttModule::class])
    abstract fun contributeNewSaccoFragment(): NewSaccoFragment

    @Module
    abstract class TijaraNewSaccoFragmenttModule {
        @Binds
        @IntoMap
        @ViewModelKey(CountryViewmodel::class)
        abstract fun bindCountryViewmodel(viewModel: CountryViewmodel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraLoginSaccoFragmentModule::class])
    abstract fun contributeLoginSaccoFragment(): LoginSaccoFragment

    @Module
    abstract class TijaraLoginSaccoFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(CountryViewmodel::class)
        abstract fun bindCountryViewmodel(viewModel: CountryViewmodel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraSaccoListFragmenttModule::class])
    abstract fun contributeSaccoListFragment(): SaccoItemFragment

    @Module
    abstract class TijaraSaccoListFragmenttModule {
        @Binds
        @IntoMap
        @ViewModelKey(SaccoListViewModel::class)
        abstract fun bindSaccoListViewModel(viewModel: SaccoListViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraBillersModule::class])
    abstract fun contributeBillers(): BillersFragment

    @Module
    abstract class TijaraBillersModule {
        @Binds
        @IntoMap
        @ViewModelKey(BillersViewModel::class)
        abstract fun bindBillersViewModel(viewModel: BillersViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraPayBillFragmentModule::class])
    abstract fun contributePayBillFragment(): PayBillFragment

    @Module
    abstract class TijaraPayBillFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraBuyShareFragmentModule::class])
    abstract fun contributeBuyShareFragment(): ShareFragment

    @Module
    abstract class TijaraBuyShareFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ShareViewModel::class)
        abstract fun bindPayBuyShareViewModel(viewModel: ShareViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSendToAccNumberFragmentModule::class])
    abstract fun contributeSendToAccNumberFragment(): SendToAccNumberFragment

    @Module
    abstract class TijaraSendToAccNumberFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(SendToAccNumberViewmodel::class)
        abstract fun bindPaySendToAccNumberViewmodel(viewModel: SendToAccNumberViewmodel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSendToPhoneFragmentModule::class])
    abstract fun contributeSendToAccPhoneFragment(): SendToPhoneFragment

    @Module
    abstract class TijaraSendToPhoneFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(SendToPhoneViewmodel::class)
        abstract fun bindPaySendToPhoneViewmodel(viewModel: SendToPhoneViewmodel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraInternalTransferFragmentModule::class])
    abstract fun contributeInternalTransferFragment(): InternalTransferFragment

    @Module
    abstract class TijaraInternalTransferFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(SelfTransferViewModel::class)
        abstract fun bindSelfTransferViewModel(viewModel: SelfTransferViewModel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSendMoneySuccessFragmentModule::class])
    abstract fun contributeSendMoneySuccessFragment(): SendMoneySuccessFragment

    @Module
    abstract class TijaraSendMoneySuccessFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(SendToPhoneViewmodel::class)
        abstract fun bindPaySendToPhoneViewmodel(viewModel: SendToPhoneViewmodel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(SelfTransferViewModel::class)
        abstract fun bindSelfTransferViewModell(viewModel: SelfTransferViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraAccountSummaryFragmentModule::class])
    abstract fun contributeAccountSummaryFragment(): AccountSummaryFragment

    @Module
    abstract class TijaraAccountSummaryFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(AccountSummaryViewModel::class)
        abstract fun bindAccountSummaryViewModel(viewModel: AccountSummaryViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraBuyAirtimeFragmentModule::class])
    abstract fun contributeBuyAirtimeFragment(): BuyAirtimeFragment

    @Module
    abstract class TijaraBuyAirtimeFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(BuyAirtimeViewmodel::class)
        abstract fun bindBuyAirtimeViewmodel(viewModel: BuyAirtimeViewmodel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSharePhoneLookupFragment::class])
    abstract fun contributeSharePhoneLookupFragment(): SharePhoneLookupFragment

    @Module
    abstract class TijaraSharePhoneLookupFragment {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraMobileMoneyTFragmenttModule::class])
    abstract fun contributeMobileMoneyTFragment(): MobileMoneyTFragment

    @Module
    abstract class TijaraMobileMoneyTFragmenttModule {
        @Binds
        @IntoMap
        @ViewModelKey(BuyAirtimeViewmodel::class)
        abstract fun bindBuyAirtimeViewmodel(viewModel: BuyAirtimeViewmodel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraDashboardFragmentModule::class])
    abstract fun contributeDashboardFragment(): DashboardFragment

    @Module
    abstract class TijaraDashboardFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(DashboardViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [HomeMainFragmentModule::class])
    abstract fun contributeHomeMainFragment(): HomeMainFragment

    @Module
    abstract class HomeMainFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(DashboardViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [PinFragmentModule::class])
    abstract fun contributePinFragmentModule(): PinFragment

    @Module
    abstract class PinFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(DashboardViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [LoanRequestFragmentModule::class])
    abstract fun contributeLoanRequestFragment(): LoanRequestFragment

    @Module
    abstract class LoanRequestFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: PinViewModel): ViewModel
    }
    /*@ContributesAndroidInjector(modules = [LoanRequestFragmentModule::class])
    abstract fun contributeLoanRequestFragment(): LoanSuccessFragment

    @Module
    abstract class LoanRequestFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: PinViewModel): ViewModel
    }*/

    @ContributesAndroidInjector(modules = [LoanRepaymentFragmentModule::class])
    abstract fun contributeLoanRepaymentFragment(): LoanRepaymentFragment

    @Module
    abstract class LoanRepaymentFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindDashboardViewModel(viewModel: PinViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraMakeDepositFragmentModule::class])
    abstract fun contributeMakeDepositFragment(): MakeDepositFragment

    @Module
    abstract class TijaraMakeDepositFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(BuyAirtimeViewmodel::class)
        abstract fun bindBuyAirtimeViewmodel(viewModel: BuyAirtimeViewmodel): ViewModel
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSuccessDepositFragmentModule::class])
    abstract fun contributeSuccessDepositFragment(): SuccessDepositFragment

    @Module
    abstract class TijaraSuccessDepositFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(BuyAirtimeViewmodel::class)
        abstract fun bindBuyAirtimeViewmodel(viewModel: BuyAirtimeViewmodel): ViewModel
    }
    @ContributesAndroidInjector(modules = [CreateStandingOrderFragmentModule::class])
    abstract fun contributeCreateStandingOrderFragment(): CreateStandingOrderFragment

    @Module
    abstract class CreateStandingOrderFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [ChequeFragmentModule::class])
    abstract fun contributeChequeFragment(): ChequeFragment

    @Module
    abstract class ChequeFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [MemberShipCardFragmentModule::class])
    abstract fun contributeMemberShipCardFragment(): MemberShipCardFragment

    @Module
    abstract class MemberShipCardFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [ATMCardFragmentModule::class])
    abstract fun contributeATMCardFragment(): ATMCardFragment

    @Module
    abstract class ATMCardFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(PinViewModel::class)
        abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraWithdrawSavingFragmentModule::class])
    abstract fun contributeWithdrawSavingFragmentt(): WithdrawSavingFragment

    @Module
    abstract class TijaraWithdrawSavingFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(WithdrawSavingViewModel::class)
        abstract fun bindWithdrawSavingViewModel(viewModel: WithdrawSavingViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraBankBottomSheetFragmentModule::class])
    abstract fun contributeBankBottomSheetFragment(): BankBottomSheetFragment

    @Module
    abstract class TijaraBankBottomSheetFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(BankDetailsViewModel::class)
        abstract fun bindBankDetailsViewModel(viewModel: BankDetailsViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraKinBottomSheetFragmentModule::class])
    abstract fun contributeKinBottomheetFragment(): KinBottomSheetFragment

    @Module
    abstract class TijaraKinBottomSheetFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(NextKinViewModel::class)
        abstract fun bindNextKinViewModel(viewModel: NextKinViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraWorkBottomSheetFragmentModule::class])
    abstract fun contributeWorkBottomSheetFragment(): WorkBottomSheetFragment

    @Module
    abstract class TijaraWorkBottomSheetFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(WorkDetailsViewModel::class)
        abstract fun bindWorkDetailsViewModel(viewModel: WorkDetailsViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraBankListFFragmentModule::class])
    abstract fun contributeBankListFFragment(): BankListFragment

    @Module
    abstract class TijaraBankListFFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(BankListViewModel::class)
        abstract fun bindBankListViewModel(viewModel: BankListViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraKinListFragmentModule::class])
    abstract fun contributeKinListFragment(): KinListFragment

    @Module
    abstract class TijaraKinListFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(KinListViewModel::class)
        abstract fun bindKinListViewModel(viewModel: KinListViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraWorkListFragmenttModule::class])
    abstract fun contributeWorkListFragment(): WorkListFragment

    @Module
    abstract class TijaraWorkListFragmenttModule {
        @Binds
        @IntoMap
        @ViewModelKey(WorkListViewModel::class)
        abstract fun bindWorkListViewModel(viewModel: WorkListViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraViewPersonalInfoFragmentModule::class])
    abstract fun contributeViewPersonalInfoFragment(): ViewPersonalInfoFragment

    @Module
    abstract class TijaraViewPersonalInfoFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ViewPersonalInfoViewModel::class)
        abstract fun bindViewPersonalInfoViewModel(viewModel: ViewPersonalInfoViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraUploadedPhotosFragment::class])
    abstract fun contributeUploadedPhotosFragment(): UploadedPhotosFragment

    @Module
    abstract class TijaraUploadedPhotosFragment {
        @Binds
        @IntoMap
        @ViewModelKey(ViewPersonalInfoViewModel::class)
        abstract fun bindViewPersonalInfoViewModel(viewModel: ViewPersonalInfoViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraIdentificationFragmentModule::class])
    abstract fun contributeIdentificationFragment(): IdentificationFragment

    @Module
    abstract class TijaraIdentificationFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(ViewPersonalInfoViewModel::class)
        abstract fun bindViewPersonalInfoViewModel(viewModel: ViewPersonalInfoViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraViewResidensenfoFragmenttModule::class])
    abstract fun contributeResidensenfoFragmentt(): ResidensenfoFragment

    @Module
    abstract class TijaraViewResidensenfoFragmenttModule {
        @Binds
        @IntoMap
        @ViewModelKey(ViewPersonalInfoViewModel::class)
        abstract fun bindViewPersonalInfoViewModel(viewModel: ViewPersonalInfoViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [TijaraLoanOptionFragmentModule::class])
    abstract fun contributeLoanOptionFragment(): LoanOptionFragment

    @Module
    abstract class TijaraLoanOptionFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(LoanProductViewModel::class)
        abstract fun bindLoanProductDetailsViewModel(viewModel: LoanProductViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraLoanProductFragmentModule::class])
    abstract fun contributeLoanProductFragment(): LoanProductFragment

    @Module
    abstract class TijaraLoanProductFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(LoanProductViewModel::class)
        abstract fun bindLoanProductViewModel(viewModel: LoanProductViewModel): ViewModel
    }







    @ContributesAndroidInjector(modules = [TijaraMiniStatementFragmentModule::class])
    abstract fun contributeTijaraMiniStatementFragmentModule(): MiniStatementFragment

    @Module
    abstract class TijaraMiniStatementFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(MiniStatementViewModel::class)
        abstract fun bindLoanMiniStatementViewModel(viewModel: MiniStatementViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraStatementFragmentModule::class])
    abstract fun contributeTijaraStatementFragmentModule(): StatementFragment

    @Module
    abstract class TijaraStatementFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(FullStatementViewModel::class)
        abstract fun bindFullStatementViewModel(viewModel: FullStatementViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraStatementAccountFragmentModule::class])
    abstract fun contributeTijaraStatementAccountFragmentModule(): StatementAccountFragment

    @Module
    abstract class TijaraStatementAccountFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(FullStatementViewModel::class)
        abstract fun bindFullStatementViewModel(viewModel: FullStatementViewModel): ViewModel
    }
}
