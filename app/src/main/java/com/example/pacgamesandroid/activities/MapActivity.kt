package com.example.pacgamesandroid.activities
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pacgamesandroid.R
import com.example.pacgamesandroid.databinding.ActivityMapBinding
import com.example.pacgamesandroid.models.Location

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var location = arrayOf(Location(53.286029, -6.24168, 25f), Location( 53.22027,-6.6596, 25f), Location(53.45375, -6.21923, 25f), Location(52.35314, -7.70071, 25f), Location(52.26016, -7.10993, 15f))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        for (i in location) {
            intent.extras?.getParcelable<Location>("location")!!
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        for (i in location) {
            val loc = LatLng(i.lat, i.lng)
            val options = MarkerOptions()
                .title("Gamestop")
                .snippet("GPS : $loc\nGames : ")
                .draggable(false)
                .position(loc)
            map.addMarker(options)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, i.zoom))
        }
    }


    override fun onBackPressed() {

        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}