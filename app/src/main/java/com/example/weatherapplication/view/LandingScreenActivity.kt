package com.example.weatherapplication.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.utilities.LogD
import com.example.weatherapplication.utilities.LogE
import com.example.weatherapplication.view.WeatherFragment
import com.example.weatherapplication.viewModel.PermissionMain
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LandingScreenActivity : AppCompatActivity() {

    private lateinit var city: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lat: String = ""
    private var lon: String = ""
    private val weatherFragment = WeatherFragment()
    private val bundle = Bundle()
    private val permission = PermissionMain(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_screen)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if(permission.isLocationPermissionGranted()){
            getLastLocation()
        }
        val btnSub = findViewById<Button>(R.id.btnSearch)
        btnSub.setOnClickListener{
            city = findViewById<EditText>(R.id.search).text.toString()
            if(city.isEmpty()){
                Toast.makeText(this, "Please enter the City Name", Toast.LENGTH_LONG).show()
            }
            else {
                bundle.putString("city", city)
                bundle.putString("lat", "")
                bundle.putString("lon", "")
                weatherFragment.apply {
                    arguments = bundle
                }
                supportFragmentManager.beginTransaction().remove(weatherFragment).commit()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainScreen, weatherFragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }

        val currentbtn = findViewById<Button>(R.id.currentWeather)
        currentbtn.setOnClickListener{
            currentbtn.setOnClickListener {
                if (!permission.isLocationPermissionGranted()) {
                    permission.requestPermission()
                } else {
                    getLastLocation()
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        val currentfragment = CurrentWeatherFragment()
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                lat = lastLocation.latitude.toString()
                lon = lastLocation.longitude.toString()
                bundle.putString("lat", lat)
                bundle.putString("lon", lon)
                currentfragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreen, currentfragment)
                    .commit()
            } else {
                LogE("Location", "getLastLocation:exception")
            }
        }
    }
}