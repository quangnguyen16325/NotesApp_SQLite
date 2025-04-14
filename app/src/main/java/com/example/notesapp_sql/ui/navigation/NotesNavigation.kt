package com.example.notesapp_sql.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapp_sql.ui.screen.AddEditNoteScreen
import com.example.notesapp_sql.ui.screen.NoteListScreen
import com.example.notesapp_sql.viewmodel.NoteViewModel

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object AddEditNote : Screen("add_edit_note?noteId={noteId}") {
        fun createRoute(noteId: Int? = null): String {
            return "add_edit_note?noteId=${noteId ?: -1}"
        }
    }
}

@Composable
fun NotesNavigation(
    viewModel: NoteViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(Screen.NoteList.route) {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.AddEditNote.createRoute(noteId))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.AddEditNote.createRoute())
                }
            )
        }

        composable(
            route = Screen.AddEditNote.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")
            AddEditNoteScreen(
                noteId = noteId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}