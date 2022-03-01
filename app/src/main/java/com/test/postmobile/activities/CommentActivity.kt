package com.test.postmobile.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.postmobile.R
import com.test.postmobile.adapter.CommentAdapter
import com.test.postmobile.network.RetrofitService
import com.test.postmobile.repository.MainRepository
import com.test.postmobile.utils.Util
import com.test.postmobile.viewmodel.CommentViewModel
import com.test.postmobile.viewmodel.MyViewModelFactory

class CommentActivity:AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private  var postId: Int? = null
    private val TAG = "PostActivity"

    private lateinit var commentViewModel: CommentViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        swipeRefreshLayout = findViewById(R.id.refreshLayout)

        postId= intent.getIntExtra("PostId",1)

        commentViewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService, postId!!))).get(
                CommentViewModel::class.java
            )
        recyclerView = findViewById(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        commentAdapter = CommentAdapter(this)
        recyclerView.adapter = commentAdapter

        viewModelObserve()

        swipeRefreshLayout.setOnRefreshListener {
            val connection: Boolean = Util.isNetworkConnected(this)
            if (connection) {
                viewModelObserve()
                swipeRefreshLayout.isRefreshing = false
            }else{
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }

        }
    }

    private fun viewModelObserve(){
        commentViewModel.commentsList.observe(this, Observer {
            Log.e(TAG, "onCreate: $it")
            commentAdapter.setCommentList(it)
        })
        commentViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
        })
        postId?.let { commentViewModel.getAllComment(it) }
    }
}
