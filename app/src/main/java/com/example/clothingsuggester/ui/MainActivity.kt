package com.example.clothingsuggester.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.clothingsuggester.BuildConfig
import com.example.clothingsuggester.R
import com.example.clothingsuggester.data.ClothesImages
import com.example.clothingsuggester.data.models.WeatherResponse
import com.example.clothingsuggester.data.source.RemoteDataSource
import com.google.android.gms.location.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lattitude: Double = 33.44
    private var longtude: Double = -94.04
    private val client = OkHttpClient()
    private val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
    }

    private fun onSuccessResponse(response: WeatherResponse) {
        val timezone = response.timezone
        val weather = response.current.temp.toInt() //.toString() +"°c"
        val weatherInCilisuis = weather - 273.15
        val im = ClothesImages(weatherInCilisuis.toInt())
        val list = im.ClothesList()
        Log.i("TAG", "onResponseeeeeeeeeeeeeee: $weather")
        Log.i("TAG", "onResponseeeeeeeeeeeeeee: $list")
/*
                    var icon = weather.current.weather?.get(0)?.icon
                    when (icon){
                        "01d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "02d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "03d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.blackcloud_lighting)
                        "04d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "09d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_rain)
                        "10d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "11d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.clouds__rain_sun)
                        "13d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.clouds_sun)
                        "50d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.darkcloud_rain)
                        "01n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "02n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "03n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "04n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "09n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_lighting)
                        "10n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "11n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "13n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.rain)
                        "50n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.rain)

                    }
*/

    }

    private fun onFailerResponse(error: Throwable) {
        Log.i("TAG", "onFailure: ${error.message}")

    }


    private fun getWeatherFromNetworkUsingOkhtto(lat: Double, long: Double) {

        val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&appid=${BuildConfig.API_KEY}"
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

                    val timezone = result.timezone
                    val weather = result.current.temp.toInt() //.toString() +"°c"
                    val weatherInCilisuis = weather - 273.15
                    val im = ClothesImages(weatherInCilisuis.toInt())
                    val list = im.ClothesList()
                    Log.i("TAG", "onResponseeeeeeeeeeeeeee: $timezone")
                    Log.i("TAG", "onResponseeeeeeeeeeeeeee: $list")
/*
                    var icon = weather.current.weather?.get(0)?.icon
                    when (icon){
                        "01d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "02d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "03d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.blackcloud_lighting)
                        "04d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "09d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_rain)
                        "10d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "11d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.clouds__rain_sun)
                        "13d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.clouds_sun)
                        "50d" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.darkcloud_rain)
                        "01n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "02n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "03n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_sun2)
                        "04n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud2)
                        "09n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.cloud_lighting)
                        "10n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "11n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.stormy)
                        "13n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.rain)
                        "50n" -> binding.showimageView.setImageResource(com.google.android.gms.location.R.drawable.rain)

                    }
*/

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
                Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
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
                getWeatherFromNetworkUsingOkhtto(lattitude, longtude)

            }
        }
    }

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
            getWeatherFromNetworkUsingOkhtto(lattitude, longtude)

        }
    }


}