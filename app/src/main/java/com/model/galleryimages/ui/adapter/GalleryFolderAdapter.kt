package com.model.galleryimages.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.model.galleryimages.R
import com.model.galleryimages.ui.model.ImageModel
import kotlinx.android.synthetic.main.gallery_image_list_items.view.*
import java.io.File


class GalleryFolderAdapter(
    private var context: Context,
    private var al_menu: ArrayList<ImageModel>
) : RecyclerView.Adapter<GalleryFolderAdapter.ViewHolder>() {

    private var itemClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_image_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = al_menu[position]

        holder.tvGalleyFolderTitle.text = item.str_folder
        var path = al_menu[position].al_imagepath?.get(0)
        val photoUri = Uri.fromFile(File(path))

        Glide.with(context).load(File(photoUri.path))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.ivGalleryFolderImage)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClicked(al_menu[position], position)
        }
    }

    fun  setListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int = al_menu.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivGalleryFolderImage: AppCompatImageView = view.ivGalleryFolderImage
        var tvGalleyFolderTitle: TextView = view.tvGalleyFolderTitle
    }

    interface ItemClickListener{
        fun onItemClicked(imageModel: ImageModel, position: Int)
    }
}