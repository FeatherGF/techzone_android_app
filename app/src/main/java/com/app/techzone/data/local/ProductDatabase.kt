package com.app.techzone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.app.techzone.data.local.dao.ProductsDao
import com.app.techzone.data.local.model.ProductEntity
import com.app.techzone.data.remote.model.Photo
import com.google.gson.Gson


class Converters {
    @TypeConverter
    fun toPhotoList(photos: List<Photo>): String = Gson().toJson(photos)

    @TypeConverter
    fun jsonPhotosToList(photos: String) = Gson().fromJson(photos, Array<Photo>::class.java).toList()
}


@Database(
    entities = [ProductEntity::class],
    version = 8,
)
@TypeConverters(Converters::class)
abstract class ProductDatabase: RoomDatabase() {
    abstract val productsDao: ProductsDao
}