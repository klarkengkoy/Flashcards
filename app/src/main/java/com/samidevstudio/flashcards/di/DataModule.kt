package com.samidevstudio.flashcards.di

import com.samidevstudio.flashcards.data.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindDeckRepository(
        fakeDeckRepository: FakeDeckRepository
    ): DeckRepository

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(
        fakeFlashcardRepository: FakeFlashcardRepository
    ): FlashcardRepository
}
