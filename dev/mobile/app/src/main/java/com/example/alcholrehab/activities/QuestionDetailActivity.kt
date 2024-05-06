package com.example.alcholrehab.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.QuestionDetailAdapter
import com.example.alcholrehab.models.CommentsData
import com.example.alcholrehab.models.SuccessModel
import com.example.alcholrehab.network.AddCommentRequest
import com.example.alcholrehab.network.BaseProvider
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlinx.coroutines.*
import android.content.SharedPreferences;



class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var textUserName: TextView
    private lateinit var textQuestionBody: TextView
    private lateinit var likesCount: TextView
    private lateinit var repliesCount: TextView
    private lateinit var textCreatedAt: TextView
    private lateinit var profileImage : ShapeableImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnSend : ImageView
    private lateinit var editTextResponse : TextInputEditText

    lateinit var images : IntArray
    private var questionid : Int = 0
    var userId : Int = 0

    private var mQuestionDetailAdapter : QuestionDetailAdapter? = null
    private lateinit var rvComments : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questiondetails)


        textUserName = findViewById(R.id.textUserName)
        textQuestionBody = findViewById(R.id.textQuestionBody)
        likesCount = findViewById(R.id.likesCount)
        repliesCount = findViewById(R.id.repliesCount)
        profileImage = findViewById(R.id.ivUserAvatar)
        textCreatedAt = findViewById(R.id.textCreatedAt)
        btnBack = findViewById(R.id.btnBack)
        rvComments = findViewById(R.id.rvComments)
        btnSend = findViewById(R.id.buttonSendResponse)
        editTextResponse = findViewById(R.id.editTextResponse)

        var isLiked = intent.extras!!.getBoolean("isLiked", false)

        textCreatedAt.text =  intent.extras!!.getString("time", "5 hours ago")
        textUserName.text = intent.extras!!.getString("username", "Likhith Nemani") + ","
        textQuestionBody.text = intent.extras!!.getString("question", "How to procees with alcohol rehab?")
        likesCount.text = intent.extras!!.getInt("upvotes").toString()
        repliesCount.text = intent.extras!!.getInt("comments").toString()
        questionid = intent.extras!!.getInt("questionid")

        val upvoteButton: ImageView = findViewById(R.id.imageLike)


        if (isLiked) {
            upvoteButton.setImageResource(R.drawable.like)
        } else {
            upvoteButton.setImageResource(R.drawable.unlike)
        }


        upvoteButton.setOnClickListener {
            // Call the API here
            CoroutineScope(Dispatchers.Main).launch {
                // Call the API here
                val sharedPref = getSharedPreferences("signInPrefs", MODE_PRIVATE)
                userId = sharedPref.getInt("user_id", 0)

                if (isLiked) {
                    val response = downvote(questionid, userId)
                    upvoteButton.setImageResource(R.drawable.unlike)
                    likesCount = findViewById(R.id.likesCount)
                    val currentLikeCountStr = likesCount.text.toString()
                    // Convert the String to an Int
                    val currentLikeCount = currentLikeCountStr.toInt()
                    // Increment the Int value by one
                    val newLikeCount = currentLikeCount - 1
                    // Convert the incremented Int back to a String
                    val newLikeCountStr = newLikeCount.toString()
                    // Set the updated like count String to the likesCount TextView
                    likesCount.text = newLikeCountStr
                    isLiked = false
                } else {
                    val response = upvote(questionid, userId)
                    upvoteButton.setImageResource(R.drawable.like)
                    likesCount = findViewById(R.id.likesCount)
                    val currentLikeCountStr = likesCount.text.toString()
                    // Convert the String to an Int
                    val currentLikeCount = currentLikeCountStr.toInt()
                    // Increment the Int value by one
                    val newLikeCount = currentLikeCount + 1
                    // Convert the incremented Int back to a String
                    val newLikeCountStr = newLikeCount.toString()
                    // Set the updated like count String to the likesCount TextView
                    likesCount.text = newLikeCountStr
                    isLiked = true
                }

                // Handle the API response here
            }
        }

        val sharedPref = this.getSharedPreferences("signInPrefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0)

        loadImages(profileImage)

        rvComments.layoutManager = LinearLayoutManager(this)
        mQuestionDetailAdapter = QuestionDetailAdapter()
        rvComments.adapter = mQuestionDetailAdapter

        var response : Response<List<CommentsData>>

        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        if (mWifi!!.isConnected) {

            lifecycleScope.launch {
                response = getCommentsData(questionid)
                if (response.isSuccessful) {
                    Log.v("got comments data", response.body().toString())
                    response.body()?.let { mQuestionDetailAdapter!!.setData(it) }
                }
            }
        }else {
//            progressbarAnim.isVisible = false
//            networkErrorAnim.isVisible = true
        }


        btnBack.setOnClickListener {
            onBackPressed() // This will mimic the back button behavior, i.e., finish the current activity and navigate to the previous one
        }
//        setSupportActionBar(toolbarQuestionDetail)
////        setDisplayShowHomeEnabled(toolbarQuestionDetail)
//        toolbarQuestionDetail.setTitle(R.string.empty)
//        toolbarQuestionDetail.setTitleTextColor(resources.getColor(R.color.colorPrimary))
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        var commentResponse : Response<List<SuccessModel>>

        btnSend.setOnClickListener {
            if (editTextResponse.text!!.length > 5){
                if (mWifi!!.isConnected) {
                    lifecycleScope.launch {
                        var request = AddCommentRequest(userId, questionid ,editTextResponse.text.toString())
                        commentResponse =
                            sendCommentsData(request)
                            editTextResponse.text!!.clear()
                            if (commentResponse.isSuccessful) {
                                Log.v("send commented data", commentResponse.body().toString())
                                response = getCommentsData(questionid)
                                if (response.isSuccessful) {
                                    Log.v("got comments data", response.body().toString())
                                    response.body()?.let { mQuestionDetailAdapter!!.setData(it) }
                                }
                            }
                    }
                }else{
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadImages(imageView: ShapeableImageView){
        images = intArrayOf(
            R.drawable.artboard_1, R.drawable.artboard_10, R.drawable.artboard_11,
            R.drawable.artboard_12, R.drawable.artboard_13, R.drawable.artboard_14,
            R.drawable.artboard_15, R.drawable.artboard_16, R.drawable.artboard_17,
            R.drawable.artboard_18, R.drawable.artboard_19, R.drawable.artboard_20,
            R.drawable.artboard_21, R.drawable.artboard_22, R.drawable.artboard_23,
            R.drawable.artboard_24, R.drawable.artboard_25, R.drawable.artboard_26,
            R.drawable.artboard_27, R.drawable.artboard_28, R.drawable.artboard_29,
            R.drawable.artboard_30, R.drawable.artboard_31, R.drawable.artboard_32,
            R.drawable.artboard_33, R.drawable.artboard_34, R.drawable.artboard_35,
            R.drawable.artboard_36, R.drawable.artboard_37, R.drawable.artboard_38,
            R.drawable.artboard_39, R.drawable.artboard_40, R.drawable.artboard_41,
            R.drawable.artboard_42, R.drawable.artboard_43, R.drawable.artboard_44,
        )

        val imageId = (Math.random() * images.size).toInt()
        imageView.setImageResource(images[imageId])
    }

    private suspend fun getCommentsData(questionid : Int)
            = BaseProvider.api.getCommentsData(questionid)

    private suspend fun upvote(questionid : Int, userid: Int)
            = BaseProvider.api.upvote(questionid, userid)

    private suspend fun downvote(questionid : Int, userid: Int)
            = BaseProvider.api.downvote(questionid, userid)


    private suspend fun sendCommentsData(request: AddCommentRequest)
        = BaseProvider.api.sendCommentData(request)
}