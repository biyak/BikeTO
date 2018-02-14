package group01_finalproject;
/**
 * @author Adele Olejarz, Rumsha Siddiqui, Stephen Lamothe, Biya Kazmi
 * @date 03/29/17; Mar 29, 2017
 * @version 1.0
 * A comparable address class, to store data from the Address dataset.
 */

public class Address implements Comparable{
	private String street;
	private String number;
	private double latitude;
	private double longitude; 

	//Constructor
	public Address(String st, String n, double la, double lo) {
		this.street = st;
		this.number = n;
		this.latitude = la;
		this.longitude = lo;
	}

	/**
 	* @return the latitude of the address
 	*/
	public double getLat() {
		return latitude;
	}

	/**
 	* @return the longitude of the address
 	*/
	public double getLon() {
		return longitude;
	}

	/**
 	* @return the street the address is on
 	*/
	public String getStreet() {
		return street;
	}

	/**
 	* @return the the number part of the address
 	*/
	public String getNumber() {
		return number;
	}
	
	/**
 	* @return a string representation of the address
 	*/
	public String toString(){
		return number + " " + street + ", " + latitude + ", " + longitude;
	}

	
	/*
	 * from the return of compareTo
	 * "The value 0 if the argument is a string lexicographically equal to this string;
	 * a value less than 0 if the argument is a string lexicographically greater than this string;
	 * and a value greater than 0 if the argument is a string lexicographically less than this string.
	 * @return an int, representing the relation between the source and target.
	 */
	@Override
	public int compareTo(Object o) {
		if(o instanceof Address){
			if(((Address) o).getStreet()==this.getStreet())//if names of streets are equal
				return 0;
			else if((this.getStreet().compareToIgnoreCase(((Address) o).getStreet()) < 0 ))//if lex. grater than this obj
					return -1;
			else //if lex. smaller than this obj
				return 1;
		}
		else
		throw new IllegalArgumentException();
	}

}
