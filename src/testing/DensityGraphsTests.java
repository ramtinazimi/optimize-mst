/**
 * @author Alexandre Stüben
 */
package testing;

import java.io.IOException;

public class DensityGraphsTests extends RandomGraphsTests{
	private double minDensity, maxDensity, incDensity;
	
	public DensityGraphsTests(String resultPath, boolean[] algos, int loops,
			int minNodes, int maxNodes, int incNodes, 
			double minDensity, double maxDensity, double incDensity, int edgeCountMultiplier) {
		super(resultPath, algos, loops, minNodes, maxNodes, incNodes, edgeCountMultiplier);
		this.minDensity = minDensity;
		this.maxDensity = maxDensity;
		this.incDensity = incDensity;
	}

	@Override
	protected void testForGivenNodeNumber(int numberOfNodes) throws IOException {
		bw.write("# Number of Nodes: " + numberOfNodes + "\n");
		bw.write("#Density\t");
		writeColumnNames();
		int minDensHelper;
		if(minDensity<incDensity)
			minDensHelper = 0;
		else 
			minDensHelper = (int) (Math.round(minDensity / incDensity));
		double densOffset = minDensity - minDensHelper *incDensity;
		for (int densHelper = minDensHelper; roundLastDigits(densHelper * incDensity+densOffset)  <= maxDensity; densHelper++) {
			double density = roundLastDigits(densHelper * incDensity+densOffset);
			int numberOfEdges = calculateRealEdges(numberOfNodes, density);
			bw.write(density + "");
			validateAndTestGivenGraphSize(numberOfNodes, numberOfEdges);
			if(minDensity==maxDensity)
				break;
		}
	}
	double roundLastDigits(double toRound)	{
		long temp = Math.round(toRound*100000);
		return ((double)temp)/100000;
	}
	// Density from 0.0 empty graph to 1.0 full graph
	public static int calculateRealEdges(int numberOfNodes, double density) {
		double nodes = (double) (numberOfNodes);
		double maxEdges = nodes * (nodes - 1) / 2; // Edges of a full graph
		// density is a value between 0 (graph is already tree) and 1 (full
		return (int) (density * (maxEdges));
	}
	protected void writeGeneralInformation() throws IOException {
		super.writeGeneralInformation();
		bw.write("# Edges: ");
		if(minDensity<maxDensity)
			bw.write("From "+minDensity + " to "+ maxDensity+" in "+incDensity+" steps.\n");
		else
			bw.write(minDensity + "\n");	
	}
}
