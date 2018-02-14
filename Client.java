//package group01_finalproject;
package group01_finalproject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.*; // used for loading the map
import java.net.*; // used for loading the map

/**
 * @author Rumsha Siddiqui, Stephen Lamothe, Adele Olejarz, Biya Kazmi
 * @date 03/25/17; March 25, 2017
 * @version 1.0
 * Runs the bike routing program, BikeTO.
 */
public class Client{
	
	public static HashMap<Integer,LatLong> intersections;
	public static HashMap<Integer,LatLong> bikeNodes;
	
	public static Graph G;
	
	public static void main(String[] args) throws IOException{
		/*
		 * READ DATA
		 */
		
		//Put intersections in hashmap
		intersections = new HashMap<Integer,LatLong>();
		String intersectionsFileName = "data/CENTRELINE_INTERSECTION_WGS84.csv";
		Scanner input =  new Scanner(new File(intersectionsFileName));
		String[] throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			LatLong l = new LatLong(Double.parseDouble(line[16]), Double.parseDouble(line[15]));
			Integer nodeID = Integer.parseInt(line[0]);
			intersections.put(nodeID,l);
		}
		input.close();
		
		
		//CREATE GRAPH based on centreline data, which tells us which nodes/intersections are connected
		G = new Graph();
		String centrelineFileName = "data/CENTRELINE_WGS84.csv";
		input =  new Scanner(new File(centrelineFileName));
		throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			if(!line[14].equals("Major Arterial") && !line[14].equals("No Intersection") ){//don't connect the Highways: unsafe
				Integer fnode = Integer.parseInt(line[11]);
				Integer tnode = Integer.parseInt(line[12]);
				//Look up corresponding lat/long points
				double fnodeLat = intersections.get(fnode).getLatitude();
				double fnodeLon = intersections.get(fnode).getLongitude();
				double tnodeLat = intersections.get(tnode).getLatitude();
				double tnodeLon = intersections.get(tnode).getLongitude();
				
				G.addEdge(fnode, fnodeLat, fnodeLon, tnode, tnodeLat, tnodeLon);
			}
		}
		input.close();
		
		
		//IMPORT BIKE PARKING on street
		bikeNodes = new HashMap<Integer, LatLong>();
		String bikeParkingOnStreetFileName = "data/BICYCLE_PARKING_ON_STREET_WGS84.csv";
		input =  new Scanner(new File(bikeParkingOnStreetFileName));
		throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			LatLong l = new LatLong(Double.parseDouble(line[6]), Double.parseDouble(line[5]));
			
			//Find intersection closest to bike parking
			Integer bpNodeID = PathCalculation.closestIntersection(l, intersections);
			bikeNodes.put(bpNodeID, intersections.get(bpNodeID));
		}
		input.close();
		
		
		/*
		 * USER INPUT
		 * Ask user for beginning and end address
		 */
		System.out.println("Hello");
		Scanner s = new Scanner(System.in); 
		Boolean INvalidinput = true; // used for while loop; loop while the input is not good
		String userString; //for storage of user's desired input
		LatLong StartCoords = new LatLong(0,0);
		LatLong EndCoords = new LatLong(0,0);
		Addresses.init();
		while(INvalidinput){
			try{
			System.out.println("Please enter your starting address in the following format: #,Street Name (ex: 121,Maplewood Ave) (NOTE: there is NO SPACE after the comma):");
			userString = s.nextLine();
			StartCoords = Addresses.getLatLong(userString);
			INvalidinput = false; // essentially if it GETS one it's valid. OTHERWISE it will throw an exception
			}
			catch(InvalidStreetException | ArrayIndexOutOfBoundsException e){
				System.out.println("INVALID STREET");
			}
		}
		INvalidinput = true;
		while(INvalidinput){
			try{
			System.out.println("Please enter your ending address in the following format: #,Street Name (ex: 121,Maplewood Ave):");
			userString = s.nextLine();
			EndCoords = Addresses.getLatLong(userString);
			INvalidinput = false; // essentially if it GETS one it's valid. OTHERWISE it will throw an exception
			}
			catch(InvalidStreetException | ArrayIndexOutOfBoundsException e){
				System.out.println("INVALID STREET");
			}
		}
		s.close();
			
			
		
		/*
		 * COMPUTE PATH
		 * should return list of lat/long points 
		 */
		
	/*	Integer source = 30075902;
		Integer destination = 13469577;
	*/
		Integer source = PathCalculation.closestIntersection(StartCoords, intersections);
		Integer destination = PathCalculation.closestIntersection(EndCoords, intersections);
		
		//Determine bike parking location by finding shortest path
		BreadthFirstSearch bfsDestinationToBikeParking = new BreadthFirstSearch(G, destination);
		Integer closestDistance = Integer.MAX_VALUE;
		Integer closestBPNodeID = 0;
		for (Integer bpNodeID : bikeNodes.keySet()){
			Integer distance = bfsDestinationToBikeParking.distTo(bpNodeID);
			if (distance < closestDistance){
				closestDistance = distance;
				closestBPNodeID = bpNodeID;
			}
		}
		
		//Print out coordinates of 3 locations:
		System.out.println("Source = " + intersections.get(source));
		System.out.println("Bike Parking Location = " + intersections.get(closestBPNodeID));
		System.out.println("Destination = " + intersections.get(destination));
		
		
		//Get path from bpNodeID to destination in array list
		ArrayList<Integer> destToBP = bfsDestinationToBikeParking.pathToArray(closestBPNodeID); //path from dest --> BP
		
		//Get path from source to bpNodeID in array list
		BreadthFirstSearch bfsSourceToBikeParking = new BreadthFirstSearch(G, source);
		ArrayList<Integer> sourceToBP = bfsSourceToBikeParking.pathToArray(closestBPNodeID);
		
		//create final path in nodeId's
		ArrayList<Integer> finalPath = new ArrayList<Integer>();
		
		//path from source to BP
		System.out.println("\nWaypoints from Source to Bike Parking Location:");
		for (int i = 0; i < sourceToBP.size() - 1; i++){
			finalPath.add(sourceToBP.get(i));
			System.out.print(intersections.get(sourceToBP.get(i)) + ", ");
		}
		//Add last point without the comma
		finalPath.add(sourceToBP.get(sourceToBP.size() - 1));
		System.out.print(intersections.get(sourceToBP.get(sourceToBP.size() - 1)));
		
		
		//want path from BP to dest, so reverse destoToBP array list:
		System.out.println("\n\nWaypoints from Bike Parking Location to Destination:");
		for (int i = destToBP.size() - 1; i >= 1; i--){
			finalPath.add(destToBP.get(i));
			System.out.print(intersections.get(destToBP.get(i)) + ", ");
		}		
		//Add last point without the comma
		finalPath.add(destToBP.get(0));
		System.out.print(intersections.get(destToBP.get(0)));
		
		
		//create array list of lat/long points:
		ArrayList<LatLong> finalLatLongPath = new ArrayList<LatLong>();
		for (int i = 0; i < finalPath.size(); i++){
			finalLatLongPath.add(intersections.get(finalPath.get(i)));
		}
		
		System.out.println("\n\nDone!\nFor this prototype build, you must copy/paste the lat/lon coordinates into the correct textboxes on the web page.");
		
		
		/*
		 * DISPLAY
		 * Show path on google map
		 */
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI("https://jsfiddle.net/rmLtu9t7/8/"));
		} catch (URISyntaxException e1) {
			System.out.println("Invalid Website URL");
		}
	}
	
	
}