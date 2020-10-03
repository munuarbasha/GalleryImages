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
import com.bumptech.glide.request.RequestOptions
import com.model.galleryimages.R
import com.model.galleryimages.ui.model.ImageModel
import kotlinx.android.synthetic.main.gallery_image_list_items.view.*
import java.io.File


class GalleryPicturesAdapter(
    private var context: Context,
    private var imageModel: ImageModel,
    private var imagePosition: Int
) : RecyclerView.Adapter<GalleryPicturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_image_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = imageModel.al_imagepath?.get(position)!!

        holder.tvGalleyFolderTitle.visibility = View.GONE
        val photoUri = Uri.fromFile(File(path))

        Glide.with(context).load(File(photoUri.path))
            .placeholder(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .apply(RequestOptions().override(250, 250))
            .into(holder.ivGalleryFolderImage)

    }

    override fun getItemCount(): Int = imageModel.al_imagepath?.size!!

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivGalleryFolderImage: AppCompatImageView = view.ivGalleryFolderImage
        var tvGalleyFolderTitle: TextView = view.tvGalleyFolderTitle
    }
}