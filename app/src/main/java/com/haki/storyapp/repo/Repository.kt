package com.haki.storyapp.repo

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.haki.storyapp.apiService.ApiService
import com.haki.storyapp.pref.UserModel
import com.haki.storyapp.pref.UserPreference
import com.haki.storyapp.response.DetailResponse
import com.haki.storyapp.response.LoginResponse
import com.haki.storyapp.response.SignUpResponse
import com.haki.storyapp.response.StoriesResponse
import com.haki.storyapp.response.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userModel =
                UserModel(successResponse.loginResult.name, successResponse.loginResult.token, true)
            saveSession(userModel)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun signUp(nama: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.signUp(nama, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, SignUpResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun stories() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStories()
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoriesResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun detail(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetail(id)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun upload(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStr(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            isNeeded: Boolean
        ): Repository? {
            if (userPreference == null || isNeeded) {
                synchronized(this) {
                    instance = Repository(apiService, userPreference)
                }
            }
            return instance
        }

    }

}