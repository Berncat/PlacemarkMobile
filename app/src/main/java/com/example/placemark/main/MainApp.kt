package com.example.placemark.main

import android.app.Application
import com.example.placemark.models.PlacemarkJSONStore
import com.example.placemark.models.PlacemarkMemStore
import com.example.placemark.models.PlacemarkStore
// import com.example.placemark.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    // val placemarks = ArrayList<PlacemarkModel>()
    // val placemarks = PlacemarkMemStore()
    lateinit var placemarks: PlacemarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        i("Placemark started")
    }
}