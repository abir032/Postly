package com.example.postly.di
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.postly.Model.DataSource.Local.DAO.PostDao
import com.example.postly.Model.DataSource.Remote.API.ApiService
import com.example.postly.Model.Repository.Contracts.Post.IFPostRepository
import com.example.postly.Model.Repository.Repositories.PostRepositories.PostRepository
import com.example.postly.Utils.Constants
import com.example.postly.Utils.NetworkManager
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
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun providePostDao(database: AppDatabase) = database.postDao()

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()


    @Provides
    fun providePostRepository(
        postDao: PostDao,
        apiService: ApiService,
        networkManager: NetworkManager
    ): IFPostRepository {
        return PostRepository(
            postDao = postDao,
            apiService = apiService,
            networkManager = networkManager
        )
    }


}