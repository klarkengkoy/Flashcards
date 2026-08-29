package com.samidevstudio.flashcards.data.local

import androidx.room.TypeConverter
import com.samidevstudio.flashcards.model.FlashcardField
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromFlashcardFieldList(value: List<FlashcardField>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFlashcardFieldList(value: String): List<FlashcardField> {
        return Json.decodeFromString(value)
    }
}
