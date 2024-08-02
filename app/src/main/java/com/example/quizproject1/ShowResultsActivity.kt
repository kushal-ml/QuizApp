package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import android.widget.SimpleAdapter

class ShowResultsActivity : AppCompatActivity() {
    private lateinit var dbHelper: QuizDatabaseHelper
    private lateinit var listView: ListView
    private lateinit var email: String

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
        setContentView(R.layout.activity_show_results)

        listView = findViewById(R.id.listViewResults)
        dbHelper = QuizDatabaseHelper(this)

        displayResults()
    }

    private fun displayResults() {
        val results: List<Result> = dbHelper.getAllResults()

        // Transform the list of Result objects to a list of maps
        val resultsList: List<Map<String, Any>> = results.map { result ->
            mapOf(
                "email" to "student1@gmail.com",
                "quizId" to result.quizId,
                "score" to result.score
            )
        }

        val from = arrayOf("email", "quizId", "score")
        val to = intArrayOf(R.id.textViewEmail, R.id.textViewQuizId, R.id.textViewScore)

        val adapter = SimpleAdapter(this, resultsList, R.layout.result_item, from, to)
        listView.adapter = adapter
    }
}
