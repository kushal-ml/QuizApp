# Quiz App

A mobile application that allows users to take quizzes, track their scores, and view results. This app includes a login and signup system, and supports both student and teacher roles with different functionalities for each.

## Features

- **Login and Signup**: Users can create an account and log in to access the app.
- **Student Role**: Students can take quizzes, view their scores, and see their results.
- **Teacher Role**: Teachers can create, edit, update, and delete quizzes.
- **Quiz Results**: After completing a quiz, students can view their scores and see a detailed result. Results are saved and can be accessed later.
- **PDF Generation**: When a student finishes a quiz and the score is saved, the result is saved in a PDF file.

## Technologies Used

- **Kotlin**: For app development.
- **SQLite**: For storing user data, quiz questions, and scores.
- **iText**: For generating PDF files.
- **Android Services**: For background operations like PDF generation.

## Screenshots

Include screenshots of your app here.

## Installation

1. Clone the repository:

```sh
git clone https://github.com/kushal-ml/QuizApp.git

2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.


##
Usage
User Roles
Signup and Login
Signup: Users can sign up by providing an email, password, name, and selecting their role (student or teacher).
Login: Users can log in using their email and password.
Student Functionalities
Take Quiz: Students can select and take quizzes.
View Results: Students can view their scores and detailed results after completing a quiz. Results are saved in a PDF file.
Teacher Functionalities
Create Quiz: Teachers can create new quizzes by providing quiz details and questions.
Edit Quiz: Teachers can edit existing quizzes.
Update Quiz: Teachers can update quiz information and questions.
Delete Quiz: Teachers can delete quizzes.
View All Results: Teachers can view the results of all quizzes.
Code Structure
Activities
MainActivity: Entry point of the app.
LoginActivity: Handles user login.
SignupActivity: Handles user signup.
StudentHomeActivity: Home screen for students.
TeacherHomeActivity: Home screen for teachers.
TakeQuizActivity: Handles quiz taking for students.
ResultActivity: Displays quiz results and saves them in a PDF file.
StudentResultsActivity: Displays all results for the logged-in student.
EditQuizActivity: Allows teachers to edit quizzes.
CreateQuizActivity: Allows teachers to create new quizzes.
UpdateQuizActivity: Allows teachers to update quizzes.
DeleteQuizActivity: Allows teachers to delete quizzes.
ShowResultsActivity: Displays results of all quizzes (for teachers).
Services
PDFGenerationService: Service that generates a PDF file when a quiz is finished and the score is saved.
Database
QuizDatabaseHelper: Helper class for SQLite database operations.
Adapters
ResultsAdapter: Adapter for displaying quiz results in a RecyclerView.
Permissions
The app requires the following permissions:

android.permission.WRITE_EXTERNAL_STORAGE
android.permission.READ_EXTERNAL_STORAGE
Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.
