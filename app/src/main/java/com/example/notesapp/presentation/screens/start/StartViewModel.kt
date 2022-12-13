package com.example.notesapp.presentation.screens.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.data.db.repository.NoteRepository
import com.example.notesapp.data.models.NoteModel


class StartViewModel() : ViewModel() {
    var repository: NoteRepository? = null

    fun getAllNotes(): LiveData<List<NoteModel>>? {
        return repository?.allNotes
    }
}