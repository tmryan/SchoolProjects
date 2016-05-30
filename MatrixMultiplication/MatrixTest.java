package sjsu.ryan.cs146.project1.part1;


import static org.junit.Assert.assertArrayEquals;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/*
   Class: CS146-01
   Semester: Spring 2016
   Project: #1 - Matrix Multiplication
   sample JUnit tests for checking and comparing the result of
   regular O(n^3) matrix multiplication algorithm with Strassen
   multiplication algorithm.	 
 */

/**
 * The main JUnit Test class to test regular multiplication 
 * and Strassen multiplication method.
 * 
 */
public class MatrixTest extends TestCase {

	private Matrix A, B; //input matrices
	private Matrix productRegularResult, productStrassenResult; // Matrices for storing the results
	private int N; // size of the NXN matrix
	
	// Test matrices
	private Matrix A4;
	private Matrix B4;
	private Matrix A16;
	private Matrix B16;
	private Matrix A512;
	private Matrix B512;
	private Matrix A1024;
	private Matrix B1024;
	
	public MatrixTest() {
		A4 = new Matrix(4);
		B4 = new Matrix(4);
		A16 = new Matrix(16);
		B16 = new Matrix(16);
		A512 = new Matrix(512);
		B512 = new Matrix(512);
		A1024 = new Matrix(1024);
		B1024 = new Matrix(1024);
	
	    // Generating random matrices beforehand for speed tests
		A4.random(5000);
		B4.random(5000);
		A16.random(5000);
		B16.random(5000);
		A512.random(5000);
		B512.random(5000);
		A1024.random(5000);
		B1024.random(5000);
	}
	
	@Before
	public void setUp() throws Exception
	{
	   N = 4; // size of the matrix
	   double[][] array1 = new double[N][N];
	   double[][] array2 = new double[N][N];
	   A = new Matrix(array1);
	   B = new Matrix(array2);
	   productRegularResult = new Matrix(N);
	   productStrassenResult = new Matrix(N);
	} // setUp()

	
	/* compare result matrices of regular multiplication method and Strassen multiplication method:
	 */
	@Test
	public void testProductCompare() {
	    // run user defined random() method to generate the matrices
		A.random(50);
	    B.random(50);
	           
	    // run multiply()
	    productRegularResult = A.multiply(B);
	     
	    // run strassen()
		productStrassenResult = A.strassen(B);
		 
	    for (int i = 0; i < N; i++) {
	    	assertArrayEquals(productRegularResult.getMatrix()[i], productStrassenResult.getMatrix()[i], 0.0001);
		}	    
	}
	
	/* multiplying a 2D array using the regular method:
	 */
	@Test
	public void testProductRegular() {
	    //expected output
		double[][] expected = {{96.0,94.0,81.0,128.0},{144.0,117.0,112.0,162.0},{132.0,112.0,101.0,152.0},{112.0,86.0,87.0,130.0}};
	    
		// input 2D arrain
		double[][] array1 = {{2.0,4.0,5.0,7.0},{6.0,7.0,2.0,8.0},{4.0,6.0,3.0,9.0},{8.0,4.0,1.0,5.0}};
		double[][] array2 = {{6.0,4.0,5.0,8.0},{8.0,7.0,8.0,8.0},{2.0,6.0,5.0,9.0},{6.0,4.0,2.0,5.0}}; 		
	    
		Matrix m1 = new Matrix(array1);
		Matrix m2 = new Matrix(array2);
	      
	    // run multiply()
		productRegularResult = m1.multiply(m2);
	     
	    for (int i = 0; i < N; i++) {
			assertArrayEquals(expected[i],productRegularResult.getMatrix()[i], 0.0);
		}
	}
	
	/* multiplying a 2D array using the Strassen method:
	 */
	@Test
	public void testProductStrassen() {
	    //expected output
		double[][] expected = {{96.0,94.0,81.0,128.0},{144.0,117.0,112.0,162.0},{132.0,112.0,101.0,152.0},{112.0,86.0,87.0,130.0}};
	    
		// input 2D array
		double[][] array1 = {{2.0,4.0,5.0,7.0},{6.0,7.0,2.0,8.0},{4.0,6.0,3.0,9.0},{8.0,4.0,1.0,5.0}};
		double[][] array2 = {{6.0,4.0,5.0,8.0},{8.0,7.0,8.0,8.0},{2.0,6.0,5.0,9.0},{6.0,4.0,2.0,5.0}}; 		
	    
		Matrix m1 = new Matrix(array1);
		Matrix m2 = new Matrix(array2);
	      
	    // run strassen()
		productStrassenResult= m1.strassen(m2);
	     
	    for (int i = 0; i < N; i++) {
			assertArrayEquals(expected[i],productStrassenResult.getMatrix()[i], 0.0);
		}
	}
	
