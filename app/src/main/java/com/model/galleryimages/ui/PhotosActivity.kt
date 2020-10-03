package com.model.galleryimages.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.model.galleryimages.R
import com.model.galleryimages.ui.ui.photos.PhotosFragment

class PhotosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_activity)
        if (savedInstanceState == null) {
            var fragment = PhotosFragment.newInstance()
            fragment.arguments = intent?.extras
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }
}
