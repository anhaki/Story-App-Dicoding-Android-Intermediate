package com.haki.storyapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.haki.storyapp.apiService.ApiService
import com.haki.storyapp.data.StoryRemoteMediator
import com.haki.storyapp.database.StoryDatabase
import com.haki.storyapp.di.wrapEspressoIdlingResource
import com.haki.storyapp.pref.LocModel
import com.haki.storyapp.pref.UserModel
import com.haki.storyapp.pref.UserPreference
import com.haki.storyapp.response.DetailResponse
import com.haki.storyapp.response.ListStoryItem
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
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {
    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        wrapEspressoIdlingResource {
            try {
                val successResponse = apiService.login(email, password)
                val userModel =
                    UserModel(
                        successResponse.loginResult.name,
                        successResponse.loginResult.token,
                        true
                    )
                saveUserSession(userModel)
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            } catch (e: Exception) {
                emit(ResultState.Error("Unexpected Error"))
            }
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
        } catch (e: Exception) {
            emit(ResultState.Error("Unexpected Error"))
        }
    }

    fun stories() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStories(1, 50, 1)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoriesResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error("Unexpected Error"))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
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
        } catch (e: Exception) {
            emit(ResultState.Error("Unexpected Error"))
        }
    }

    fun upload(imageFile: File, description: String, latitude: Double?, longitude: Double?) =
        liveData {
            emit(ResultState.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = if (latitude == null && longitude == null) {
                    apiService.uploadStr(multipartBody, requestBody)
                } else {
                    apiService.uploadStr(multipartBody, requestBody, latitude, longitude)
                }
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            } catch (e: Exception) {
                emit(ResultState.Error("Unexpected Error"))
            }
        }

    suspend fun saveLocSession(loc: LocModel) {
        userPreference.saveLocSession(loc)
    }

    fun getLocSession(): Flow<LocModel> {
        return userPreference.getLocSession()
    }


    suspend fun saveUserSession(user: UserModel) {
        userPreference.saveUserSession(user)
    }

    fun getUserSession(): Flow<UserModel> {
        return userPreference.getUserSession()
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
            storyDatabase: StoryDatabase,
            isNeeded: Boolean
        ): Repository? {
            if (isNeeded) {
                synchronized(this) {
                    instance = Repository(apiService, userPreference, storyDatabase)
                }
            }
            return instance
        }

    }

}