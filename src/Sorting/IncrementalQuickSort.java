package Sorting;

import java.util.Random;
import java.util.Stack;

import Kruskal.PartitionStrategy;
import mst.Edge;

/**
 * 
 * @author Ramtin & Florian
 *
 */
public class IncrementalQuickSort {

	private static int idx;
	private static Edge[] edges;
	private static Stack<Integer> S;

	public static void init(Edge[] edgesNew, int left, int right){
		edges = edgesNew;
        S = new Stack<Integer>();
        S.push(right+1);
        idx = left;
	}
	public static Edge getMin(){
        incrementalQuickSortIterative();
        idx++;
        S.pop();
        return edges[idx - 1];
    }
	
	private static void incrementalQuickSortIterative() {
		Random rand = new Random();
		int pidx;
		while (S.peek() != idx) {
			pidx = rand.nextInt(S.peek() - idx) + idx;
			pidx = PartitionStrategy.partitionLomuto(edges,idx, S.peek()-1, pidx);
			S.push(pidx);
		}
	}


}
