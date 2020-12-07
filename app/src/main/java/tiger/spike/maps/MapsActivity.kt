package tiger.spike.maps

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tiger.spike.R
import tiger.spike.databinding.MessageItemBinding
import tiger.spike.util.LocationUtil
import tiger.spike.view.CustomInfoWindow
import tiger.spike.view.OptionsBottomSheetFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    OptionsBottomSheetFragment.ItemClickListener {

    private lateinit var map: GoogleMap
    private var locationPermissionGranted = false
    private lateinit var locationViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        locationViewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        getLocationPermission()
        updateLocationUI()

        locationViewModel.markerDetailsList.observe(this, {
            drawMap()
        })

        locationViewModel.userMarkerOption.observe(this, {
            run {
                drawMap()
                if (it != null) {
                    map.addMarker(it)
                    map.setOnMarkerClickListener { marker ->
                        if (marker.isInfoWindowShown) {
                            marker.hideInfoWindow()
                        } else {
                            marker.showInfoWindow()
                        }
                        true
                    }
                }
            }
        })

        map.setInfoWindowAdapter(CustomInfoWindow(this))

        map.setOnMapClickListener { position ->
            locationViewModel.updateUserMarkerOption(
                MarkerOptions().position(position)
                    .title("Your marker")
                    .zIndex(1.0f)
                    .visible(true)
                    .alpha(0.7f)
                    .snippet(LocationUtil.getAddress(position, this))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
            )
        }

        map.setOnInfoWindowClickListener { marker ->
            if (marker.title == "Your marker") {
                showAlertDialogForPoint(
                    LatLng(marker.position.latitude, marker.position.longitude),
                    marker.snippet
                )

            } else {
                startActivity(Intent(this, MarkerDetailActivity::class.java).apply {
                    putExtra(
                        RESP_INDEX,
                        locationViewModel.markerDetailsList.value?.firstOrNull {
                            it.note == marker.title && LatLng(
                                it.latitude,
                                it.longitude
                            ) == marker.position
                        })
                })
            }
        }

        map.setOnMapLongClickListener {
            supportFragmentManager.let {
                OptionsBottomSheetFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
    }

    private fun drawMap() {
        map.clear()
        LocationUtil.createMarkers(locationViewModel.getCustomizedMarkerList(), map)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    updateLocationUI()
                }
            }
        }
    }

    //Using a library to handle permission (such as permissionsdispatcher) can reduce the code
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                map.apply {
                    isMyLocationEnabled = true
                    uiSettings?.isMyLocationButtonEnabled = true
                }
                LocationUtil.getDeviceLocation(this, map)
            } else {
                map.apply {
                    isMyLocationEnabled = false
                    uiSettings?.isMyLocationButtonEnabled = false
                }
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun showAlertDialogForPoint(point: LatLng?, address: String) {
        val view = DataBindingUtil.inflate<MessageItemBinding>(
            LayoutInflater.from(this@MapsActivity),
            R.layout.message_item,
            null,
            false
        )
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(view.root)
        view.title.text = address
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE, "Save Marker"
        ) { _, _ -> // Define color of marker icon
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            val note = view.noteEdit.text.toString()
            locationViewModel.storeMarkerInCloud(point, note, address)
        }
        alertDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE, "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onItemClick(item: String, filter: String) {
        locationViewModel.filterMarkers(item, filter)
    }
}