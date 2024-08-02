package com.example.quizproject1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etName: EditText
    private lateinit var rgRole: RadioGroup
    private lateinit var btnSignup: Button
    private lateinit var db: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etName = findViewById(R.id.etName)
        rgRole = findViewById(R.id.rgRole)
        btnSignup = findViewById(R.id.btnSignup)
        db = QuizDatabaseHelper(this)

        rgRole.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val role = radioButton.text.toString()
            // You can also store the role in a variable or a data class
        }

        btnSignup.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val name = etName.text.toString().trim()
        val selectedRoleId = rgRole.checkedRadioButtonId
        val role = if (selectedRoleId != -1) {
            findViewById<RadioButton>(selectedRoleId).text.toString()
        } else {
            ""
        }

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && role.isNotEmpty()) {
            try {
                val user = User(email, password, name, role)
                val userId = db.addUser(user)
                if (userId > 0) {
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}