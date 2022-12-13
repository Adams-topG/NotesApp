package com.example.notesapp.data.db.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.data.db.dao.NoteDao
import com.example.notesapp.data.models.NoteModel

class NoteRealization(private val noteDao:NoteDao):NoteRepository {
    override val allNotes: LiveData<List<NoteModel>>
        get() = noteDao.getAllNotes()

    override suspend fun insertNote(noteModel: NoteModel, onSuccess:suspend () -> Unit) {
        noteDao.insert(noteModel)
        onSuccess()
    }

    override suspend fun deleteNote(noteModel: NoteModel, onSuccess:suspend () -> Unit) {
        noteDao.delete(noteModel)
        onSuccess()
    }
}