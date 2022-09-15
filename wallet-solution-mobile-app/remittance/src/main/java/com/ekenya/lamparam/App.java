package com.ekenya.lamparam;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;
import timber.log.Timber;

/**
 * Created by Bourne Koloh on 25 March,2021.
 * Eclectics International, Products and R&D
 * PROJECT: LAMPARAM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setLocale (Locale newLocale){
        Locale.setDefault(newLocale);
        Configuration config = new Configuration();
        config.locale = newLocale;
        getBaseContext().getResources().updateConfiguration(config, null);

    }
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        if(getBaseContext().getResources().getConfiguration().locale.getLanguage() != languageCode) {
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            //
            onConfigurationChanged(config);
        }
    }
}
