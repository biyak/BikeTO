package group01_finalproject;

/**
 * @author Rumsha Siddiqui, Biya Kazmi, Adele Olejarz, Stephen Lamothe
 * @date 03/25/17; March 25, 2017
 * @version 1.0
 * A data type to represent a latitude and longitude coordinate.
 */
public class LatLong implements Comparable<LatLong>{
	private double latitude;
	private double longitude;
	
	/**
	 * Constructor
	 * @param latitude Latitude of a point
	 * @param longitude Longitude of a point
	 */
	public LatLong(double lat, double lon){
		latitude = lat;
		longitude = lon;
	}

	/**
	 * @return Latitude of the point
	 */
	public double getLatitude(){
		return latitude;
	}

	/**
	 * @return Longitude of the point
	 */
	public double getLongitude(){
		return longitude;
	}
	
	/**
	 * Uses the Haversine formula to calculate the distance between
	 * two lat/long points in decimal degrees
	 * @param other The lat/long coordinate (in decimal degree format) to calculate the distance to
	 * @return Distance between two lat/long points in km
	 */
	public double distTo(LatLong other){
		double a = 6378.137; //equatorial radius of Earth in km
		double b = 6356.752; //center of Earth spheroid to each pole in km
		double radius = (2*a + b) / 3; //mean earth radius
		
		double lat1 = Math.toRadians(latitude);
		double lat2 = Math.toRadians(other.getLatitude());
		double deltaLat = Math.toRadians(other.getLatitude() - latitude);
		double deltaLon = Math.toRadians(other.getLongitude() - longitude);
		
		double haversine = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
				           Math.cos(lat1) * Math.cos(lat2) *
				           Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		
		double angle = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
		
		return  radius * angle;
	}
	
	/**
	 * Convert to a string format
	 * @return A string representation of the lat/long coordinate
	 */
	public String toString () {
		return this.latitude + ", " + this.longitude;
	}
	
	@Override
	public boolean equals(Object o) {
		LatLong other = (LatLong) o;
		return (latitude == (other.getLatitude()) && (longitude == (other.getLongitude())));
	}

	@Override
	public int compareTo (LatLong other) {
		if (latitude == (other.getLatitude()) && (longitude == (other.getLongitude()))){
			return 0;
		}
		return 1;
	
	}
	
	
	/**
	 * Unit testing
	 */
	public static void main(String[] args) {
		LatLong l1 = new LatLong(40.7486, -73.9864);
		LatLong l2 = new LatLong(41.7486, -74.9864);	
		System.out.println(l1.getLatitude());
		System.out.println(l1.distTo(l2)); //should be 139.1 km
	}

}
