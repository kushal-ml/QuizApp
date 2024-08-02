package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var nextButton: Button

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var email: String
    private var quizId: Int = -1

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, StudentHomeActivity::class.java).apply {
                    putExtra("EMAIL", email)
                }
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
        setContentView(R.layout.activity_quiz)

        questionTextView = findViewById(R.id.tvQuestion)
        optionsRadioGroup = findViewById(R.id.rgOptions)
        nextButton = findViewById(R.id.btnNext)

        email = intent.getStringExtra("EMAIL") ?: ""
        quizId = intent.getIntExtra("QUIZ_ID",0)
        if (quizId != -1) {
            questions = QuizDatabaseHelper(this).getQuestionsByQuizId(quizId)
            displayQuestion()
        }

        nextButton.setOnClickListener {
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                displayQuestion()
            } else {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("SCORE", score)
                    putExtra("EMAIL", email)
                    putExtra("QUIZ_ID", quizId)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            val currentQuestion = questions[currentQuestionIndex]
            questionTextView.text = currentQuestion.text
            optionsRadioGroup.removeAllViews()
            currentQuestion.options.forEach { option ->
                val radioButton = RadioButton(this)
                radioButton.text = option
                optionsRadioGroup.addView(radioButton)
            }
        }
    }

    private fun checkAnswer() {
        val selectedRadioButtonId = optionsRadioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val selectedAnswer = selectedRadioButton.text.toString()
            val correctAnswer = questions[currentQuestionIndex].answer
            if (selectedAnswer == correctAnswer) {
                score++
            }
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
        }
    }
}
