package com.example.filmlistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmlistan.ui.theme.FilmlistanTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmlistanTheme {
                    FilmlistanScreen()
                }
            }
        }
    }

data class Film(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
    val bild: Int? = null,
)

val startfilmer = listOf(
    Film(1, "Jurassic Park", 1993, "Äventyr", bild = R.drawable.jurassic_park),
    Film(2, "The Lost World: Jurassic Park", 1997, "Äventyr", bild = R.drawable.the_lost_world_jurassic_park),
    Film(5, "Jurassic World: Fallen Kingdom", 2018, "Äventyr", bild = R.drawable.jurassic_world_fallen_kingdom),
    Film(7, "Jurassic World Rebirth", 2025, "Äventyr", bild = R.drawable.jurassic_world_rebirth),
)

val filmDatabas = listOf(
    Film(0, "The Matrix", 1999, "Sci-Fi", bild = R.drawable.the_matrix),
    Film(0, "Avatar", 2009, "Äventyr", bild = R.drawable.avatar),
    // Exempel: Film(0, "Avatar", 2009, "Äventyr", bild = R.drawable.avatar_bild)
)

@Composable
fun BildPlats(
    titel: String,
    bild: Int? = null, // Ny parameter
    modifier: Modifier = Modifier,
) {
    val form = modifier
        .size(width = 80.dp, height = 120.dp)
        .clip(RoundedCornerShape(8.dp))

    if (bild != null) {
        Image(
            painter = painterResource(id = bild),
            contentDescription = titel,
            contentScale = ContentScale.Crop,
            modifier = form,
        )
    } else {
        Box(
            modifier = form.background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = titel.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun FilmKort(
    film: Film,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF252525)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BildPlats(titel = film.titel, bild = film.bild)
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = film.titel,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "${film.ar} · ${film.genre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                TextButton(onClick = onRemove) {
                    Text("Ta bort", color = Color(0xFFFF5252))
                }
            }
            Checkbox(
                checked = film.sedd,
                onCheckedChange = { onToggle() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmlistanScreen(viewModel: FilmlistanViewModel = viewModel()) {
    val state = viewModel.uiState
    val attSe = state.filmer.filter { !it.sedd }
    val sedda = state.filmer.filter { it.sedd }
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = { Text("Filmlistan", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFD700)
                ),
                navigationIcon = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Meny",
                            tint = Color.Black
                        )
                    }
                    // Själva menyn som fälls ut
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rensa alla filmer") },
                            onClick = {
                                viewModel.clearAll()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Om appen") },
                            onClick = {
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = state.nyTitel,
                onValueChange = { viewModel.onNewTitleChange(it) },
                label = { Text("Titel") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.addFilm() },
                enabled = state.nyTitel.isNotBlank(),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFF252525),
                    disabledContentColor = Color.Gray
                )
            ) {
                Text("Lägg till")
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Text(
                        text = "Vill se",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White, // Gör texten vit
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(attSe, key = { it.id }) { film ->
                    FilmKort(
                        film = film,
                        onToggle = { viewModel.toggleSeen(film.id) },
                        onRemove = { viewModel.removeFilm(film.id) }
                    )
                }

                item {
                    Text(
                        text = "Sett",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(sedda, key = { it.id }) { film ->
                    FilmKort(
                        film = film,
                        onToggle = { viewModel.toggleSeen(film.id) },
                        onRemove = { viewModel.removeFilm(film.id) }
                    )
                }
            }
        }
    }
}