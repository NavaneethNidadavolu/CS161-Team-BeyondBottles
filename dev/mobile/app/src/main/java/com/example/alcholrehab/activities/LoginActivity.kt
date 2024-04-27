package com.example.alcholrehab.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alcholrehab.R
import com.example.alcholrehab.models.UserModel
import com.example.alcholrehab.network.BaseProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    lateinit var btnSignup: Button
    lateinit var etName: TextInputEditText
    lateinit var etPassword : TextInputEditText
    lateinit var etEmail : TextInputEditText
    lateinit var layoutUserName: TextInputLayout
    lateinit var txtAlreadyUser: TextView
    lateinit var emailLayout: TextInputLayout
    lateinit var txtWelcome: TextView
    private lateinit var userName : String
    lateinit var email : String
    lateinit var password : String
    private val sharedPrefFile = "signInPrefs"
    var mode = "signup"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignup = findViewById(R.id.btnGetOTP)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        layoutUserName = findViewById(R.id.layoutUserName)
        txtAlreadyUser = findViewById(R.id.txtAlreadyUser)
        emailLayout = findViewById(R.id.layout_email)
        txtWelcome = findViewById(R.id.txtWelcome)

        btnSignup.setOnClickListener {

            if (etName.text.toString().length < 3) {
                layoutUserName.error = "Name must have atleast 3 letters"
                layoutUserName.isErrorEnabled = true
                layoutUserName.isFocusable = true
            } else {
                if (mode == "signup") {
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

                                showToast("Signup Success")

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
                        } else {
                            showToast("Signup Failed")
                        }
                    }
                } else {
                    layoutUserName.isErrorEnabled = false
                    layoutUserName.isFocusable = false

                    userName = etName.text.toString()
                    password = etPassword.text.toString()


                    var response : Response<List<UserModel>>
                    val sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)

                    lifecycleScope.launch {
                        response = login(userName, password)
                        if (response.isSuccessful){

                            showToast("Login Success")

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
                        } else {
                            showToast("Login Failed")
                        }
                    }
                }
            }
        }


        txtAlreadyUser.setOnClickListener {
            if (mode == "signup") {
                emailLayout.visibility = View.GONE
                txtWelcome.text = "Please login to enter the app"
                btnSignup.text = "LOGIN"
                txtAlreadyUser.text = "WANT TO BECOME A MEMBER ?"
                mode = "login"
            } else {
                emailLayout.visibility = View.VISIBLE
                txtWelcome.text = "Please register to enter the app"
                btnSignup.text = "SIGN UP"
                txtAlreadyUser.text = "I AM ALREADY A MEMBER"
                mode = "register"
            }
        }


    }

    private fun showToast(message: String) {
        // Create a Toast with the provided message and duration
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)

        // Optionally, you can set other properties such as gravity, offset, etc.
        // Example: toast.setGravity(Gravity.CENTER, 0, 0)

        // Show the Toast
        toast.show()
    }
}

private suspend fun registerUser(username : String, email : String, password : String)
        = BaseProvider.api.registerNewUser(username, email, password)

private suspend fun login(username : String, password : String)
        = BaseProvider.api.login(username, password)