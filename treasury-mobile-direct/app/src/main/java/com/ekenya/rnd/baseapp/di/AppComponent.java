package com.ekenya.rnd.baseapp.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.ekenya.rnd.baseapp.di.injectables.AppNetworkModule;
import com.ekenya.rnd.baseapp.di.injectables.AppObjectsModule;
import com.ekenya.rnd.baseapp.di.injectables.AppRepositoryModule;
import com.ekenya.rnd.baseapp.di.injectables.FragmentModule;
import com.ekenya.rnd.baseapp.TMDApp;
import com.ekenya.rnd.baseapp.di.injectables.ActivityModule;
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule;
import com.ekenya.rnd.common.sms.ISendSmsRepository;
import com.ekenya.rnd.common.sms.SmsRequest;
import com.google.gson.Gson;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

@Singleton
@Component(
        modules = {AndroidSupportInjectionModule.class,
                AppModule.class,
                ViewModelModule.class,
                ActivityModule.class,
                FragmentModule.class,
                AppNetworkModule.class,
                AppObjectsModule.class,
                AppRepositoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<TMDApp> {


    @NotNull
    Application getApp();

    @NotNull
    ISendSmsRepository smsRepository();

    @NotNull
    Gson getGson();

    @NotNull
    Context getContext();

    @NotNull
    SharedPreferences sharedPreferences();

    @NotNull
    SmsRequest getSmsRequest();

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<TMDApp> {
        @NotNull
        public abstract AppComponent build();
    }
}
