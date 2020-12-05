package tiger.spike.util

import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tiger.spike.model.Place
import java.io.IOException

object LocationUtil {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private const val DEFAULT_ZOOM = 15
    private const val HEX_COLOR_DIGITS = 6

    fun getAddress(latLng: LatLng, context: Context): String {

        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
        try {

            addresses = Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex + 1) {
                    addressText +=
                        if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("LocationUtil", e.message.toString())
        }
        return addressText
    }

    fun createMarkers(markers: MutableList<Place>?, map: GoogleMap) {
        markers?.forEach {
            map.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .icon(getMarkerColor(it.snippet))
                    .title(it.name)
                    .snippet(it.address)
            )
        }
    }

    @VisibleForTesting
    fun getMarkerColor(userName: String?): BitmapDescriptor {
        val color = StringBuilder()
        color.append("#")
        val length = userName?.length ?: 0
        for (i in 0 until HEX_COLOR_DIGITS) {
            color.append(if (i >= length) "0" else userName?.get(i)?.hashCode()?.rem(9))
        }

        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color.toString()), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    fun getDeviceLocation(context: Context, map: GoogleMap) {
        var lastKnownLocation: Location?
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.result
                    lastKnownLocation?.run {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}