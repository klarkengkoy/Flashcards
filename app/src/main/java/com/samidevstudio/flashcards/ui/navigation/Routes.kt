package com.samidevstudio.flashcards.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object DeckList : Route

    @Serializable
    data class DeckDetails(val deckId: String, val deckName: String) : Route
}
