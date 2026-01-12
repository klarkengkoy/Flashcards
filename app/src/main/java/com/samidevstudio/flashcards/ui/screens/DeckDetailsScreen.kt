package com.samidevstudio.flashcards.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samidevstudio.flashcards.model.Flashcard
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.viewmodel.DeckDetailsViewModel

@Composable
fun DeckDetailsScreen(
    route: Route.DeckDetails,
    viewModel: DeckDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(route.deckId) {
        viewModel.setDeckId(route.deckId)
    }

    val cards by viewModel.cards.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(route.deckName, style = MaterialTheme.typography.headlineMedium)
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cards) { card ->
                FlashcardItem(card)
            }
        }
    }
}

@Composable
fun FlashcardItem(card: Flashcard) {
    var rotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500),
        label = "cardRotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { rotated = !rotated },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Text(
                    text = card.front,
                    style = MaterialTheme.typography.headlineLarge
                )
            } else {
                Text(
                    text = card.back,
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
