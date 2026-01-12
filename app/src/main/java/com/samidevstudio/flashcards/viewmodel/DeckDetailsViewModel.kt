package com.samidevstudio.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import com.samidevstudio.flashcards.data.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DeckDetailsViewModel @Inject constructor(
    private val repository: FlashcardRepository
) : ViewModel() {
    private val _deckId = MutableStateFlow<String?>(null)
    
    val cards = _deckId.flatMapLatest { id ->
        if (id == null) kotlinx.coroutines.flow.flowOf(emptyList())
        else repository.getCardsByDeckId(id)
    }

    fun setDeckId(id: String) {
        _deckId.value = id
    }
}
