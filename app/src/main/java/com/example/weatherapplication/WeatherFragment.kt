package com.example.weatherapplication

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weatherapplication.data.api.RetrofitClient
import com.example.weatherapplication.utilities.LogD
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {

    private val key = "c79b6cb39826aca9755ade5999cd13bd"
    private lateinit var lat: String
    private lateinit var lon: String
    private lateinit var city: String

    private lateinit var temperature: String
    private lateinit var wind: String
    private lateinit var humidity: String
    private lateinit var pressure: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)
        val temperatureView = view.findViewById<TextView>(R.id.temperature)
        val humidityView = view.findViewById<TextView>(R.id.humidity)
        val pressureView = view.findViewById<TextView>(R.id.pressure)
        val windView = view.findViewById<TextView>(R.id.wind)
        val textWeather = view.findViewById<TextView>(R.id.loadingText)
        val mainWeather = view.findViewById<LinearLayout>(R.id.weather)
        textWeather.visibility = View.VISIBLE
        mainWeather.visibility = View.VISIBLE
        val cityApi = RetrofitClient.getInstance()
        city = arguments?.getString("city").toString()
        GlobalScope.launch {
            val result = cityApi.getLatLon(city,1, key)
            if(result.body()==null || result.body()!!.isEmpty()){
                textWeather.setText("Please Enter Correct City Name")
            }
            else if (result.isSuccessful) {
                textWeather.setText("Weather Update")
                textWeather.setTypeface(null, Typeface.BOLD)
                textWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                lon = result.body()?.get(0)?.lon.toString()
                lat = result.body()?.get(0)?.lat.toString()
                LogD("Response 1: ", result.body().toString())
                val result2 = cityApi.getWeather(lat, lon, key)
                if (result2.isSuccessful) {
                    temperature = result2.body()?.main?.temp.toString()
                    wind = result2.body()?.wind?.speed.toString()
                    humidity = result2.body()?.main?.humidity.toString()
                    pressure = result2.body()?.main?.pressure.toString()

                    temperatureView.post{
                        temperatureView.text = temperature
                    }
                    pressureView.post{
                        pressureView.text = pressure
                    }
                    humidityView.post{
                        humidityView.text = humidity
                    }
                    windView.post{
                        windView.text = wind
                    }
                    LogD("response: ", result2.body().toString())
                }
            }
        }
        return view
    }

}