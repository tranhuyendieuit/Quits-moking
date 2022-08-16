package com.example.quitsmokingnow.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quitsmokingnow.model.ListReasonQuitSmoking
@Dao
interface ListReasonDao {
    @Query("SELECT * FROM table_reason")
    fun getAll(): List<ListReasonQuitSmoking>

    @Insert
    fun insertAll(vararg reasonQuitSmoking: ListReasonQuitSmoking)

    @Delete
    fun delete(reasonQuitSmoking: ListReasonQuitSmoking)

    @Query("DELETE from table_reason")
    fun deleteAll()
}