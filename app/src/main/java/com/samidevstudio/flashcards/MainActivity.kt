package com.samidevstudio.flashcards

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.samidevstudio.flashcards.ui.components.illustrations.StudyEmptyState
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.ui.screens.DeckDetailsScreen
import com.samidevstudio.flashcards.ui.screens.DeckListScreen
import com.samidevstudio.flashcards.ui.theme.FlashcardsCustomStudyCardsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            FlashcardsCustomStudyCardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlashcardsAppContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsAppContent() {
    val backStack = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { Json.encodeToString(it) } },
            restore = { strings -> 
                val list = mutableStateListOf<Route>()
                strings.forEach { list.add(Json.decodeFromString(it)) }
                list
            }
        )
    ) { mutableStateListOf<Route>(Route.DeckList) }
    
    val navigator = rememberListDetailPaneScaffoldNavigator<Route>()
    
    val isListAndDetailVisible = navigator.scaffoldDirective.maxHorizontalPartitions > 1
    val currentRoute = backStack.lastOrNull()
    val selectedDeckId = (currentRoute as? Route.DeckDetails)?.deckId
    
    // Sync navigator with backstack
    LaunchedEffect(currentRoute) {
        if (currentRoute is Route.DeckDetails) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        } else {
            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
        }
    }

    BackHandler(enabled = backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }

    Scaffold(
        topBar = {
            if (isListAndDetailVisible) {
                TopAppBar(
                    title = {
                        Text(
                            text = if (currentRoute is Route.DeckDetails) 
                                currentRoute.deckName 
                            else 
                                stringResource(R.string.deck_list_title)
                        )
                    },
                    navigationIcon = {
                        if (currentRoute is Route.DeckDetails) {
                            IconButton(onClick = { backStack.removeAt(backStack.lastIndex) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button_desc)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        val safeHorizontal = WindowInsets.safeDrawing.asPaddingValues()
        val symmetricalPadding = max(
            max(safeHorizontal.calculateLeftPadding(layoutDirection), safeHorizontal.calculateRightPadding(layoutDirection)),
            16.dp
        )

        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective.copy(
                horizontalPartitionSpacerSize = 0.dp,
                defaultPanePreferredWidth = 420.dp
            ),
            value = navigator.scaffoldValue,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            listPane = {
                AnimatedPane(
                    modifier = Modifier
                        .preferredWidth(420.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    if (isListAndDetailVisible) {
                        DeckListScreen(
                            onNavigateToDetails = { 
                                if (backStack.lastOrNull() is Route.DeckDetails) {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                                backStack.add(it)
                            },
                            showTopBar = false,
                            selectedDeckId = selectedDeckId,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = symmetricalPadding)
                        )
                    } else {
                        DeckListScreen(
                            onNavigateToDetails = { 
                                backStack.add(it)
                            },
                            showTopBar = true,
                            selectedDeckId = selectedDeckId
                        )
                    }
                }
            },
            detailPane = {
                val route = backStack.filterIsInstance<Route.DeckDetails>().lastOrNull()
                AnimatedPane(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    if (route != null) {
                        DeckDetailsScreen(
                            route = route,
                            onBack = { backStack.removeAt(backStack.lastIndex) },
                            showTopBar = !isListAndDetailVisible,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = if (isListAndDetailVisible) symmetricalPadding else 0.dp)
                        )
                    } else {
                        StudyEmptyState()
                    }
                }
            }
        )
    }
}
