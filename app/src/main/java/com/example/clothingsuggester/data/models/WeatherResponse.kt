package com.example.clothingsuggester.data.models

data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val daily: List<Daily>

)

data class WeatherItem(
    val id: Int,
    val main: String,
    val description : String,
    val icon : String
)

data class Temp(
    val day: Double,
    val max: Double,
    val min: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Minutely(
    val dt: Int,
    val precipitation: Int
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherItem>,
    val pop: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Daily(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moon_phase: Double,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherItem>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)

data class Current(
    val dt: Int,
    val sunrise: Int,
    val sunset : Int,
    val temp : Double,
    val feels_like : Double,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Double,
    val uvi : Double,
    val clouds : Int,
    val visibility : Int,
    val wind_speed : Double,
    val wind_deg : Int,
    val weather : List<WeatherItem>,

    )
