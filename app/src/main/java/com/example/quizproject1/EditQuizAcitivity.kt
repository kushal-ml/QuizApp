package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditQuizActivity : AppCompatActivity() {

    private lateinit var etQuizTitle: EditText
    private lateinit var etQuizTimer: EditText
    private lateinit var btnUpdateQuiz: Button
    private lateinit var btnCancel : Button
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper

    private var quizId: Int = 0

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
        setContentView(R.layout.activity_edit_quiz)

        etQuizTitle = findViewById(R.id.etQuizTitle)
        etQuizTimer = findViewById(R.id.etQuizTimer)
        btnUpdateQuiz = findViewById(R.id.btnUpdateQuiz)
        btnCancel = findViewById(R.id.btnCancel)
        quizDatabaseHelper = QuizDatabaseHelper(this)

        quizId = intent.getIntExtra("QUIZ_ID", 0)
        val quiz = quizDatabaseHelper.getQuizById(quizId)


        btnCancel.setOnClickListener {
            val intent = Intent(this, TeacherHomeActivity::class.java)
            startActivity(intent)
        }

        if (quiz != null) {
            loadQuizDetails(quiz)
            btnUpdateQuiz.setOnClickListener {
                val title = etQuizTitle.text.toString()
                val timer = etQuizTimer.text.toString().toIntOrNull()

                if (title.isNotEmpty() && timer != null) {
                    val questions = quizDatabaseHelper.getQuestionsByQuizId(quizId)
                    val updatedQuiz = Quiz(title, questions, timer, quiz.userId, quizId)
                    quizDatabaseHelper.updateQuiz(updatedQuiz)
                    Toast.makeText(this, "Quiz updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close this activity and return to the previous one
                } else {
                    Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Quiz not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadQuizDetails(quiz: Quiz) {
        etQuizTitle.setText(quiz.title)
        etQuizTimer.setText(quiz.timer.toString())
    }

}

