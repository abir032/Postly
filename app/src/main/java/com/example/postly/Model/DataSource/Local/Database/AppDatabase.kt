// data/local/database/AppDatabase.kt
package com.example.postly.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.postly.Model.DataSource.Local.DAO.PostDao
import com.example.postly.Model.DataSource.Local.DAO.UserDao
import com.example.postly.Model.DataSource.Local.Entity.PostEntity
import com.example.postly.Model.DataSource.Local.Entity.UserEntity
import com.example.postly.Utils.Constants

@Database(
    entities = [UserEntity::class,PostEntity::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}