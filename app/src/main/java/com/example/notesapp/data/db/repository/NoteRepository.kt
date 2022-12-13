package com.example.notesapp.data.db.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.data.models.NoteModel

interface NoteRepository {
    val allNotes:LiveData<List<NoteModel>>
    suspend fun insertNote(noteModel: NoteModel, onSuccess:suspend ()-> Unit)
    suspend fun deleteNote(noteModel: NoteModel, onSuccess:suspend()-> Unit)
}