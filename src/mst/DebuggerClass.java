package mst;

import java.util.Random;
import java.util.Stack;

/**
 * *
 * 
 * @author Alex, Florian, Ramtin
 * 
 *         Created summer term 2015.
 * 
 */
public class DebuggerClass {

	public static Random random = new Random();

	/**
	 * Prints out a view of a list of edges.
	 * 
	 * @param edges
	 */
	public static void print(Edge[] edges, int left, int right) {
		for (int i = left; i <= right; i++) {
			if (edges[i] != null) {
				System.out.println(edges[i].toString());
			} else {
				System.out.println("NULL");
			}

		}
	}

	/**
	 * Tests whether partition has worked correctly on the edges relative to
	 * their weights.
	 * 
	 * @param edges
	 *            List of edges.
	 * @param p
	 *            pivot element which should divide the list of edges in a group
	 *            with edge weights less than p and edge weights larger than p.
	 * @return true, if partiton correct. False, otherwise.
	 */
	public static boolean partitionWorksCorrectly(int edges[], int left,
			int right, int p) {

		for (int i = left; i <= p; i++) {
			if (edges[i] > edges[p]) {

				return false;
			}
		}
		for (int i = p + 1; i <= right; i++) {

			if (edges[i] < edges[p]) {
				return false;
			}
		}

		return true;
	}

	public static int partition(int[] a, int lo, int hi) {
		int i = lo;
		int j = hi + 1;
		int v = a[lo];
		while (true) {

			// find item on lo to swap
			while (a[++i] < v)
				if (i == hi)
					break;

			// find item on hi to swap
			while (v < a[--j])
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
		return j;
	}

	/**
	 * Using Hoare's partition method to partition an array of Edges into two
	 * sublists with edges with weights less than the edge weight of pivotIndex
	 * and edges with weight greater than this. This source code is taken from:
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
	public static int partitionHoare(int[] a, int lo, int hi, double pivotValue) {
		int i = lo;
		int j = hi + 1;
		// swap(a, pivotIndex, lo);
		// Edge v = a[lo];
		while (true) {

			// find item on lo to swap
			while (a[i] < pivotValue) {
				i++;
				if (i == lo)
					break;
			}

			System.out.println("Left: " + i);
			// find item on hi to swap
			while (pivotValue < a[--j])
				if (j == lo)
					break; // redundant since a[lo] acts as sentinel

			System.out.println("RIGHT:" + j);
			// check if pointers cross
			if (i >= j)
				break;

			swap(a, i, j);
			System.out.print("SWAP: \t");
			for (int k = 0; k < a.length; k++) {
				System.out.print(a[k] + "\t");
			}
			System.out.println();
		}

		// put partitioning item v at a[j]
		// swap(a, lo, j);

		// now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
		return j + 1;
	}

	
	
	public static int partitionHoare(int[] a, int lo, int hi, int pivotIndex) {
		int i = lo;
		int j = hi + 1;
		swap(a, pivotIndex, lo);
		int v = a[lo];
		while (true) {

			// find item on lo to swap
			while (a[++i] < v)
				if (i == lo)
					break;

			// find item on hi to swap
			while (v < a[--j])
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
		return j;
	}


	/**
	 * Returns a copy of the list.
	 * 
	 * @param list
	 * @param left
	 * @param right
	 * @return
	 */
	public static Edge[] copyList(Edge[] list, int left, int right) {

		Edge[] copiedList = new Edge[right - left + 1];

		for (int i = left; i <= right; i++) {
			copiedList[i - left] = list[i];
		}

		return copiedList;
	}

	

	public static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}




	

	public static void print(Stack<Integer> s) {

		for (int i = 0; i < s.size(); i++) {
			System.out.print(s.get(i) + "\t");
		}
		System.out.println();
	}

	public static boolean sorted(Edge[] edges) {

		for (int i = 0; i < edges.length - 1; i++) {

			if (edges[i].weight > edges[i + 1].weight) {

				return false;
			}
		}

		return true;
	}

	public static boolean sorted(double[] edges) {

		for (int i = 0; i < edges.length - 1; i++) {

			if (edges[i] > edges[i + 1]) {

				return false;
			}
		}

		return true;
	}
}