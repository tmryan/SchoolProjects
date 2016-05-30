package sjsu.ryan.cs146.project1.part2;

/*
Class: CS146-01
Semester: Spring 2016
Project: #1 - Quicksort
Sample JUnit tests for quickSort1, quickSort2 i.e., with pivot as last element and median, their running time,
and count of comparisons.
*/

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
* The main JUnit Test class to test each method in Quicksort
* 
*/
public class QuicksortTest {

	private Quicksort QS;
	
	@Before
	public void setUp() throws Exception {
	   QS = new Quicksort();
	   // Removing shuffle since new arrays are being 
	   // created for each test below
	} // setUp()
	
	
	
	/* 
	 * Method to test the Sorting of an empty List
	 */
	@Test
	public void testEmpty() {
		int[] array1 = new int[0];
		int[] array2 = new int[0]; //correct sorted array
	          
		QS.quickSort1(array1, 0, array1.length - 1);
		assertArrayEquals(array1,array2);
		
		QS.quickSort2(array2, 0, array1.length - 1);
		assertArrayEquals(array1,array2);
	}   
	
	
	/* 
	 * Method to test the Sorting of an already sorted list:
	 */
	@Test
	public void testSorted() {
	    int[] array1 = new int[20];
	    int[] array2 = new int[20];
		int[] array3 = new int[20];
	           
		 for (int i = 0; i < 10; i++) {
	         array1[i] = i*2;
	         array2[i] = i*2;
		     array3[i] = i*2;
	
	     }
		 // sort using Java's inbuilt sorting method
	     Arrays.sort(array3);
	      
	     // run quickSort1()
	     QS.quickSort1(array1, 0, array1.length - 1);
	     assertArrayEquals(array1,array3);
	     
	     // run quickSort2()
	     QS.quickSort2(array2, 0, array2.length - 1);
	     assertArrayEquals(array2,array3);
	}
	
	
	/* 
	 * Method to test the Sorting of a reverse sorted list:
	 */
	@Test
	public void testReverseSorted() {
	     int[] array1 = new int[10];
	     int[] array2 = new int[10];
	     
	     int[] array3 = new int[10];
	     
	     for (int i = 0; i < 10; i++) {
	    	 array1[i] = (100-i);
	         array2[i] = (100-i);
	         array3[i] = (100-i);
	     }
	     //sort array3 
	     Arrays.sort(array3); 
	
	     // run quickSort1()
	     QS.quickSort1(array1, 0, array1.length - 1);
	     assertArrayEquals(array1,array3);
	     
	     // run quickSort2()
	     QS.quickSort2(array2, 0, array2.length - 1);
	     assertArrayEquals(array2,array3);
	}
	
	/*
	 * Method to test the select method 
	 */
	 @Test
	 public void testselect() {
		 int[] array1 = new int[100];
	    
		 for (int i = 0; i < 100; i++) {
			 array1[i] = i;
		 }
		 // median is 49
		 int median=QS.select(array1,0, array1.length-1, array1.length/2);
		 System.out.println("median:"+ array1[QS.select(array1,0, array1.length-1, array1.length/2)]);
		 assertEquals(49, array1[median]);
		 System.out.println();
	 }
	
	
	/*
	 * Method to test the randomness to the tests:
	 */
	 @Test
	  public void testRandom() {
		 int[] array1 = new int[10];
		 
		 for (int i = 0; i < 10; i++) {
			 array1[i] = (int) Math.random()*10;
		 } 
	
		 //copy arrays
		 int[] array2 = Arrays.copyOf(array1, array1.length);  
		 int[] array3 = Arrays.copyOf(array1, array1.length); 
		 // correct sorted array 
		 Arrays.sort(array3);
	
		 // run quickSort1()
		 QS.quickSort1(array1, 0, array1.length - 1);
		 assertArrayEquals(array1,array3);
	    
		 // run quickSort2()
		 QS.quickSort2(array2, 0, array2.length - 1);
		 assertArrayEquals(array2,array3);
	  }
	
