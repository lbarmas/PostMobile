package com.test.postmobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.postmobile.R
import com.test.postmobile.adapter.PostAdapter
import com.test.postmobile.network.RetrofitService
import com.test.postmobile.repository.MainRepository
import com.test.postmobile.utils.Util
import com.test.postmobile.viewmodel.MyViewModelFactory
import com.test.postmobile.viewmodel.PostViewModel


class PostActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val TAG = "PostActivity"

    private lateinit var postViewModel: PostViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        swipeRefreshLayout = findViewById(R.id.refreshLayout)

        postViewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService, null))).get(
                PostViewModel::class.java
            )
        recyclerView = findViewById(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        postAdapter = PostAdapter(this)
        recyclerView.adapter = postAdapter

        viewModelObserve()
        itemClickAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            val connection: Boolean = Util.isNetworkConnected(this)
            if (connection) {
                viewModelObserve()
                swipeRefreshLayout.isRefreshing = false
            } else {
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
            }

        }

    }

   private fun viewModelObserve() {
        postViewModel.postsList.observe(this, Observer {
            Log.e(TAG, "onCreate: $it")
            postAdapter.setPostsList(it)

        })
        postViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
        })
        postViewModel.getAllPosts()
    }

  private  fun itemClickAdapter() {
        postAdapter.onItemClick = {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("PostId", it.userId)
            startActivity(intent)
        }
    }
}

