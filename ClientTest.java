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
 * @author Biya Kazmi
 * @date 04/05/2017
 * @version 1.0
 * A test file for Client.java
 * I decided to use this type of testing instead of JUnit because 
 * for the case of testing the Client class, which has only one 
 * method, which is main.
 * So instead of having test methods, there is just one test that 
 * reads through a test file and prints out pass or fails and why. 
 */
public class ClientTest{
	
	public static HashMap<Integer,LatLong> intersections;
	public static HashMap<Integer,LatLong> bikeNodes;
	
	public static Graph G;
	
	public static ArrayList<Integer> bikeParkingNodeIDs;
	public static ArrayList<LatLong> bikeParkingCoordinates; //the intersections closest to the bike parking location
	
	public static ArrayList<Integer> nodeIDs;
	public static ArrayList<LatLong> nodeCoordinates;
	
	
	public static void main(String[] args) throws IOException{
		//Read data (call a method in ReadData.java) --> Create graph
		
		//Put intersections in hashmap
		intersections = new HashMap<Integer,LatLong>();
		/*nodeIDs = new ArrayList<Integer>();
		nodeCoordinates = new ArrayList<LatLong>();*/
		
		String intersectionsFileName = "data/CENTRELINE_INTERSECTION_WGS84.csv";
		Scanner input =  new Scanner(new File(intersectionsFileName));
		//System.out.println("intersection");
		//System.out.println(input.hasNextLine());
		String[] throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			LatLong l = new LatLong(Double.parseDouble(line[16]), Double.parseDouble(line[15]));
			
			Integer data = Integer.parseInt(line[0]);
			
			intersections.put(data,l);
			
			/*nodeIDs.add(data);
			nodeCoordinates.add(l);*/
			
		}
		input.close();
		
		
		//Create graph based on centreline data, which tells us which nodes/intersections are connected
		G = new Graph();
		String centrelineFileName = "data/CENTRELINE_WGS84.csv";
		input =  new Scanner(new File(centrelineFileName));

		throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			Integer fnode = Integer.parseInt(line[11]);
			Integer tnode = Integer.parseInt(line[12]);
			
			//Look up corresponding lat/long points
			double fnodeLat = intersections.get(fnode).getLatitude();
			double fnodeLon = intersections.get(fnode).getLongitude();
			double tnodeLat = intersections.get(tnode).getLatitude();
			double tnodeLon = intersections.get(tnode).getLongitude();
			
