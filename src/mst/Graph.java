package mst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Alexandre Stüben
 */
public class Graph {
	public Edge[] edges;
	private static int numberOfNodes;
	
	public Graph(Edge[] input, int numNodes) {
		edges = input;
		numberOfNodes = numNodes;
	}
	
	public int getNumOfNodes() {
		return numberOfNodes;
	}

	public double getWeight() {
		double sum = 0;
		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != null) {
				sum += edges[i].getWeight();
			}
		}
		return sum;
	}

//	@Override
//	public Graph clone() {
//		System.out.println("begin cloning");
//		Edge[] inp = new Edge[this.edges.length];
//		// System.out.println("allocated");
//		for (int i = 0; i < this.edges.length; i++)
//			inp[i] = edges[i].clone();
//		// System.out.println("cloned edges");
//		Graph graph = new Graph(inp, this.numberOfNodes);
//		System.out.println("finished...");
//		return graph;
//
//	}

	@Override
	public String toString() {
		String output = "Number of Nodes: " + numberOfNodes + "\n" + "Edges:";

		for (Edge e : edges) {
			output += e.toString() + "\t";
		}

		return output;
	}

	/**
	 * parses a graph from a text-file
	 * 
	 * @param path
	 *            Path to text-file
	 * @return
	 * @throws IOException
	 */
	public static Graph convertGraphFromFile(String path) throws IOException {
		System.out.print("---Getting txt File. ");
		long beginTime, endTime;
		beginTime = System.nanoTime();
		Edge[] edges;
		Graph graph;
		BufferedReader br = new BufferedReader(new FileReader(path));

		int numberOfNodes = Integer.parseInt(br.readLine().trim());
		int numberOfEdges = Integer.parseInt(br.readLine().trim());
		edges = new Edge[numberOfEdges];
		graph = new Graph(edges, numberOfNodes);

		String line;
		String[] text;
		int node1, node2;
		double weight;
		int i = 0;
		while ((line = br.readLine()) != null) {
			text = line.split("\\s");
			node1 = Integer.parseInt(text[0]);
			node2 = Integer.parseInt(text[1]);
			weight = Double.parseDouble(text[2]);
			edges[i] = new Edge(node1, node2, weight);
			i++;
		}
		br.close();
		endTime = System.nanoTime();
		System.out.println("File input converted to graph, took "
				+ (endTime - beginTime) / 1000000 + " milliseconds---");
		return graph;
	}
}