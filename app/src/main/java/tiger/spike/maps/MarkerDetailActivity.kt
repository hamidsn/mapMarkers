package tiger.spike.maps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import tiger.spike.maps.databinding.ActivityMarkerDetailBinding
import tiger.spike.model.MarkerResponse

const val RESP_INDEX = "resp_index"

class MarkerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_marker_detail)

        val extras = intent.extras
        val markerResponse: MarkerResponse? = extras?.getSerializable(RESP_INDEX) as MarkerResponse
        markerResponse?.run {
            binding.resp = this
        }

        binding.direction.setOnClickListener {
            val gmmIntentUri: Uri = Uri.parse(
                    getString(
                            R.string.direction_uri,
                            markerResponse?.latitude.toString(),
                            markerResponse?.longitude.toString()
                    )
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}
