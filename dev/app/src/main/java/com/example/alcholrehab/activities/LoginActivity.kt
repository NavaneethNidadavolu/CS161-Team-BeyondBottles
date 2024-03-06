package com.example.alcholrehab.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alcholrehab.R
import com.example.alcholrehab.models.UserModel
import com.example.alcholrehab.network.BaseProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var btnSignup: Button
    lateinit var etName: TextInputEditText
    lateinit var etPassword : TextInputEditText
    lateinit var etEmail : TextInputEditText
    lateinit var layoutUserName: TextInputLayout
    private lateinit var userName : String
    lateinit var email : String
    lateinit var password : String
    private val sharedPrefFile = "signInPrefs"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignup = findViewById(R.id.btnGetOTP)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        layoutUserName = findViewById(R.id.layoutUserName)

        btnSignup.setOnClickListener {

            if (etName.text.toString().length < 3) {
                layoutUserName.error = "Name must have atleast 3 letters"
                layoutUserName.isErrorEnabled = true
                layoutUserName.isFocusable = true
            } else {
                layoutUserName.isErrorEnabled = false
                layoutUserName.isFocusable = false

                userName = etName.text.toString()
                email = etEmail.text.toString()
                password = etPassword.text.toString()


                var response : Response<List<UserModel>>
                val sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)

                lifecycleScope.launch {
                    response = registerUser(userName, email, password)
                    if (response.isSuccessful){
                        with(sharedPref.edit()) {
                            Log.e("Login worked", "success")

                            putString("user_name", response.body()!![0].username)
                            putString("email", response.body()!![0].email)
                            putInt("user_id",response.body()!![0].userId)
                            apply()

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }


    }
}

private suspend fun registerUser(username : String, email : String, password : String)
        = BaseProvider.api.registerNewUser(username, email, password)