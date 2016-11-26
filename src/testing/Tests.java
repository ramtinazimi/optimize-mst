/**
 * @author Alexandre Stüben
 */
package testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mst.Graph;
import Kruskal.KruskalAlgo;
import Prim.PrimBH;
import Prim.PrimQH;

public abstract class Tests {

	protected final int numberOfAlgos;
	private static long beginTime, endTime;
	protected static final String[] algoNames = new String[]{"NormalKruskal",
			"FilterKruskal", "FilterKruskalP", "QKruskal\t", 
			"BinHeapPrim\t", "QuickHeapPrim"};
	protected String resultPath;
	protected final boolean[] algos;
	protected BufferedWriter bw;

	public Tests(String resultPath, boolean[] algos) {
		this.resultPath = resultPath;
		this.algos = algos;
		int count = 0;
		for (boolean bool : algos) {
			if (bool)
				count++;
		}
		numberOfAlgos = count;
		System.out.println("Number of algorithms used: " + numberOfAlgos);
	}
	protected long[] resultTimes;

	/**
	 * Begins the whole testing process
	 */
	public abstract void start();

	
	protected  void writeGeneralInformation() throws IOException{
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		bw.write("# "+df.format(dateobj)+"\n");
	}
	
	
	/**
	 * writes the head line into Buffered Writer
	 * 
	 * @throws IOException
	 */
	protected void writeColumnNames() throws IOException {
		for (int i = 0; i < algos.length; i++) {
			if (algos[i]){
				bw.write(algoNames[i] + "\t\t\t");
			}
		}
		bw.write("\n");
	}
	/**
	 * @param graph
	 * @param algos
	 *            A boolean array, which defines which algorithms should be used
	 * @return the time taken for every algorithm used, as a long array in
	 *         nanoseconds
	 * @throws IOException
	 */
	protected long[] testAlgos(Graph graph, boolean[] algos) {
		long[] time = new long[numberOfAlgos];
		int i = 0;

		
		if (algos[0]) // Normal Kruskal
			time[i++] = normalKruskal(graph);
		if (algos[1]) // Filter Kruskal
			time[i++] = filterKruskal(graph);	
		if (algos[2]) // Filter Kruskal Plus
			time[i++] = filterKruskalPlus(graph);
		if (algos[3]) // qKruskal
			time[i++] = qKruskal(graph);
		if (algos[4])  // Run Binaryheap Code:
			time[i++]= bPrim(graph);
		if (algos[5])  // Run Quickheap Code:
			time[i++]= qPrim(graph);

		return time;
	}

	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long normalKruskal(Graph graph) {
		Graph result;
		System.out.print("NORMAL");
		KruskalAlgo algo = new KruskalAlgo(graph);
		beginTime = System.nanoTime();
		result = algo.standardKruskal();
		endTime = System.nanoTime();
		System.out.println("-KRUSKAL\t\t" + result.getWeight());
		return (endTime - beginTime);
	}

	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long filterKruskal(Graph graph) {
		Graph result;
		System.out.print("FILTER");
		KruskalAlgo algo = new KruskalAlgo(graph);
		beginTime = System.nanoTime();
		result = algo.filterKruskal();
		endTime = System.nanoTime();
		System.out.println("-KRUSKAL\t\t" + result.getWeight());
		return (endTime - beginTime);
	}

	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long filterKruskalPlus(Graph graph) {
		Graph result;
		System.out.print("FILTER-");
		KruskalAlgo algo = new KruskalAlgo(graph);
		beginTime = System.nanoTime();
		result = algo.filterKruskalPlus();
		endTime = System.nanoTime();
		System.out.println("KRUSKAL-PLUS\t" + result.getWeight());
		return (endTime - beginTime);
	}
	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long qKruskal(Graph graph) {
		Graph result;
		System.out.print("QKRUSKAL \t\t");
		KruskalAlgo algo = new KruskalAlgo(graph);
		beginTime = System.nanoTime();
		result = algo.qKruskal();
		endTime = System.nanoTime();
		System.out.println(result.getWeight());
		return (endTime - beginTime);
	}

	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long bPrim(Graph graph) {
					
		System.out.print("BINARYHEAP-PRIM\t\t");
		PrimBH primBH = new PrimBH(graph);
		beginTime = System.nanoTime();
		primBH.start();
		endTime = System.nanoTime();
		System.out.println(primBH.weight() );
		return (endTime - beginTime);
	}
	/**
	 * @param graph
	 * @param numberOfNodes
	 * @return the time taken to execute the specific algorithm.
	 */
	static long qPrim(Graph graph) {

		System.out.print("QPRIM\t\t\t");
		PrimQH primQH = new PrimQH(graph);
		beginTime = System.nanoTime();
		primQH.start();
		endTime = System.nanoTime();
		System.out.println(primQH.weight());
		return (endTime - beginTime);
	}

	protected static double inSeconds(long nanoseconds) {
		return (double) nanoseconds / 1000000000;
	}

	/**
	 * @param resultPath
	 * @return a buffered Writer for the result file
	 * @throws IOException
	 */
	protected static BufferedWriter openResultFile(String resultPath)
			throws IOException {
		File results = new File(resultPath);
		// if file doesnt exists, then create it
		if (!results.exists())
			results.createNewFile();
		FileWriter fw = new FileWriter(results.getAbsoluteFile());
		return new BufferedWriter(fw);

	}
}
