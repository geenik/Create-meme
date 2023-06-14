# Create Meme App

Create Meme App is an Android application that allows users to create and customize memes by adding text and images. The app integrates with Firebase Authentication and Firestore for user authentication and data storage.

## Features

- Choose an image from the gallery or take a photo using the device's camera.
- Add text to the selected image to create a meme.
- Scale and drag images and text to customize their position and size.
- Save the created meme with a custom name to the device's gallery.
- View a list of saved memes with options to delete or download them.
- Firebase user authentication with sign-up and sign-in functionality.

## Architecture

This project follows the MVVM (Model-View-ViewModel) architecture pattern. MVVM is a software architectural pattern that separates the user interface (View) from the data and business logic (Model), using a ViewModel as an intermediary between the two. It promotes a separation of concerns, improves testability, and allows for easier maintenance and scalability of the codebase.

### Components

- **Model**: Represents the data and business logic of the application. It encapsulates data retrieval, manipulation, and storage operations.

- **View**: Represents the user interface of the application. It observes changes in the ViewModel and updates its display accordingly.

- **ViewModel**: Acts as an intermediary between the View and the Model. It exposes data and commands that the View can bind to. It also handles the communication between the View and the Model, such as retrieving data from the Model and updating the Model based on user interactions.

The MVVM architecture pattern enhances the separation of concerns, making the codebase more modular and maintainable. It also enables easier unit testing of individual components.


## Getting Started

To get started with the Create Meme App, follow these steps:

### Prerequisites

- Android Studio
- Firebase account

### Installation

1. Clone the repository or download the project files.
2. Open the project in Android Studio.
3. Connect the project to your Firebase project by following the Firebase setup instructions for Android apps.
4. Update the `build.gradle` file in the app module with the necessary dependencies and configurations.
5. Build and run the app on an Android emulator or a physical device.

### Dependencies

The Create Meme App uses the following dependencies:

- AndroidX libraries for UI components and lifecycle management.
- Firebase Authentication for user authentication.
- Firebase Firestore for data storage.
- Firebase Storage for uploading and downloading images.
- Kotlin Coroutines for asynchronous programming.
- Glide for image loading and caching.


### Usage

- Sign up or sign in with your Firebase account to access the app's features.
- Choose an image by either taking a photo or selecting one from the gallery.
- Add text to the image by entering it in the provided text field and tapping the "Add Text" button.
- Scale and drag the added text or images to adjust their position and size.
- Save the meme by providing a custom name and tapping the "Save" button.
- View the list of saved memes in the "Loaded Memes" screen, where you can delete or download them.
- Tap the floating action button (FAB) to go back to the


