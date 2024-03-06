package com.example.alcholrehab.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.models.SuccessModel
import com.example.alcholrehab.network.BaseProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Response

class PostQuestionActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnPost : MaterialButton
    private lateinit var postText : TextInputEditText
    private lateinit var progressBar : ProgressBar

    var userId : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postquestion)

        btnBack = findViewById(R.id.btnBack)
        btnPost =findViewById(R.id.textPublish)
        postText = findViewById(R.id.etQuestion)
        progressBar = findViewById(R.id.progressBar)

        progressBar.isVisible = false

        val sharedPref = this.getSharedPreferences("signInPrefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0)

        var questionResponse : Response<List<SuccessModel>>

        btnPost.setOnClickListener{
            progressBar.isVisible = true
            if (postText.text!!.length > 10){
                lifecycleScope.launch {
                    questionResponse = postQuestionData(1, postText.text.toString())
                    if (questionResponse.isSuccessful) {
                        progressBar.isVisible = false
                        val showQueDetailed = Intent(applicationContext, MainActivity::class.java)
                        startActivity(showQueDetailed)
                    }
                }
            }else{
                Toast.makeText(this, "Post should be of atleast 10 characters", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun postQuestionData(userid : Int, question : String)
            = BaseProvider.api.postQuestionData(userid, question)
}