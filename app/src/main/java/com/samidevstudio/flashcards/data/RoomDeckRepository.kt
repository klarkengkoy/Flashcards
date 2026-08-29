package com.samidevstudio.flashcards.data

import com.samidevstudio.flashcards.data.local.dao.DeckDao
import com.samidevstudio.flashcards.data.local.entities.asEntity
import com.samidevstudio.flashcards.data.local.entities.asExternalModel
import com.samidevstudio.flashcards.model.Deck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomDeckRepository @Inject constructor(
    private val deckDao: DeckDao
) : DeckRepository {
    override fun getDecks(): Flow<List<Deck>> =
        deckDao.getDecks().map { it.map { entity -> entity.asExternalModel() } }

    override fun getDeckById(id: String): Flow<Deck?> =
        deckDao.getDeckById(id).map { it?.asExternalModel() }

}
