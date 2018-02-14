package group01_finalproject;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author Biya Kazmi, Rumsha Siddiqui, Adele Olejarz, Stephen Lamothe
 * @date 04/05/2017
 * @version 1.0
 * A test file for LatLong.java
 */
public class LatLongTest {

	private static final double[] latitudes = {0,1,2,3,4,5};
	private static final double[] longitudes = {0,1,2,3,4,5};

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetters() {
		double n = 0;
		double m = 0;
		for(double lat = 0 ; lat<latitudes.length; lat++){
			m = 0;
			for(double lon = 0 ; lon<longitudes.length; lon++){
				LatLong coordinates = new LatLong(lat,lon);
//				System.out.println("lat: " + coordinates.getLatitude() + " " + n);
//				System.out.println("lon: " + coordinates.getLongitude() + " " + m);
				assertTrue(coordinates.getLatitude() == n);
				assertTrue(coordinates.getLongitude() == m);
				m++;
			}
			n++;
		}
	}
	@Test
	public void testDistTo(){
		LatLong l1 = new LatLong(43.586671, 79.676957);
		LatLong l2 = new LatLong(38.617017, 90.211024);
		LatLong l3 = new LatLong(43.2608576, -79.9892652);
		System.out.println(l3.distTo(l1) + " " + 10142);
		assertEquals(l1.distTo(l2), 1039, 10);
		assertEquals(l3.distTo(l1), 10142, 10);
		
	}

}
