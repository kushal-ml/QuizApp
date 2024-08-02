package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var db: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        db = QuizDatabaseHelper(this)

        btnLogin.setOnClickListener {
            login()
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            try {
                val user = db.getUserByEmail(email)
                if (user != null) {
                    if (user.password == password) {
                        val role = user.role
                        Log.d("LoginActivity", "Passing Email: $email")
                        val intent = if (role.equals("teacher", ignoreCase = true)) {
                            Intent(this, TeacherHomeActivity::class.java).apply { putExtra("EMAIL", user.email) }
                        } else {
                            Intent(this, StudentHomeActivity::class.java).apply {
                                putExtra("EMAIL", user.email)
                            }
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }
}

