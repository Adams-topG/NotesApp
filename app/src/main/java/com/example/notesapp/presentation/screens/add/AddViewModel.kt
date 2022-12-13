package com.example.notesapp.presentation.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.db.NoteDatabase
import com.example.notesapp.data.db.repository.NoteRealization
import com.example.notesapp.data.db.repository.NoteRepository
import com.example.notesapp.data.models.NoteModel
import com.example.notesapp.data.models.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddViewModel:ViewModel() {
    var repository: NoteRepository? = null

    init {
        val noteDao =
            NoteDatabase.database?.getNoteDao()
        noteDao?.let { repository = NoteRealization(it) }
    }

    fun insert(noteModel:NoteModel, onSuccess:suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            repository?.insertNote(noteModel){
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }

    fun delete(noteModel: NoteModel, onSuccess:suspend () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            repository?.deleteNote(noteModel){
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }


}