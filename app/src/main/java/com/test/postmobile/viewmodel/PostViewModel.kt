package com.test.postmobile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.postmobile.model.Post
import com.test.postmobile.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel constructor(private val repository: MainRepository): ViewModel() {

    val postsList = MutableLiveData<List<Post>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllPosts() {
        val response = repository.getAllPosts()
        response.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                postsList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}