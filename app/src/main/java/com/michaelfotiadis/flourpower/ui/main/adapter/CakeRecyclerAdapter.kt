package com.michaelfotiadis.flourpower.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

        with(holder.cardRoot) {
            this.setOnClickListener { callback.onCakeClicked(item) }
        }

        with(holder.titleText) {
            this.text = item.title
        }

        with(holder.imageView) {

            contentDescription = item.title

            // keep in mind: http calls will fail
            GlideApp.with(this)
                .load(item.image)
                .fitCenter()
                .error(R.drawable.ic_error_outline)
                .into(this)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val cardRoot = itemView.findViewById<CardView>(R.id.card_layout)
        internal val titleText = itemView.findViewById<TextView>(R.id.title_text)
        internal val imageView = itemView.findViewById<ImageView>(R.id.image_view)
    }

    class CakeDiffCallback : DiffUtil.ItemCallback<UiCakeItem>() {
        override fun areItemsTheSame(oldItem: UiCakeItem, newItem: UiCakeItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: UiCakeItem, newItem: UiCakeItem): Boolean {
            return oldItem.description == newItem.description &&
                oldItem.image == newItem.image
        }
    }
}
