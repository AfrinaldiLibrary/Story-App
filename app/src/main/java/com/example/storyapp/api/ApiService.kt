package com.example.storyapp.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): DicodingStoryResponse

    @Multipart
    @POST("stories")
    fun uploadStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: String
    ): Call<UploadResponse>

    @GET("stories")
    fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Call<DicodingStoryResponse>
}