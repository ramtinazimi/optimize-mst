package Kruskal;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import Sorting.DualPivotQuicksort;
import Sorting.Heap;
import Sorting.IncrementalQuickSort;
import mst.Graph;
import mst.Edge;

/**
 * @author Ramtin Azimi Date: Created 2015.
 * 
 *         In the following, one will see the algorithms which are explained in
 *         the paper "The Filter-Kruskal Minimum Spanning Tree Algorithm" from
 *         Osipov et al. This is an iterative implementation of those in the
 *         paper explained algorithms. This implementation should work for all
 *         sorts of graphs.
 * 
 *         An implementation of various algorithms which improve on the standard
 *         Kruskal algorithm. There are four algorithms in this class: 1. The
 *         standard Kruskal: Time: O(|E| log|E|). 2. The QKruskal. 3. The
 *         FilterKruskal. Time: O(|V| + |E|log|E| log \frac{|V|}{|E|})4. The
 *         FilterKruskalPlus. Theoretically better, but is practially worse than
 *         FilterKruskal.
 * 
 *         QKruskal improves on the standard Kruskal by not sorting all the
 *         edges at once, but by partitionining the list recursively in lighter
 *         and heavier edges. Then we use the standard Kruskal on the lighter
 *         part. If we have a mst, we stop, else we recurse on the heavier part.
 * 
 *         FilterKrusal improves on QKruskal in the following way: Before going
 *         to the heavier part, we filter out edges which will definetly not be
 *         in the mst because they create a cycle.
 * 
 *         FilterKruskalPlus improves theoretically on FilterKruskal by adding
 *         those edges between two vertices which have only one adjacent vertex.
 * 
 *         The class is dived into three parts: 1. The four above explained
 *         algorihtms in iterative style. 2. The three (QKruskal, FilterKruskal,
 *         FilterKruskal+) above explained algorihtms in recursion style. 3.
 *         Other subroutine functions used for three above explained algorithms.
 * 
 */

public class KruskalAlgo {

	// Has the current size of the mst.
	private int mstSize;

	// The edges of the minimal spanning tree (which was computed)
	private Edge[] mst;

	// Number of nodes in the given Graph.
	private int numberOfNodes;

	// The edges of the original graph which is given to Kruskal.
	private Edge[] edges;

	// This array containes for each node the number of adjacent nodes it has.
	// This is useful for FilterKruskalPlus.
	private int[] vertexDegree;

	// This is the UnionFind data structure. This is needed for Kruskal.
	private UnionFindWeighted uf;

	public KruskalAlgo(Graph graph) {
		this.edges = graph.edges.clone();
		this.numberOfNodes = graph.getNumOfNodes();
		mstSize = 0;
		uf = new UnionFindWeighted(numberOfNodes);
		mst = new Edge[numberOfNodes - 1];

	}

	// ********************************************************************************
	// ********************************************************************************
	// The following three functions are
	// 1. standardKruskal
	// 2. qKruskal
	// 3. filterKruskal
	// 4. filterKruskalPlus
	//
	// Each of the functions follows the same goal: Mainly to construct an mst
	// by the given mst. The difference lies in the way they do it. Though the
	// last three mentioned algorithms have similar ideas with some more
	// extensions for proficiency pruposes.
	// The first function is the standardKruskal which is known from the basic
	// Algorithm and Data Structures class.
	//
	// Each of those functions uses a subroutine called BoundedKruskal which
	// applies the known kruskal only on a subset of edges, which is specified
	// by the user via a left and a right index.
	//
	// There are four different BoundedKruskal-functions to choose from (we come
	// back to point later). But each of those functions stands for a different
	// sorting method used for Kruskal.
	// ********************************************************************************
	// ********************************************************************************

