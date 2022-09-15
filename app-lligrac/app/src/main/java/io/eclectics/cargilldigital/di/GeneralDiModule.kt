package io.eclectics.cargilldigital.di

import android.content.Context
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.eclectics.cargilldigital.R
import javax.inject.Singleton


// Tells Dagger this is a Dagger module
// Install this module in Hilt-generated SingletonComponent
@InstallIn(SingletonComponent::class)
@Module
class GeneralDiModule {

   /* @Provides
    @Singleton
    fun provideRootBeer(@ApplicationContext appContext: Context): RootBeer {
        return RootBeer(appContext)
    }*/

    @Singleton
    @Provides
    fun navOptions():NavOptions{
       val navOptions = NavOptions.Builder()
           .setEnterAnim(R.anim.slide_in)
           .setExitAnim(R.anim.slide_out)
           .setPopEnterAnim(R.anim.slide_in)
           .setPopExitAnim(R.anim.slide_out)
           .build()

       return navOptions
    }

   /* @Singleton
    @Provides
    fun progressDialog(@):SweetAlertDialog{
       var pDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
    }*/

   /* @Provides
    fun provideSweetAlertDialog(@ApplicationContext appContext: Context): SweetAlertDialog {
        val progressDialog = SweetAlertDialog(appContext, SweetAlertDialog.WARNING_TYPE)
        progressDialog.setCancelable(false)
        return SweetAlertDialog(appContext)
    }*/
    @Singleton
    @Provides
    fun provideSweetProgressDialog(@ApplicationContext appContext: Context): SweetAlertDialog {
        val progressDialog = SweetAlertDialog(appContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.setCancelable(false)
        return SweetAlertDialog(appContext)
    }

}