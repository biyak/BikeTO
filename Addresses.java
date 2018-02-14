package group01_finalproject;

import java.util.ArrayList;
import java.io.*;

/**
 * @author Adele Olejarz, Rumsha Siddiqui, Stephen Lamothe, Biya Kazmi
 * @date 03/29/17; Mar 29, 2017
 * @version 1.0
 * A module made to handle Address objects. 
 * features many methods for processing the address objects
 */

//ASSUMPTIONS: Init() must be called before addresses can be used.
public class Addresses {
	private static Address[] Ad;//used for construction, regardless of size
	private static Address[] Addresses; //for returning after sorting
	private static final String path = "data/ADDRESS_POINT_WGS84.csv";
	private static boolean initialized = false;
	public static int MAX_SIZE = 600000;
	private static int length;

	public static void init() {
		Ad = new Address[MAX_SIZE];
		try {
			readIn();
			initialized = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	* @param a the user's input
 	* @return a latlong object that represents the geographical coordinate of the address
 	*/
	public static LatLong getLatLong(String a) throws InvalidStreetException {
		//user's input should be NUMBER,STREETNAME with any case. String formatting method takes care of case etc.
		//because streetname may have spaces.
		
		Address closeAddress = findClose(a);		
		return new LatLong(closeAddress.getLat(), closeAddress.getLon());
		
}
	/**
	* @param a the user's input
 	* @return the closest Address object to the input
 	*/
	private static Address findClose(String a) throws InvalidStreetException{
		String[] in = a.split(",");
		in[1] = formatString(in[1]);
		ArrayList<Address> Spots = QuickFind(in[1],0,length-1);
		if(Spots == null){
			throw new InvalidStreetException();
		}
		
		int number = removeLet(in[0]);		
		int diff = 999999; //a min function, minimizing distance from the number ((in theory))
		Address closestA = null;
		for(Address x : Spots){
			if(Math.abs(number-removeLet(x.getNumber())) < diff){
				diff = Math.abs(number-removeLet(x.getNumber()));
				closestA = x;				
			}
		}
		return closestA;
		
		
		
	}
	
	/**
	* @param a the number of the address
 	* @return the number, without letters in the number part
 	*/
	private static int removeLet(String a){//some of the addresses have letters, like 2B making them not comparable
		//this takes care of that.
		String hold = a.replaceAll("[^0-9]", ""); // proper RegEx found here.
		//      http://stackoverflow.com/questions/10372862/java-string-remove-all-non-numeric-characters
		return Integer.parseInt(hold);
	}
	
	/**
	* @param a the user's address
 	* @return the street name, formatted to better fit the dataset
 	*/
	private static String formatString(String a){
		a = a.replace(".", ""); // split by spaces, so every word gets capitalized properly
		String[] cap;
		cap = a.split(" ");
		String firstLet; String rest;
		a = "";
		for(int p = 0; p < cap.length;p++){
			firstLet = cap[p].substring(0, 1);
			rest = cap[p].substring(1);
			rest = rest.toLowerCase();
			firstLet = firstLet.toUpperCase();
			cap[p] = firstLet+rest;
			cap[p] = cap[p].replace("Street","St");
			cap[p] = cap[p].replace("Road","Rd");
			cap[p] = cap[p].replace("Boulevard","Blvd");
			cap[p] = cap[p].replace("Place","Pl");
			cap[p] = cap[p].replace("Avenue","Ave");
			cap[p] = cap[p].replace("Court","Crt");
			cap[p] = cap[p].replace("East","E");
			cap[p] = cap[p].replace("West","W");
			cap[p] = cap[p].replace("North","N");
			cap[p] = cap[p].replace("South","S");
			
			if(p == 0)
				a+= cap[p];
			else
				a+= " "+cap[p];
			
		}
		
		return a;
		
	}
	
 
	/**
 	* reads in from the dataset
 	*/
	public static void readIn() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(path));
		String line = r.readLine();
		line = r.readLine(); // do it twice to get rid of heading row

		String tempRd; // temporary string to hold road name
		String tempNu; // temporary int to store address number
		double tempLat;
		double tempLong;
		length = 0;

		while (line != null) { //reading in the data. 
			String[] data = line.split(","); //the values are comma separated
			tempNu = data[3]; //4th column is Number
			tempRd = data[4]; //5th col is the Street Name
			tempLat = Double.parseDouble(data[18]); //latitude is column 19
			tempLong = Double.parseDouble(data[17]); // long is col 18
			
			Ad[length]=new Address(tempRd, tempNu, tempLat, tempLong); //make a new Address Object for this line
			line = r.readLine();//go to next line
			length ++; // add one to the size. NEED THIS because we need to remove NULL entries before sorting.

		}
		r.close();
		Addresses = new Address[length];
		for(int i = 0; i < length; i++){
			Addresses[i] = Ad[i];
		}
	
		QuickX.sort(Addresses);
		Ad = Addresses;
		

	}
	
	public static Address[] getAddresses() throws UninitializedTypeException{
		if(initialized)
		return Addresses;
		else
			throw new UninitializedTypeException();
	}
	
	//a modified algorithm i designed, ironically based off of binary search, not quicksort i think
	private static ArrayList<Address> QuickFind(String s, int lo, int hi){
		ArrayList<Address> matches = new ArrayList<Address>();
		int mid = (lo + hi)/2;
		int savemid = mid;
		if(lo == hi && Ad[mid].getStreet() != s){
			return null;
		}
		
		if (s.compareTo(Ad[mid].getStreet())==0){ //if found, we check the surrounding area to get ALL matching strings
			while(mid>=0 && s.equals(Ad[mid].getStreet())){//thats the modification
				matches.add(Ad[mid]);
				mid-=1; //check to the left first				
			}
			mid = savemid + 1;
			while((mid < length) && s.equals(Ad[mid].getStreet())){
				matches.add(Ad[mid]);
				mid++;
			}
			
			return matches;
		}
		else if(s.compareTo(Ad[mid].getStreet())<0){
			//int a = s.compareTo(Ad[mid].getStreet());//for testing
			return QuickFind(s,lo,mid);			
		}
		else{
			//int a = s.compareTo(Ad[mid].getStreet());//for testing
			return QuickFind(s,mid+1,hi);
		}
	}
	
	
	/**
	 * Unit testing
	 */
	//public static void main(String[] args) throws InvalidStreetException {
		
		/*init();
		for(int i = 0; i < length;i++){
			System.out.println(Ad[i]);
		}
		
		System.out.println("Testing Formatting");
		System.out.println(formatString("HALCYON STREET."));
		System.out.println(formatString("goldwalk boulevard"));
		System.out.println(formatString("Main St. South"));
		System.out.println(temp[1]);
		System.out.println(Ad[1]);
		System.out.println(temp[178]);
		System.out.println(Ad[178]);
		*/
		/*init();
		for(int i = 0; i < length;i++){
			System.out.println(Ad[i]);
		}
		
		System.out.println(QuickFind("Jellicoe Ave",0,length));
		System.out.println(findClose("40,Jellicoe Avenue"));
		System.out.println("Jellicoe Ave".compareTo("Foch St"));
		System.out.println("Foch St".compareTo("Jellicoe Ave"));
		System.out.println(findClose("7,lloyd george avenue"));
		System.out.println(findClose("53,Foch Ave."));
		System.out.println(getLatLong("56,foch avenue"));
		
	}*/
 
}
