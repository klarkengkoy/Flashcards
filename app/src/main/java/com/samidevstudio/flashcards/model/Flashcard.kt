package com.samidevstudio.flashcards.model

import kotlinx.serialization.Serializable

@Serializable
data class Flashcard(
    val id: String,
    val deckId: String,
    val frontFields: List<FlashcardField>,
    val backFields: List<FlashcardField>
)

@Serializable
data class FlashcardField(
    val label: String, // e.g., "Kanji", "Meaning", "Year"
    val content: String, // The main text
    val phonetic: String? = null // Optional reading, still useful for many subjects but not core to structure
)

@Serializable
data class Deck(
    val id: String,
    val name: String,
    val description: String = "",
    val cardCount: Int = 0
)
