package com.example.weatherapplication.view

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.utilities.LogE
import com.example.weatherapplication.utilities.closeKeyboard
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
    private val NotFoundFragment = NotFoundFragment()
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
        var mainScreen = findViewById<FrameLayout>(R.id.mainScreen)
        val btnSub = findViewById<Button>(R.id.btnSearch)
        btnSub.setOnClickListener{
            btnSub.setEnabled(false)
            supportFragmentManager.beginTransaction()
                .remove(weatherFragment)
                .commit()
            closeKeyboard(this)
            city = findViewById<EditText>(R.id.search).text.toString()
            if(city.isEmpty()){
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainScreen, NotFoundFragment)
                    commit()
                }
                Toast.makeText(this, "Please enter the City Name", Toast.LENGTH_LONG).show()
            }
            else {
                bundle.putString("city", city)
                bundle.putString("lat", "")
                bundle.putString("lon", "")
                weatherFragment.apply {
                    arguments = bundle
                }
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainScreen, weatherFragment)
                    addToBackStack(null)
                    commit()
                }
            }
            Handler().postDelayed(Runnable {
                btnSub.setEnabled(true)
            },500)
        }

        val currentBtn = findViewById<Button>(R.id.currentWeather)
        currentBtn.setOnClickListener {
            mainScreen.removeAllViews()
            currentBtn.setEnabled(false)
            closeKeyboard(this)
            if (!permission.isLocationPermissionGranted()) {
                permission.requestPermission()
            } else {
                getLastLocation()
            }
            Handler().postDelayed(Runnable {
                currentBtn.setEnabled(true)
            }, 500)
        }
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        val currentFragment = CurrentWeatherFragment()
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                lat = lastLocation.latitude.toString()
                lon = lastLocation.longitude.toString()
                bundle.putString("lat", lat)
                bundle.putString("lon", lon)
                currentFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainScreen, currentFragment)
                    .commit()
            } else {
                LogE("Location", "getLastLocation:exception")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}