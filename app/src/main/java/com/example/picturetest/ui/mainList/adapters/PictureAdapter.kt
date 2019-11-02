package com.example.picturetest.ui.mainList.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.Picture
import com.example.picturetest.R
import kotlinx.android.synthetic.main.item_picture.view.*
import com.bumptech.glide.load.engine.DiskCacheStrategy


private const val VIEW_TYPE_LOADING = 0
private const val VIEW_TYPE_NORMAL = 1

class PictureAdapter(private var onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pictures: ArrayList<Picture> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            VIEW_TYPE_NORMAL -> PicturesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false))
            else -> ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false))

        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PicturesViewHolder){
            holder.tvAuthor.text = pictures[position].author
            Glide
                .with(holder.itemView)
                .load(pictures[position].downloadUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon)
            holder.itemView.setOnClickListener(onClickListener)
        }

    }

    override fun getItemCount() =  pictures.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position == pictures.size) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL





    fun updateData(basicModels: List<Picture>){
        this.pictures = ArrayList(basicModels)
        notifyDataSetChanged()
    }

    fun getList() = pictures

    inner class PicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor = itemView.tvAuthor
        val ivIcon = itemView.ivIcon
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}