	/* 
	 * Method to test the timing of quickSort1
	 */
	@Test
	public void testquickSort1Timing() {
		// create an array and a sorted backup
		int[] array1 = QS.populate(10000, 10000);
		int[] array4 = QS.populate(100000, 100000);
	   	int[] array2 = QS.populate(1000000, 1000000);
	   	int[] array3 = QS.populate(10000000, 10000000);
	   	int[] array5 = QS.populate(100000000, 100000000);
	   
	   	long start = System.currentTimeMillis();
	   	QS.quickSort1(array1, 0, array1.length - 1);
	   	long end = System.currentTimeMillis();
	   	long elapsed = end - start;
	   	System.out.println("quickSort1 time to sort 10000 elements in ms:"+ elapsed);
	
		start = System.currentTimeMillis();
		QS.quickSort1(array4, 0, array4.length - 1);
		end = System.currentTimeMillis();
		elapsed = end - start;
		System.out.println("quickSort1 time to sort 100000 elements in ms:"+ elapsed);
	   	
	   	start = System.currentTimeMillis();
	   	QS.quickSort1(array2, 0, array2.length - 1);
	   	end = System.currentTimeMillis();
	   	elapsed = end - start;
	   	System.out.println("quickSort1 time to sort 1000000 elements in ms:"+ elapsed);
	   
	   	start = System.currentTimeMillis();
	   	QS.quickSort1(array3, 0, array3.length - 1);
	   	end = System.currentTimeMillis();
	   	elapsed = end - start;
	   	System.out.println("quickSort1 time to sort 100000000 elements in ms:"+ elapsed);
	   	
	   	start = System.currentTimeMillis();
	   	QS.quickSort1(array5, 0, array5.length - 1);
	   	end = System.currentTimeMillis();
	   	elapsed = end - start;
	   	System.out.println("quickSort1 time to sort 1000000000 elements in ms:"+ elapsed);
	   	System.out.println();
	}
	
	/* 
	 * Method to test the timing of quickSort2
	 */
	@Test
	public void testquickSort2Timing() {
		// create an array and a sorted backup
		int[] array1 = QS.populate(10000, 10000);
		int[] array4 = QS.populate(100000, 100000);
		int[] array2 = QS.populate(1000000, 1000000);
		int[] array3 = QS.populate(10000000, 10000000);
	   	int[] array5 = QS.populate(100000000, 100000000);
	   
		long start = System.currentTimeMillis();
		QS.quickSort2(array1, 0, array1.length - 1);
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		System.out.println("quickSort2 time to sort 10000 elements in ms:"+ elapsed);
	
		start = System.currentTimeMillis();
		QS.quickSort2(array4, 0, array4.length - 1);
		end = System.currentTimeMillis();
		elapsed = end - start;
		System.out.println("quickSort2 time to sort 100000 elements in ms:"+ elapsed);
		
		start = System.currentTimeMillis();
		QS.quickSort2(array2, 0, array2.length - 1);
		end = System.currentTimeMillis();
		elapsed = end - start;
		System.out.println("quickSort2 time to sort 1000000 elements in ms:"+ elapsed);
	   
		start = System.currentTimeMillis();
		QS.quickSort2(array3, 0, array3.length - 1);
		end = System.currentTimeMillis();
		elapsed = end - start;
		System.out.println("quickSort2 time to sort 100000000 elements in ms:"+ elapsed);
	   	
	   	start = System.currentTimeMillis();
	   	QS.quickSort2(array5, 0, array5.length - 1);
	   	end = System.currentTimeMillis();
	   	elapsed = end - start;
	   	System.out.println("quickSort2 time to sort 1000000000 elements in ms:"+ elapsed);
	   	System.out.println();
	} 
	
	/*
	 * Method to test the number of comparisons in sorting an already sorted array of 10 numbers.
	 * Number of comparisons should be 45
	 */
	@Test
	public void testgetPartitionCount() {
		int[] array1 = new int[10];
		
		for (int i = 0; i < 10; i++) {
			array1[i] = i*20;
		}
	   
		QS.quickSort1(array1, 0, array1.length - 1);
		System.out.println("comparisons in already sorted:"+ QS.getPartitionCount());
		long compare=QS.getPartitionCount();
		assertEquals(45, compare);
	}
	
	
	/*
	 * Method to test the number of comparisons in reverse sorted array of 10 numbers.
	 * Number of comparisons should be 45.
	 */	
	@Test
	public void testgetPartitionCountA() {
		int[] array1 = new int[10];
	   
		for (int i = 0; i < 10; i++) {
			array1[i] = (100-i);
		}
	   
		QS.quickSort1(array1, 0, array1.length - 1);
		System.out.println("comparisons in reverse sorted:"+ QS.getPartitionCount());
		long compare=QS.getPartitionCount();
		assertEquals(45, compare);
	} // getPartCount()
	
