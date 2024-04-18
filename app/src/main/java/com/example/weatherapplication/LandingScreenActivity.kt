package com.example.weatherapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapplication.utilities.LogD
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LandingScreenActivity : AppCompatActivity() {

    private lateinit var city: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_screen)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val btnSub = findViewById<Button>(R.id.btnSearch)
        btnSub.setOnClickListener{
            city = findViewById<EditText>(R.id.search).text.toString()
            val weatherFragment = WeatherFragment()
            val forecastFragment = ForecastFragment()
            val bundle = Bundle()
            bundle.putString("city", city)
            weatherFragment.apply{
                arguments = bundle
            }
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.mainScreen, weatherFragment)
                commit()
            }
        }
    }
}