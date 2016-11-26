package Sorting;

/****************************************************************************
 * This demonstrates binary heap operations along with the heapSort. 
 * The code has been copied from the internet(the source can be seen below). 
 * We slightly modified this class so it works with our Edge class. 
 * Furthermore we implemented a new Method, so when given an array we can choose bounds 
 * 								within those it should be sorted. 
 *
 * This is from: http://www.cs.cmu.edu/~adamchik/15-121/lectures/Binary%20Heaps/code/Heap.java
 *****************************************************************************/

import mst.Edge;

public class Heap {

	private static int size; // Number of elements in heap
	private static Edge[] heap; // The heap array

	/**
	 * Initializing the edges.
	 * 
	 * @param array
	 * @param left
	 * @param right
	 */
	public static void init(Edge[] array, int left, int right) {

		size = right - left + 1;
		heap = new Edge[size + 1];

		System.arraycopy(array, left, heap, 1, size);// we do not use 0 index

		buildHeap();
	}

	/**
	 * runs at O(size)
	 */
	private static void buildHeap() {
		for (int k = size / 2; k > 0; k--) {
			percolatingDown(k);
		}
	}

	private static void percolatingDown(int k) {
		Edge tmp = heap[k];
		int child;

		for (; 2 * k <= size; k = child) {
			child = 2 * k;

			if (child != size && heap[child].compareTo(heap[child + 1]) > 0)
				child++;

			if (tmp.compareTo(heap[child]) > 0)
				heap[k] = heap[child];
			else
				break;
		}
		heap[k] = tmp;
	}

	/**
	 * Sorts a given array of items.
	 */
	public void heapSort(Edge[] array) {
		size = array.length;
		heap = new Edge[size + 1];
		System.arraycopy(array, 0, heap, 1, size);
		buildHeap();

		for (int i = size; i > 0; i--) {
			Edge tmp = heap[i]; // move top item to the end of the heap array
			heap[i] = heap[1];
			heap[1] = tmp;
			size--;
			percolatingDown(1);
		}
		for (int k = 0; k < heap.length - 1; k++)
			array[k] = heap[heap.length - 1 - k];
	}

	/**
	 * Deletes the top item
	 */
	public static Edge deleteMin() throws RuntimeException {
		if (size == 0)
			throw new RuntimeException();
		Edge min = heap[1];
		heap[1] = heap[size--];
		percolatingDown(1);
		return min;
	}

	public static boolean isEmpty() {

		if (size == 0) {
			return true;
		}

		return false;
	}

	public String toString() {
		String out = "";
		for (int k = 1; k <= size; k++)
			out += heap[k] + " ";
		return out;
	}

}