package tiger.spike.model

import java.io.Serializable

data class MarkerResponse(
        val note: String = "",
        val userName: String = "",
        val address: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
) : Serializable
