package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.model.Deck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface DeckRepository {
    fun getDecks(): Flow<List<Deck>>
    fun getDeckById(id: String): Flow<Deck?>
}

@Singleton
class FakeDeckRepository @Inject constructor() : DeckRepository {
    private val decks = listOf(
        Deck("1", "Kotlin Basics", "Fundamental concepts of Kotlin", 20),
        Deck("2", "Jetpack Compose", "Building modern UI with Compose", 15),
        Deck("3", "Hilt DI", "Dependency injection for Android", 10)
    )

    override fun getDecks(): Flow<List<Deck>> = flowOf(decks)

    override fun getDeckById(id: String): Flow<Deck?> = flowOf(decks.find { it.id == id })
}