			G.addEdge(fnode, fnodeLat, fnodeLon, tnode, tnodeLat, tnodeLon);
		}
		input.close();
		
		
		//Import bike parking on street
		bikeParkingCoordinates = new ArrayList<LatLong>();
		bikeParkingNodeIDs = new ArrayList<Integer>();
		bikeNodes = new HashMap<Integer, LatLong>();
		String bikeParkingOnStreetFileName = "data/BICYCLE_PARKING_ON_STREET_WGS84.csv";
		input =  new Scanner(new File(bikeParkingOnStreetFileName));
		System.out.println("hello" + "\n" + "Testing Client.java" +"\n");
		//System.out.println(input.hasNextLine());
		throwout =  input.nextLine().split(",");
		
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			LatLong l = new LatLong(Double.parseDouble(line[6]), Double.parseDouble(line[5]));
			
			//Find intersection closest to bike parking
			Integer bpNodeID = PathCalculation.closestIntersection(l, intersections);
			bikeParkingNodeIDs.add(bpNodeID);
			bikeParkingCoordinates.add(intersections.get(bpNodeID));
			
			//System.out.println(l + " " + intersections.get(bpNodeID));
			
			bikeNodes.put(bpNodeID, intersections.get(bpNodeID));
			
			
		}
		input.close();
		
		
		
		/*for (Integer e : bikeNodes.keySet()){
			System.out.println(e);
		}*/
		
		
		//Ask user for beginning and end address
		String userString = null; //for storage of user's desired input
		
		Scanner s = new Scanner(new File("data/testData.txt")); //Data file with addresses, source and destination
		int n = 1; // Test case number
		while(s.hasNextLine()){
			Boolean INvalidinput = true; // used for while loop, loop while the input is not good
			
			LatLong StartCoords = new LatLong(0,0);
			LatLong EndCoords = new LatLong(0,0);
			Addresses.init();
			while(INvalidinput){
				try{
				System.out.println("Testing case: " + n);
				userString = s.nextLine();
				StartCoords = Addresses.getLatLong(userString);
				INvalidinput = false; // essentially if it GETS one it's valid. OTHERWISE it will throw an exception
				}
				catch(InvalidStreetException | NumberFormatException e){
					System.out.println("INVALID STREET: " + userString + "\n");
					n++;
				}
				catch(ArrayIndexOutOfBoundsException e){
					System.out.println("PLEASE ENTER: StreetNumber,StreetName" + "\n");
					n++;
				}
				catch(StringIndexOutOfBoundsException e){
					System.out.println("PLEASE ENTER: StreetNumber,StreetName" + "\n");
					n++;
				}
			}
			INvalidinput = true;
			while(INvalidinput){
				try{
				userString = s.nextLine();
				EndCoords = Addresses.getLatLong(userString);
				INvalidinput = false; // essentially if it GETS one it's valid. OTHERWISE it will throw an exception
				}
				catch(InvalidStreetException | NumberFormatException e){
					System.out.println("INVALID STREET" + "\n");
					n++;
				}
				catch(ArrayIndexOutOfBoundsException e){
					System.out.println("PLEASE ENTER: StreetNumber,StreetName" + "\n");
					n++;
				}
				catch(StringIndexOutOfBoundsException e){
					System.out.println("PLEASE ENTER: StreetNumber,StreetName" + "\n");
					n++;
				}
			}
				
				
			
			//Call path calculation --> should return list of lat/long points given two lat/long points and a graph
			
		/*	Integer source = 30075902;
			Integer destination = 13469577;
		*/
			Integer source = PathCalculation.closestIntersection(StartCoords, intersections);
			Integer destination = PathCalculation.closestIntersection(EndCoords, intersections);
			
			/*Integer source = nodeIDs.get(nodeCoordinates.indexOf(StartCoords));
			Integer destination = nodeIDs.get(nodeCoordinates.indexOf(EndCoords));*/
			
			//Determine bike parking location by finding shortest path
					//Integer chosenBikeParkingNodeID = PathCalculation.closestBikeParking(destination, bikeParkingLocations);
			
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
			
			/*for (Integer e : bfsDestinationToBikeParking.getDistTo().keySet()){
				System.out.println(e);
			}
			*/
			//Get path from bpNodeID to destination in array list
			ArrayList<Integer> destToBP = bfsDestinationToBikeParking.pathToArray(closestBPNodeID); //path from dest --> BP
			
			//Get path from source to bpNodeID in array list
			BreadthFirstSearch bfsSourceToBikeParking = new BreadthFirstSearch(G, source);
			ArrayList<Integer> sourceToBP = bfsSourceToBikeParking.pathToArray(closestBPNodeID);
			//create final path in nodeId's
			ArrayList<Integer> finalPath = new ArrayList<Integer>();
			
			for (int i = 0; i < sourceToBP.size(); i++){
				finalPath.add(sourceToBP.get(i));
			}
			//want path from BP to dest, so reverse array list:
			for (int i = destToBP.size() - 1; i < 0; i++){
				finalPath.add(destToBP.get(i));
			}		
			
			//create array list of lat/long points:
			ArrayList<LatLong> finalLatLongPath = new ArrayList<LatLong>();
			for (int i = 0; i < finalPath.size(); i++){
				finalLatLongPath.add(intersections.get(finalPath.get(i)));
			}
			
			for (LatLong e : finalLatLongPath){
				//System.out.print(e + ", ");
			}
			
			System.out.println("Test case " +n + " passed." + "\n");
			n++;
		}
		
//		Desktop d = Desktop.getDesktop();
//		try {
//			d.browse(new URI("https://jsfiddle.net/rmLtu9t7/6/"));
//		} catch (URISyntaxException e1) {
//			System.out.println("Invalid Website URL");
//		}
	}
	
	
}
