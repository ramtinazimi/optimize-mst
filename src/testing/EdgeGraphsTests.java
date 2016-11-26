/**
 * @author Alexandre Stüben
 */
package testing;

import java.io.IOException;

public class EdgeGraphsTests extends RandomGraphsTests{
	private int minEdges, maxEdges,  incEdges;
	public EdgeGraphsTests(String resultPath, boolean[] algos, int loops,
			int minNodes, int maxNodes, int incNodes, int minEdges,
			int maxEdges, int incEdges,  int edgeCountMultiplier) {
		super(resultPath, algos, loops, minNodes, maxNodes, incNodes, edgeCountMultiplier);
		this.minEdges = minEdges;
		this.maxEdges = maxEdges;
		this.incEdges = incEdges;
	}
	@Override
	protected void testForGivenNodeNumber(int numberOfNodes) throws IOException {
		bw.write("# Number of Nodes: " + numberOfNodes + "\n");
		bw.write("#Edges\t");
		writeColumnNames();
		for (int numberOfEdges = minEdges; numberOfEdges <= maxEdges; numberOfEdges = numberOfEdges*edgeCountMultiplier+incEdges){ 
			bw.write(numberOfEdges + "");
			validateAndTestGivenGraphSize(numberOfNodes, numberOfEdges);
		}
	}
	
	protected void writeGeneralInformation() throws IOException {
		super.writeGeneralInformation();
		bw.write("# Edges: ");
		if(minEdges<maxEdges)
			bw.write("From "+minEdges + " to "+ maxEdges+" in "+incEdges+" steps.\n");
		else
			bw.write(minEdges + "\n");	
	}

}
