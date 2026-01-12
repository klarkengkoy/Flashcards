package com.samidevstudio.flashcards.model

data class Flashcard(
    val id: String,
    val front: String,
    val back: String,
    val deckId: String
)

data class Deck(
    val id: String,
    val name: String,
    val description: String = "",
    val cardCount: Int = 0
)
