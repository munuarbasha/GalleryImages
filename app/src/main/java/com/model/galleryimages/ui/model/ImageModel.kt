package com.model.galleryimages.ui.model

import java.io.Serializable

data class ImageModel (
    var str_folder: String? = null,
    var al_imagepath: ArrayList<String>? = null
    ): Serializable