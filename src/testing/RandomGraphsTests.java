/**
 * @author Alexandre Stüben
 */
package testing;

import graphGenerator.DenseGraphGenerator;
import graphGenerator.GraphGenerator;
import graphGenerator.LightGraphGenerator;

import java.io.IOException;

import mst.Graph;

public abstract class RandomGraphsTests extends Tests{
	protected int  minNodes,  maxNodes,  incNodes;
	protected int loops;
	protected int edgeCountMultiplier;
	
	public RandomGraphsTests(String resultPath, boolean[] algos, int loops, 
			int minNodes, int maxNodes, int incNodes, int edgeCountMultiplier) {
		super(resultPath, algos);
		this.minNodes = minNodes;
		this.maxNodes = maxNodes;
		this.incNodes = incNodes;
		this.loops = loops;
		this.edgeCountMultiplier = edgeCountMultiplier;
	}
	/**
	 * Begins the whole testing process
	 */
	@Override
	public void start(){
		try {
			bw = openResultFile(resultPath);
	
			writeGeneralInformation();
			// main loop for the amount of nodes used
			for (int numberOfNodes = minNodes; numberOfNodes <= maxNodes; 
					numberOfNodes += incNodes) 	{
					System.out.println("# Nodes: " + numberOfNodes);
					testForGivenNodeNumber(numberOfNodes);
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("Could not open result file.");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * writes general Information like date etc. into Buffered Writer
	 * @throws IOException
	 */
	protected void writeGeneralInformation() throws IOException {
		super.writeGeneralInformation();
		bw.write("# Iterations per size: "+loops+"\n");
		bw.write("# Nodes: ");
		if(minNodes<maxNodes)
			bw.write("From "+minNodes + " to "+ maxNodes+" in "+incNodes+" steps.\n");
		else
			bw.write(minNodes + "\n");	
	}
	
	/**
	 * writes the head line into Buffered Writer
	 * @throws IOException
	 */
	protected void writeColumnNames() throws IOException {
		for (int i = 0; i < algos.length; i++) {
			if (algos[i])
				bw.write(algoNames[i] + "\t\t");
		}
		bw.write("\n");
	}
	/**
	 * tests for a fixed amount of nodes
	 * @param numberOfNodes
	 * @throws IOException
	 */
	protected abstract void testForGivenNodeNumber(int numberOfNodes)throws IOException;
	
	/**
	 * if graph size (number of nodes and edges) is set, validate and set the
	 * correct GraphGenerator.
	 * @param numberOfNodes
	 * @param numberOfEdges
	 * @throws IOException
	 */
	protected void validateAndTestGivenGraphSize(int numberOfNodes, int numberOfEdges)throws IOException{
		GraphGenerator graph;
		if( ValidateEdges.validateNodesEdges(numberOfNodes, numberOfEdges)){
			if(ValidateEdges.areNodesBelowCalculateableThreshold(numberOfNodes)){
				if(ValidateEdges.thresholdLightDenseGraph(numberOfNodes, numberOfEdges))
					graph = new LightGraphGenerator(numberOfNodes, numberOfEdges);
				else
					graph = new DenseGraphGenerator(numberOfNodes, numberOfEdges);
			}
			else
				graph = new LightGraphGenerator(numberOfNodes, numberOfEdges);
			testSpecificSize( graph);
		}
		else{
			System.out.println("Skip this.");
		}
	}
	
	/**
	 * Tests with graphs of a certain size, assuming all inputs are correct
	 * @param graph
	 * @throws IOException
	 */
	protected void testSpecificSize(GraphGenerator graphGen) throws IOException{
		long [][] times = testAllIterations(graphGen);
		long [] averageTime = calculateAverageTime(times); 
		long [] errorOfTime = calculateAverageErrorOfTime(times, averageTime);
		writeTimesToFile(averageTime, errorOfTime);
	}
	
	private long[] calculateAverageErrorOfTime(long[][] times, long  [] averageTime) {
		long [] errorOfTime = new long [numberOfAlgos];
		for(int i=0; i< numberOfAlgos; i++){	
			for (int loop = 0; loop < loops; loop++) 
				errorOfTime [i] += Math.abs(averageTime[i] - times[loop][i] ); 
			errorOfTime[i] /= loops;
		}
		return errorOfTime;
	}
	private long[] calculateAverageTime(long[][] times) {
		long  [] averageTime = new long [numberOfAlgos];
		for(int i=0; i< numberOfAlgos; i++){
			for (int loop = 0; loop < loops; loop++) 
				averageTime[i] += times[loop][i]; 
			averageTime[i] /= loops;
		}
		return averageTime;
	}
	
	private void writeTimesToFile(long[] averageTime, long[] errorOfTime) throws IOException {
		for(int i=0; i< numberOfAlgos; i++){	
			bw.write("\t\t" + inSeconds(averageTime[i]));
			bw.write("\t" + inSeconds(errorOfTime[i])); 
		}
		bw.write("\n");
	}
	
	private long[][] testAllIterations(GraphGenerator graphGen) {
		long [][] times = initTimes();
		for (int loop = 0; loop < loops; loop++) {
			System.out.println("Generate Graph # " + (loop + 1));
			Graph testgraph = graphGen.generateGraph();
			times[loop] = testAlgos(testgraph, algos);
		}
		return times;
	}
	
	private long[][] initTimes() {
		long [][] times = new long [loops][numberOfAlgos];
		for(int i=0 ; i<loops; i++){
			for(int j=0 ; j<numberOfAlgos; j++)
				times[i][j] = 0;
		}
		return times;
	}
}
