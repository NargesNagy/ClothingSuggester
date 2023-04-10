package com.example.clothingsuggester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.clothingsuggester.data.models.WeatherResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var lattitude: Double = 33.44
    var longtude: Double = -94.04
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geWeatherFromNetworkUsingOkhtto(lattitude, longtude)

    }

    private fun geWeatherFromNetworkUsingOkhtto(lat: Double, long: Double) {
// https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&appid=fccb113f3db977a207025c87caa649c0

/*
           val url = HttpUrl.Builder()
               .scheme("https")
               .host("api.openweathermap.org/data/2.5/onecall")
               .addQueryParameter("lat" , lat.toString())
               .addQueryParameter("lon", long.toString())
               .addQueryParameter("appid", "fccb113f3db977a207025c87caa649c0")
               .build()

*/

        val requset = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&appid=fccb113f3db977a207025c87caa649c0")
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
}