package com.example.notesapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.data.models.NoteModel

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteModel: NoteModel)
    @Delete
    suspend fun delete(noteModel: NoteModel)
    @Query("SELECT * from note_table")
    fun getAllNotes():LiveData<List<NoteModel>>
}