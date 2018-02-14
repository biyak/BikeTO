FOR TESTING THE GOOGLE MAPS API
  - Due to an issue with Google Maps API, some latitude/longitude points
	cannot be converted into waypoints. A "zero results" error signifies this.
	removing the coordinate(s) that are causing this issue will allow the map
	to function correctly. Currently, there is no way to tell which coordinates are invalid.