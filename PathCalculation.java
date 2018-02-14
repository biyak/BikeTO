package group01_finalproject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rumsha Siddiqui, Stephen Lamothe, Biya Kazmi, Adele Olejarz
 * @date 03/25/17; March 25, 2017
 * @version 1.0
 * Contains methods involved in calculating a path from a starting point to an ending point, incorporating 
 * a bike parking location near the destination.
 */
public class PathCalculation {
	
	/**
	 * Finds the closest intersection/node to the given lat/long coordinate
	 * @param coordinate Lat/Long coordinate
	 * @param listIntersections List of lat/long coordinates of the intersections
	 * @return Lat/long coordinate of closest intersection 
	 */
	public static Integer closestIntersection(LatLong coordinate, HashMap<Integer, LatLong> intersections){
		Integer closestNodeID = 0;
		double closestDistance = Double.POSITIVE_INFINITY;
		
		for(Integer nodeID : intersections.keySet()){
			double distance = coordinate.distTo(intersections.get(nodeID));
			if (distance < closestDistance){
				closestNodeID = nodeID;
				closestDistance = distance;
			}
		}
		return closestNodeID;
	}
}
