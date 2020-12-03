#SAMPLE APP

## Features
* Login Firebase


## Technical Feature
This repository contains a sample map app that implements Firebase Login, Firebase Database, LiveData, Google Maps, MCCM architect

## Library Reference:
* JetPack Architecture 
* Firebase
* Google Maps

## Special Feature
* Login/Signup
* Map Screen
* Marker Screen
* Add marker
* See markers of all users
* Filter markets by long click on map

## Screenshot
![alt text][login] ![alt text][currentLocation] ![alt text][addMarker] ![alt text][markerDetail] ![alt text][filter] ![alt text][detailActivity]

[login]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t1.jpeg "Login Screen"
[currentLocation]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t2.jpeg "Current location"
[addMarker]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t3.jpeg "Add marker"
[markerDetail]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t4.jpeg "Marker details"
[filter]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t5.jpeg "Filter markers"
[detailActivity]: https://github.com/hamidsn/mapMarkers/blob/main/screenshot/t6.jpeg "Filter markers"

## Steps
* Creating API key for maps
* Enable FB authentication and set up app
* Create LoginActivity with FB auth features
* Create MapsActivity and view model
* Handle permission and move to current location
* Create marker and read note from user
* Create Firebase realtime database - please note this database works only 30 days till 2020-12-31
* Save a new marker to FB DB
* Load all saved markers from FB Db - it caches also for offline mode
* Different marker color for different users
* ActionSheet to filter note or user name
* Create Info Window for markers
* todo: cluster markers
* todo Unit Test





  
