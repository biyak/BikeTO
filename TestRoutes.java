package group01_finalproject;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Biya Kazmi, Rumsha Siddiqui, Adele Olejarz, Stephen Lamothe
 * @date 04/05/2017
 * @version 1.0
 * A test file for the routes created by this program.
 */

public class TestRoutes {
	
	public static HashMap<Integer,LatLong> intersections;
	public static HashMap<Integer,LatLong> bikeNodes;
	
	public static Graph G;
	
	public static ArrayList<Integer> bikeParkingNodeIDs;
	public static ArrayList<LatLong> bikeParkingCoordinates; //the intersections closest to the bike parking location
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		//Put intersections in hashmap
		intersections = new HashMap<Integer,LatLong>();
		
		String intersectionsFileName = "data/CENTRELINE_INTERSECTION_WGS84.csv";
		Scanner input =  new Scanner(new File(intersectionsFileName));
		String[] throwout =  input.nextLine().split(",");
		while (input.hasNextLine()){
			String[] line = input.nextLine().split(",");
			LatLong l = new LatLong(Double.parseDouble(line[16]), Double.parseDouble(line[15]));
			
			Integer data = Integer.parseInt(line[0]);
			
			intersections.put(data,l);
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
		System.out.println("Inputted data");

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the correctness of the paths by checking to see if the two consecutive vertices
	 * on the returned path are indeed connected in the original graph.
	 * 
	 * Testing all the nodes in the intersections key set was taking too long because the data set
	 * is quite large. So only one path was chosen to test the correctness of.
	 */
	@Test
	public void testBFS() {
		boolean works = true;
		Integer source = 0;
		Integer destination = 0;
		
		//Choose one path to test the correctness of:
		for (Integer s : intersections.keySet()){
			source = s;
		}
		for (Integer s : intersections.keySet()){
			destination = s;
		}
		BreadthFirstSearch bfs = new BreadthFirstSearch(G, source);
		ArrayList<Integer> path = bfs.pathToArray(destination);
		
		for (int i = 0; i < path.size() - 2; i++){
			if (!G.hasEdge(path.get(i), path.get(i+1)))
				works = false;
		}
		
		System.out.println("Done");
		assertTrue(works);	
	}
}
