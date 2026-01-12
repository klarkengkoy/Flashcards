package com.samidevstudio.flashcards.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.viewmodel.DeckListViewModel

@Composable
fun DeckListScreen(
    onNavigateToDetails: (Route.DeckDetails) -> Unit,
    viewModel: DeckListViewModel = hiltViewModel()
) {
    val decks by viewModel.decks.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Decks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(decks) { deck ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            onNavigateToDetails(Route.DeckDetails(deck.id, deck.name)) 
                        }
                ) {
                    ListItem(
                        headlineContent = { Text(deck.name) },
                        supportingContent = { Text("${deck.cardCount} cards") }
                    )
                }
            }
        }
    }
}
