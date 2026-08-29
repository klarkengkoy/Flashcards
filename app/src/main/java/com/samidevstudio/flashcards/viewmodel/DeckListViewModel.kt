package com.samidevstudio.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samidevstudio.flashcards.data.DeckRepository
import com.samidevstudio.flashcards.model.Deck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DeckListUiState(
    val decks: List<Deck> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class DeckListViewModel @Inject constructor(
    repository: DeckRepository
) : ViewModel() {
    val uiState: StateFlow<DeckListUiState> = repository.getDecks()
        .map { decks -> DeckListUiState(decks = decks, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DeckListUiState(isLoading = true)
        )
}
