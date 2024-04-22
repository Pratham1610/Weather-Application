package com.example.weatherapplication.viewModel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale

class PermissionMain(private val context: Context, private val activity: AppCompatActivity) {

    private var permissionLauncher: ActivityResultLauncher<Array<String>>
    init {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            isLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: isLocationGranted
            isInternetGranted = permissions[Manifest.permission.INTERNET] ?: isInternetGranted

            if (!isLocationGranted && !shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showPermissionDeniedDialog()
            }
        }
    }

    private var isLocationGranted = false
    private var isInternetGranted = false

    fun requestPermission() {
        val permissionRequest: MutableList<String> = ArrayList()

        isLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isInternetGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED

        if (!isLocationGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (!isInternetGranted) {
            permissionRequest.add(Manifest.permission.INTERNET)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Location Permission Denied")
            .setMessage("This app needs the Location permission to provide current weather information. Please grant the permission in Settings.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Go to Settings") { _, _ ->
                val intent = Intent().apply {
                    action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
            .create()
            .show()
    }
}
