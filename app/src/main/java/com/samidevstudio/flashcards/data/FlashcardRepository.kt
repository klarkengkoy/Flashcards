package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getCardsByDeckId(deckId: String): Flow<List<Flashcard>>
}
