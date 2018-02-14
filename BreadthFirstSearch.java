package group01_finalproject;
/**
 * @author Rumsha Siddiqui, Stephen Lamothe, Adele Olejarz, Biya Kazmi
 * @date March 26, 2017
 * @version 1.0
 * This class finds the shortest possible path from a source to every other node in a 
 * graph based on breadth first search.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class BreadthFirstSearch {

	private HashMap<Integer, Integer> edgeTo; //edgeTo[v] = previous edge on shortest s-v path
	private HashMap<Integer, Integer> distTo; //distTo[v] = number of edges on shortest s-v path
	
	
	/**
	 * Computes the shortest path between the source node s and every
	 * other node on the Graph
	 * @param M The Graph
	 * @param s The source node (i.e. the starting point/city)
	 */
	public BreadthFirstSearch(Graph M, Integer s){
		edgeTo = new HashMap<Integer, Integer>();
		distTo = new HashMap<Integer, Integer>();
		
		
		bfs(M, s);
	}
	
	/*
	 * breadth first search from a single source
	 */
	private void bfs(Graph M, Integer s){
		Queue<Integer> q = new Queue<Integer>();
		
		//Initialize the distTo values for each city
		for (Vertex e : M.getVertices()){
			distTo.put(e.getID(), Integer.MAX_VALUE);
		}
		distTo.put(s, 0);
		edgeTo.put(s, s);
		q.enqueue(s);
		
		while(!q.isEmpty()){
			Integer v = q.dequeue();
			
			for (Vertex w : M.adjacentTo(v)){
				if (!edgeTo.containsKey(w.getID())) {//if w has not been visited yet
					edgeTo.put(w.getID(), v); //add edge to Vertex w
					Integer d = distTo.get(v) + 1;
					distTo.put(w.getID(), d); //update distTo[w]
					q.enqueue(w.getID());
				}
			}
			
		}
	}
	
	
	/**
	 * Returns the shortest path between the source node and node v
	 * @param v The destination node
	 * @return The sequence of nodes on the shortest path, as an ArrayList
	 */
	public ArrayList<Integer> pathToArray(Integer v){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		
		if (!edgeTo.containsKey(v)) //If there is not path from the source node to node v
			return arr;
		
		Stack<Integer> path = new Stack<Integer>();
		Integer x;
		for (x = v; distTo.get(x) != 0; x = edgeTo.get(x)){
			path.push(x);
		}
		path.push(x);
		
		while(!path.isEmpty()){
			arr.add(path.pop());
		}
		return arr;
	}
	
	public Integer distTo(Integer v){
		return distTo.get(v);
	}
	
	
	/**
	 * Determines whether or not a vertex has been visited (i.e. considered when performing the path
	 * calculation)
	 * @param v The name of the vertex
	 * @return True if the vertex has been visited by the bfs algorithm
	 */
	public boolean hasEdgeTo(Integer v){
		return edgeTo.containsKey(v);
	}
	
	
	/**
	 * Unit testing
	 */
	public static void main(String[] args) {
		Graph m = new Graph();
		m.addEdge(100,1,1,200,2,2);
		m.addEdge(200,2,2,300,3,3);
		m.addEdge(100,1,1,300,3,3);
		
		/*
		System.out.println(m.adj.get("c"));
		System.out.println(m.adj.get("b"));*/

		/*m.adjToString("a");
		m.adjToString("b");
		m.adjToString("c");*/
		
		//BreadthFirstSearch b = new BreadthFirstSearch(m, 100);
		
		/*ST<Vertex,Vertex> s = new ST<Vertex,Vertex>();
		s.put(c1, c2);
		System.out.println(s.contains(c3));*/
	}
}
