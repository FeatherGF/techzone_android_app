package com.app.techzone.di

import android.content.Context
import androidx.room.Room
import com.app.techzone.data.local.ProductDatabase
import com.app.techzone.data.remote.api.ApiConstants
import com.app.techzone.data.remote.api.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProductApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): ProductApi {
        return builder.build().create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room
            .databaseBuilder(
                context,
                ProductDatabase::class.java,
                "products.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit
            .Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

}