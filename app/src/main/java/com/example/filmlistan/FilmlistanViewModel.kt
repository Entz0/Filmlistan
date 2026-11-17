package com.example.filmlistan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class FilmlistanUiState(
    val nyTitel: String = "",
    val filmer: List<Film> = startfilmer,
)

class FilmlistanViewModel : ViewModel() {

    fun clearAll() {
        uiState = uiState.copy(filmer = emptyList())
    }
    var uiState by mutableStateOf(FilmlistanUiState())
        private set

    fun onNewTitleChange(text: String) {
        uiState = uiState.copy(nyTitel = text)
    }

    private var nextId = 4

    fun addFilm() {
        val sökTitel = uiState.nyTitel.trim()
        if (sökTitel.isEmpty()) return

        val hittadFilm = filmDatabas.find { it.titel.equals(sökTitel, ignoreCase = true) }

        val nyFilm = if (hittadFilm != null) {
            hittadFilm.copy(id = nextId)
        } else {
            Film(
                id = nextId,
                titel = sökTitel,
                ar = 0,
                genre = "Okänd"
            )
        }

        nextId = nextId + 1

        uiState = uiState.copy(
            nyTitel = "",
            filmer = uiState.filmer + nyFilm,
        )
    }
    fun toggleSeen(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.map { film ->
                if (film.id == id) film.copy(sedd = !film.sedd) else film
            }
        )
    }
    fun removeFilm(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.filter { it.id != id }
        )
    }
}