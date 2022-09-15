package com.ekenya.lamparam.di

import com.ekenya.lamparam.storage.SharedPreferencesStorage
import com.ekenya.lamparam.storage.Storage
import dagger.Binds
import dagger.Module

/**
 * Tells dagger that this is a module
 * Because of @Binds, StorageModule needs to be an abstract class
 */
@Module
abstract class StorageModule {

    // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}