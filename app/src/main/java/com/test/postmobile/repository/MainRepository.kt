package com.test.postmobile.repository


import com.test.postmobile.network.RetrofitService

class MainRepository(private val retrofitService: RetrofitService, private val postId: Int?) {
    fun getAllPosts() = retrofitService.getAllPost()
    fun getAllComments() = retrofitService.getAllComments(postId!!)
}