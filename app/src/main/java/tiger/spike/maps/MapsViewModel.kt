package tiger.spike.maps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import tiger.spike.model.MarkerResponse
import tiger.spike.model.Place

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    private var userName: String? = ""
    private var markersDatabaseReference: DatabaseReference? = null

    private var _userMarkerOptuion = MutableLiveData<MarkerOptions>()
    val markerList: MutableList<Place> = mutableListOf()
    private val filteredMarkerList: MutableList<Place> = mutableListOf()

    val userMarkerOption: LiveData<MarkerOptions>
        get() = _userMarkerOptuion

    var markerDetailsList = MutableLiveData<ArrayList<MarkerResponse>>()
    var newPlace = arrayListOf<MarkerResponse>()

    init {
        //fetch userName and userId
        FirebaseAuth.getInstance().let { firebaseAuth ->
            userName = firebaseAuth.currentUser?.displayName
        }
        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        markersDatabaseReference = mFirebaseDatabase.reference.child("markers")

        fetchMarkers()
    }

    //Coroutine is used to fetch data from Firebase DataBase
    private fun fetchMarkers() {
        viewModelScope.launch {
            try {
                //fetch all previously saved markers from Firebase
                val mChildEventListener = object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        val savedMarker: MarkerResponse? = dataSnapshot.getValue(MarkerResponse::class.java)
                        if (savedMarker != null) {
                            newPlace.run {
                                add(
                                        MarkerResponse(
                                                savedMarker.note,
                                                savedMarker.userName,
                                                savedMarker.address,
                                                savedMarker.latitude,
                                                savedMarker.longitude
                                        )
                                )
                            }
                            markerDetailsList.value = newPlace

                            markerList.run {
                                add(
                                        Place(
                                                savedMarker.note,
                                                LatLng(savedMarker.latitude, savedMarker.longitude),
                                                savedMarker.userName
                                        )
                                )
                            }
                        }
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onCancelled(databaseError: DatabaseError) {}
                }
                markersDatabaseReference?.addChildEventListener(mChildEventListener as ChildEventListener)

            } catch (e: Exception) {
                Log.e("viewmodel", e.message, e)
            }
        }
    }

    fun updateUserMarkerOption(markerOption: MarkerOptions) {
        _userMarkerOptuion.value = markerOption
    }

    fun storeMarkerInCloud(point: LatLng?, note: String, address: String) {
        val newMarker = MarkerResponse(note, userName
                ?: "", address, point!!.latitude, point.longitude)
        markersDatabaseReference!!.child((System.currentTimeMillis() / 1000L).toString())
                .setValue(
                        newMarker
                ).addOnSuccessListener {
                    //success
                    _userMarkerOptuion.postValue(null)
                }.addOnFailureListener {
                    //fail error handling ?
                }

    }

    fun filterMarkers(filterArea: String, filterText: String) {
        filteredMarkerList.clear()

        markerDetailsList.value?.forEach {
            if (if (filterArea == "note") {
                        it.note.contains(filterText, true)
                    } else {
                        it.userName.contains(filterText, true)
                    }) {
                filteredMarkerList.run {
                    add(Place(it.note, LatLng(it.latitude, it.longitude), it.userName)
                    )
                }
            }
        }
        markerDetailsList.postValue(newPlace)
    }

    fun getCustomizedMarkerList(): MutableList<Place>? {
        return if (filteredMarkerList.size == 0) markerList else filteredMarkerList
    }

}