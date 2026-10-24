# Flashcards - Custom Study Cards

I built this Android flashcard app to help me study kanji and Japanese vocabulary. It stores decks and cards on the device and supports both portrait and landscape study layouts.

## Showcase

<p align="center">
  <img src="https://raw.githubusercontent.com/klarkengkoy/klarkengkoy/main/assets/screenshots/flashcards/showcase/screenshot_1_minimalist_flashcard_app.png" alt="Minimalist flashcard app showcase" width="23%" />
  <img src="https://raw.githubusercontent.com/klarkengkoy/klarkengkoy/main/assets/screenshots/flashcards/showcase/screenshot_2_recall.png" alt="Flashcards recall showcase" width="23%" />
  <img src="https://raw.githubusercontent.com/klarkengkoy/klarkengkoy/main/assets/screenshots/flashcards/showcase/screenshot_3_focus.png" alt="Flashcards focus showcase" width="23%" />
  <img src="https://raw.githubusercontent.com/klarkengkoy/klarkengkoy/main/assets/screenshots/flashcards/showcase/screenshot_4_reveal.png" alt="Flashcards reveal showcase" width="23%" />
</p>

## Core Features
- Study cards with a flip animation to reveal meanings and readings.
- Keep decks and cards available offline with local storage.
- Use a list-detail layout that adapts to phones, tablets, and foldables.
- Study in portrait or landscape, with layouts that account for display cutouts and system bars.

## Technical Stack
- **Jetpack Compose**: Declarative UI with Material 3 components.
- **Material 3 Adaptive**: List-detail layouts.
- **State & Navigation**: MVVM with StateFlow and Navigation 3.
- **Room**: Local storage for decks and cards.
- **Hilt**: Dependency injection.
- **Build**: Kotlin 2.4 and Android Gradle Plugin 9.3.

## Screenshots

### Portrait Mode
<p align="center">
  <img src="screenshots/deck_list_portrait.png" alt="Deck list in portrait" width="32%" />
  <img src="screenshots/flashcard_front_portrait.png" alt="Flashcard front in portrait" width="32%" />
  <img src="screenshots/flashcard_back_portrait.png" alt="Flashcard back in portrait" width="32%" />
</p>

### Landscape Mode
<p align="center">
  <img src="screenshots/landscape_empty_state.png" alt="Empty study view in landscape" width="60%" />
</p>
<p align="center">
  <img src="screenshots/landscape_flashcard_front.png" alt="Flashcard front in landscape" width="60%" />
</p>
<p align="center">
  <img src="screenshots/landscape_flashcard_back.png" alt="Flashcard back in landscape" width="60%" />
</p>
