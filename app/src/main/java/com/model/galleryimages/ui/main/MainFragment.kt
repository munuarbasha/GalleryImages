package com.model.galleryimages.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.model.galleryimages.R
import com.model.galleryimages.ui.PhotosActivity
import com.model.galleryimages.ui.adapter.GalleryFolderAdapter
import com.model.galleryimages.ui.model.ImageModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment(), GalleryFolderAdapter.ItemClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val REQUEST_PERMISSIONS = 1001
    private lateinit var viewModel: MainViewModel
    var al_images: ArrayList<ImageModel> = ArrayList()
    var boolean_folder = false
    var obj_adapter: GalleryFolderAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)
        galleryImagesList.layoutManager = layoutManager
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            when {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                }
                else -> {
                    requestPermissions(
                        activity!!,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_PERMISSIONS
                    )
                }
            }
        } else {
            Log.e("Else", "Else")
            fn_imagespath()
        }
    }

    fun fn_imagespath(): ArrayList<ImageModel> {
        al_images.clear()
        var int_position = 0
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        var absolutePathOfImage: String? = null
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = activity!!.getContentResolver().query(
            uri, projection, null, null,
            "$orderBy DESC"
        )!!
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            Log.e("Column", absolutePathOfImage)
            Log.e("Folder", cursor.getString(column_index_folder_name))
            for (i in 0 until al_images.size) {
                if (al_images.get(i).str_folder
                        .equals(cursor.getString(column_index_folder_name))
                ) {
                    boolean_folder = true
                    int_position = i
                    break
                } else {
                    boolean_folder = false
                }
            }
            if (boolean_folder) {
                val al_path: ArrayList<String> = ArrayList()
                al_images.get(int_position).al_imagepath?.let { al_path.addAll(it) }
                al_path.add(absolutePathOfImage)
                al_images.get(int_position).al_imagepath = al_path
            } else {
                val al_path: ArrayList<String> = ArrayList()
                al_path.add(absolutePathOfImage)
                val obj_model = ImageModel()
                obj_model.str_folder = cursor.getString(column_index_folder_name)
                obj_model.al_imagepath = al_path
                al_images.add(obj_model)
            }
        }
        for (i in 0 until al_images.size) {
            Log.e("FOLDER", al_images.get(i).str_folder)
            for (j in 0 until al_images.get(i).al_imagepath!!.size) {
                Log.e("FILE", al_images.get(i).al_imagepath!!.get(j))
            }
        }
        obj_adapter = GalleryFolderAdapter(context!!, al_images)
        obj_adapter?.setListener(this)
        galleryImagesList.adapter = obj_adapter
        return al_images
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_imagespath()
                    } else {
                        Toast.makeText(
                            context,
                            "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    i++
                }
            }
        }
    }

    override fun onItemClicked(imageModel: ImageModel, position: Int) {
        val intent = Intent(
            context,
            PhotosActivity::class.java
        )
        val bundle = Bundle()
        bundle.putInt("value", position)
        bundle.putSerializable("model", imageModel)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
