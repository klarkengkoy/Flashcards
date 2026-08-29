package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    fun getDecks(): Flow<List<Deck>>
    fun getDeckById(id: String): Flow<Deck?>
}
