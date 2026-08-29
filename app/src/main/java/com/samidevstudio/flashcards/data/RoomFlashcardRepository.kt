package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.data.local.dao.FlashcardDao
import com.samidevstudio.flashcards.data.local.entities.asEntity
import com.samidevstudio.flashcards.data.local.entities.asExternalModel
import com.samidevstudio.flashcards.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFlashcardRepository @Inject constructor(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {
    override fun getCardsByDeckId(deckId: String): Flow<List<Flashcard>> =
        flashcardDao.getCardsByDeckId(deckId).map { it.map { entity -> entity.asExternalModel() } }

    suspend fun insertCards(cards: List<Flashcard>) {
        flashcardDao.insertCards(cards.map { it.asEntity() })
    }
}
