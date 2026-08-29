package com.samidevstudio.flashcards.di

import android.content.Context
import androidx.room.Room
import com.samidevstudio.flashcards.data.local.FlashcardDatabase
import com.samidevstudio.flashcards.data.local.dao.DeckDao
import com.samidevstudio.flashcards.data.local.dao.FlashcardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FlashcardDatabase {
        return Room.databaseBuilder(
            context,
            FlashcardDatabase::class.java,
            "flashcards_db"
        ).build()
    }

    @Provides
    fun provideDeckDao(database: FlashcardDatabase): DeckDao = database.deckDao()

    @Provides
    fun provideFlashcardDao(database: FlashcardDatabase): FlashcardDao = database.flashcardDao()
}
