package com.example.clothingsuggester.data.models

data class Current(
    val dt: Int,
    val sunrise: Int,
    val sunset : Int,
    val temp : Double,
    val feels_like : Double,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val uvi : Int,
    val clouds : Int,
    val visibility : Int,
    val wind_speed : Double,
    val wind_deg : Int,
    val weather : List<WeatherItem>,

    ) {
}