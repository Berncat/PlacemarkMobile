

package com.example.placemark.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.placemark.R
import com.example.placemark.databinding.ActivityPlacemarkBinding
import com.example.placemark.helpers.showImagePicker
import com.example.placemark.main.MainApp
import com.example.placemark.models.PlacemarkModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var placemark = PlacemarkModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp


        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            binding.btnAdd.setText(R.string.save_placemark)
            Picasso.get()
                .load(placemark.image)
                .into(binding.placemarkImage)
            if (placemark.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_placemark_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            if (placemark.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_placemark_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            placemark.image = result.data!!.data!!
                            Picasso.get()
                                .load(placemark.image)
                                .into(binding.placemarkImage)
                            binding.chooseImage.setText(R.string.change_placemark_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}