package com.ekenya.rnd.tijara.utils

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.adapters.*
import com.ekenya.rnd.tijara.adapters.layoutAdapter.BillerCategoryAdapter
import com.ekenya.rnd.tijara.adapters.spinnerAdapter.RequestReceivedAdapter
import com.ekenya.rnd.tijara.network.model.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.github.ybq.android.spinkit.SpinKitView
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("listSaccos")
fun bindRecyclerListSacco(recyclerView: RecyclerView, data: List<SaccoList>?) {
    val adapter = recyclerView.adapter as SaccoListAdapter
    adapter.submitList(data)
}
@BindingAdapter("listAccSaccos")
fun bindRecyclerlistAccSaccos(recyclerView: RecyclerView, data: List<SaccoDetail>?) {
    val adapter = recyclerView.adapter as SaccoAccountsAdapter
    adapter.submitList(data)
}
@BindingAdapter("listBank")
fun bindRecyclerBankList(recyclerView: RecyclerView, data:List<BankInfo>?){
    val adapter=recyclerView.adapter as BankListAdapter
    adapter.submitList(data)
}
@BindingAdapter("listWork")
fun bindRecyclerWorkList(recyclerView: RecyclerView, data:List<WorkInfo>?){
    val adapter=recyclerView.adapter as WorkListAdapter
    adapter.submitList(data)
}
@BindingAdapter("listkin")
fun bindRecyclerKinList(recyclerView: RecyclerView, data:List<NextOfKin>?){
    val adapter=recyclerView.adapter as KinsListAdapter
    adapter.submitList(data)
}
@BindingAdapter("listLoanProduct")
fun bindRecyclerLoanProduct(recyclerView: RecyclerView, data:List<LoanProduct>?){
    val adapter=recyclerView.adapter as LoanProductAdapter
    adapter.submitList(data)
}
@BindingAdapter("listTempGuarantor")
fun bindRecyclerTempGuarantor(recyclerView: RecyclerView, data:List<TempGuarantor>?){
    val adapter=recyclerView.adapter as TempGuarantorAdapter
    adapter.submitList(data)
}
@BindingAdapter("listActiveLoan")
fun bindRecyclerListActiveLoan(recyclerView: RecyclerView, data:List<ActivesLoan>?){
    val adapter=recyclerView.adapter as ActiveLoanAdapter
    adapter.submitList(data)
}
@BindingAdapter("listLoanHistory")
fun bindRecyclerListLoanHistory(recyclerView: RecyclerView, data:List<LoanDataHistory>?){
    val adapter=recyclerView.adapter as LoanProductHistoryAdapter
    adapter.submitList(data)
}

@BindingAdapter("listMiniStat")
fun bindRecyclerMiniStat(recyclerView: RecyclerView, data:List<MiniStatementData>?){
    val adapter=recyclerView.adapter as MinistatementAdapter
    adapter.submitList(data)
}
@BindingAdapter("listPendLoan")
fun bindRecyclerlistPendLoann(recyclerView: RecyclerView, data:List<ActivesLoan>?){
    val adapter=recyclerView.adapter as PendingLoanAdapter
    adapter.submitList(data)
}
/*@BindingAdapter("listBillers")
fun bindRecyclerBillers(recyclerView: RecyclerView, data:List<BillerData>?){
    val adapter=recyclerView.adapter as BillMerchantsAdapter
    adapter.submitList(data)
}*/
@BindingAdapter("listCatBillers")
fun bindRecyclerCatBillers(recyclerView: RecyclerView, data:List<BillerCatgData>?){
    val adapter=recyclerView.adapter as BillerCategoryAdapter
    adapter.submitList(data)
}
@BindingAdapter("listmobileMoney")
fun bindRecyclerlistmobileMoney(recyclerView: RecyclerView, data:List<ServiceProviderItem>?){
    val adapter=recyclerView.adapter as MobileMoneyAdapter
    adapter.submitList(data!!)
}
@BindingAdapter("listshareRequest")
fun bindRecyclershareRequest(recyclerView: RecyclerView, data:List<RequestsReceived>?){
    val adapter=recyclerView.adapter as RequestReceivedAdapter
    adapter.submitList(data)
}
@BindingAdapter("listshareSent")
fun bindRecyclershareSent(recyclerView: RecyclerView, data:List<RequestsSent>?){
    val adapter=recyclerView.adapter as RequestSentAdapter
    adapter.submitList(data)
}
@BindingAdapter("listGuarantors")
fun bindRecyclerGuarantors(recyclerView: RecyclerView, data:List<GuarantorData>?){
    val adapter=recyclerView.adapter as MyGuarontorsAdapter
    adapter.submitList(data)
}



@BindingAdapter("btnclickLoading")
fun bindLoadingBtnClick(progressBar: SpinKitView, status: GeneralResponseStatus?) {
    when (status) {
        GeneralResponseStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        GeneralResponseStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
        GeneralResponseStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
    }
}



