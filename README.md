# Notes App

A simple Android Notes application built with Jetpack Compose. This app allows users to add, view, and delete notes.

## Features

*   **Add Notes:** Easily add new notes via a dialog.
*   **View Notes:** Display all notes in a scrollable list.
*   **Delete Notes:** Remove notes individually.
*   **Search Notes:** Filter notes by title using a search bar.
*   **Responsive UI:** Built with Jetpack Compose for a modern and responsive user experience.
*   **Dark/Light Theme:** Adapts to the system's dark or light theme.

## Technologies Used

*   **Kotlin:** The primary language for Android development.
*   **Jetpack Compose:** Android's modern toolkit for building native UI.
*   **Material Design 3:** For a consistent and aesthetically pleasing user interface.

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/NotesApp.git
    cd NotesApp
    ```
2.  **Open in Android Studio:**
    Open the project in Android Studio (Jellyfish | 2023.3.1 or newer is recommended).

3.  **Build and Run:**
    *   Select an emulator or connect a physical Android device.
    *   Click the 'Run' button (green triangle icon) in Android Studio.

The app will be installed and launched on your selected device/emulator.

## Project Structure

*   `app/src/main/java/com/example/notesapp/`: Contains the core application logic.
    *   `MainActivity.kt`: The entry point of the application.
    *   `data/Note.kt`: Data class defining the structure of a note.
    *   `ui/NotesScreen.kt`: The main screen composable for displaying notes and handling interactions.
    *   `ui/AddNoteDialog.kt`: Composable for the dialog to add new notes.
    *   `ui/NoteItem.kt`: Composable for displaying individual note items in the list.
    *   `ui/theme/`: Contains theme-related definitions (Color, Theme, Type).

## Future Enhancements (Potential)

*   Persistent storage for notes (e.g., Room database).
*   Edit existing notes.
*   Add note content/description in addition to the title.
*   Improved search functionality.
*   Sorting options for notes.

## License

This project is open-source and available under the [MIT License](LICENSE).
