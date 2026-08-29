package com.samidevstudio.flashcards.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samidevstudio.flashcards.model.Flashcard
import com.samidevstudio.flashcards.model.FlashcardField

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val deckId: String,
    val frontFields: List<FlashcardField>,
    val backFields: List<FlashcardField>
)

fun FlashcardEntity.asExternalModel() = Flashcard(
    id = id,
    deckId = deckId,
    frontFields = frontFields,
    backFields = backFields
)

fun Flashcard.asEntity() = FlashcardEntity(
    id = id,
    deckId = deckId,
    frontFields = frontFields,
    backFields = backFields
)
