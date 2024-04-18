package com.example.weatherapplication.data.api

import com.example.weatherapplication.model.CityLocationItem
import com.example.weatherapplication.model.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface ApiInterface {

    @GET("geo/1.0/direct")
    suspend fun getLatLon(
        @Query("q") city: String,
        @Query("limit") limit: Int,
        @Query("appid") key: String
    ): Response<List<CityLocationItem>>

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String
    ): Response<CurrentWeather>


    // TODO: Forecast API is not working yet
    @GET("data/2.5/forecast")
    suspend fun getForecast(
    @Query("lat")  lat: String,
    @Query("lon") lon: String,
    @Query("exclude") exclude: String,
    @Query("appid") key: String
    ): Response<List<CurrentWeather>>

}