package com.panda.syncweatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.panda.syncweatherapp.data.api.WeatherApi
import com.panda.syncweatherapp.data.model.WeatherData
import com.panda.syncweatherapp.data.repository.dataSourceImpl.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class WeatherActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }
    var lat:Double? = null
    var long:Double? = null
    lateinit var progressBar: ProgressBar

    lateinit var cityName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        progressBar = ProgressBar(this)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            getWeatherInfo()
        }
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpUI(data:WeatherData){
        Toast.makeText(this,"weather updated",Toast.LENGTH_SHORT).show()
        val textView : TextView = findViewById(R.id.textView)
        val result = StringBuilder()
        result.let {
            it.append("City : ${data.name}\n")
            it.append("temp:${data.main.temp} \n")
            it.append("min temp :${data.main.temp_min}\n")
            it.append("max temp :${data.main.temp_max}\n")
            it.append("pressure :${data.main.pressure}\n")
            it.append("humidity :${data.main.humidity}\n")
        }
        textView.text = result.toString()

    }
    private fun getWeatherInfo(){
        if (isNetworkAvailable(this)){
            if (this::cityName.isInitialized){
                CoroutineScope(Dispatchers.IO).launch {
                    val response = WeatherRemoteDataSourceImpl().getWeatherByCity(cityName)
                    val data = response.body() as WeatherData
                    withContext(Dispatchers.Main){
                        setUpUI(data)
                    }

                    Log.i("MYDATA","Temp ${data.main.temp}")
                    Log.i("MYDATA",response.body().toString())
                }
            }
        }else{
            Toast.makeText(this,"Network not available",Toast.LENGTH_SHORT).show()
        }

    }
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        lat = location.latitude
                        long = location.longitude
                        cityName = getAddress(location.latitude,location.longitude)
                    }
                }
            },
            Looper.myLooper()
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }



    private fun getAddress(latitude: Double, longitude: Double): String {
        val result = StringBuilder()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                result.append(address.locality).append(",")
                result.append(address.countryCode)
            }
        } catch (e: IOException) {
            //Log.e("tag", e.getMessage())
        }
        return result.toString()
    }



    private fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }


}