package com.example.clothingsuggester.data.source

import android.util.Log
import com.example.clothingsuggester.BuildConfig
import com.example.clothingsuggester.data.ClothesImages
import com.example.clothingsuggester.data.models.WeatherResponse
import com.example.clothingsuggester.utils.Constant
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RemoteDataSource {

    fun geWeatherFromNetworkUsingOkhtto(lat: Double, long: Double, onSuccessCallback: (response: WeatherResponse) -> Unit,
                                                onFailureCallback: (error: Throwable) -> Unit)  {

/*
           val url = HttpUrl.Builder()
               .scheme("https")
               .host("api.openweathermap.org/data/2.5/onecall")
               .addQueryParameter("lat" , lat.toString())
               .addQueryParameter("lon", long.toString())
               .addQueryParameter("appid", BuildConfig.API_KEY)
               .build()
*/
        val url  = "${Constant.BASE_URL}?lat=$lat&lon=$long&appid=${BuildConfig.API_KEY}"
        val client = OkHttpClient()
        val requset = Request.Builder().url(url).build()

        client.newCall(requset).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailureCallback(e)

            }

            override fun onResponse(call: Call, response: Response) {

                response.body?.string().toString().let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)
                    Log.i("TAG", "onResponse: ${result.current.temp}")

                    ////
                    onSuccessCallback(result)

                    val timezone = result.timezone
                    val weather = result.current.temp.toInt() //.toString() +"°c"
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
            }

        })


    }

}