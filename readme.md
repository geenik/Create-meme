# Create Meme App

Create Meme App is an Android application that allows users to create and customize memes by adding text and images. The app integrates with Firebase Authentication and Firestore for user authentication and data storage.

## Features

- Choose an image from the gallery or take a photo using the device's camera.
- Add text to the selected image to create a meme.
- Scale and drag images and text to customize their position and size.
- Save the created meme with a custom name to the device's gallery.
- View a list of saved memes with options to delete or download them.
- Firebase user authentication with sign-up and sign-in functionality.


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


