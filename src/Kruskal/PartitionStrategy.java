package Kruskal;

/**
 * 
 * This class provides two different partition strategies, namely Lomuto and Hoare.
 * Each of them be called by partitioning via a given index of an array or by a given weight of an edge.
 * 
 * Furthermore, there are two more functions, swap and randInt, which are often used for partitiong an array.
 * 
 * @author Ramtin
 */

import java.util.Random;

import mst.Edge;

public class PartitionStrategy {

	public static Random random = new Random();

	

	// ********************************************************************************
	// ********************************************************************************
	// ****** PARTITION VIA LOMUTO**********************
	// ********************************************************************************
	// ********************************************************************************
	/**
	 * Returns an index such that all edges on the left to the pivot have an
	 * edge weight smaller than the one of the pivot edge and all edges on the
	 * right to the pivot edge have an edge weight bigger or equal to the pivot.
	 * 
	 * Definition: An edge left/right to the pivot means, that the index of the
	 * edge in the array is smaller/bigger than the pivot.
	 * 
	 * 
	 * @param a
	 * @param left
	 * @param right
	 * @param pivot
	 * @return
	 */
	public static int partitionLomuto(Edge[] a, int left, int right, int pivot) {

		swap(a, pivot, right);
		double x = a[right].getWeight();
		int i = left - 1;
		for (int j = left; j < right; j++) {
			if (a[j].getWeight() <= x) {
				i++;
				swap(a, i, j);
			}
		}

		swap(a, i + 1, right);
		return i + 1;
	}

	/**
	 * Returns an index i such that all edges on the left of i have an edge
	 * weight smaller than pivotValue and all edges on the right to i have an
	 * edge weight bigger or equal to the pivotValue.
	 * 
	 * Partition is done by the Lomuto method.
	 * 
	 * 
	 * @param a
	 * @param left
	 * @param right
	 * @param pivotValue
	 * @return
	 */
	public static int partitionLomuto(Edge[] a, int left, int right,
			double pivotValue) {

		// swap(a, pivotValue, right);
		// double x = a[right].getWeight();
		int i = left - 1;
		int pivotIndex = left;
		for (int j = left; j <= right; j++) {
			if (a[j].getWeight() < pivotValue) {
				i++;
				swap(a, i, j);
			} else if (a[j].getWeight() == pivotValue) {
				i++;
				swap(a, i, j);
				pivotIndex = i;
			}
		}

		swap(a, i, pivotIndex);
		return i;
	}

	
	// ********************************************************************************
	// ********************************************************************************
	// ****** PARTITION VIA HOARE
	// ********************************************************************************
	// ********************************************************************************
	/**
	 * Using Hoare's partition to return an index for which the array is
	 * subdived into two sublists containing edges with weights less or equal
	 * than the pivotValue and edges with weight greater or equal than the
	 * pivotValue. Note: The index which is being returned does not necessairly
	 * contain the edge with weight equal to the pivotvalue.
	 * 
	 * This source code is a slight modification of this:
	 * http://algs4.cs.princeton.edu/23quicksort/Quick.java.html
	 * 
	 * @param a
	 *            the array
	 * @param lo
	 *            Lower bound of the array until which the partition should be
	 *            included.
	 * @param hi
	 *            Higher bound of the array until which the partition should be
	 *            included.
	 * @param pivotIndex
	 *            The index of the edge on which the partition should be
	 *            started.
	 * @return The new index after partitioning the array with respect to the
	 *         edge at pivoIndex.
	 */
	public static int partitionHoare(Edge[] a, int lo, int hi, double pivotValue) {
		int i = lo;
		int j = hi + 1;
		// swap(a, pivotIndex, lo);
		// Edge v = a[lo];
		while (true) {

			// find item on lo to swap
			while (a[i].weight < pivotValue) {
				i++;
				if (i == lo)
					break;
			}

			// find item on hi to swap
			while (pivotValue < a[--j].weight)
				if (j == lo)
					break; // redundant since a[lo] acts as sentinel

			// check if pointers cross
			if (i >= j)
				break;

			swap(a, i, j);

		}

		// put partitioning item v at a[j]
		// swap(a, lo, j);

		// now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
		return j + 1;
	}

	public static int partitionHoare(Edge[] a, int lo, int hi, int pivotIndex) {
		int i = lo;
		int j = hi + 1;
		swap(a, pivotIndex, lo);
		Edge v = a[lo];
		while (true) {

			// find item on lo to swap
			while (a[++i].weight < v.weight)
				if (i == lo)
					break;

			// find item on hi to swap
			while (v.weight < a[--j].weight)
				if (j == lo)
					break; // redundant since a[lo] acts as sentinel

			// check if pointers cross
			if (i >= j)
				break;

			swap(a, i, j);
		}

		// put partitioning item v at a[j]
		swap(a, lo, j);

		// now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
		return j + 1;
	}

	// ********************************************************************************
	// ********************************************************************************
	// ****** Some helper functins:
	// 1. Swap: swaps two objects in an given edge array.
	// 2. randInt: picks a random Integer within the given bounds.
	// ********************************************************************************
	// ********************************************************************************
	public static void swap(Edge[] a, int i, int j) {
		Edge temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * Returns a random number between min and max, inclusive. The difference
	 * between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// nextInt is exclusive of the top value, so add 1 to make it inclusive.
		int randomNum = random.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
