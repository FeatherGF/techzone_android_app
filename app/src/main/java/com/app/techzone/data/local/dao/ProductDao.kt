package com.app.techzone.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.techzone.data.local.model.ProductEntity

@Dao
interface ProductsDao {

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM productentity WHERE sorting = :sorting ORDER BY pk")
    fun pagingSource(sorting: String): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM productentity WHERE sorting = :sorting ORDER BY pk")
    suspend fun getKeys(sorting: String): List<ProductEntity>

    @Query("DELETE FROM productentity WHERE sorting = :sorting")
    suspend fun clearAll(sorting: String)

    @Query(
        """
        SELECT lastUpdated from productentity WHERE sorting = :sorting 
        ORDER BY lastUpdated DESC LIMIT 1
        """
    )
    suspend fun lastUpdated(sorting: String): Long?
}