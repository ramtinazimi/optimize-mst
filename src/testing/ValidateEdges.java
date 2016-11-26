/**
 * @author Alexandre Stüben
 */
package testing;

public class ValidateEdges {

	protected static final int maxInt = 2147483647;
	protected static final int maxCalculateableNodes = 65536;
	protected static final int maxNodesNumberWhereDenseGraphPossible = 92682;
	
	/**
	 * @param numberOfNodes
	 * @param numberOfEdges
	 * @return true, if graphs which given number of nodes and edges can be
	 * generated, false otherwise
	 */
	public static boolean validateNodesEdges(int numberOfNodes, int numberOfEdges){
		//The max amount of edges can be calculated
		if(areNodesBelowCalculateableThreshold(numberOfNodes))
			return validateCalculatableNodesEdges(numberOfNodes, numberOfEdges);
		//Avoid dense graph, if a full graph is not possible
		if(notCalcultableButDenseGraphPossible(numberOfNodes))
			return (thresholdLightDenseGraph(numberOfNodes, numberOfEdges));
		//Now, a light graph is asserted!
		else
			return true;
	}
	/**
	 * the edge number of a full graph for given nodes exceeds the int- range 
	 * (overflow), but a half full graph does not (a dense graph is possible, 
	 * but cannot be calculated, because it relies on he possibility to 
	 * calculate a full graph.
	 * @param numberOfNodes
	 * @return true, if dense graph possible, false otherwise
	 */
	public static boolean notCalcultableButDenseGraphPossible(int numberOfNodes){
		return (numberOfNodes<= maxNodesNumberWhereDenseGraphPossible);
	}
	/**
	 * @param numberOfNodes
	 * @return true, if  the amount of edges of a full graph given the node
	 *  number can be calculated, false otherwise
	 */
	public static boolean areNodesBelowCalculateableThreshold(int numberOfNodes){
		return (numberOfNodes <= maxCalculateableNodes);
	}
	/**
	 * 
	 * @param numberOfNodes
	 * @param numberOfEdges
	 * @return true, if graph can be generated, false otherwise
	 */
	private static boolean validateCalculatableNodesEdges(int numberOfNodes, int numberOfEdges){
		boolean reult = numberOfNodes <= numberOfEdges+1 && numberOfEdges <= maxEdges(numberOfNodes);
		if(!reult )
			System.out.println("Too many, or not enough edges.\n" +
					 "Nodes: " + numberOfNodes +
					" Edges: " + numberOfEdges);
		return (reult);
	}
	/**
	 * Prints the information, that with given node number, a the calculation 
	 * of a dense graph is not possible
	 * @param numberOfNodes
	 * @param numberOfEdges
	 * @return true, if Light Graph should be generated, false otherwise.
	 */
	public static boolean thresholdLightDenseGraph(int numberOfNodes, int numberOfEdges){
    	boolean result;
		if(numberOfNodes % 2 == 0)
    		result = ( numberOfEdges < (numberOfNodes/4)* (numberOfNodes-1) );
    	else
    		result = ( numberOfEdges < numberOfNodes*((numberOfNodes-1)/4));
    	return result;
	}
	
	/**
	 * 
	 * @param numberOfNodes
	 * @return the amount of edges of a full graph given a number of nodes
	 */
    private static int maxEdges(int numberOfNodes){
    	if(numberOfNodes % 2 == 0)
    		return  (numberOfNodes/2)* (numberOfNodes-1);
    	else
    		return numberOfNodes*((numberOfNodes-1)/2);
	}
}
