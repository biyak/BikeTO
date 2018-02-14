package group01_finalproject;
/**
 * @author Rumsha Siddiqui, Stephen Lamothe, Adele Olejarz, Biya Kazmi
 * @date March 27, 2017
 * @version 1.0
 * An ADT that represents a graph with directed edges between vertices.
 *
 */
import java.util.*;

public class Graph {
	private HashMap<Vertex, TreeSet<Vertex>> myAdjList; //maps a vertex to the set of vertices that it is connected to
	private HashMap<Integer, Vertex> myVertices; //maps a unique integer id to a vertex
	private static final TreeSet<Vertex> EMPTY_SET = new TreeSet<Vertex>(); //an empty set to return if not tree set exists for a particular vertex
	private int myNumVertices;
	private int myNumEdges;

	/**
	 * Construct empty Graph
	 */
	public Graph() {
		myAdjList = new HashMap<Vertex, TreeSet<Vertex>>();
		myVertices = new HashMap<Integer, Vertex>();
		myNumVertices = myNumEdges = 0;
	}
	
	/**
	 * Create and add a new vertex name with no neighbors (if vertex does not yet exist)
	 * 
	 * @param name The integer ID of the vertex
	 * @param lat The latitude coordinate of the vertex
	 * @param lon The longitude coordinate of the vertex
	 * @return The new vertex that was created and added
	 */
	public Vertex addVertex(Integer name, double lat, double lon) {
		Vertex v;
		v = myVertices.get(name);
		if (v == null) {
			v = new Vertex(name, lat, lon);
			myVertices.put(name, v);
			myAdjList.put(v, new TreeSet<Vertex>());
			myNumVertices += 1;
		}
		return v;
	}

	/**
	 * Returns the Vertex matching v
	 * @param name a Integer name of a Vertex that may be in this Graph
	 * @return the Vertex with a name that matches v or 
	 * 		   null if no such Vertex exists in this Graph
	 */
	public Vertex getVertex(Integer name) {
		return myVertices.get(name);
	}

	/**
	 * Returns true iff v is in this Graph, false otherwise
	 * @param name a Integer name of a Vertex that may be in this Graph
	 * @return true iff v is in this Graph
	 */
	public boolean hasVertex(Integer name) {
		return myVertices.containsKey(name);
	}

	/**
	 * Determines whether or not a particular edge exists between vertices with unique integer IDs
	 * @param from The ID of the first vertex
	 * @param to The ID of the second Vertex
	 * @return True iff from -> to exists in this Graph
	 */
	public boolean hasEdge(Integer from, Integer to) {

		if (!hasVertex(from) || !hasVertex(to))
			return false;
		return myAdjList.get(myVertices.get(from)).contains(myVertices.get(to));
	}
	
	/**
	 * Add an edge to the graph
	 * @param from The ID of the first vertex
	 * @param flat The latitude of the first vertex
	 * @param flon The longitude of the first vertex
	 * @param to The ID of the second vertex
	 * @param tlat The latitude of the second vertex
	 * @param tlon The longitude of the second vertex
	 */
	public void addEdge(Integer from, double flat, double flon, Integer to, double tlat, double tlon) {
		Vertex v, w;
		if (hasEdge(from, to))
			return;
		myNumEdges += 1;
		if ((v = getVertex(from)) == null)
			v = addVertex(from,flat,flon);
		if ((w = getVertex(to)) == null)
			w = addVertex(to,tlat,tlon);
		myAdjList.get(v).add(w);
		myAdjList.get(w).add(v);
	}

	/**
	 * Get an iterator over the neighbors of Vertex v identified by its integer ID
	 * @param name the integer ID of Vertex v
	 * @return an Iterator over Vertices that are adjacent
	 * to Vertex v, empty set if v is not in graph
	 */
	public Iterable<Vertex> adjacentTo(Integer name) {
		if (!hasVertex(name))
			return EMPTY_SET;
		return myAdjList.get(getVertex(name));
	}

	/**
	 * Get an iterator over the neighbors of Vertex v
	 * @param v the Vertex
	 * @return an Iterator over Vertices that are adjacent
	 * to the Vertex v, empty set if v is not in graph
	 */
	public Iterable<Vertex> adjacentTo(Vertex v) {
		if (!myAdjList.containsKey(v))
			return EMPTY_SET;
		return myAdjList.get(v);
	}

	/**
	 * Returns an Iterator over all Vertices in this Graph
	 * @return an Iterator over all Vertices in this Graph
	 */
	public Iterable<Vertex> getVertices() {
		return myVertices.values();
	}
	
	/**
	 * Get the number of vertices in the graph
	 * @return The number of vertices in the graph
	 */
	public int numVertices()
	{
		return myNumVertices;
	}
	
	/**
	 * Get the number of edges in the graph
	 * @return The number of edges in the graph
	 */
	public int numEdges()
	{
		return myNumEdges;
	}
	
	/**
	 * Create a string representation of the graph
	 * @return A representation of the graph as a String
	 */
	public String toString() {
		String s = "";
		for (Vertex v : myVertices.values()) {
			s += v + ": ";
			for (Vertex w : myAdjList.get(v)) {
				s += w + " ";
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Unit testing
	 */
	public static void main(String[] args) {
		Graph G = new Graph ();
		G.addEdge(100,1,1, 200,2,2);
		G.addEdge(100,1,1, 300,3,3);
		G.addEdge(300,3,3, 400,4,4);
		G.addEdge(400,4,4, 500,5,5);
		G.addEdge(400,4,4, 600,6,6);
		G.addEdge(500,5,5, 600,6,6);
		G.addVertex(700,7,7);
		// print out graph
		System.out.println(G);

		// print out graph again by iterating over vertices and edges
		for (Vertex v : G.getVertices()) {
			System.out.print(v + ": ");
			for (Vertex w : G.adjacentTo(v.getID())) {
				System.out.print(w + " ");
			}
			System.out.println();
		}
	}
}