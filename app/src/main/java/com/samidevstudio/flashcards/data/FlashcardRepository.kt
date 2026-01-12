package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface FlashcardRepository {
    fun getCardsByDeckId(deckId: String): Flow<List<Flashcard>>
}

@Singleton
class FakeFlashcardRepository @Inject constructor() : FlashcardRepository {
    private val cards = listOf(
        Flashcard("1", "私", "I, me (Watashi)", "1"),
        Flashcard("2", "人", "Person (Hito)", "1"),
        Flashcard("3", "学", "Study, learning (Gaku)", "1"),
        Flashcard("4", "Composable", "A function marked with @Composable", "2"),
        Flashcard("5", "State", "Value that changes over time", "2")
    )

    override fun getCardsByDeckId(deckId: String): Flow<List<Flashcard>> = 
        flowOf(cards.filter { it.deckId == deckId })
}
