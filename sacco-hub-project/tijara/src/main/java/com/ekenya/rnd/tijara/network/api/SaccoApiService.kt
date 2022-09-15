package com.ekenya.rnd.tijara.network.api
import com.ekenya.rnd.common.network.SaccoInstance.retrofit
import com.ekenya.rnd.tijara.network.model.*
import com.ekenya.rnd.tijara.network.model.local.ClientBasicInfoFinalResponse
import com.ekenya.rnd.tijara.network.model.local.CountyResponse
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.requestDTO.PayBillDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
/**retrofit interface, where to define all our fun*/
interface SaccoApiService{
    @POST("auth/activate")
    fun getPhoneNumber(@Body phoneActivateDTO: PhoneActivateDTO): Deferred<ActivatePhoneResponse>
    @POST("auth/get-associated-orgs")
    fun getSaccos(@Body countryDTO: CountryDTO): Deferred<Response<SelectSaccoResponse>>
    @POST("auth/get-associated-orgs")
    fun getNewSaccos(@Body newAccDTO: NewAccDTO): Deferred<NewSaccos>
    @POST("auth/get-associated-orgs")
    fun getAssociatedSacco(@Body loginSaccoDTO: LoginSaccoDTO): Deferred<SelectSaccoResponse>
    @POST("auth/activation-code")
    fun getUsername(@Body resetPassDTO: ResetPassDTO): Deferred<ResetPasswordResponse>
    @POST("auth/verify-code")
    fun verifyOTP(@Body verifyOtpDTO: VerifyOtpDTO): Deferred<OTPResponse>
    @POST("auth/validate-default-pin")
    fun defaultPin(@Body defaultPinDTO: DefaultPinDTO): Deferred<GeneralPostResponse>
    @POST("auth/resend-otp")
    fun resendRegOtp(@Body resetPassDTO: ResetPassDTO): Deferred<GeneralPostResponse>
    @POST("auth/change-default-pin")
    fun newPin(@Body newPinDTO: NewPinDTO): Deferred<GeneralPostResponse>
    @POST("auth/verify-user")
    fun verifyUser(@Body verifyUserDTO: VerifyUserDTO): Deferred<GeneralPostResponse>
    @POST("auth/login")
    fun login(@Body loginDTO: LoginDTO):
            Deferred<LoginResponse>
    @POST("client/create/basic")
    fun userPersonalInfo(@Body signUpDTO: SignUpDTO): Deferred<SignupResponse>
    @POST("client/create/basic/initial")
    fun registerClientInfoAsync(@Body basicInfoDTO: BasicInfoDTO): Deferred<RegistrationResponse>
    @POST("client/create/basic/final")
    fun submitClientInfo(@Body clientInfoDTO: SubmitClientInfoDTO): Deferred<ClientBasicInfoFinalResponse>
    @Multipart
    @POST("client/create/basic/final")
    fun uploadIdPhotosAsync(
        @Part vararg body: MultipartBody.Part,
        @Part("form_id") formId: RequestBody,
        @Part("tax_pin") krapin: RequestBody,
        @Part("email") email: RequestBody
    ): Deferred<ClientBasicInfoFinalResponse>
    @POST("auth/set-credentials")
    fun userCredentials(@Body signUpCredentialDTO: SignUpCredentialDTO): Deferred<GeneralPostResponse>
    @POST("auth/new-password")
    fun setNewPassword(@Body newPassDTO: NewPassDTO): Deferred<GeneralPostResponse>
    @POST("auth/change-password")
    fun firstTimePassword(@Body changeFirstPinDTO: ChangeFirstPinDTO): Deferred<ChangePasswordResponse>
    @POST("client/create/work-info")
    fun workDetails(@Body workDTO: WorkDTO): Deferred<GeneralPostResponse>
    @POST("client/create/kin")
    fun nextKin(@Body nextKinDTO: NextKinDTO): Deferred<GeneralPostResponse>
    @POST("client/create/bank-accounts")
    fun createBankAcc(@Body bankDetailsDTO: BankDetailsDTO): Deferred<GeneralPostResponse>
    @POST("pg/deposit/preview")
    fun makeDepositToSavingAcc(@Body makeDepositDTO:MakeDepositDTO): Deferred<GeneralPreviewResponse>
    @POST("pg/deposit/commit")
    fun commitDeposit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("loan/products")
    fun getLoanProduct(): Deferred<LoanProductResponse>
    @POST("loan/product-details")
    fun getLoanHistory(@Body loanDetailsDTO: LoanDetailsDTO): Deferred<LoanHistoryResponse>
    @POST("loan/active-loans")
    fun getActiveLoans(): Deferred<ActiveLoanResponse>
    @POST("loan/active-loans")
    fun getpendingLoans(@Body pendLoanDTO: PendLoanDTO): Deferred<ActiveLoanResponse>
    @POST("loan/active-loans")
    fun getMustGuarantedLoan(@Body loanGuarantedDTO: LoanGuarantedDTO): Deferred<LoanProductResponse>
    @POST("loan-guarantor/loans-guaranteed")
    fun getNewGuarantedLoan(@Body newLoanRequestDTO: NewLoanRequestDTO): Deferred<NewLoanGuarantedRequest>
    @POST("loan-guarantor/loans-guaranteed")
    fun getAllGuarantedLoan(): Deferred<NewLoanGuarantedRequest>
    @POST("loan-guarantor/commission-request")
    fun getActLoan(@Body actLoanDTO: ActLoanDTO): Deferred<GeneralPostResponse>
    @POST("loan/application-preview")
    fun applyLoanPreview(@Body loanRequestDTO: LoanRequestDTO): Deferred<GeneralPreviewResponse>
    @POST("loan/application-commit")
    fun applyLoanCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("loan/repayment-preview")
    fun repayLoan(@Body payLoanDTO: PayLoanDTO): Deferred<GeneralPreviewResponse>
    @POST("loan/repayment-preview")
    fun payLoanAcc(@Body payFrmAccDTO: PayFrmAccDTO): Deferred<GeneralPreviewResponse>
    @POST("loan/repayment-commit")
    fun payLoanCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("loan-guarantor/my-guarantors")
    fun getguarantors(): Deferred<GuarantorResponse>
    @POST("loan-guarantor/temp-guarantors")
    fun getTempGuarantors(@Body loanDetailsDTO: LoanDetailsDTO): Deferred<TempGuarantorsResponse>
    @POST("loan-guarantor/request-preview")
    fun addGuarantorsPreveiw(@Body addGuarantorDTO: AddGuarantorDTO): Deferred<AddGuarantorsPreview>
    @POST("loan-guarantor/commit-request")
    fun addGuarantorsCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("loan-guarantor/add-temp-guarantor-request-preview")
    fun addGuarantorPreveiw(@Body adGuarantorDTO: AdGuarantorDTO): Deferred<AddGuarantorsPreview>
    @POST("loan-guarantor/add-temp-guarantor-request-commit")
    fun addGuarantorCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("loan-guarantor/remove-temp-guarantor")
    fun removeTempGuarantor(@Body remGuranDTO: RemGuranDTO): Deferred<GeneralPostResponse>
    @POST("loan-guarantor/remove-all-temp-guarantors")
    fun deleteTempGuarantor(@Body loanDetailsDTO: LoanDetailsDTO): Deferred<GeneralPostResponse>
    @POST("loan-guarantor/update-temp-guarantor")
    fun updateTempGuarantor(@Body updateGuraDTO: UpdateGuraDTO): Deferred<GeneralPostResponse>
    @POST("loan/calculator")
    fun calculateLoan(@Body loanCalDTO: LoanCalDTO): Deferred<LoanCalculatorResponse>
    @POST("saving-account-withdrawal")
    fun withdrawCash(@Body withdrawSavingDTO: WithdrawSavingDTO): Deferred<GeneralPostResponse>
    @POST("pg/withdrawal-trigger-preview")
    fun buyAirtimePreview(@Body buyAirtimeDTO: BuyAirtimeDTO): Deferred<GeneralPreviewResponse>
    @POST("pg/withdrawal-trigger-commit")
    fun airtimeCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("pg/pesalink/transfer/preview")
    fun sendToPhone(@Body stPhoneDTO: STPhoneDTO): Deferred<PesalinkPreviewResponse>
    @POST("pg/pesalink/transfer/preview")
    fun sendToAccount(@Body stAccountDTO: STAccountDTO): Deferred<PesalinkPreviewResponse>
    @POST("pg/pesalink/transfer/commit")
    fun commitToAccount(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("pg/biller/pay/bill/preview")
    fun payBill(@Body payBillDTO: PayBillDTO): Deferred<GeneralPreviewResponse>
    @POST("pg/biller/pay/bill/commit")
    fun payBillCommit(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("pg/pesalink/phone-number/look-up")
    fun pesalinkPhoneCheck(@Body pesalinkPhoneCheckDTO: PesalinkPhoneCheckDTO): Deferred<PesalinkPCheckResponse>


    @POST("organization/index")
    fun getOrganizationList() : Deferred<OrganizationResponse>
    @POST("gender/index")
    fun getGender(@Body genderDTO: GenderDTO): Deferred<GenderResponse>
    @POST("client/accounts")
    fun getUserSavingAccounts(): Deferred<AccountsResponse>
    @POST("client/bank-info")
    fun getBankInfo(): Deferred<ViewBankInfoResponse>
    @POST("client/work-info")
    fun getworkInfo(): Deferred<ViewWorkInfoResponse>
    @POST("client/next-of-kins")
    fun getKinsInfo(): Deferred<ViewNextOfKinResponse>
    @POST("client/details")
    fun getPersonalInfo(): Deferred<ProfileInfoResponse>
    @POST("bank/index")
    fun getBankList(): Deferred<BankResponse>
    @POST("bank-branch/index")
    fun getBankBranchList(@Body bankBranchDTO: BankBranchDTO): Deferred<BankBranchResponse>
    @POST("employer/index")
    fun getEmployer(): Deferred<EmployerResponse>
    @POST("employment-terms/index")
    fun getEmpTerm(): Deferred<EmploymentTermResponse>
    @POST("relationship-types/index")
    fun getRshipTypes(): Deferred<RShipResponse>
    @POST("account-statement/index")
    fun getUserAccountsSummary(@Body accountSummaryDTO: AccountSummaryDTO): Deferred<AccountSummary>

    @POST("loan/statement")
    fun getLoanSummary(@Body loanStatementDTO: LoanStatementDTO): Deferred<LoanSummaryResponse>
    @POST("saving-account/index")
    fun getSavingAccount(): Deferred<SavingAccounts>
    @POST("saving-account/index")
    fun getSavingAcc(@Body savingAccDTO: SavingAccDTO): Deferred<SavingAccounts>
    @POST("service-provider/index")
    fun getServiceProvider(): Deferred<ServiceProviderResponse>
    @POST("client/full-statement-preview")
    fun getFullStatement(@Body fullStatementDTO: FullStatementDTO): Deferred<FullStatementResponse>
    @POST("client/full-statement-generate")
    fun genarateFullStatement(@Body statementDTO: StatementDTO): Deferred<CommitStatementResponse>
    @POST("client/mini-statement")
    fun getMiniStatement(@Body miniStatementDTO: MiniStatementDTO): Deferred<MinistatementResponse>
    @POST("billers/index")
    fun getBillers(): Deferred<BillerResponse>
    @POST("biller-category/index")
    fun getBillersCategories(): Deferred<BillerCategories>
    @POST("billers/index")
    fun getBillersFiltered(@Body billerDTO: BillerDTO): Deferred<BillerResponse>
    //has genral lookup
    @POST("pg/biller/presentment")
    fun billerNoLookup(@Body billerLookupDTO: BillerLookupDTO): Deferred<GeneralBillerLookUpResponse>
    @POST("shares/member-lookup")
    fun shareNoLookup(@Body shareLookupDTO: ShareLookupDTO): Deferred<ShareMemberLookupResponse>
    @POST("shares/request-preview")
    fun shareTransfer(@Body shareDTO: ShareDTO): Deferred<ShareTransferResponse>
    @POST("shares/commit-request")
    fun commitShare(@Body commitShareDTO: CommitShareDTO): Deferred<CommitShareResponse>
    @POST("shares/requests")
    fun requestShareStatus(): Deferred<ShareRequestResponse>
    @POST("shares/commission-request")
    fun cancelTransfer(@Body cancelTransferDTO: CancelTransferDTO): Deferred<GeneralPostResponse>
    @POST("internal-transfer/preview")
    fun selfTransfer(@Body internalTDTO: InternalTDTO): Deferred<InternalTPreviewResponse>
    @POST("internal-transfer/commit")
    fun commitSelfTransfer(@Body statementDTO: StatementDTO): Deferred<SelfCommitResponse>
    @POST("internal-transfer/preview")
    fun memberTransfer(@Body memberTDTO: MemberTDTO): Deferred<MemberPreviewResponse>
    @POST("pg/nairobi-county/parking-fees")
    fun getVehicleTypes(@Body parkingDTO:ParkingTDTO): Deferred<ParkingResponse>
    @POST("pg/biller/presentment")
    fun parkingLookUp(@Body parkingLookUpDTO: ParkingLookUpDTO): Deferred<ParkingLookUpResponse>
    @POST("pg/biller/presentment")
    fun countyLookUp(@Body countyLookUpDTO: CountyLookUpDTO): Deferred<GeneralBillerLookUpResponse>
    @POST("pg/counties")
    fun getCountiesAsync(): Deferred<CountyResponse>
    @POST("standing-order/index")
    fun getStandingOrderAccAsync(): Deferred<StandingOrdersResponse>
    @POST("standing-order/frequencies")
    fun getStandingOrderFrequencyAccAsync(): Deferred<StandingOrderFrequeny>
    @POST("standing-order/preview")
    fun createStandingOrderAsync(@Body standingOrderDTO: StandingOrderDTO): Deferred<GeneralPreviewResponse>
    @POST("standing-order/commit")
    fun commitStandingOrderAsync(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("client-request/preview")
    fun createChequePreviewAsync(@Body chequeDTO: ChequeDTO): Deferred<GeneralPreviewResponse>
    @POST("organization-branch/index")
    fun getChequeBranches(): Deferred<ChequeBranchesResponse>
    @POST("client-request/commit")
    fun commitCheque(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("client-request/commit")
    fun commitCard(@Body statementDTO: StatementDTO): Deferred<GeneralCommitResponse>
    @POST("client-request/preview")
    fun createMemCardAsync(@Body mCardDTO: MCardDTO): Deferred<GeneralPreviewResponse>
    @POST("client-complaint/create")
    fun createComplainAsync(@Body complaintDTO: ComplaintDTO): Deferred<GeneralPostResponse>
    @POST("client/update/basic")
    fun updateEmailAsync(@Body emailDTO: EmailDTO): Deferred<GeneralPostResponse>
    @POST("client-request/preview")
    fun createATM(@Body atmdto: ATMDTO): Deferred<GeneralPreviewResponse>
    @Multipart
    @POST("client/update/basic")
    fun uploadProfileAsync(@Part vararg body: MultipartBody.Part): Deferred<ProfilePicResponse>
    @POST("identity-type/index")
    fun getIdentityType(): Deferred<IDTypeResponse>
    @Multipart
    @POST("auth/reset-pin")
    fun resetPinsAsync(
        @Part vararg body: MultipartBody.Part,
        @Part("resetOption") resetOption: RequestBody,
        @Part("identifierNumber") idNumber: RequestBody,
    ): Deferred<ResetPinResponse>




}
/**object that we will later use in all our fun to make the api calls*/
object SaccoApi{
    val retrofitService: SaccoApiService by lazy {
        retrofit.create(SaccoApiService::class.java)
    }
}