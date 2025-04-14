package com.example.notesapp_sql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp_sql.data.NoteDatabase
import com.example.notesapp_sql.repository.NoteRepository
import com.example.notesapp_sql.ui.navigation.NotesNavigation
import com.example.notesapp_sql.ui.theme.NotesAppTheme
import com.example.notesapp_sql.viewmodel.NoteViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NoteDatabase.getDatabase(applicationContext)
        val repository = NoteRepository(database.noteDao())
        val viewModelFactory = NoteViewModel.NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        setContent {
            NotesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesNavigation(viewModel = viewModel)
                }
            }
        }
    }
}