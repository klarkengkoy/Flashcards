package com.samidevstudio.flashcards.viewmodel

import androidx.lifecycle.ViewModel
import com.samidevstudio.flashcards.data.DeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeckListViewModel @Inject constructor(
    repository: DeckRepository
) : ViewModel() {
    val decks = repository.getDecks()
}