	/**
	 * Calculating the mst via STANDARD KRUKSAL. Running time: O(m log m), where as
	 * m:= Number of edges.
	 * 
	 * @return Graph ArrayGraph is the MST of the given Graph. MST Edges are
	 *         saved in ArrayGraph.edges.
	 */
	public Graph standardKruskal() {

		boundedKruskalDPQS(edges, 0, edges.length - 1);

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException("Kein zusammenh�ngender Graph");
		}
		return new Graph(mst, numberOfNodes);
		
		

	}

	/**
	 * Determines the mst via QKRUSKAL for the given Graph.
	 * 
	 * @return MST
	 */
	public Graph qKruskal() {

		Stack<Integer> s = new Stack<Integer>();

		s.push(edges.length - 1);
		s.push(0);

		// This is the index/position of the pivot edge before partitioning with
		// respect to the pivot.
		int oldPivot;
		
		// This is the index/position of the pivot edge after partitioning with
		// respect to the pivot.
		int newPivot;
		int first;
		int second;
		while (mstSize < numberOfNodes - 1 && !s.empty()) {

			
			first = s.pop();
			second = s.peek();
			if (second - first + 1 <= numberOfNodes) {

				// System.out.println("KRUSKAL ON:"+ second +"-"+ first);

				boundedKruskalDPQS(edges, first, second);
				s.pop();

			} else {

				oldPivot = PivotStrategy.randomPivot(first, second);
				newPivot = PartitionStrategy.partitionHoare(edges, first, second,
						oldPivot);


				s.push(newPivot);
				s.push(newPivot - 1);
				s.push(first);

			}
		}

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}

		
		return new Graph(mst, numberOfNodes);
	}

	/**
	 * Determines the mst via FILTERKRUSKAL for the given Graph.
	 * 
	 * @return MST
	 */
	public Graph filterKruskal() {
		
		Stack<Integer> s = new Stack<Integer>();

		s.push(edges.length - 1);
		s.push(0);

		// This is the index/position of the pivot edge before partitioning with
		// respect to the pivot.
		int oldPivot;
		// This is the index/position of the pivot edge after partitioning with
		// respect to the pivot.
		int newPivot;
		int first;
		int second;
		while (mstSize < numberOfNodes - 1 && !s.empty()) {

			// System.out.println(firstElementOnStack+" "+secondElementOnStack);
			first = s.pop();
			second = s.peek();
			if (second - first + 1 <= numberOfNodes) {

				second = s.pop();
				boundedKruskalDPQS(edges, first, second);

				// We get the indeces of the next partition with heavier edges.
				// The stack could be empty at this point, if we have already
				// reached the index m-1 in the stack.
				if (!s.empty()) {
					first = s.pop();
					second = s.peek();

					first = filter(edges, first, second);

					if (first > second) {
						if (!s.empty()) {
							s.pop();
						}

					} else {
						s.push(first);
					}
				}

			} else {
				oldPivot = PivotStrategy.randomPivot(first, second);

				newPivot = PartitionStrategy.partitionHoare(edges, first, second,
						oldPivot);

				s.push(newPivot);
				s.push(newPivot - 1);
				s.push(first);
				
			}
		}

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}

		return new Graph(mst, numberOfNodes);
	}

	/**
	 * Determines the mst via FILTERKRUSKALPLUS for the given Graph.
	 * 
	 * @return MST
	 */
	public Graph filterKruskalPlus() {
		// System.out.println("Running FilterKruskal...");

		// Calculating the number of adjacent nodes for each node.
		setVertexDegrees();
		
		Stack<Integer> s = new Stack<Integer>();
		s.push(edges.length - 1);
		s.push(0);

		// This is the index/position of the pivot edge before partitioning with
		// respect to the pivot.
		int oldPivot;
		// This is the index/position of the pivot edge after partitioning with
		// respect to the pivot.
		int newPivot;

		int first;
		int second;
		while (mstSize < numberOfNodes - 1 && !s.empty()) {

			first = s.pop();
			second = s.peek();
			if (second - first + 1 <= numberOfNodes) {

				second = s.pop();
				// System.out.println("KRUSKAL ON: " + first + " "
				// + second);
				boundedKruskalDPQS(edges, first, second);

				// We get the indeces of the next partition with heavier edges.
				// The stack could be empty at this point, if we have already
				// reached the index m-1 in the stack.
				if (!s.empty()) {
					first = s.pop();
					second = s.peek();

					first = filterPlus(edges, first, second);

					if (first > second) {
						if (!s.empty()) {
							s.pop();
						}

					} else {
						s.push(first);
					}
				}

			} else {
				oldPivot = PivotStrategy.randomPivot(first, second);

				newPivot = PartitionStrategy.partitionHoare(edges, first, second,
						oldPivot);

				s.push(newPivot);
				s.push(newPivot - 1);
				s.push(first);
				// System.out.println(first + "-" + (newPivot-1)+
				// "-"+newPivot+"-"+second);
			}
		}

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}

		return new Graph(mst, numberOfNodes);
	}

	// ********************************************************************************
	// ********************************************************************************
	// ****KRUSKAL BOUNDED WITH DIFFERENT SORTING METHODS*********************
	// Provided Sorting methods in order of appearance:
	// 1. Sorting via the sorting algorithm provided by the Java Library (JS).
	// 2. Sorting via the DualPivotQuickSort-Algorithm (DPQS).
	// 3. Sorting via a Heap structure (BH).
	// 4. Sorting via the IncrementalQuickSort-Algorithm (IQS).
	// ********************************************************************************
	// ********************************************************************************

	/**
	 * Standard Kruskal is applied to the edges who are within the bounds of
	 * left and right(exclusive) of the list. Sorting is done via the method
	 * provided in Java.
	 * 
	 * Running time: O(n log n), where as n:= right-left+1.
	 * 
	 * @param edges
	 *            Edge list which Kruskal should be applied to.
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 * 
	 */
	private void boundedKruskalJS(Edge[] edges, int left, int right) {

		// Sort edges with respect to their weights in ascending order.

		// Sorting via Java Library.
		Arrays.sort(edges, left, right + 1);

		for (int i = left; i <= right; i++) {

			// Test, whether vertices of edge i are in different components.
			if (uf.find(edges[i].getA()) != uf.find(edges[i].getB())) {
				// Add edge i to the minimal spanning tree because no cycle is
				// caused.

				mst[mstSize] = edges[i];
				mstSize++;
				uf.union(edges[i].a, edges[i].b);
			}

			// We can stop whenever the mst has the size of the numberOfNodes-1.
			if (mstSize == numberOfNodes - 1) {
				return;
			}
		}
	}

	/**
	 * Standard Kruskal is applied to the edges who are within the bounds of
	 * left and right(exclusive) of the list. Sorting is done via
	 * DualPivotQuickSort.
	 * 
	 * Running time: O(n log n), where as n:= right-left+1.
	 * 
	 * @param edges
	 *            Edge list which Kruskal should be applied to.
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 * 
	 */
	private void boundedKruskalDPQS(Edge[] edges, int left, int right) {

		// Sort edges with respect to their weights in ascending order.

		// Sorting via DualPivotQuickSort
		DualPivotQuicksort.sort(edges, left, right + 1);

		for (int i = left; i <= right; i++) {

			// Test, whether vertices of edge i are in different components.
			if (uf.find(edges[i].getA()) != uf.find(edges[i].getB())) {
				// Add edge i to the minimal spanning tree because no cycle is
				// caused.

				mst[mstSize] = edges[i];
				mstSize++;
				uf.union(edges[i].a, edges[i].b);
			}

			// We can stop whenever the mst has the size of the numberOfNodes-1.
			if (mstSize == numberOfNodes - 1) {
				return;
			}
		}
	}

	/**
	 * Standard Kruskal is applied to the edges who are within the bounds of
	 * left and right(exclusive) of the list. Sorting is done via a Heap.
	 * 
	 * Running time: O(n log n), where as n:= right-left+1.
	 * 
	 * @param edges
	 *            Edge list which Kruskal should be applied to.
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 * 
	 */
	private void boundedKruskalBH(Edge[] edges, int left, int right) {

		Edge e;
		Heap.init(edges, left, right);

		while (!Heap.isEmpty() && mstSize < numberOfNodes - 1) {

			e = Heap.deleteMin();

			// Test, whether vertices of edge i are in different components.
			if (uf.find(e.getA()) != uf.find(e.getB())) {
				// Add edge i to the minimal spanning tree because no cycle is
				// caused.

				mst[mstSize] = e;
				mstSize++;
				uf.union(e.getA(), e.getB());
			}

		}

	}

	/**
	 * Standard Kruskal is applied to the edges who are within the bounds of
	 * left and right(exclusive) of the list. Sorting is done via
	 * IncrementalQuickSort.
	 * 
	 * Running time: O(n log n), where as n:= right-left+1.
	 * 
	 * @param edges
	 *            Edge list which Kruskal should be applied to.
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 * 
	 */
	private void boundedKruskalIQS(Edge[] edges, int left, int right) {

		Edge e;
		IncrementalQuickSort.init(edges, left, right);

		for (int i = left; i <= right; i++) {
			e = IncrementalQuickSort.getMin();
			// Test, whether vertices of edge i are in different components.
			if (uf.find(e.getA()) != uf.find(e.getB())) {
				// Add edge i to the minimal spanning tree because no cycle is
				// caused.

				mst[mstSize] = edges[i];
				mstSize++;
				uf.union(edges[i].a, edges[i].b);
			}

			// We can stop whenever the mst has the size of the numberOfNodes-1.
			if (mstSize == numberOfNodes - 1) {
				return;
			}
		}
	}

	
	/**
	 * 
	 * @param edges
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 * @return index Index from where on nothing is null anymore.
	 */
	private int filter(Edge[] edges, int left, int right) {

		// Approach:
		// Setting the ones null which need to be deleted --> moving all the
		// nulls to beginning of an array
		// Remembering the index to the first element which is not null. We
		// return this index.
		int j = left;

		for (int i = left; i <= right; i++) {
			if (uf.find(edges[i].getA()) == uf.find(edges[i].getB())) {

				edges[i] = null;
				PartitionStrategy.swap(edges, i, j);
				j++;
			}
		}

		return j;
	}

	/**
	 * Used for the algorithm FilterKruskalPlus algorithm. It deletes heavy
	 * edges whose vertices are in the same component.
	 * 
	 * @param edges
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 */
	private int filterPlus(Edge[] edges, int left, int right) {

		// Idea: setting the ones null which need to be deleted --> moving all
		// the nulls to beginning of array
		// point to the first element which is not null
		int j = left;
		for (int i = left; i <= right; i++) {
			// Case: u and v are in the same component.
			if (uf.find(edges[i].getA()) == uf.find(edges[i].getB())) {
				edges[i] = null;
				PartitionStrategy.swap(edges, i, j);
				j++;
			}// Case: u and v are in different components.
			else if (vertexDegree[edges[i].getA()] == 1
					|| vertexDegree[edges[i].getB()] == 1) {

				mst[mstSize] = (edges[i]);
				mstSize++;
				edges[i] = null;
				PartitionStrategy.swap(edges, i, j);
				j++;

			}
		}
		return j;
	}

	/**
	 * This function determines the number of adjacent nodes for each node.
	 * 
	 * This information is used for FilterKruskalPlus.
	 * 
	 */
	private void setVertexDegrees() {

		vertexDegree = new int[numberOfNodes];

		for (Edge e : edges) {
			vertexDegree[e.getA()] = vertexDegree[e.getA()] + 1;
			vertexDegree[e.getB()] = vertexDegree[e.getB()] + 1;
		}
	}

	// ********************************************************************************
	// ********************************************************************************
	// RECURSIVE IMPLEMENTATION. (This should not be used)
	// This code is only added for completness. This is the recursive implementation
	// of the above mentioned function
	// 1. qKruskal
	// 2. filterKruskal
	// 3. filterKruskalPlus
	//
	// It is implemented in a recrusive way as the paper had suggested and it
	// was interesting to see that for dense graph the recursive version can't
	// handle the overhead.
	// ********************************************************************************
	// ********************************************************************************

	/**
	 * This is for starting qKruskal in recursion.
	 */
	public Graph qKruskalRecursive() {

		qKruskal(edges, 0, edges.length - 1);

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}
		return new Graph(mst, numberOfNodes);
	}

	/**
	 * This is a recursive function. It determines the mst via qKruskal within
	 * the bound of right and left of the array. Right and left bounds are
	 * inclusive.
	 * 
	 * @param edges
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 */
	private void qKruskal(Edge[] edges, int left, int right) {
		// Test, whether the size of the edge list (right-left+1) is linear in n
		if ((right - left + 1) <= numberOfNodes) {
			// If the size is linear in n, kruskal on the edges[left...right]
			boundedKruskalDPQS(edges, left, right);
		} else {
			// Choosing the edge at position pivot, s.t. the edge list is
			// partitoned with respect to this edge.

			// int pivot = pivotIndexViaShulffling(edges, left, right);
			// int pivot = randInt(left, right);
			int originalPivotPosition = left;
			// After repositioning the edge, s.t. all edges left have weight
			// less and right have heavier weight, we determine its new
			// position.
			int newPivotPosition = PartitionStrategy.partitionLomuto(edges, left,
					right, originalPivotPosition);

			// Apply filterKruskal for edges with small weights
			qKruskal(edges, left, newPivotPosition);

			// If we have a mst already, we stop and don't recurse on the
			// heavier edges anymore.
			if (mstSize == numberOfNodes - 1) {
				return;
			}

			// We don't have a mst yet and apply filterKruskal for edges with
			// heavy weights.
			qKruskal(edges, newPivotPosition + 1, right);

			// If we have a mst already, we stop and don't go to the next level
			// of recursion anymore.
			if (mstSize == numberOfNodes - 1) {
				return;
			}
		}
	}

	/**
	 * This is for FilterKruskal in recursive Style. Use this for starting it.
	 */
	public Graph filterKruskalRecursive() {
		filterKruskal(edges, 0, edges.length - 1);

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}
		
		return new Graph(mst, numberOfNodes);
	}

	/**
	 * Determines an mst with FilterKruskal for the array of edges within the
	 * bounds of right and left. Both bounds are inclusive. For using this just
	 * call filterKruskalRecursive().
	 * 
	 * @param edges
	 * 
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 */
	private void filterKruskal(Edge[] edges, int left, int right) {
		// Test, whether the size of the edge list (right-left+1) is linear in n
		if ((right - left + 1) <= numberOfNodes) {
			// If the size is linear in n, kruskal on the edges[left...right]
			boundedKruskalDPQS(edges, left, right);
		} else {
			// Choosing the edge at position pivot, s.t. the edge list is
			// partitoned with respect to this edge.

			// int pivot = pivotIndexViaShulffling(edges, left, right);
			// int pivot = randInt(left, right);
			int originalPivotPosition = left;
			// After repositioning the edge, s.t. all edges left have weight
			// less and right have heavier weight, we determine its new
			// position.
			int newPivotPosition = PartitionStrategy.partitionLomuto(edges, left,
					right, originalPivotPosition);

			// Apply filterKruskal for edges with small weights
			filterKruskal(edges, left, newPivotPosition);

			if (mstSize == numberOfNodes - 1) {
				return;
			}
			// filter out heavy weights which cannnot be part of the MST because
			// they induce a cylce in the graph
			// pnew will be the index from
			int pnew = filter(edges, newPivotPosition + 1, right);

			// Apply filterKruskal for edges with heavy weights
			filterKruskal(edges, pnew, right);
			if (mstSize == numberOfNodes - 1) {
				return;
			}
		}
	}

	/**
	 * This is for starting qKruskal in recursion.
	 */
	public Graph filterKruskalPlusRecursive() {
		filterKruskalPlus(edges, 0, edges.length - 1);

		if (mstSize < numberOfNodes - 1) {

			throw new IllegalArgumentException(
					"Kein Zusammenh�ngender Graph. Neue Eingabe bitte.");
		}
		return new Graph(mst, numberOfNodes);
	}

	/**
	 * Determines a mst with FilterKruskalPlus for the array of edges within the
	 * bounds of right and left. Both bounds are inclusive.
	 * 
	 * @param edges
	 * @param left
	 *            A left bound for the list can be specified.
	 * @param right
	 *            A right bound for the list can be specified. Left bound needs
	 *            to be less than right bound.
	 */
	private void filterKruskalPlus(Edge[] edges, int left, int right) {
		// Test, whether the size of the edge list (right-left+1) is linear in n
		// System.out.println("edges-size: "+(right-left+1)+" numberOfVertices: "+numberOfVertices);
		if ((right - left + 1) < numberOfNodes) {
			// If the size is linear in n, we run Kruskal on the edges bounded
			// by left and right
			boundedKruskalDPQS(edges, left, right);
		} else {

			// Like QuickSort we partition the edges int two subgroup.
			// A group of edges(left..p-1) and a group of edges(p...right)
			// with edge weights less and greater than edges[p].weight,
			// respectively. p is called the pivot.

			// Choosing a good pivot: Median from random sample with size
			// sqrt(k)from edge[left...right]

			int originalPivotPosition = PartitionStrategy.randInt(left, right);

			int newPivotPosition = PartitionStrategy.partitionLomuto(edges, left,
					right, originalPivotPosition);

			// Apply filterKruskal for edges with small weights
			filterKruskalPlus(edges, left, newPivotPosition);

			// filter out heavy weights which cannnot be part of the MST because
			// they induce a cylce in the graph
			int pnew = filterPlus(edges, newPivotPosition + 1, right);

			// Apply filterKruskal for edges with heavy weights
			filterKruskalPlus(edges, pnew, right);
		}
	}

	/**
	 * ONLY FOR TESTING THE ALGOS.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {

			Graph G = Graph.convertGraphFromFile("Instanzen/Carsten/Instanz_8");
			// Graph G = Instances.getExample2();

			KruskalAlgo algo = new KruskalAlgo(G);
			Graph mst = null;

			
			long beginTime = System.nanoTime();
			mst = algo.qKruskal();
			long endTime = System.nanoTime();
			
		
			System.out.println("Kruskal- Time:" + (endTime - beginTime)
					+ "Weight" + mst.getWeight());


		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
