package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity7 : AppCompatActivity() {


    private val PREFS_NAME = "auth_prefs"
    private val KEY_USERS_SET = "users_set"
    private val USER_PREFIX = "user_"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_LOGGED_EMAIL = "logged_email"

    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main7)

        val signInButton = findViewById<Button>(R.id.button2)
        val registerBtn  = findViewById<TextView>(R.id.textView4)

        val emailEt    = findViewById<EditText>(R.id.editTextText)
        val passwordEt = findViewById<EditText>(R.id.editTextText2)
        val toggleTv   = findViewById<TextView>(R.id.textView7)

        var pwVisible = false
        fun applyPwVisibility(visible: Boolean) {
            passwordEt.transformationMethod = if (visible)
                HideReturnsTransformationMethod.getInstance()
            else
                PasswordTransformationMethod.getInstance()
            passwordEt.setSelection(passwordEt.text?.length ?: 0)
            toggleTv.text = if (visible) "Hide Password" else "Show Password"
        }
        applyPwVisibility(false)

        toggleTv.setOnClickListener {
            pwVisible = !pwVisible
            applyPwVisibility(pwVisible)
        }

        signInButton.setOnClickListener {
            val email = emailEt.text?.toString()?.trim().orEmpty()
            val password = passwordEt.text?.toString()?.trim().orEmpty()

            fun toast(msg: String) =
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

            // Basic validation
            if (email.isEmpty()) {
                toast("Please enter your email")
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                toast("Enter a valid email address")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                toast("Please enter your password")
                return@setOnClickListener
            }

            when (val result = verifyLogin(email, password)) {
                is LoginResult.Success -> {

                    prefs.edit()
                        .putBoolean(KEY_IS_LOGGED_IN, true)
                        .putString(KEY_LOGGED_EMAIL, email)
                        .apply()

                    toast("Login successful")
                    startActivity(Intent(this, MainActivity2::class.java))

                }
                is LoginResult.NoAccount -> {
                    toast("No account found for this email")
                }
                is LoginResult.WrongPassword -> {
                    toast("Incorrect password")
                }
            }
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity8::class.java))
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun verifyLogin(email: String, password: String): LoginResult {
        val users = prefs.getStringSet(KEY_USERS_SET, emptySet()) ?: emptySet()
        if (email !in users) return LoginResult.NoAccount

        val savedPw = prefs.getString("${USER_PREFIX}${email}_password", null)
        if (savedPw == null) return LoginResult.NoAccount
        if (savedPw != password) return LoginResult.WrongPassword

        return LoginResult.Success
    }

    private sealed class LoginResult {
        data object Success : LoginResult()
        data object NoAccount : LoginResult()
        data object WrongPassword : LoginResult()
    }
}
