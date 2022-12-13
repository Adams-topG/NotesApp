package com.example.notesapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.presentation.screens.add.AddFragment
import java.io.Serializable
import java.util.Date
@Entity(tableName="note_table")
data class NoteModel (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo
    val title:String,
    @ColumnInfo
    val description:String,
    @ColumnInfo
    val date:Long = System.currentTimeMillis()
    ) : Serializable
