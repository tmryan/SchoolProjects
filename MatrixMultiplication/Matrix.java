package sjsu.ryan.cs146.project1.part1;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Matrix model containing a reference to a double[][] matrix and its size. Includes methods for
 * O(n^3) matrix multiplication and Strassen multiplication as well as methods for addition and
 * subtraction of matrices.
 * 
 * @author Thomas Ryan
 *
 */
public class Matrix {
	private double[][] matrix;
	int size;
	
	/////////////////
	// Constructors
	//////////////
	
	public Matrix() {
		matrix = new double[2][2];
		size = 2;
	}
	
	public Matrix(double[][] matrix) {
		this.matrix = matrix;
		size = matrix[0].length;
	}
	
	public Matrix(int size) {
		matrix = new double[size][size];
		this.size = size;
	}

	////////////
	// Methods
	/////////
	
	// Randomly generates double values up to given ceiling and populates this.matrix
	public void random(double ceiling) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j] = ThreadLocalRandom.current().nextDouble(0, ceiling);
			}
		}
	}
	
	public double[][] getMatrix() {
		return matrix; 
	}
	
	public int getSize() {
		return this.size;
	}
	
	/////////////
	// Strassen
	//////////
	
	// Strassen interface method
	public Matrix strassen(Matrix b) {
		return new Matrix(strassenWorkHorse(this.matrix, b.matrix));		
	}
	
	// Worker method for Strassen algorithm
	public double[][] strassenWorkHorse(double[][] a, double[][] b) {
		int n = a.length;
		
		// If we have a n <= 4 square matrix, use the O(n^3) algorithm
		if(n <= 4) {
			return multiplyWorkHorse(a, b);
		} else {
			// find A11, B11, A12, B12, ..., A22, B22
			double[][] A11 = partitionM(a, 0, (n/2)-1, 0, (n/2)-1);
			double[][] A12 = partitionM(a, 0, (n/2)-1, ((n/2)-1)+1, n-1);
			double[][] A21 = partitionM(a, ((n/2)-1)+1, n-1, 0, (n/2)-1);
			double[][] A22 = partitionM(a, ((n/2)-1)+1, n-1, ((n/2)-1)+1, n-1);
			double[][] B11 = partitionM(b, 0, (n/2)-1, 0, (n/2)-1);
			double[][] B12 = partitionM(b, 0, (n/2)-1, ((n/2)-1)+1, n-1);
			double[][] B21 = partitionM(b, ((n/2)-1)+1, n-1, 0, (n/2)-1);
			double[][] B22 = partitionM(b, ((n/2)-1)+1, n-1, ((n/2)-1)+1, n-1);

			// Find P1, P2, ..., P7
			double[][] P1 = strassenWorkHorse(add(A11, A22), add(B11, B22));
			double[][] P2 = strassenWorkHorse(add(A21, A22), B11);
			double[][] P3 = strassenWorkHorse(A11, subtract(B12, B22));
			double[][] P4 = strassenWorkHorse(A22, subtract(B21, B11));
			double[][] P5 = strassenWorkHorse(add(A11, A12), B22);
			double[][] P6 = strassenWorkHorse(subtract(A21, A11), add(B11, B12));
			double[][] P7 = strassenWorkHorse(subtract(A12, A22), add(B21, B22));

			// Find C11, C12, C21, C22
			double[][] C11 = subtract(add(add(P1, P4), P7), P5);
			double[][] C12 = add(P3, P5);
			double[][] C21 = add(P2, P4);
			double[][] C22 = subtract(add(add(P1, P3), P6), P2);
			
			// Combine Cij's to get result
			return combine(C11, C12, C21, C22);
		}
	}
	
	// Partitions given portion of given matrix into new matrix
	public double[][] partitionM(double[][] a, int rowStart, int rowEnd, int colStart, int colEnd) {
		int n = a.length/2;
		double[][] resultC = new double[n][n];
				
		for(int i = rowStart; i <= rowEnd; i++) {
			for(int j = colStart; j <= colEnd; j++) {
				resultC[i-rowStart][j-colStart] = a[i][j];
			}
		}
		
		return resultC;
	}
	
	// Combines Cij steps from Strassen algorithm
	public double[][] combine(double[][] a11, double[][] a12, double[][] a21, double[][] a22) {
		int n = a11.length * 2;
		double[][] resultC = new double[n][n];
			
		for(int i = 0; i < n/2; i++) {
			for(int j = 0; j < n/2; j++) {
				resultC[i][j] = a11[i][j];
				resultC[i][j+(n/2)] = a12[i][j];
				resultC[i+(n/2)][j] = a21[i][j];
				resultC[i+(n/2)][j+(n/2)] = a22[i][j];
			}
		}
		
		return resultC;
	}
	
	///////////////
	// Matrix Ops
	////////////
	
	public static double[][] add(double[][] a, double[][] b) {
		double[][] resultC = new double[a.length][a.length];
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a.length; j++) {
				resultC[i][j] = a[i][j] + b[i][j];
			}
		}

		return resultC;
	}
	
	public static double[][] subtract(double[][] a, double[][] b) {
		double[][] resultC = new double[a.length][a.length];
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a.length; j++) {
				resultC[i][j] = a[i][j] - b[i][j];
			}
		}

		return resultC;
	}
	
	// Multiplication interface method
	public Matrix multiply(Matrix b) {
		return new Matrix(multiplyWorkHorse(this.matrix, b.matrix));
	}
	
	// Worker method for O(n^3) matrix multiplication
	public double[][] multiplyWorkHorse(double[][] a, double[][] b) {
		int n = a.length;
		double[][] resultC = new double[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				for(int k = 0; k < n; k++) {
					resultC[i][j] += a[i][k] * b[k][j];			
				}
			}
		}
		
		return resultC;
	}
	
	//////////////
	// toSTring()
	///////////

	@Override
	public String toString() {
		StringBuilder matrixStr = new StringBuilder();
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrixStr.append(matrix[i][j]).append(" ");
			}
			matrixStr.append("\n");
		}
		
		return matrixStr.toString();
	}
}
