package com.example.clothingsuggester.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import com.example.clothingsuggester.utils.SharedPreferencesUtil
import com.google.android.gms.location.*
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val remoteDataSource = RemoteDataSource()
    private var lattitude: Double = 33.44
    private var longtude: Double = -94.04
    var clothesWeather = 8
    private lateinit var sharedPreference: SharedPreferencesUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreference = SharedPreferencesUtil(this)
        getCurrentLocation()
        sharedPreference.getImageofTheDay()
    }

    private fun onSuccessResponse(response: WeatherResponse) {

        runOnUiThread {

            val timezone = response.timezone
            val formatedDate: String =
                SimpleDateFormat("EEE, d MMM yyyy ", Locale.ENGLISH).format(Date())

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
            binding.apply {
                //textCountryName.text = timezone
                textDate.text = formatedDate
                textTemperature.text = (response.current.temp.toInt() - 273.5).toString() + "°C"
                textCloudValue.text = response.daily?.get(0)?.pressure.toString() + " hpa"
                textHumidityValue.text = response.daily?.get(0)?.humidity.toString() + " %"
                textWindValue.text = response.daily?.get(0)?.wind_speed.toString() + " m/s"

            }

            clothesWeather = (response.current.temp.toInt() - 273.5).toInt()
            if (sharedPreference.getImageofTheDay().second != formatedDate) {
                sharedPreference.saveImageofTheDay(getRandomImage(clothesWeather), formatedDate)
                /*
                var randomImage = getRandomImage()
                if( randomImage == getImageofTheDay().first){
                    randomImage = getRandomImage()
                    saveImageofTheDay(randomImage, formatedDate)
                }else{
                    saveImageofTheDay(randomImage, formatedDate)
                }
                */
            }
            if (sharedPreference.getImageofTheDay().first != null) {
                binding.imageClothes.setImageResource(sharedPreference.getImageofTheDay().first)
            } else {
                binding.imageClothes.setImageResource(getRandomImage(clothesWeather))
            }

        }
    }

    private fun onFailerResponse(error: Throwable) {
        Log.i("TAG", "onFailure: ${error.message}")
    }

    private fun getRandomImage(tempreture: Int): Int {
        val images = ClothesImages(tempreture)
        val list = images.getListOfClothesAccordingToTempreture()
        val randomIndex = Random.nextInt(list.size - 1)
        val randomImage = list[randomIndex]
        return randomImage
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
                remoteDataSource.getWeatherFromNetworkUsingOkhtto(
                    lattitude,
                    longtude,
                    ::onSuccessResponse,
                    ::onFailerResponse
                )
              //  getAddrss(lattitude, longtude)
                getAddrss(30.855963, 31.1221095)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mlocationRequest = LocationRequest()
        mlocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mlocationRequest.interval = 5
        mlocationRequest.fastestInterval = 0
        mlocationRequest.numUpdates = 1
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
            remoteDataSource.getWeatherFromNetworkUsingOkhtto(
                lattitude,
                longtude,
                ::onSuccessResponse,
                ::onFailerResponse
            )
            getAddrss(30.855963, 31.1221095)
        }
    }

    private fun getAddrss(lat: Double, long: Double) {
        val geocoder: Geocoder
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(
                lat, long, 1
            )

            val address: String =
                addresses!![0].getAddressLine(0)
            val city: String = addresses!![0].locality
            val state: String = addresses!![0].adminArea
            val country: String = addresses!![0].countryName
            val postalCode: String = addresses!![0].postalCode
            val knownName: String = addresses!![0].featureName

            binding.textCountryName.text = "$city _ $state"
        } catch (e: Exception) {

        }

    }

    companion object {
        private const val PERMISSION_REQUEST_ACESS_LOCATION = 100
    }
}