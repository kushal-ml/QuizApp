package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TakeQuizActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var btnNext: Button

    private var questionList: List<Question> = listOf()
    private var currentQuestionIndex = 0
    private var score = 0
    private var timer: CountDownTimer? = null
    private var quizTimer: Int = 0
    private var quizId: Int = -1
    private lateinit var quiz: Quiz
    private lateinit var tvQuizTitle: TextView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, StudentHomeActivity::class.java)
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
        setContentView(R.layout.activity_take_quiz)

        tvTimer = findViewById(R.id.tvTimer)
        tvQuestion = findViewById(R.id.tvQuestion)
        rgOptions = findViewById(R.id.rgOptions)
        btnNext = findViewById(R.id.btnNext)


        val quizId = intent.getIntExtra("QUIZ_ID", 1)
        val db = QuizDatabaseHelper(this)
        val quiz = db.getQuizById(quizId)

        if (quiz != null) {
            quizTimer = quiz.timer
            questionList = quiz.questions

            startTimer()
            showQuestion()

            btnNext.setOnClickListener {
                checkAnswer()
                if (currentQuestionIndex < questionList.size - 1) {
                    currentQuestionIndex++
                    showQuestion()
                } else {
                    // Stop the timer and navigate to ResultActivity
                    timer?.cancel()
                    goToResultActivity()
                    val intent = Intent(this, QuizService::class.java)
                    stopService(intent)

                }
            }
        } else {
            Toast.makeText(this, "Quiz not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer((quizTimer * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = "Time left: ${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                showTimeUpDialog()
            }
        }.start()
    }

    private fun showTimeUpDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quiz Time is up!")
        builder.setMessage("You can either sign out or go to the results page.")
        builder.setPositiveButton("Results") { _, _ ->
            goToResultActivity()
        }
        builder.setNegativeButton("Sign Out") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun showQuestion() {
        val question = questionList[currentQuestionIndex]
        tvQuestion.text = question.text
        rgOptions.removeAllViews()
        question.options.forEach { option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            rgOptions.addView(radioButton)
        }

        if (currentQuestionIndex == questionList.size - 1) {
            btnNext.text = "Finish"
        }
    }

    private fun checkAnswer() {
        val selectedOptionId = rgOptions.checkedRadioButtonId
        if (selectedOptionId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
            val selectedAnswer = selectedRadioButton.text.toString()
            if (selectedAnswer == questionList[currentQuestionIndex].answer) {
                score++
            }
        }
    }

    private fun goToResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", questionList.size)
        startActivity(intent)
        finish()
    }
}


