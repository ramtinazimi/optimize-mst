/**
 * @author Alexandre Stüben
 */
package graphGenerator;
import java.util.BitSet;
import java.util.Random;

import mst.Edge;
import mst.Graph;

public class AdjacentList {
	public BitSet [] graph;
	public int numberOfEdges;

	static Random randomGenerator;
	
	public AdjacentList(int numberOfNodes){
		this.numberOfEdges = 0;
		graph = new BitSet [numberOfNodes];
		for (int i = 0; i < numberOfNodes; i++) 
			graph[i] = new BitSet(numberOfNodes);
	}
	
	/**
	 * Set all boolean in BitSet to True, include all possible edges to the graph.
	 * Set actual numberOfEdges
	 */
	void setAllToTrue(){
		for (int i = 0; i < numberOfNodes(); i++) {
			graph[i] = new BitSet(numberOfNodes());
			for (int j = 0; j < numberOfNodes(); j++) {
				graph[i].set(j);
			}
		}
		numberOfEdges = countEdges();
	}
	
	int numberOfNodes(){
		return this.graph.length;
	}
	/**
	 * Get true if edge defined by nodes a and b is has been added to the graph
	 * false if not
	 * @param a
	 * @param b
	 * @return
	 */
	boolean getBit(int a, int b){
		return this.graph[a].get(b);
	}
	
	/**
	 * Sets specified edge to false, returns true and decreases numberOfEdges
	 * if edge was previously true, return false otherwise
	 * @param a
	 * @param b
	 * @return
	 */
	boolean setFalse(int a,int b){
		if(graph[a].get(b)){
			this.graph[a].set(b, false);
			this.graph[b].set(a, false);
			this.numberOfEdges--;
			return true;
		}
		return false;
	}
	
	/**
	 * Analog to setFalse. returns true if previously false, false otherwise.
	 * Sets the specified value to true.
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean setTrue(int a, int b) {
		if(!graph[a].get(b)){
			this.graph[a].set(b, true);
			this.graph[b].set(a, true);
			this.numberOfEdges++;
			return true;
		}
		return false;
	}
	
	/**
	 * Converts the AdjacentList to an Instance of the Graph class. Random 
	 * values from 0 to 1 are set as weights.
	 * @return
	 */
	Graph convertToArrayGraph(){
		randomGenerator = new Random();
		 Edge[] graph = new Edge[numberOfEdges];
		 int k=0;
		 for (int i = 0; i < numberOfNodes(); i++) {
			 for (int j = i+1; j < numberOfNodes(); j++) {
				 if(this.graph[i].get(j)){
					 double weight = randomGenerator.nextDouble();
					 graph[k++] = new Edge(i, j, weight);
				 }
			 }
		 }
		 return new Graph (graph, numberOfNodes());
	}
	
	public int countEdges(){
		int k=0;
		for (int i = 0; i < numberOfNodes(); i++) {
			for (int j = i+1; j < numberOfNodes(); j++) {
				if(this.graph[i].get(j)){
					k++;
				}
			}
		}
		return k;
	}
	
	/**
	 * Prints graph to console in boolean values, will spam your console
	 */
	public void printGraph(){
		System.out.println();
		for (int i = 0; i < numberOfNodes(); i++) {
			for (int j = 0; j < numberOfNodes(); j++) {
					System.out.print( this.graph[i].get(j)+ " " );
			}
			System.out.println();
		}
	}


}

