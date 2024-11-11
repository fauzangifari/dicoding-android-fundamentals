package com.fauzangifari.dicodingexamandroidbegin.di

import android.app.Application
import androidx.room.Room
import com.fauzangifari.dicodingexamandroidbegin.data.local.room.FavoriteEventDatabase
import com.fauzangifari.dicodingexamandroidbegin.data.remote.retrofit.ApiConfig
import com.fauzangifari.dicodingexamandroidbegin.data.remote.retrofit.EventServices
import com.fauzangifari.dicodingexamandroidbegin.data.repository.EventRepository
import com.fauzangifari.dicodingexamandroidbegin.data.repository.FavoriteEventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Dari Retrofit
    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig {
        return ApiConfig()
    }

    @Provides
    @Singleton
    fun provideEventServices(apiConfig: ApiConfig): EventServices {
        return apiConfig.getApiService()
    }

    @Provides
    @Singleton
    fun provideEventRepository(apiServices: EventServices): EventRepository {
        return EventRepository(apiServices)
    }

    // Dari Database Room
    @Provides
    @Singleton
    fun provideFavoriteEventDatabase(app: Application): FavoriteEventDatabase {
        return Room.databaseBuilder(
            app,
            FavoriteEventDatabase::class.java,
            FavoriteEventDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteEventRepository(db: FavoriteEventDatabase): FavoriteEventRepository {
        return FavoriteEventRepository(db.favoriteEventDao)
    }

}