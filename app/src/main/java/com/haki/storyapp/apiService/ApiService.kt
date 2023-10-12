package com.haki.storyapp.apiService

import com.haki.storyapp.response.DetailResponse
import com.haki.storyapp.response.LoginResponse
import com.haki.storyapp.response.SignUpResponse
import com.haki.storyapp.response.StoriesResponse
import com.haki.storyapp.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): SignUpResponse

    @GET("stories")
    suspend fun getStories(): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStr(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Path("id") id: String
    ): DetailResponse
}