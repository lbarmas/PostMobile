package com.test.postmobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.postmobile.R
import com.test.postmobile.model.Post

class PostAdapter(val context: Context) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts = mutableListOf<Post>()
    var onItemClick: ((Post)->Unit) ?= null

    fun setPostsList(posts: List<Post>) {
        this.posts = posts.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts = posts[position]
        holder.title.text = posts.title
        holder.body.text = posts.body
    }

    override fun getItemCount(): Int {
        return posts.size
    }


    inner class PostViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val title: TextView = itemView!!.findViewById(R.id.title)
        val body: TextView = itemView!!.findViewById(R.id.body)

        init {
            itemView?.setOnClickListener {
                onItemClick?.invoke(posts[adapterPosition])
            }
        }
    }
}