	/*
	 * Testing speed of both algorithms on sorted arrays
	 */
	@Test
	public void testSpeedsOnSortedArrays() {
	    int[] array1 = QS.populate(10000, 10000);
		// sort using Java's built-in sorting method
	    Arrays.sort(array1);
	      
	    // Copies for the speed test
	    int[] array2 = Quicksort.copyArray(array1);
		int[] array3 = Quicksort.copyArray(array1);
		
	    // run quickSort1()
  		long start = System.currentTimeMillis();
	    QS.quickSort1(array1, 0, array1.length - 1);
	   	long end = System.currentTimeMillis();
	   	long elapsed = end - start;
	   	System.out.println("quickSort1 time to sort sorted array in ms:"+ elapsed);
	   	assertArrayEquals(array1,array3);
	     
	   	// run quickSort2()
	   	start = System.currentTimeMillis();
	   	QS.quickSort2(array2, 0, array2.length - 1);
		end = System.currentTimeMillis();
		elapsed = end - start;
		System.out.println("quickSort2 time to sort sorted array in ms:"+ elapsed);
		assertArrayEquals(array2,array3);
		System.out.println();
	}
		
	/* 
	 * Comparing partitions between algorithms for n = 10000, 100000, 1000000, and 10000000 arrays
	 */
	@Test
	public void testComparePartitionCounts() {
		Quicksort qs1 = new Quicksort();
		Quicksort qs2 = new Quicksort();
		
		int[] array1 = QS.populate(10000, 10000);
		int[] array2 = QS.populate(100000, 100000);
		int[] array3 = QS.populate(1000000, 1000000);
		int[] array4 = QS.populate(10000000, 10000000);
		int[] array9 = QS.populate(100000000, 100000000);

		
		int[] array5 = Quicksort.copyArray(array1);
		int[] array6 = Quicksort.copyArray(array2);
		int[] array7 = Quicksort.copyArray(array3);
		int[] array8 = Quicksort.copyArray(array4);
		int[] array10 = Quicksort.copyArray(array9);
	   
		// Testing quickSort1()
		qs1.quickSort1(array1, 0, array1.length - 1);
		System.out.println("quickSort1 partition count: "+ qs1.getPartitionCount());
		qs1.resetPartitionCount();
		qs1.quickSort1(array2, 0, array2.length - 1);
		System.out.println("quickSort1 partition count: "+ qs1.getPartitionCount());
		qs1.resetPartitionCount();
		qs1.quickSort1(array3, 0, array3.length - 1);
		System.out.println("quickSort1 partition count: "+ qs1.getPartitionCount());
		qs1.resetPartitionCount();
		qs1.quickSort1(array4, 0, array4.length - 1);
		System.out.println("quickSort1 partition count: "+ qs1.getPartitionCount());
		qs1.resetPartitionCount();
		qs1.quickSort1(array9, 0, array9.length - 1);
		System.out.println("quickSort1 partition count: "+ qs1.getPartitionCount());
		
		// Testing quickSort2()
		qs2.quickSort2(array5, 0, array5.length - 1);
		System.out.println("quickSort2 partition count: "+ qs2.getPartitionCount());
		qs2.resetPartitionCount();
		qs2.quickSort2(array6, 0, array6.length - 1);
		System.out.println("quickSort2 partition count: "+ qs2.getPartitionCount());
		qs2.resetPartitionCount();
		qs2.quickSort2(array7, 0, array7.length - 1);
		System.out.println("quickSort2 partition count: "+ qs2.getPartitionCount());
		qs2.resetPartitionCount();
		qs2.quickSort2(array8, 0, array8.length - 1);
		System.out.println("quickSort2 partition count: "+ qs2.getPartitionCount());
		qs2.resetPartitionCount();
		qs2.quickSort2(array10, 0, array10.length - 1);
		System.out.println("quickSort2 partition count: "+ qs2.getPartitionCount());
		System.out.println();
	} 

} // class QuicksortTest
