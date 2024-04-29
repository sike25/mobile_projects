package hu.ait.weatherapp.network

import hu.ait.weatherapp.data.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  HOST: https://api.openweathermap.org/
 *  PATH: data/2.5/weather
 *  QUERY params:
 *  ?
 *  q = Budapest,hu
 *  &
 *  units=imperial
 *  &
 *  appid=f3d694b
 */

interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getWeatherResults(
        @Query("q") q: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ) : WeatherResult }