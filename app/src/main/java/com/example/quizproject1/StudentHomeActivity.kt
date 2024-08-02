package com.example.quizproject1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StudentHomeActivity : AppCompatActivity() {

    private lateinit var btnTakeQuiz: Button
    private lateinit var btnShowResults: Button
    private lateinit var email: String

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
        setContentView(R.layout.activity_student_home)

        email = intent.getStringExtra("email") ?: "No email received"
        Log.d("StudentHomeActivity", "Received Email: $email")

        btnTakeQuiz = findViewById(R.id.btnTakeQuiz)
        btnShowResults = findViewById(R.id.btnShowResults)

        btnTakeQuiz.setOnClickListener {
            val intent = Intent(this, QuizListActivity::class.java).apply {
                putExtra("EMAIL", email)
            }
            Log.d("StudentHomeActivity", "Passing Email: $email to QuizListActivity")
            startActivity(intent)
        }

        btnShowResults.setOnClickListener {
            val intent = Intent(this, StudentResultsActivity::class.java).apply {
                putExtra("EMAIL", email)
            }
            Log.d("StudentHomeActivity", "Passing Email: $email to ShowResultsActivity")
            startActivity(intent)
        }
    }

}