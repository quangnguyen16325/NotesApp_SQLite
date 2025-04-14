package com.example.notesapp_sql

import android.app.Application
import com.example.notesapp_sql.data.NoteDatabase
import com.example.notesapp_sql.repository.NoteRepository

class NotesApplication : Application() {
    val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}