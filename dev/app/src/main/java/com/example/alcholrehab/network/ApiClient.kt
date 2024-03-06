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
    suspend fun getFeedData(): Response<List<QuestionsData>>

    @POST("/getcomments")
    suspend fun getCommentsData(@Query("questionid") questionid : Int): Response<List<CommentsData>>

    @POST("/addcomment")
    suspend fun sendCommentData(@Query("questionid") questionid : Int,
                                @Query("comment") comment : String,
                                @Query("userid") userid : Int): Response<List<SuccessModel>>

    @POST("/register")
    suspend fun registerNewUser(@Query("username") username : String,
                                @Query("email") email : String,
                                @Query("password") password : String) : Response<List<UserModel>>

    @POST("/addquestion")
    suspend fun postQuestionData(@Query("userid") userid : Int,
                                 @Query("question") question : String): Response<List<SuccessModel>>
}