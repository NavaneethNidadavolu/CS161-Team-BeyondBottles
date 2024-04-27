package com.example.alcholrehab.network

import androidx.annotation.Keep
import com.example.alcholrehab.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

@Keep
interface ApiClient {

    @FormUrlEncoded
    @POST("/questions/{qid}")
    suspend fun getYearwiseData(@Path("exam") exam : String,
                                @Path("year") year :Int,
                                @Field("session_id") session_id : String): Response<QuestionModel>

    @GET("/questions")
    suspend fun getFeedData(@Query("userid") userid : Int): Response<List<QuestionsData>>

    @GET("/getcomments")
    suspend fun getCommentsData(@Query("questionid") questionid : Int): Response<List<CommentsData>>

    @GET("/leaderboard")
    suspend fun leaderboard(): Response<List<LeadershipModel>>

    @GET("/getblogs")
    suspend fun getBlogs(@Query("type") type : String): Response<List<BlogItem>>

    @POST("/addcomment")
    suspend fun sendCommentData(@Body request: AddCommentRequest): Response<List<SuccessModel>>

    @POST("/upvote")
    suspend fun upvote(@Query("questionid") questionid : Int,
                       @Query("userid") userid : Int): Response<List<SuccessModel>>

    @POST("/downvote")
    suspend fun downvote(@Query("questionid") questionid : Int,
                       @Query("userid") userid : Int): Response<List<SuccessModel>>

    @POST("/register")
    suspend fun registerNewUser(@Query("username") username : String,
                                @Query("email") email : String,
                                @Query("password") password : String) : Response<List<UserModel>>

    @POST("/login")
    suspend fun login(@Query("username") username : String,
                      @Query("password") password : String) : Response<List<UserModel>>

    @POST("/addquestion")
    suspend fun postQuestionData(@Body request: PostQuestionRequest): Response<List<SuccessModel>>
}

data class PostQuestionRequest(
    val userid: Int,
    val question: String
)

data class AddCommentRequest(
    val userid: Int,
    val questionid: Int,
    var comment: String
)

