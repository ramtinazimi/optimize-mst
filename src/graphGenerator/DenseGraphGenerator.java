/**
 * @author Alexandre Stüben
 */
package graphGenerator;

import java.util.Random;
import java.util.Stack;

import mst.Graph;

public class DenseGraphGenerator extends GraphGenerator{

	public DenseGraphGenerator(int numberOfNodes, int numberOfEdges) {
		super(numberOfNodes, numberOfEdges);
	}
	
	/**
	 * generates Graph, which has a density of > 0.5 , by erasing random edges
	 * of a full graph, whereby already erased edges are discarded.
	 * 
	 * This way, the amount of neglected random edges does not exceed 50%
	 * 
	 * @return a Graph Object
	 */
	public Graph generateGraph() {
		// Idea: generate full graph an look, then cut of edges and look, if it
		// is connected at the end
		AdjacentList graph;
		do {
			graph= new AdjacentList(numberOfNodes);
			graph.setAllToTrue();
			int restEdges = graph.numberOfEdges - numberOfEdges;
			emptyGraph(graph, restEdges);
		} while (notConnected(graph));
		System.out.println("Dense graph of density " + this.getRealDensity()
				+ " generated.");
		Graph retGraph = graph.convertToArrayGraph(); 
		assert retGraph.edges.length == numberOfEdges;
		return retGraph;
	}
	

	/**
	 * Erasing random edges from graph, whereby already existing edges are 
	 * discarded.
	 * This way, the amount of neglected random edges does not exceed 50%
	 * 
	 * @param graph		The AdjacentList object, which should be emptied
	 * @param restEdges 	The amount of edges which should be erased
	 */
	private void emptyGraph(AdjacentList graph,  int restEdges){
		randomGenerator = new Random();

		int count = 100;
		
		while (graph.numberOfEdges > numberOfEdges) {
			int b, a = randomGenerator.nextInt(numberOfNodes);
			while (a == (b = randomGenerator.nextInt(numberOfNodes))) {
			} // Ensures that a != b
			graph.setFalse(a, b);
			count = calcPercent(graph.numberOfEdges - numberOfEdges, restEdges, count);
		}
	}
	
	/**
	 * @param graph
	 * @return Boolean value, indicating if graph is connected or no
	 */
	private static Boolean notConnected(AdjacentList graph) {
		int numberOfNodes = graph.numberOfNodes();
		
		Boolean[] found = new Boolean[numberOfNodes];
		for (int i = 0; i < numberOfNodes; i++)
			found[i] = false;

		Stack<Integer> search = new Stack<Integer>();
		search.add(0);
		found[0] = true;
		while (search.size() != 0) {
			int a = search.pop();
			for (int b = 0; b < numberOfNodes; b++) {
				if (graph.getBit(a, b) &&  !found[b]) {
					search.add(b);
					found[b] = true;
				}

			}
		}
		Boolean unConnected = false;
		for (int i = 0; i < numberOfNodes; i++) {
			if (found[i] == false) {
				System.out.print("Unconnected found, node " + i + "\n");
				unConnected = true;
			}
		}
		return unConnected;
	}
}
