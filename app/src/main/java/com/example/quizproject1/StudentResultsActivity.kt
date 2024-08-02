package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentResultsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: QuizDatabaseHelper
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter

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
        setContentView(R.layout.activity_student_results)

        initDatabaseHelper()
        setupRecyclerView()
        fetchAndDisplayResults()
    }

    private fun initDatabaseHelper() {
        databaseHelper = QuizDatabaseHelper(this)
    }

    private fun setupRecyclerView() {
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchAndDisplayResults() {
        val results = databaseHelper.getAllResults()
        val resultsMaps = results.map { result ->
            val quizTitle = databaseHelper.getQuizTitle(result.quizId) ?: "Test 1"
            mapOf(
                "quizTitle" to quizTitle,
                "score" to result.score
            ) as Map<String, Any>
        }
        resultsAdapter = ResultsAdapter(resultsMaps)
        resultsRecyclerView.adapter = resultsAdapter
    }
}