package com.example.quizproject1


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var etQuizTitle: EditText
    private lateinit var etQuestionText: EditText
    private lateinit var etOption1: EditText
    private lateinit var etOption2: EditText
    private lateinit var etOption3: EditText
    private lateinit var etOption4: EditText
    private lateinit var etAnswer: EditText
    private lateinit var etQuizTimer: EditText
    private lateinit var btnAddQuestion: Button
    private lateinit var btnSaveQuiz: Button

    private val questions = mutableListOf<Question>()
    private var userId: Int = -1

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, TeacherHomeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_sign_out -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        etQuizTitle = findViewById(R.id.etQuizTitle)
        etQuestionText = findViewById(R.id.etQuestionText)
        etOption1 = findViewById(R.id.etOption1)
        etOption2 = findViewById(R.id.etOption2)
        etOption3 = findViewById(R.id.etOption3)
        etOption4 = findViewById(R.id.etOption4)
        etAnswer = findViewById(R.id.etAnswer)
        etQuizTimer = findViewById(R.id.etQuizTimer)
        btnAddQuestion = findViewById(R.id.btnAddQuestion)
        btnSaveQuiz = findViewById(R.id.btnSaveQuiz)

        userId = intent.getIntExtra("USER_ID", -1)

        btnAddQuestion.setOnClickListener {
            val questionText = etQuestionText.text.toString()
            val option1 = etOption1.text.toString()
            val option2 = etOption2.text.toString()
            val option3 = etOption3.text.toString()
            val option4 = etOption4.text.toString()
            val answer = etAnswer.text.toString()

            if (questionText.isNotEmpty() && option1.isNotEmpty() && option2.isNotEmpty() && option3.isNotEmpty() && option4.isNotEmpty() && answer.isNotEmpty()) {
                val question = Question(questionText, listOf(option1, option2, option3, option4), answer)
                questions.add(question)
                Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show()
                clearQuestionInputs()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnSaveQuiz.setOnClickListener {
            val quizTitle = etQuizTitle.text.toString()
            val quizTimer = etQuizTimer.text.toString().toIntOrNull() ?: 0
            if (quizTitle.isNotEmpty() && questions.isNotEmpty()) {
                val quiz = Quiz(quizTitle, questions, quizTimer, userId)
                QuizDatabaseHelper(this).addQuiz(quiz)
                Toast.makeText(this, "Quiz Saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please add a title and at least one question", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearQuestionInputs() {
        etQuestionText.text.clear()
        etOption1.text.clear()
        etOption2.text.clear()
        etOption3.text.clear()
        etOption4.text.clear()
        etAnswer.text.clear()
    }
}

