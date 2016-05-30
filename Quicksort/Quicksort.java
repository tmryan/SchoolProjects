package sjsu.ryan.cs146.project1.part2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class containing Quicksort and supporting methods. 
 * 
 * @author Thomas Ryan
 *
 */
public class Quicksort {
	private long partitionCount;
	
	////////////////
	// Constructor
	/////////////
	
	public Quicksort() {
		partitionCount = 0;
	}
	
	///////////////
	//Quicksort 1
	////////////
	
	// QuickSort implementation 1 - last element pivot
	public void quickSort1(int[] list, int p, int r) {
		if(p < r) {
			int q = partition(list, p, r);
			quickSort1(list, p, q-1);
			quickSort1(list, q+1, r);
		}
	}
	
	// Partition with last element as pivot
	public int partition(int[] list, int p, int r) {
		int i = p-1; 
		
		for(int j = p; j < r ; j++) {
			partitionCount++;
			if(list[j] <= list[r]) {
				i++;
				swap(list, i, j);
			}
		}
		
		// Swapping pivot with lowest higher element
		swap(list, i+1, r);
		return i+1;
	}

	///////////////
	//Quicksort 2
	////////////
	
	// QuickSort implementation 2 - median pivot
	public void quickSort2(int[] list, int p, int r) {
		if(p < r) {
			int q = partitionMedian(list, p, r);
			quickSort1(list, p, q-1);
			quickSort1(list, q+1, r);
		}
	}
	
	// Partition with median element as pivot
	public int partitionMedian(int[] list, int p, int r) {
		int i = p-1; 

		// Finding median element for pivot
		int q = select(list, p, r, (r-p+1)/2);
		swap(list, q, r);
		
		for(int j = p; j < r ; j++) {
			partitionCount++;
			if(list[j] <= list[r]) {
				i++;
				swap(list, i, j);
			}
		}
		
		// Swapping pivot with lowest higher element
		swap(list, i+1, r);
		return i+1;
	}

	////////////
	// Methods
	/////////
	
	// Populate an array with n random values up to ceiling
	public int[] populate(int n, int ceiling) {
		int[] list = new int[n];
		
		for(int i = 0; i < n; i++) {
			list[i] = ThreadLocalRandom.current().nextInt(ceiling);
		}
		
		return list;
	}
	
	// Swap
	public void swap(int[] list, int i ,int j) {
		int temp = list[i];
		list[i] = list[j];
		list[j] = temp;
		
		// done gone and swapped 'em
	}
	
	public long getPartitionCount() {
		return partitionCount;
	}
	
	public void resetPartitionCount() {
		partitionCount = 0;
	}
	
	public static int[] copyArray(int[] list) {
		int[] copyList = new int[list.length];
		
		for(int i = 0; i < list.length; i++) {
			copyList[i] = list[i];
		}
		
		return copyList;
	}
	
	//////////////////////
	// Median of Medians
	///////////////////
	
	// I believe this is incorrect
	public int medianOfMedians(int[] list, int p, int r, int k) {
		int q = 0;
				
		if(r-p+1 <= 10) {
			quickSort1(list, p, (r-p+1)/2);
			q = k;
		} else {
			for(int i = p; i < (r-p+1)/5; i++) {
				if(i*5+4 < r) { 
					insertionSort(list, i*5, 3);
					swap(list, i*5, (i*5)+2);
				} else {
					insertionSort(list, i*5, ((r-p)%5)/2);
					swap(list, i*5, (i*5)+2);
				}
			}
			
			// select on medians to get pivot
			q = select(list, p, (r-p+1)/5, ((r-p+1)/10));
		}
				
		return q;
	}
	
	//////////////////////
	// Randomized-Select
	///////////////////
	
	public int select(int[] list, int p, int r, int i) {
		int q = 0;
		int k = 0;
		
		if(p == r) {
			return p;
		}
		
		q = randomizedPatition(list, p, r);
		k = q-p+1;
		
		if(i == k) {
			return q;
		} else if(i < k) {
			return select(list, p, q-1, i);
		} else {
			return select(list, q+1, r, i-k);
		}
	}
	
	public int randomizedPatition(int[] list, int p, int r) {				
		int i = p-1; 
		
	    int q = ThreadLocalRandom.current().nextInt(p, r);
		swap(list, q, r);
	    
		for(int j = p; j < r ; j++) {
			if(list[j] <= list[r]) {
				i++;
				swap(list, i, j);
			}
		}
		
		// Swapping pivot with lowest higher element
		swap(list, i+1, r);
		return i+1;
	}
	
	////////////////
	// Other Sorts
	/////////////
	
	// Insertion sort from p to k over list
	public void insertionSort(int[] list, int p, int k) {
		int j = 0;
		int temp = 0;
		
		for(int i = p; i < p+k; i++) {
			temp = list[i];
			j = i;
				
			while(j > 0 && list[j-1] > temp) {
				list[j] = list[j-1];
				j--;
			}
			list[j] = temp;
		}
	}
	
	// Selection sort from p to k over list
	public void selectionSort(int[] list, int p, int k) {
		for(int i = p; i < p+k; i++) {
			for(int j = i; j < p+k; j++) {
				if(list[j] < list[i]) {
					swap(list, i, j);
				}
			}
		}
	}
}
