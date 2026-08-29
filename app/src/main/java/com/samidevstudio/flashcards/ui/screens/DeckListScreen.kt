package com.samidevstudio.flashcards.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samidevstudio.flashcards.R
import com.samidevstudio.flashcards.model.Deck
import com.samidevstudio.flashcards.ui.components.shimmerEffect
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.viewmodel.DeckListViewModel

@Composable
fun DeckListScreen(
    onNavigateToDetails: (Route.DeckDetails) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeckListViewModel = hiltViewModel(),
    showTopBar: Boolean = true,
    selectedDeckId: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeckListContent(
        decks = uiState.decks,
        isLoading = uiState.isLoading,
        onDeckClick = { deck ->
            onNavigateToDetails(Route.DeckDetails(deck.id, deck.name))
        },
        showTopBar = showTopBar,
        selectedDeckId = selectedDeckId,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListContent(
    decks: List<Deck>,
    isLoading: Boolean,
    onDeckClick: (Deck) -> Unit,
    showTopBar: Boolean = true,
    selectedDeckId: String? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (showTopBar) 
            MaterialTheme.colorScheme.surface 
        else 
            Color.Transparent,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(stringResource(R.string.deck_list_title)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "contentTransition"
        ) { loading ->
            // Use smaller adaptive size for narrower list pane
            val gridColumns = if (showTopBar) GridCells.Adaptive(minSize = 300.dp) else GridCells.Fixed(1)
            
            if (loading) {
                LazyVerticalGrid(
                    columns = gridColumns,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        end = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(88.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect()
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = gridColumns,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        end = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = decks,
                        key = { deck -> deck.id }
                    ) { deck ->
                        DeckItemCard(
                            deck = deck,
                            isSelected = deck.id == selectedDeckId,
                            onClick = { onDeckClick(deck) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeckItemCard(
    deck: Deck,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.98f else 1f,
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 0.dp else 2.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = deck.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(
                        R.string.deck_list_supporting_text,
                        deck.cardCount,
                        deck.description
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
