package com.michaelfotiadis.flourpower.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.michaelfotiadis.flourpower.R
import com.michaelfotiadis.flourpower.di.GlideApp
import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem

class CakeRecyclerAdapter(private val callback: Callback) :
    ListAdapter<UiCakeItem, CakeRecyclerAdapter.ViewHolder>(CakeDiffCallback()) {

    interface Callback {

        fun onCakeClicked(uiCakeItem: UiCakeItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_cake, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CakeRecyclerAdapter.ViewHolder, position: Int) {

        val item = getItem(position)

        with(holder.titleText) {
            this.text = item.title
            this.setOnClickListener { callback.onCakeClicked(item) }
        }

        with(holder.imageView) {

            contentDescription = item.title

            val circularProgressDrawable = CircularProgressDrawable(context)
                .apply {
                    strokeWidth = 5f
                    centerRadius = 10f
                    start()
                }

            // keep in mind: http calls will fail
            GlideApp.with(this)
                .load(item.image)
                .fitCenter()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_error_outline)
                .into(this)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val titleText = itemView.findViewById<TextView>(R.id.title_text)
        internal val imageView = itemView.findViewById<ImageView>(R.id.image_view)
    }

    class CakeDiffCallback : DiffUtil.ItemCallback<UiCakeItem>() {
        override fun areItemsTheSame(oldItem: UiCakeItem, newItem: UiCakeItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: UiCakeItem, newItem: UiCakeItem): Boolean {
            return oldItem.description == newItem.description
                && oldItem.image == newItem.image
        }
    }
}
