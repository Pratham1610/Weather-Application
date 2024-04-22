package com.example.weatherapplication.view

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.R
import com.example.weatherapplication.viewModel.WeatherViewModel

class CurrentWeatherFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var temperatureView: TextView
    private lateinit var humidityView: TextView
    private lateinit var pressureView: TextView
    private lateinit var windView: TextView
    private lateinit var detailWeather: TextView
    private lateinit var minTemp: TextView
    private lateinit var curCity: TextView
    private lateinit var maxTemp: TextView
    private lateinit var mainWeather: LinearLayout
    private var lat: String = ""
    private var lon: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)
        temperatureView = view.findViewById(R.id.temperature)
        humidityView = view.findViewById(R.id.humidity)
        pressureView = view.findViewById(R.id.pressure)
        windView = view.findViewById(R.id.wind)
        mainWeather = view.findViewById(R.id.weather)
        curCity = view.findViewById(R.id.curCity)
        detailWeather = view.findViewById(R.id.detailweather)
        minTemp = view.findViewById(R.id.minTemp)
        maxTemp = view.findViewById(R.id.maxTemp)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            temperatureView.text = buildString {
                val temperatureCelsius = currentWeather.main.temp - 273.15
                append(String.format("%.2f", temperatureCelsius))
                append("°C")
            }
            humidityView.text = buildString {
                append(currentWeather.main.humidity.toString())
                append("%")
            }
            minTemp.text = buildString {
                val temperatureCelsius = currentWeather.main.temp_min - 273.15
                append(String.format("%.2f", temperatureCelsius))
                append("°C")
            }
            maxTemp.text = buildString {
                val temperatureCelsius = currentWeather.main.temp_max - 273.15
                append(String.format("%.2f", temperatureCelsius))
                append("°C")
            }
            pressureView.text = buildString {
                append(currentWeather.main.pressure.toString())
                append(" hPa")
            }
            windView.text = buildString {
                append(currentWeather.wind.speed.toString())
                append(" m/s")
            }
            curCity.text = currentWeather.name
            detailWeather.text = currentWeather.weather[0].main
        })
        lat = arguments?.getString("lat") ?: ""
        lon = arguments?.getString("lon") ?: ""
        viewModel.getCurrentWeather(lat, lon)
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.currentWeather.removeObservers(viewLifecycleOwner)
        viewModel.cityLocationItem.removeObservers(viewLifecycleOwner)
    }
}