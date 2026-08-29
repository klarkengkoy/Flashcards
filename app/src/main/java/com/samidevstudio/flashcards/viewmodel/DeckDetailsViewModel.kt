package com.samidevstudio.flashcards.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samidevstudio.flashcards.data.FlashcardRepository
import com.samidevstudio.flashcards.model.Flashcard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DeckDetailsUiState(
    val cards: List<Flashcard> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DeckDetailsViewModel @Inject constructor(
    private val repository: FlashcardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _deckId = savedStateHandle.getStateFlow<String?>(DECK_ID_KEY, null)
    
    val uiState: StateFlow<DeckDetailsUiState> = _deckId
        .flatMapLatest { id ->
            if (id == null) flowOf(DeckDetailsUiState())
            else repository.getCardsByDeckId(id).map { cards ->
                DeckDetailsUiState(cards = cards, isLoading = false)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DeckDetailsUiState(isLoading = true)
        )

    fun setDeckId(id: String) {
        if (_deckId.value != id) {
            savedStateHandle[DECK_ID_KEY] = id
        }
    }

    companion object {
        private const val DECK_ID_KEY = "deckId"
    }
}
