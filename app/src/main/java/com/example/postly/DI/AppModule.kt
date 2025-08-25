// di/AppModule.kt
package com.example.postly.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.postly.Utils.Constants
import com.example.postly.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        Log.d("HILT","!1000: Room initialized")
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false) // Handles DB schema changes
            .build()
    }

    @Provides
    fun providePostDao(database: AppDatabase) = database.postDao()

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()
}