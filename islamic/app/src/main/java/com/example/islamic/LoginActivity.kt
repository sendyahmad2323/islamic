package com.example.islamic

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Cek apakah user sudah login (FirebaseAuth)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Jika sudah login, langsung ke MainActivity (tuan rumah semua fragment)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // 2. Jika belum login, tampilkan layout login
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        // Inisialisasi komponen UI
        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val signupLink = findViewById<TextView>(R.id.tvSignup)
        val forgotLink = findViewById<TextView>(R.id.tvForgotPassword)

        // 3. Klik tombol Login
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            // Pindah ke MainActivity dan clear backstack
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Login Gagal: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Klik link Sign Up (jika ada)
        signupLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 5. Klik link Forgot Password (jika ada)
        forgotLink.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
