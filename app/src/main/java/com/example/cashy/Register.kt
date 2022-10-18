package com.example.cashy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        auth = Firebase.auth

        val loginText : TextView = findViewById(R.id.textView_login)
        loginText.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        val registerButton : Button = findViewById(R.id.button_register)
        registerButton.setOnClickListener{
            performSignUp()
        }
        if (auth.currentUser != null) {
            Toast.makeText(baseContext, "Välkommen ${auth.currentUser?.email}.",
                Toast.LENGTH_SHORT).show()
            //man kan göra en intent här för att komma vidare till appen om man är inloggad
        }
    }
    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.editText_email_register)
        val password = findViewById<EditText>(R.id.editText_password_register)

        if (email.text.isEmpty() && password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(baseContext, "Success.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}