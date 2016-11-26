/**
 * @author Alexandre Stüben
 */
package graphGenerator;

import java.util.Random;

import mst.Graph;

public abstract class GraphGenerator {
	static Random randomGenerator;
	protected int numberOfNodes;
	protected int numberOfEdges;

	public GraphGenerator(int numberOfNodes, int numberOfEdges) {
		if ((numberOfEdges < numberOfNodes) || numberOfEdges > maxEdges(numberOfNodes)) {
			//This case should not happen, because valid inputs are asserted in the Tests-classes
			System.err.println("Less Edges than Nodes, or more Edges than possible.");
			return;
		}
		setValues(numberOfNodes, numberOfEdges);
	}

	/**
	 * Setting the values for nodes and the edges and testing whether the
	 * fulfill some conditions.
	 * @param numberOfNodes
	 * @param numberOfEdges
	 */
	public void setValues(int numberOfNodes, int numberOfEdges) {
		if ((numberOfEdges >= numberOfNodes - 1)
				& (numberOfEdges <=  maxEdges(numberOfNodes) )) {
			this.numberOfNodes = numberOfNodes;
			this.numberOfEdges = numberOfEdges;
		} else {
			System.err.println("Either too many or not"
					+ "enough Vertices to generate Graph!");
		}
	}

	/**
	 * generates a random graph regarding the density.
	 * @return
	 */
	public abstract Graph generateGraph();

	/**
	 * Following code prints progress to System.out in 5-Percent-iterations
	 * @param restEdges	The remaining edges
	 * @param int totalEdges The amount of Edges in total
	 * @param count 	The next percentage to print
	 * @return
	 */
	protected int calcPercent(int restEdges, int totalEdges, int count){
		int now = (int) (100.0 * (double) restEdges / ((double) totalEdges));
		while(now <= count){
			System.out.print(count + "% ");
			count -= 5;
		}
		return count;
	}

	/**
	 * Calculate the maximal possible number of edges in regard to numberOfNodes
	 * @param numberOfNodes
	 * @return
	 */
    static int maxEdges(int numberOfNodes){
    	int maxEdges = (int) numberOfNodes*(numberOfNodes-1)/2;
    	if(maxEdges < numberOfNodes)
    		return 2147483647;
    	else
    		return maxEdges;
	}
	/**
	 * @return returns the exact density, => 0.0 means 0 edges, 1.0 means full
	 *         graph
	 */
	public double getRealDensity() {
		double nodes = (double) (numberOfNodes);
		double edges = (double) (numberOfEdges);
		double maxEdges = nodes * (nodes - 1) / 2;
		return (edges) / (maxEdges);

	}
}
