package com.app.techzone.di

import com.app.techzone.data.api.ApiConstants
import com.app.techzone.data.api.PhotosApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object PhotoApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): PhotosApi{
        return builder.build().create(PhotosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder{
        return Retrofit
            .Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

}