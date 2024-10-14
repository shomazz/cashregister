package com.adyen.android.assignment.di

import com.adyen.android.assignment.util.DispatchersFactory
import com.adyen.android.assignment.util.DispatchersFactoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Singleton
    fun provideDispatchersFactory(): DispatchersFactory = DispatchersFactoryImpl()

}