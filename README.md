# AttendTrack

AttendTrack is a simple and modern Android application designed to help teachers and instructors manage classes and track student attendance effortlessly. The app features a clean, edge-to-edge user interface and ensures all data is stored locally on the device.

## Key Features

* **Class Management**: Easily create, edit, and delete classes for different subjects or terms.
* **Student Roster**: Add and manage a list of students for each class, including their name and a unique ID.
* **Intuitive Attendance Tracking**: Mark attendance for an entire class with a simple and intuitive swipe interface—swipe right for "Present" and left for "Absent".
* **Detailed History**: View a complete attendance history for each individual student, clearly showing their status for every recorded day.
* **Modern UI**: A clean, modern user interface that supports edge-to-edge display for a seamless look on modern Android devices.
* **Local Data Storage**: All class, student, and attendance data is stored securely on the device using the Android Room Persistence Library.

## Screenshots

| Home Screen                                       | Classes List                                      | Student List                                      |
| :------------------------------------------------ | :------------------------------------------------ | :------------------------------------------------ |
| ![Home Screen](URL_TO_HOME_SCREEN_IMAGE)          | ![Classes List](URL_TO_CLASSES_LIST_IMAGE)        | ![Student List](URL_TO_STUDENT_LIST_IMAGE)        |
| **Take Attendance** | **Attendance History** |
| ![Take Attendance](URL_TO_TAKE_ATTENDANCE_IMAGE)  | ![Attendance History](URL_TO_ATTENDANCE_HISTORY_IMAGE)|

## Tech Stack & Architecture

This project is a native Android application built entirely in **Java** and utilizes a standard architecture with modern Android development components.

* **Language**: Java
* **Database**: [Android Room Persistence Library](https://developer.android.com/training/data-storage/room) for robust, local SQL database management.
* **UI Components**:
    * [Material Design Components](https://material.io/develop/android/docs/getting-started) for a modern look and feel.
    * `RecyclerView` for displaying lists of classes and students efficiently.
    * `ConstraintLayout` for building complex and responsive layouts.
    * `ViewCompat` and `WindowCompat` to implement the edge-to-edge display.
* **Concurrency**: `ExecutorService` and `Handler` are used to perform database operations on a background thread, ensuring the UI remains responsive.

## Getting Started
### Download
[![Latest Release](https://img.shields.io/github/v/release/Fayz-Rahman/AttendTrack_android)](https://github.com/Fayz-Rahman/AttendTrack_android/releases)

### Build
To build and run this project yourself, follow these steps:

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/Fayz-Rahman/AttendTrack_android.git
    ```
2.  **Open in Android Studio:**
    * Open the latest stable version of Android Studio.
    * Select `File > Open` and choose the cloned project directory.
3.  **Build the Project:**
    * Allow Android Studio to sync the Gradle files and download all the necessary dependencies.
    * Click `Build > Make Project`.
4.  **Run the App:**
    * Select a target device (emulator or physical device running Android 9.0 Pie or newer).
    * Click `Run > Run 'app'`.
