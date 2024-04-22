package com.example.weatherapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.data.api.RetrofitClient
import com.example.weatherapplication.model.CityLocationItem
import com.example.weatherapplication.model.CurrentWeather
import com.example.weatherapplication.utilities.LogD
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val key = "c79b6cb39826aca9755ade5999cd13bd"

    private val apiService = RetrofitClient.apiService

    private val _cityLocationItem = MutableLiveData<List<CityLocationItem>>()
    val cityLocationItem: LiveData<List<CityLocationItem>> = _cityLocationItem

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    fun getLatLon(city: String, limit: Int) {
        viewModelScope.launch {
            val response = apiService.getLatLon(city, limit, key)
            if (response.isSuccessful) {
                _cityLocationItem.value = response.body()
            } else {
                LogD("error", "latlon Api")
            }
        }
    }

    fun getCurrentWeather(lat: String, lon: String) {
        viewModelScope.launch {
            val response = apiService.getWeather(lat, lon, key)
            if (response.isSuccessful) {
                _currentWeather.value = response.body()
            } else {
                LogD("error", "weather Api")
            }
        }
    }
}