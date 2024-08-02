package com.example.quizproject1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Question(val text: String, val options: List<String>, val answer: String)
data class Quiz(val title: String, val questions: List<Question>, val timer: Int, val userId: Int, val id: Int? = null)
data class User(val email: String, val password: String, val name: String, val role: String, val id: Int? = null)
data class Result(val email: String, val quizId: Int, val score: Int)

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_ROLE = "role"

        private const val TABLE_QUIZZES = "quizzes"
        private const val COLUMN_QUIZ_ID = "id"
        private const val COLUMN_QUIZ_TITLE = "title"
        private const val COLUMN_QUIZ_TIMER = "timer"
        private const val COLUMN_QUIZ_USER_ID = "user_id"

        private const val TABLE_QUESTIONS = "questions"
        private const val COLUMN_QUESTION_ID = "id"
        private const val COLUMN_QUESTION_TEXT = "text"
        private const val COLUMN_QUESTION_OPTIONS = "options"
        private const val COLUMN_QUESTION_ANSWER = "answer"
        private const val COLUMN_QUESTION_QUIZ_ID = "quiz_id"

        private const val TABLE_RESULTS = "results"
        private const val COLUMN_RESULT_ID = "id"
        private const val COLUMN_RESULT_EMAIL = "email"
        private const val COLUMN_RESULT_QUIZ_ID = "quiz_id"
        private const val COLUMN_RESULT_SCORE = "score"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_USER_EMAIL TEXT UNIQUE NOT NULL, 
                $COLUMN_USER_PASSWORD TEXT NOT NULL,
                $COLUMN_USER_NAME TEXT NOT NULL,
                $COLUMN_USER_ROLE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsersTable)
        Log.d("DBHelper", "Users table created")

        val createQuizzesTable = """
            CREATE TABLE $TABLE_QUIZZES (
                $COLUMN_QUIZ_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_QUIZ_TITLE TEXT NOT NULL, 
                $COLUMN_QUIZ_TIMER INTEGER NOT NULL, 
                $COLUMN_QUIZ_USER_ID INTEGER, 
                FOREIGN KEY($COLUMN_QUIZ_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()
        db.execSQL(createQuizzesTable)
        Log.d("DBHelper", "Quizzes table created")

        val createQuestionsTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COLUMN_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_QUESTION_TEXT TEXT NOT NULL, 
                $COLUMN_QUESTION_OPTIONS TEXT NOT NULL, 
                $COLUMN_QUESTION_ANSWER TEXT NOT NULL, 
                $COLUMN_QUESTION_QUIZ_ID INTEGER, 
                FOREIGN KEY($COLUMN_QUESTION_QUIZ_ID) REFERENCES $TABLE_QUIZZES($COLUMN_QUIZ_ID)
            )
        """.trimIndent()
        db.execSQL(createQuestionsTable)
        Log.d("DBHelper", "Questions table created")

        val createResultsTable = """
            CREATE TABLE $TABLE_RESULTS (
                $COLUMN_RESULT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_RESULT_EMAIL TEXT NOT NULL,  
                $COLUMN_RESULT_QUIZ_ID INTEGER,
                $COLUMN_RESULT_SCORE INTEGER,
                FOREIGN KEY($COLUMN_RESULT_QUIZ_ID) REFERENCES $TABLE_QUIZZES($COLUMN_QUIZ_ID)
            )
        """.trimIndent()
        db.execSQL(createResultsTable)
        Log.d("DBHelper", "Results table created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DBHelper", "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESULTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZZES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_EMAIL, user.email)
            put(COLUMN_USER_PASSWORD, user.password)
            put(COLUMN_USER_NAME, user.name)
            put(COLUMN_USER_ROLE, user.role)
        }
        val userId = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        Log.d("DBHelper", "User added with ID: $userId")
        return userId
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_NAME, COLUMN_USER_ROLE),
            "$COLUMN_USER_EMAIL=?",
            arrayOf(email),
            null, null, null
        )
        try {
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
                val role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
                return User(email, password, name, role, id)
            }
        } finally {
            cursor.close()
            db.close()
        }
        return null
    }

    fun addQuiz(quiz: Quiz) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_QUIZ_TITLE, quiz.title)
            put(COLUMN_QUIZ_TIMER, quiz.timer)
            put(COLUMN_QUIZ_USER_ID, quiz.userId)
        }
        val quizId = db.insert(TABLE_QUIZZES, null, contentValues).toInt()

        quiz.questions.forEach { question ->
            val questionValues = ContentValues().apply {
                put(COLUMN_QUESTION_TEXT, question.text)
                put(COLUMN_QUESTION_OPTIONS, question.options.joinToString(";"))
                put(COLUMN_QUESTION_ANSWER, question.answer)
                put(COLUMN_QUESTION_QUIZ_ID, quizId)
            }
            db.insert(TABLE_QUESTIONS, null, questionValues)
        }
        db.close()
        Log.d("DBHelper", "Quiz added with ID: $quizId")
    }

    fun getQuestionsByQuizId(quizId: Int): List<Question> {
        val questionList = mutableListOf<Question>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_QUESTIONS,
            arrayOf(COLUMN_QUESTION_TEXT, COLUMN_QUESTION_OPTIONS, COLUMN_QUESTION_ANSWER),
            "$COLUMN_QUESTION_QUIZ_ID=?",
            arrayOf(quizId.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_TEXT))
                val options = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_OPTIONS)).split(";")
                val answer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION_ANSWER))
                questionList.add(Question(text, options, answer))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return questionList
    }

    fun getQuizById(id: Int): Quiz? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_QUIZZES,
            arrayOf(COLUMN_QUIZ_TITLE, COLUMN_QUIZ_TIMER, COLUMN_QUIZ_USER_ID),
            "$COLUMN_QUIZ_ID=?",
            arrayOf(id.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TITLE))
            val timer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TIMER))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_USER_ID))
            cursor.close()
            db.close()
            return Quiz(title, getQuestionsByQuizId(id), timer, userId, id)
        }
        cursor.close()
        db.close()
        return null
    }

    fun insertScore(email: String, quizId: Int, score: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_RESULT_EMAIL, email)
            put(COLUMN_RESULT_QUIZ_ID, quizId)
            put(COLUMN_RESULT_SCORE, score)
        }
        val result = db.insert(TABLE_RESULTS, null, contentValues)
        db.close()
        return result != -1L
    }

    fun deleteQuiz(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_QUIZZES, "$COLUMN_QUIZ_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllQuizzes(): List<Quiz> {
        val quizzes = mutableListOf<Quiz>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_QUIZZES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TITLE))
                val timer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TIMER))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_USER_ID))
                quizzes.add(Quiz(title, getQuestionsByQuizId(id), timer, userId, id))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return quizzes
    }

    fun updateQuiz(quiz: Quiz): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_QUIZ_TITLE, quiz.title)
            put(COLUMN_QUIZ_TIMER, quiz.timer)
            put(COLUMN_QUIZ_USER_ID, quiz.userId) // Include userId in the update if necessary
        }
        val result = db.update(TABLE_QUIZZES, contentValues, "$COLUMN_QUIZ_ID = ?", arrayOf(quiz.id.toString()))
        db.close()
        return result > 0
    }

    fun getQuizTitle(quizId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_QUIZZES,
            arrayOf(COLUMN_QUIZ_TITLE),
            "$COLUMN_QUIZ_ID = ?",
            arrayOf(quizId.toString()),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUIZ_TITLE))
            cursor.close()
            db.close()
            return title
        }

        cursor.close()
        db.close()
        return null
    }

    // Removed userId filter as there is no userId in results table
    fun getResultsByUserEmail(email: String): List<Result> {
        val resultList = mutableListOf<Result>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_RESULTS,
            arrayOf(COLUMN_RESULT_EMAIL, COLUMN_RESULT_QUIZ_ID, COLUMN_RESULT_SCORE),
            "$COLUMN_RESULT_EMAIL = ?",
            arrayOf(email),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val quizId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESULT_QUIZ_ID))
                val score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESULT_SCORE))
                resultList.add(Result(email, quizId, score))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return resultList
    }

    fun getAllResults(): List<Result> {
        val resultList = mutableListOf<Result>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_RESULTS,
            arrayOf(COLUMN_RESULT_EMAIL, COLUMN_RESULT_QUIZ_ID, COLUMN_RESULT_SCORE),
            null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULT_EMAIL))
                val quizId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESULT_QUIZ_ID))
                val score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESULT_SCORE))
                resultList.add(Result(email, quizId, score))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return resultList
    }

}
