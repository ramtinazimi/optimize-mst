/**
 * @author Alexandre Stüben
 */
package graphGenerator;

import java.util.Random;

import mst.Graph;

public class LightGraphGenerator extends GraphGenerator{

	public LightGraphGenerator(int numberOfNodes, int numberOfEdges) {
		super(numberOfNodes, numberOfEdges);
	}
	/**
	 * generates Graph, which has a density of < 0.5
	 * @return an graph Object
	 */
	public Graph generateGraph(){
		AdjacentList graph = WilsonAlgorithm.wilsonAlgo(numberOfNodes);
		// The remaining edges to be added to the graph
		int restEdges = numberOfEdges - numberOfNodes + 1;
		fillGraph(graph, restEdges);
		System.out.println(" Light graph of density " + this.getRealDensity()
				+ " generated.");
		Graph retGraph = graph.convertToArrayGraph(); 
		assert retGraph.edges.length == numberOfEdges;
		return retGraph;
	}
	
	/**
	 * Adding random edges to graph, whereby already existing edges are 
	 * discarded.
	 * This way, the amount of neglected random edges does not exceed 50%
	 * @param graph		The AdjacentList object, which should be filled
	 * @param restEdges 	The amount of edges, which should be added
	 */
	void fillGraph(AdjacentList graph, int restEdges){
		int count = 100;	// To print progress
		
		randomGenerator = new Random();
		while (graph.numberOfEdges < numberOfEdges) {
			int b, a = randomGenerator.nextInt(numberOfNodes);
			while (a == (b = randomGenerator.nextInt(numberOfNodes))) {
			} // Ensures that a != b
			if (graph.setTrue(a, b))
				restEdges--;
			count = calcPercent(restEdges, (numberOfEdges - numberOfNodes + 1), count);
		}
	}
}
