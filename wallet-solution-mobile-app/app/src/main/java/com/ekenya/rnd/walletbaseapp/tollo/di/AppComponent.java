package com.ekenya.rnd.walletbaseapp.tollo.di;

import android.app.Application;


import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp;
import com.ekenya.rnd.walletbaseapp.tollo.di.injectables.FragmentModule;
import com.ekenya.rnd.common.repo.SampleRepository;
import com.ekenya.rnd.walletbaseapp.tollo.di.injectables.ActivityModule;
import com.ekenya.rnd.walletbaseapp.tollo.di.injectables.ViewModelModule;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

import org.jetbrains.annotations.NotNull;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,  AppModule.class,  ViewModelModule.class,
                ActivityModule.class, FragmentModule.class} )
public interface AppComponent extends AndroidInjector<WalletSolutionApp> {
    @NotNull
    SampleRepository sampleRepository();

    @NotNull
    Application getApp();

    @Component.Builder
    public abstract static class Builder extends AndroidInjector.Builder<WalletSolutionApp> {
        @NotNull
        public abstract AppComponent build();
    }
}
