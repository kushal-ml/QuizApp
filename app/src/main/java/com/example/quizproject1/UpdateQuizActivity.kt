package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UpdateQuizActivity : AppCompatActivity() {

    private lateinit var databaseHelper: QuizDatabaseHelper
    private lateinit var btnNext: Button
    private lateinit var quizRecyclerView: RecyclerView
    private var selectedQuiz: Quiz? = null

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
        setContentView(R.layout.activity_update_quiz)

        databaseHelper = QuizDatabaseHelper(this)
        quizRecyclerView = findViewById(R.id.quizRecyclerView)

        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            if (selectedQuiz != null) {
                val intent = Intent(this, EditQuizActivity::class.java)
                intent.putExtra("QUIZ_ID", selectedQuiz!!.id)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a quiz", Toast.LENGTH_SHORT).show()
            }
        }

        quizRecyclerView.layoutManager = LinearLayoutManager(this)
        loadQuizzes()
    }

    private fun loadQuizzes() {
        val quizList = databaseHelper.getAllQuizzes()
        val adapter = QuizListAdapter(quizList) { quiz ->
            selectedQuiz = quiz
            Toast.makeText(this, "Selected quiz: ${quiz.title}", Toast.LENGTH_SHORT).show()
        }
        quizRecyclerView.adapter = adapter
    }
}
