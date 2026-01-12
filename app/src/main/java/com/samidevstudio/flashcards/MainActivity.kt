package com.samidevstudio.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.samidevstudio.flashcards.ui.navigation.Route
import com.samidevstudio.flashcards.ui.screens.DeckDetailsScreen
import com.samidevstudio.flashcards.ui.screens.DeckListScreen
import com.samidevstudio.flashcards.ui.theme.FlashcardsCustomStudyCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardsCustomStudyCardsTheme {
                FlashcardsAppContent()
            }
        }
    }
}

@Composable
fun FlashcardsAppContent() {
    val backStack = remember { mutableStateListOf<Any>(Route.DeckList) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { 
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider = { key ->
                when (key) {
                    is Route.DeckList -> NavEntry(key) {
                        DeckListScreen(
                            onNavigateToDetails = { backStack.add(it) }
                        )
                    }
                    is Route.DeckDetails -> {
                        NavEntry(key) {
                            DeckDetailsScreen(route = key)
                        }
                    }
                    else -> error("Unknown route: $key")
                }
            }
        )
    }
}
