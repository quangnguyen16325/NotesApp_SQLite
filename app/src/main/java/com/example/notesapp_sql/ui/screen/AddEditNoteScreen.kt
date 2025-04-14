package com.example.notesapp_sql.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notesapp_sql.data.NoteEntity
import com.example.notesapp_sql.ui.theme.NoteBlue
import com.example.notesapp_sql.ui.theme.NoteGreen
import com.example.notesapp_sql.ui.theme.NotePink
import com.example.notesapp_sql.ui.theme.NotePurple
import com.example.notesapp_sql.ui.theme.NoteYellow
import com.example.notesapp_sql.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Int?,
    viewModel: NoteViewModel,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableIntStateOf(0) }

    val isEditing = noteId != null && noteId != -1

    LaunchedEffect(noteId) {
        if (isEditing) {
            noteId?.let { id ->
                coroutineScope.launch {
                    viewModel.getNoteById(id)?.let { note ->
                        title = note.title
                        content = note.content
                        selectedColorIndex = note.color ?: 0
                    }
                }
            }
        }
    }

    val noteColors = listOf(
        Color.White,
        NoteYellow,
        NoteGreen,
        NoteBlue,
        NotePink,
        NotePurple
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (isEditing) "Edit Note" else "Add Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotBlank()) {
                        if (isEditing) {
                            noteId?.let { id ->
                                val updatedNote = NoteEntity(
                                    id = id,
                                    title = title,
                                    content = content,
                                    color = selectedColorIndex
                                )
                                viewModel.updateNote(updatedNote)
                            }
                        } else {
                            viewModel.insertNote(
                                title = title,
                                content = content,
                                color = selectedColorIndex
                            )
                        }
                        onBackClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Title") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("Note content") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select Color",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                noteColors.forEachIndexed { index, color ->
                    ColorItem(
                        color = color,
                        selected = selectedColorIndex == index,
                        onClick = { selectedColorIndex = index }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 2.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}

fun NoteViewModel.getNoteById(id: Int): NoteEntity? {
    val notes = notes.value
    return notes.find { it.id == id }
}