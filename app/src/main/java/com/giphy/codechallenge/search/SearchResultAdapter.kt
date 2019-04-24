package com.giphy.codechallenge.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.giphy.codechallenge.R
import com.giphy.sdk.core.models.Media

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ItemViewHolder>() {

    private lateinit var items: List<Media>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val title: String? = items[position].title
        val url: String? = items[position].images.fixedHeightDownsampled?.gifUrl

        holder.let{
            it.gifTitle.text = title
            Glide
                .with(holder.itemView.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(it.gifImageView)
        }
    }

    fun replaceItems(_items: List<Media>) {
        items = _items
        notifyDataSetChanged()
    }

    inner class ItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the ImageView that will add each gif to
        val gifTitle: TextView = view.findViewById(R.id.title)
        val gifImageView: ImageView = view.findViewById(R.id.gifPreview)
    }
}