	/*
	 * Verifying functionality of Matrix.add()
	 */
	@Test
	public void testMatrixAddition() {
	    //expected output
		double[][] expected = {{8.0,8.0,10.0,15.0},{14.0,14.0,10.0,16.0},{6.0,12.0,8.0,18.0},{14.0,8.0,3.0,10.0}};
	    
		// input 2D array
		double[][] m1 = {{2.0,4.0,5.0,7.0},{6.0,7.0,2.0,8.0},{4.0,6.0,3.0,9.0},{8.0,4.0,1.0,5.0}};
		double[][] m2 = {{6.0,4.0,5.0,8.0},{8.0,7.0,8.0,8.0},{2.0,6.0,5.0,9.0},{6.0,4.0,2.0,5.0}}; 		
		
	    // run strassen()
		double[][] sum = Matrix.add(m1, m2);
	     
	    for (int i = 0; i < N; i++) {
			assertArrayEquals(expected[i],sum[i], 0.0);
		}
	}
	
	/*
	 * Verifying functionality of Matrix.subtract()
	 */
	@Test
	public void testMatrixSubtraction() {
	    //expected output
		double[][] expected = {{-4.0,0.0,0.0,-1.0},{-2.0,0.0,-6.0,0.0},{2.0,0.0,-2.0,0.0},{2.0,0.0,-1.0,0.0}};
	    
		// input 2D array
		double[][] m1 = {{2.0,4.0,5.0,7.0},{6.0,7.0,2.0,8.0},{4.0,6.0,3.0,9.0},{8.0,4.0,1.0,5.0}};
		double[][] m2 = {{6.0,4.0,5.0,8.0},{8.0,7.0,8.0,8.0},{2.0,6.0,5.0,9.0},{6.0,4.0,2.0,5.0}}; 		
		
	    // run strassen()
		double[][] difference = Matrix.subtract(m1, m2);
	     
	    for (int i = 0; i < N; i++) {
			assertArrayEquals(expected[i],difference[i], 0.0);
		}
	}
	
	/*
	 * Comparing speed and output from both algorithms on a pair of 4x4 matrices. Speed is printed to console.
	 */
	@Test
	public void testSpeedAndOutput4x4() {	
	    // run multiply()
		long startTime = System.currentTimeMillis();
	    productRegularResult = A4.multiply(B4);
		long stopTime = System.currentTimeMillis();
		System.out.println("4x4 O(n^3) time: " + (stopTime - startTime) + "ms");
		
	    // run strassen()
		startTime = System.currentTimeMillis();
		productStrassenResult = A4.strassen(B4);
		stopTime = System.currentTimeMillis();
		System.out.println("4x4 Strassen time: " + (stopTime - startTime) + "ms");
		
	    for (int i = 0; i < N; i++) {
	    	assertArrayEquals(productRegularResult.getMatrix()[i], productStrassenResult.getMatrix()[i], 0.0001);
	    }
	}
	
	/*
	 * Comparing speed and output from both algorithms on a pair of 16x16 matrices. Speed is printed to console.
	 */
	@Test
	public void testSpeedAndOutput16x16() {	
	    // run multiply()
		long startTime = System.currentTimeMillis();
	    productRegularResult = A16.multiply(B16);
		long stopTime = System.currentTimeMillis();
		System.out.println("16x16 O(n^3) time: " + (stopTime - startTime) + "ms");
		
	    // run strassen()
		startTime = System.currentTimeMillis();
		productStrassenResult = A16.strassen(B16);
		stopTime = System.currentTimeMillis();
		System.out.println("16x16 Strassen time: " + (stopTime - startTime) + "ms");
		
	    for (int i = 0; i < N; i++) {
	    	assertArrayEquals(productRegularResult.getMatrix()[i], productStrassenResult.getMatrix()[i], 0.0001);
	    }
	}
	
	/*
	 * Comparing speed and output from both algorithms on a pair of 512x512 matrices. Speed is printed to console.
	 */
	@Test
	public void testSpeedAndOutput512x512() {	
	    // run multiply()
		long startTime = System.currentTimeMillis();
	    productRegularResult = A512.multiply(B512);
		long stopTime = System.currentTimeMillis();
		System.out.println("512x512 O(n^3) time: " + (stopTime - startTime) + "ms");
		
	    // run strassen()
		startTime = System.currentTimeMillis();
		productStrassenResult = A512.strassen(B512);
		stopTime = System.currentTimeMillis();
		System.out.println("512x512 Strassen time: " + (stopTime - startTime) + "ms");
		
	    for (int i = 0; i < N; i++) {
	    	assertArrayEquals(productRegularResult.getMatrix()[i], productStrassenResult.getMatrix()[i], 0.001);
	    }
	}
	
	/*
	 * Comparing speed and output from both algorithms on a pair of 1024x1024 matrices. Speed is printed to console.
	 */
	@Test
	public void testSpeedAndOutput1024x1024() {	
	    // run multiply()
		long startTime = System.currentTimeMillis();
	    productRegularResult = A1024.multiply(B1024);
		long stopTime = System.currentTimeMillis();
		System.out.println("1024x1024 O(n^3) time: " + (stopTime - startTime) + "ms");
		
	    // run strassen()
		startTime = System.currentTimeMillis();
		productStrassenResult = A1024.strassen(B1024);
		stopTime = System.currentTimeMillis();
		System.out.println("1024x1024 Strassen time: " + (stopTime - startTime) + "ms");
		
	    for (int i = 0; i < N; i++) {
	    	assertArrayEquals(productRegularResult.getMatrix()[i], productStrassenResult.getMatrix()[i], 0.01);
	    }
	}

} // class MatrixTest