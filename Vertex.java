package group01_finalproject;
/**
 * @author Rumsha Siddiqui, Stephen Lamothe, Adele Olejarz, Biya Kazmi
 * @date March 26, 2017
 * @version 1.0
 * An ADT for a vertex that stores the information of a vertex's unique ID and longitude/latitude coordinate 
 * in one object.
 *
 */
public class Vertex implements Comparable<Vertex> {

	private Integer nodeID; 	// unique identifier for vertex
	private double latitude; 		
	private double longitude;

	/**
	 * Constructor
	 * @param v The unique identifier for the vertex
	 * @param lat The latitude point of the vertex
	 * @param lon The longitude point of the vertex
	 */
	public Vertex(Integer v, double lat, double lon) {
		nodeID = v;
		latitude = lat;
		longitude = lon;
	}
	
	/**
	 * Get the unique integer ID of the vertex
	 * @return The unique integer ID of the vertex
	 */
	public Integer getID(){
		return nodeID;
	}

	/**
	 * Get the latitude of the vertex
	 * @return The latitude point of the vertex
	 */
	public double getLatitude(){
		return latitude;
	}
	
	/**
	 * Get the longitude of the vertex
	 * @return The longitude point of the vertex
	 */
	public double getLongitude(){
		return longitude;
	}
	
	/**
	 * Convert the vertex to a string representation
	 * @return The integer ID of the vertex in string form
	 */
	public String toString() {
		return nodeID.toString();
	}

	/**
	 * How to compare one vertex to another
	 * @return 0 if two vertices have the same unique ID
	 */
	public int compareTo(Vertex other) {
		return nodeID.compareTo(other.nodeID);
	}
}
