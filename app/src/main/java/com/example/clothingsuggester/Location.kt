package com.example.clothingsuggester

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient

class Location(val context: Context) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lattitude : Double = 31.1926745
    var longtude : Double = 29.9245787


}