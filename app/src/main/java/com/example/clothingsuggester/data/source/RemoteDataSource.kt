package com.example.clothingsuggester.data.source

import com.example.clothingsuggester.BuildConfig
import com.example.clothingsuggester.data.models.WeatherResponse
import com.example.clothingsuggester.utils.Constant
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RemoteDataSource {
    val client = OkHttpClient()

    fun getWeatherFromNetworkUsingOkhtto(
        lat: Double, long: Double, onSuccessCallback: (response: WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    ) {

        val url = "${Constant.BASE_URL}?lat=$lat&lon=$long&appid=${BuildConfig.API_KEY}"
        val requset = Request.Builder().url(url).build()

        client.newCall(requset).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailureCallback(e)

            }

            override fun onResponse(call: Call, response: Response) {

                response.body?.string().toString().let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)
                    onSuccessCallback(result)
                }
            }

        })


    }

}