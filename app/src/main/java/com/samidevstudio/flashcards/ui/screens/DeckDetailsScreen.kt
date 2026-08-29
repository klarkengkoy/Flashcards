package com.samidevstudio.flashcards.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samidevstudio.flashcards.R
import com.samidevstudio.flashcards.model.Flashcard
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.ui.theme.*
import com.samidevstudio.flashcards.viewmodel.DeckDetailsViewModel

@Composable
fun DeckDetailsScreen(
    route: Route.DeckDetails,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeckDetailsViewModel = hiltViewModel(),
    showTopBar: Boolean = true
) {
    LaunchedEffect(route.deckId) {
        viewModel.setDeckId(route.deckId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeckDetailsContent(
        deckId = route.deckId,
        deckName = route.deckName,
        cards = uiState.cards,
        isLoading = uiState.isLoading,
        onBack = onBack,
        showTopBar = showTopBar,
        modifier = modifier
    )
}

@Composable
fun DeckDetailsContent(
    deckId: String,
    deckName: String,
    cards: List<Flashcard>,
    isLoading: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showTopBar: Boolean = true
) {
    Scaffold(
        modifier = modifier,
        containerColor = if (showTopBar) MaterialTheme.colorScheme.surface else Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(deckName) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button_desc)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (cards.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.select_deck_prompt))
            }
        } else {
            // Key the pager state to deckId so it resets when changing decks
            val pagerState = key(deckId) {
                rememberPagerState(pageCount = { cards.size })
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    ),
                    pageSpacing = 24.dp
                ) { page ->
                    FlashcardStudyCard(card = cards[page])
                }
                
                // Card Counter
                Text(
                    text = stringResource(R.string.card_counter, pagerState.currentPage + 1, cards.size),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FlashcardStudyCard(
    card: Flashcard,
    modifier: Modifier = Modifier
) {
    var rotated by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()
    
    // Reset rotation when card content changes
    LaunchedEffect(card.id) {
        rotated = false
    }
    
    // Gradient Selection
    val gStart = if (isDarkTheme) GradientStartDark else GradientStart
    val gEnd = if (isDarkTheme) GradientEndDark else GradientEnd
    
    // Elastic Flip Animation
    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardFlip"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Lift Animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "scale"
    )
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 8.dp,
        label = "elevation"
    )

    Card(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                rotated = !rotated
            },
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (rotation <= 90f) {
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(gStart, gEnd))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Front Side
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    card.frontFields.forEachIndexed { index, field ->
                        FlashcardSideContent(
                            title = field.label,
                            content = field.content,
                            phonetic = field.phonetic,
                            isPrimary = index == 0,
                            isDark = false
                        )
                    }
                }
            } else {
                // Back Side
                Box(
                    Modifier.graphicsLayer {
                        rotationY = 180f
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        card.backFields.forEachIndexed { index, field ->
                            FlashcardSideContent(
                                title = field.label,
                                content = field.content,
                                phonetic = field.phonetic,
                                isPrimary = index == 0,
                                isDark = true
                            )
                            if (index < card.backFields.lastIndex) {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(
                                    modifier = Modifier.width(48.dp),
                                    thickness = 2.dp,
                                    color = Color.White.copy(alpha = 0.3f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardSideContent(
    title: String,
    content: String,
    phonetic: String?,
    isPrimary: Boolean,
    isDark: Boolean
) {
    val contentColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
    val labelColor = if (isDark) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = labelColor,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(if (isPrimary) 12.dp else 8.dp))
        Text(
            text = content,
            style = if (isPrimary) {
                if (content.length > 8) 
                    MaterialTheme.typography.displaySmall 
                else 
                    MaterialTheme.typography.displayLarge
            } else {
                MaterialTheme.typography.headlineMedium
            },
            color = contentColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        phonetic?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isDark) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
