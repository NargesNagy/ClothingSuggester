package com.example.clothingsuggester

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.*

class Location(val context: Context) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lattitude : Double = 31.1926745
    var longtude : Double = 29.9245787


}