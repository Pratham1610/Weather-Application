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

class WeatherViewModel : ViewModel() {

    private val key = "c79b6cb39826aca9755ade5999cd13bd"

    private val apiService = RetrofitClient.apiService

    private val _cityLocationItem = MutableLiveData<List<CityLocationItem>>()
    val cityLocationItem: LiveData<List<CityLocationItem>> = _cityLocationItem

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessfully = MutableLiveData<Boolean>(true)
    val isSuccessful: LiveData<Boolean> = _isSuccessfully

    fun getLatLon(city: String, limit: Int) {
        _isLoading.value = true
        _isSuccessfully.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getLatLon(city, limit, key)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (!responseBody.isNullOrEmpty()) {
                        LogD("API OUTPUT latlog: ", responseBody.toString())
                        _isSuccessfully.value = true
                        _cityLocationItem.value = responseBody?:null
                    } else {
                        _isSuccessfully.value = false
                        LogD("error", "Empty response body")
                    }
                } else {
                    _isSuccessfully.value = false
                    LogD("error", "Unsuccessful response")
                }
            } catch (e: Exception) {
                _isSuccessfully.value = false
                LogD("error", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrentWeather(lat: String, lon: String) {
        _isLoading.value = true
        _isSuccessfully.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getWeather(lat, lon, key)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.toString().isNotEmpty()) {
                        LogD("API OUTPUT 1: ", responseBody.toString())
                        _isSuccessfully.value = true
                        _currentWeather.value = responseBody?:null
                    } else {
                        _isSuccessfully.value = false
                        LogD("error", "Empty response body")
                    }
                } else {
                    _isSuccessfully.value = false
                    LogD("error", "Unsuccessful response")
                }
            } catch (e: Exception) {
                _isSuccessfully.value = false
                LogD("error", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}