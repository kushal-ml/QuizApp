package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizListActivity : AppCompatActivity() {

    private lateinit var databaseHelper: QuizDatabaseHelper
    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var adapter: QuizListAdapter
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
        setContentView(R.layout.activity_quiz_list)

        databaseHelper = QuizDatabaseHelper(this)
        quizRecyclerView = findViewById(R.id.quizRecyclerView)
        quizRecyclerView.layoutManager = LinearLayoutManager(this)

        email = intent.getStringExtra("EMAIL") ?: ""
        Log.d("QuizListActivity", "Received Email: $email")

        loadQuizzes()
    }

    private fun loadQuizzes() {
        val quizList = databaseHelper.getAllQuizzes()
        adapter = QuizListAdapter(quizList) { quiz ->
            // Handle quiz item click
            val intent = Intent(this, TakeQuizActivity::class.java).apply {
                putExtra("QUIZ_ID", quiz.id)
                putExtra("EMAIL", email)
            }


            Log.d("QuizListActivity", "Passing Email: $email to TakeQuizActivity")
            startActivity(intent)
        }
        quizRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()  // Notify the adapter that the data set has changed
    }
}


