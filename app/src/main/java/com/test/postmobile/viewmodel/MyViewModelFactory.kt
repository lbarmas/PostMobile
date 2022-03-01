package com.test.postmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.postmobile.repository.MainRepository

class MyViewModelFactory constructor(private val repository: MainRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            PostViewModel(this.repository) as T
        } else
            return if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
                CommentViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
    }
}