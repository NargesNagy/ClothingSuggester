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
import com.example.clothingsuggester.R
import com.example.clothingsuggester.data.ClothesImages
import com.example.clothingsuggester.data.models.WeatherResponse
import com.example.clothingsuggester.data.source.RemoteDataSource
import com.example.clothingsuggester.databinding.ActivityMainBinding
import com.example.clothingsuggester.utils.Constant
import com.google.android.gms.location.*
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lattitude: Double = 33.44
    private var longtude: Double = -94.04
    var clothesWeather = 8
    private val client = OkHttpClient()
    private val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        getRandomImage()
        saveImageofTheDay(getRandomImage())
        getImageofTheDay()

        binding.imageClothes.setImageResource(getRandomImage())

    }

    private fun onSuccessResponse(response: WeatherResponse) {

        runOnUiThread {
            val timezone = response.timezone
            val weather = response.current.temp.toInt() //.toString() +"°c"
            val weatherInCilisuis = weather - 273.15
            val im = ClothesImages(weatherInCilisuis.toInt())
            val list = im.ClothesList()
            Log.i("TAG", "onResponseeeeeeeeeeeeeee: $weather")
            Log.i("TAG", "onResponseeeeeeeeeeeeeee: $list")

            binding.textCountryName.text = timezone


            Log.i("TAG", "onCreateView: ${response.current.weather?.get(0)?.icon}")

            val formatedDate: String =
                SimpleDateFormat("EEE, d MMM yyyy ", Locale.ENGLISH).format(Date())
            binding.textDate.text = formatedDate
            binding.textTemperature.text = (response.current.temp.toInt()-273.5).toString() + "°C"


            var icon = response.current.weather?.get(0)?.icon
            when (icon) {
                "01d" -> binding.imageWeather.setImageResource(R.drawable.cloud_sun2)
                "02d" -> binding.imageWeather.setImageResource(R.drawable.cloud2)
                "03d" -> binding.imageWeather.setImageResource(R.drawable.blackcloud_lighting)
                "04d" -> binding.imageWeather.setImageResource(R.drawable.cloud2)
                "09d" -> binding.imageWeather.setImageResource(R.drawable.cloud_rain)
                "10d" -> binding.imageWeather.setImageResource(R.drawable.cloud_sun2)
                "11d" -> binding.imageWeather.setImageResource(R.drawable.clouds__rain_sun)
                "13d" -> binding.imageWeather.setImageResource(R.drawable.clouds_sun)
                "50d" -> binding.imageWeather.setImageResource(R.drawable.darkcloud_rain)
                "01n" -> binding.imageWeather.setImageResource(R.drawable.stormy)
                "02n" -> binding.imageWeather.setImageResource(R.drawable.cloud2)
                "03n" -> binding.imageWeather.setImageResource(R.drawable.cloud_sun2)
                "04n" -> binding.imageWeather.setImageResource(R.drawable.cloud2)
                "09n" -> binding.imageWeather.setImageResource(R.drawable.cloud_lighting)
                "10n" -> binding.imageWeather.setImageResource(R.drawable.stormy)
                "11n" -> binding.imageWeather.setImageResource(R.drawable.stormy)
                "13n" -> binding.imageWeather.setImageResource(R.drawable.rain)
                "50n" -> binding.imageWeather.setImageResource(R.drawable.rain)

            }

            binding.textCloudValue.text = response.daily?.get(0)?.pressure.toString() + " hpa"
            binding.textHumidityValue.text = response.daily?.get(0)?.humidity.toString() + " %"
            binding.textWindValue.text = response.daily?.get(0)?.wind_speed.toString() + " m/s"
        }
    }

    private fun onFailerResponse(error: Throwable) {
        Log.i("TAG", "onFailure: ${error.message}")

    }

    private fun getRandomImage(): Int {
        val images = ClothesImages(clothesWeather)
        val list = images.ClothesList()
        val randomIndex = Random.nextInt(list.size - 1)
        val randomImage = list[randomIndex]
        Log.i("TAG", "randomImageeeeeeeeeeee: $randomImage")
        saveImageofTheDay(randomImage)
        return randomImage
    }

    private fun saveImageofTheDay(i: Int) {
        val sharedPreference =
            this.getSharedPreferences(Constant.SHARED_PREFERINCES_NAME, MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt(Constant.SHARED_CLOTHES_KEY, i)
        Log.i("TAG", "imimimmm: $i")

    }

    private fun getImageofTheDay(): Int {
        val sharedPreference =
            this.getSharedPreferences(Constant.SHARED_PREFERINCES_NAME, MODE_PRIVATE)
        val im = sharedPreference.getInt(Constant.SHARED_CLOTHES_KEY, 1)
        Log.i("TAG", "immmmmm: $im")
        return im
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
                remoteDataSource.getWeatherFromNetworkUsingOkhtto(
                    lattitude,
                    longtude,
                    ::onSuccessResponse,
                    ::onFailerResponse
                )

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
            remoteDataSource.getWeatherFromNetworkUsingOkhtto(
                lattitude,
                longtude,
                ::onSuccessResponse,
                ::onFailerResponse
            )
        }
    }


}