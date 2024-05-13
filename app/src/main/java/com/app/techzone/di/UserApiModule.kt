package com.app.techzone.di

import android.content.Context
import com.app.techzone.data.remote.api.UserApi
import com.app.techzone.data.remote.repository.EncryptedSharedPreferencesImpl
import com.app.techzone.data.remote.repository.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): UserApi {
        return builder.build().create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepo(api: UserApi, prefs: EncryptedSharedPreferencesImpl): UserRepo {
        return UserRepo(api, prefs)
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferencesImpl(@ApplicationContext context: Context): EncryptedSharedPreferencesImpl {
        return EncryptedSharedPreferencesImpl(context)
    }

}