package com.samidevstudio.flashcards.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samidevstudio.flashcards.model.Deck

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val cardCount: Int
)

fun DeckEntity.asExternalModel() = Deck(
    id = id,
    name = name,
    description = description,
    cardCount = cardCount
)

fun Deck.asEntity() = DeckEntity(
    id = id,
    name = name,
    description = description,
    cardCount = cardCount
)
