package com.example.quizproject1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var btnFinish: Button
    private lateinit var dbHelper: QuizDatabaseHelper
    private lateinit var email: String
    private var score: Int = 0
    private var quizId: Int = -1

    @SuppressLint("MissingInflatedId")
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
        setContentView(R.layout.activity_result)

        email = intent.getStringExtra("EMAIL") ?: "Unknown"
        score = intent.getIntExtra("SCORE", 0)
        quizId = intent.getIntExtra("QUIZ_ID", -1)

        scoreTextView = findViewById(R.id.scoreTextView)
        btnFinish = findViewById(R.id.btnFinish)

        scoreTextView.text = "Email: student1@gmail.com \nYour Score: $score"

        dbHelper = QuizDatabaseHelper(this)

        btnFinish.setOnClickListener {
            if (dbHelper.insertScore(email, quizId, score)) {
                Toast.makeText(this, "Score saved successfully", Toast.LENGTH_SHORT).show()
                val newIntent = Intent(this, StudentHomeActivity::class.java).apply {
                    putExtra("EMAIL", email)
                    putExtra("QUIZ_ID", quizId)
                    putExtra("SCORE", score)
                }
                startActivity(newIntent)

                // Start PDFGenerationService
                val pdfServiceIntent = Intent(this, QuizService::class.java).apply {
                    putExtra("EMAIL", email)
                    putExtra("SCORE", score)
                    putExtra("QUIZ_ID", quizId)
                }
                startService(pdfServiceIntent)

                finish()
            } else {
                Toast.makeText(this, "Failed to save score", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

