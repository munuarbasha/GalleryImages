package com.model.galleryimages.ui.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.model.galleryimages.MainActivity
import com.model.galleryimages.R
import com.model.galleryimages.ui.adapter.GalleryPicturesAdapter
import com.model.galleryimages.ui.model.ImageModel
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.photos_fragment.galleryImagesList


class PhotosFragment : Fragment() {

    companion object {
        fun newInstance() = PhotosFragment()
    }

    private lateinit var viewModel: PhotosViewModel
    var adapter: GalleryPicturesAdapter? = null
    var imageModel: ImageModel? = null
    var position: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)
        galleryImagesList.layoutManager = layoutManager
        arguments?.let {
            if (it.containsKey("value") && it.containsKey("model")) {
                imageModel = it.getSerializable("model") as ImageModel
                position = it.getInt("value")
                adapter = GalleryPicturesAdapter(context!!, imageModel!!, position!!)

            }
        }
        galleryImagesList.adapter = adapter
    }

}