@BindingAdapter("buttonClickLoading")
fun bindLoadingButtonClick(progressBar: MaterialProgressBar, status: GeneralResponseStatus?) {
    when (status) {
        GeneralResponseStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        GeneralResponseStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
        GeneralResponseStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
    }
}
@BindingAdapter("disableButton")
fun disableButton(button: Button, status: GeneralResponseStatus?) {
    when (status) {
        GeneralResponseStatus.LOADING -> {
            button.alpha = .3f
            button.isEnabled = false
        }
        GeneralResponseStatus.DONE -> {
            button.alpha = 1f
            button.isEnabled = true
        }
        GeneralResponseStatus.ERROR -> {
            button.alpha = 1f
            button.isEnabled = true
        }

    }
}
@BindingAdapter("emptyList")
fun bindEmptyList(constraintLayout: ConstraintLayout, empty: Boolean) {
    when (empty) {
        true -> constraintLayout.visibility = View.VISIBLE
        false -> constraintLayout.visibility = View.GONE
    }
}
@BindingAdapter("emptyListLinear")
fun bindEmptyListLinear(linearLayout: LinearLayout, empty: Boolean) {
    when (empty) {
        true -> linearLayout.visibility = View.VISIBLE
        false -> linearLayout.visibility = View.GONE
    }
}
@BindingAdapter("emptyListRelative")
fun bindEmptyListRelative(relativeLayout: RelativeLayout, empty: Boolean) {
    when (empty) {
        true -> relativeLayout.visibility = View.VISIBLE
        false -> relativeLayout.visibility = View.GONE
    }
}
@BindingAdapter("notEmptyList")
fun bindNotEmptyList(relativeLayout: RelativeLayout, empty: Boolean) {
    when (empty) {
        true -> relativeLayout.visibility = View.VISIBLE
        false -> relativeLayout.visibility = View.GONE
    }
}
@BindingAdapter("notEmptyListLinear")
fun bindNotEmptyList(linearLayout: LinearLayout, empty: Boolean) {
    when (empty) {
        true -> linearLayout.visibility = View.VISIBLE
        false -> linearLayout.visibility = View.GONE
    }
}
@BindingAdapter("notEmptyListConstraint")
fun bindNotEmptyListConstraint(constraintLayout: ConstraintLayout, empty: Boolean) {
    when (empty) {
        true -> constraintLayout.visibility = View.VISIBLE
        false -> constraintLayout.visibility = View.GONE
    }
}
@BindingAdapter("errorPage")
fun bindErrorPage(constraintLayout: ConstraintLayout, isError: Boolean) {
    when (isError) {
        true -> constraintLayout.visibility = View.VISIBLE
        false -> constraintLayout.visibility = View.GONE
    }
}
@BindingAdapter("formateTimeNoSec")
fun bindTimeNoSec(textView: TextView, time: String?) {
    time?.let {
        val format = SimpleDateFormat("HH:mm", Locale.US)
        val dateFormat = format.parse(time)
        val weekdayString = SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(dateFormat)
        textView.text = weekdayString.toString()
    }
}
@BindingAdapter("priceInt")
fun bindPriceInt(priceIntTv: TextView, priceIntString: Int?) {
    priceIntString?.let {
        priceIntTv.text = priceIntString.toString()
    }
}
@BindingAdapter("generalName")
fun bindName(nameTv: TextView, nameString: String?) {
    nameString?.let {
        nameTv.text = camelCase(nameString)
    }
}
@BindingAdapter("imageSrc")
fun loadImageSrc(imageView: ImageView, imgUrl: String?) {
    if (imgUrl.isNullOrEmpty() || imgUrl.isNullOrBlank()) {
        Glide.with(imageView.context)
            .load(R.mipmap.app_logo)
            .apply(
                RequestOptions().placeholder(R.mipmap.app_logo).error(R.mipmap.app_logo)
            )
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            // .onlyRetrieveFromCache(true)
            .into(imageView)
    } else {
        Glide.with(imageView.context)
            .load(imgUrl)
            .apply(
                RequestOptions().placeholder(R.mipmap.app_logo).error(R.mipmap.app_logo)
            )
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //  .onlyRetrieveFromCache(true)
            .into(imageView)
    }
}
@BindingAdapter("loadImage")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .centerInside()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        //  .onlyRetrieveFromCache(true)
        .into(imageView)
}
@BindingAdapter("userPhotoCircular")
fun bindUserPhotoCircular(imageView: ImageView, imgUrl: String?) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .circleCrop()
        .into(imageView)
}
fun bindUserPhotoSized(imageView: ImageView, imgUrl: String?) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .fitCenter()
        .into(imageView)
}
@BindingAdapter("loadDateDayOfWeek")
fun loadDateDayOfWeek(textView: TextView, date: String?) {
    date?.let {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)//
        val dateFormat = format.parse(date)
        val weekdayString =
            SimpleDateFormat("HH:mm a | MMM d, yyyy ", Locale.getDefault()).format(dateFormat)
        textView.text = weekdayString.toString()
    }
}
