package com.example.clothingsuggester

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.clothingsuggester.data.models.WeatherResponse
import com.google.android.gms.location.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lattitude: Double = 33.44
    var longtude: Double = -94.04
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        Log.i("TAG", "getLocations: ${lattitude} ggggggggggg ${longtude}")
        //geWeatherFromNetworkUsingOkhtto(lattitude, longtude)

    }


    private fun geWeatherFromNetworkUsingOkhtto(lat: Double, long: Double) {
// https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&appid=fccb113f3db977a207025c87caa649c0

/*
           val url = HttpUrl.Builder()
               .scheme("https")
               .host("api.openweathermap.org/data/2.5/onecall")
               .addQueryParameter("lat" , lat.toString())
               .addQueryParameter("lon", long.toString())
               .addQueryParameter("appid", BuildConfig.API_KEY)
               .build()
*/

        val url  = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&appid=${BuildConfig.API_KEY}"
        val requset = Request.Builder()
            .url(url)
            .build()
        client.newCall(requset).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Log.i("TAG", "onFailure: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {

                response.body?.string().toString().let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)
                    Log.i("TAG", "onResponse: ${result.current.temp}")

                }
            }

        })


    }

    // location

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationIsEnabled()) {
                getLocations()
            } else {
                //Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                enableLocationSettings()
            }
        } else {
            requestPermission()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACESS_LOCATION = 100
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACESS_LOCATION) {
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()

            } else {
                //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isLocationIsEnabled(): Boolean {
        val locationManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun enableLocationSettings() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingIntent)
    }


    @SuppressLint("MissingPermission")
    private fun getLocations() {

        fusedLocationProviderClient.lastLocation?.addOnCompleteListener {
            //@NonNull
            val location: Location? = it.getResult()
            if (location == null) {
                requestNewLocationData()
            } else it.apply {
                lattitude = location.latitude
                longtude = location.longitude
                Log.i("TAG", "getLocations: ${lattitude} ggggggggggg ${longtude}")
                geWeatherFromNetworkUsingOkhtto(lattitude, longtude)

            }
        }
    }

    /////////////////////////////
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // initialize locationrequest
        // object with aproparate methods
        val mlocationRequest = LocationRequest()
        mlocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mlocationRequest.interval = 5
        mlocationRequest.fastestInterval = 0
        mlocationRequest.numUpdates = 1

        // setting locationrequest
        // on fusedlocationclient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            mlocationRequest, mLocationCallBack,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation = locationResult.lastLocation
            lattitude = mLastLocation.latitude
            longtude = mLastLocation.longitude

            Log.i("TAG", "onLocationResult: ${lattitude} hhh ${longtude}")
            geWeatherFromNetworkUsingOkhtto(lattitude, longtude)

        }
    }


}