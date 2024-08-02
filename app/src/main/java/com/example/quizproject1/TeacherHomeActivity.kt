package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class TeacherHomeActivity : AppCompatActivity() {

    private lateinit var btnCreateQuiz: Button
    private lateinit var btnUpdateQuiz: Button
    private lateinit var btnDeleteQuiz: Button
    private lateinit var btnShowResults: Button

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
        setContentView(R.layout.activity_teacher_home)

        btnCreateQuiz = findViewById(R.id.btnCreateQuiz)
        btnUpdateQuiz = findViewById(R.id.btnUpdateQuiz)
        btnDeleteQuiz = findViewById(R.id.btnDeleteQuiz)
        btnShowResults = findViewById(R.id.btnShowResults)

        btnCreateQuiz.setOnClickListener {
            val intent = Intent(this, CreateQuizActivity::class.java)
            startActivity(intent)
        }

        btnUpdateQuiz.setOnClickListener {
            val intent = Intent(this, UpdateQuizActivity::class.java)
            startActivity(intent)
        }

        btnDeleteQuiz.setOnClickListener {
            val intent = Intent(this, DeleteQuizActivity::class.java)
            startActivity(intent)
        }

        btnShowResults.setOnClickListener {
            val intent = Intent(this, ShowResultsActivity::class.java)
            startActivity(intent)
        }
    }
}
