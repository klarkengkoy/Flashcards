package com.samidevstudio.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samidevstudio.flashcards.data.local.dao.DeckDao
import com.samidevstudio.flashcards.data.local.dao.FlashcardDao
import com.samidevstudio.flashcards.data.local.entities.DeckEntity
import com.samidevstudio.flashcards.data.local.entities.FlashcardEntity

@Database(
    entities = [DeckEntity::class, FlashcardEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FlashcardDatabase : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun flashcardDao(): FlashcardDao
}
