package mst;

import java.util.Arrays;

import testing.*;
/**
 * @author Alexandre Stüben
 */
public class InputParser {
	private static final String[] algoAbr = new String[]{"nK", "fK", "fKp",
			"qK", "bP", "qP", "all"};
	private static final int numberOfAlgorithms = 6;
	private final String[] input;
	private String resultPath = "result.txt";
	private boolean[] algos;
	/**
	 * stores the input String [] locally
	 * @param input
	 */
	public InputParser(String[] input) {
		this.input = input;
	}
	/**
	 * parse the entire input to a Tests-Object, which exact type depends on the
	 * parameter
	 * @return
	 * @throws Exception
	 */
	public Tests parseInput() throws Exception {
		this.algos = parseAlgos();
		if (isInInput("-t"))
			this.resultPath = parseResultPath();
		if (isInInput("-p") && !isInInput("-e") && !isInInput("-d")) {
			return parseGraphInput();
		}
		if (isInInput("-e") && !isInInput("-p") && !isInInput("-d")) {
			return parseEdgeInput();
		}
		if (isInInput("-d") && !isInInput("-p") && !isInInput("-e")) {
			return parseDensityInput();
		}
		System.out.println("-p, -e and -d are exclusive.");
		throw new Exception("Parameter not set correctly.");
	}

	/**
	 * parses the Input, if the input seems to have the indentation to open a
	 * graph from file to be tested on
	 * 
	 * @return an object of type FileGraphTests
	 * @throws Exception
	 */
	private Tests parseGraphInput() throws Exception {
		System.out.println("Parse parameter for file-input.");
		String graphPath = parseGraphPath();
		return new FileGraphTests(graphPath, resultPath, algos);
	}
	/**
	 * parses the Input, if the input seems to have the indentation to generate
	 * graphes due to inputs of the densities
	 * 
	 * @return an object of type DensityGraphsTests
	 * @throws Exception
	 */
	private Tests parseDensityInput() throws Exception {
		System.out.println("Parse parameter for defined densities.");
		int loops = parseLoops();
		int[] nodes = parseNodes();
		double[] densities = parseDensities();
		double incDensity = 0;
		int edgeCountMultiplier = 1;
		if (isInInput("-log")) {
			edgeCountMultiplier = (int) densities[2];
			if (edgeCountMultiplier < 2) {
				System.out.println("Multiplier must be 2 or greater");
				throw new Exception("Multiplier must be 2 or greater");
			}
		} else
			incDensity =  densities[2];
		return new DensityGraphsTests(resultPath, algos, loops, nodes[0],
				nodes[1], nodes[2], densities[0], densities[1], incDensity,
				edgeCountMultiplier);
	}
	/**
	 * parses the Input, if the input seems to have the indentation to generate
	 * graphs due to inputs of the number of edges
	 * 
	 * @return an object of type DensityGraphsTests
	 * @throws Exception
	 */
	private Tests parseEdgeInput() throws Exception {
		System.out.println("Parse parameter for defined edges.");
		int loops = parseLoops();
		int[] nodes = parseNodes();
		int[] edges = parseEdges();
		System.out.println(edges[0]);
		int incEdges = 0, edgeCountMultiplier = 1;
		if (isInInput("-log")) {
			edgeCountMultiplier = edges[2];
			if (edgeCountMultiplier < 2) {
				System.out.println("Multiplier must be 2 or greater");
				throw new Exception("Multiplier must be 2 or greater");
			}
		} else
			incEdges = edges[2];
		return new EdgeGraphsTests(resultPath, algos, loops, nodes[0],
				nodes[1], nodes[2], edges[0], edges[1], incEdges,
				edgeCountMultiplier);
	}
	/*
	 * Following: the Parser for a specific setting, respectively
	 */
	/**
	 * parses the parameter, which defines which algorithms are used
	 * 
	 * @return boolean []
	 * @throws Exception
	 */
	private boolean[] parseAlgos() throws Exception {
		boolean[] algos = new boolean[numberOfAlgorithms];
		int pos = getPosition("-a");
		if (pos < 0) {
			throw new Exception("Missing -a");
		} else {
			pos++;
			while ((pos < input.length)
					&& (Arrays.asList(algoAbr).contains(input[pos]))) {
				switch (input[pos]) {
					case "nK" :
						algos[0] = true;
						pos++;
						break;
					case "fK" :
						algos[1] = true;
						pos++;
						break;
					case "fKp" :
						algos[2] = true;
						pos++;
						break;
					case "qK" :
						algos[3] = true;
						pos++;
						break;
					case "bP" :
						algos[4] = true;
						pos++;
						break;
					case "qP" :
						algos[5] = true;
						pos++;
						break;
					case "all" : {
						for (int j = 0; j < algos.length; j++)
							algos[j] = true;
						pos++;
					}
						;
						break;
					default : {
						throw new Exception("Not a correct parameter: "
								+ input[pos]);
					}
				}
			}
			return algos;
		}
	}
	/**
	 * parses the number of iteration, a graph of a defined size should be
	 * generated and tested
	 * @return number of loops
	 * @throws Exception
	 */
	private int parseLoops() {
		int pos = getPosition("-l");
		if (pos < 0)
			return 1;
		pos++;
		return Integer.parseInt(input[pos]);
	}
	/**
	 * parses the setting, which defines the amount of nodes used in the
	 * graph-generation
	 * @return
	 * @throws Exception
	 */
	private int[] parseNodes() throws Exception {
		int[] nodes = new int[3];
		int pos = getPosition("-n");
		if (pos < 0) {
			throw new Exception("Missing -n");
		}
		pos++;
		try {
			int i = 0;
			nodes[i] = Integer.parseInt(input[pos + i++]);
			nodes[i] = Integer.parseInt(input[pos + i++]);
			nodes[i] = Integer.parseInt(input[pos + i++]);
		} catch (Exception ex) {
			try {
				System.out.println("Only one node-parameter.");
				nodes[0] = Integer.parseInt(input[pos]);
				nodes[1] = nodes[0];
				nodes[2] = nodes[0];
			} catch (Exception ex2) {
				throw new Exception("Could not read options after -n");
			}
		}
		return nodes;
	}
	/**
	 * parses the setting, which defines the densities used in the
	 * graph-generation
	 * @return
	 * @throws Exception
	 */
	private double[] parseDensities() throws Exception {
		double[] densities = new double[3];
		int pos = getPosition("-d");
		if (pos < 0) {
			throw new Exception("Missing -d");
		}
		pos++;
		try {
			int i=0; 
			densities[i] = Double.parseDouble(input[pos + i++]);
			densities[i] = Double.parseDouble(input[pos + i++]);
			densities[i] = Double.parseDouble(input[pos + i++]);
		} catch (Exception ex) {
			try {
				System.out.println("Only one density-parameter.");
				densities[0] = Double.parseDouble(input[pos]);
				densities[1] = densities[0];
				densities[2] = densities[0];
			} catch (Exception ex2) {
				throw new Exception("Could not read options after -d");
			}
		}
		return densities;
	}
	/**
	 * parses the setting, which defines the amount of edges used in the
	 * graph-generation
	 * @return
	 * @throws Exception
	 */
	private int[] parseEdges() throws Exception {
		int[] edges = new int[3];
		int pos = getPosition("-e");
		if (pos < 0) {
			throw new Exception("Missing -e");
		}
		pos++;
		try {
			int i = 0;
			edges[i] = Integer.parseInt(input[pos + i++]);
			edges[i] = Integer.parseInt(input[pos + i++]);
			edges[i] = Integer.parseInt(input[pos + i++]);
			System.out.println(edges[2]);
		} catch (Exception ex) {
			try {
				System.out.println("only one edge-parameter.");
				edges[0] = Integer.parseInt(input[pos]);
				edges[1] = edges[0];
				edges[2] = edges[0];
			} catch (Exception ex2) {
				System.out.println("Could not read options after -e");
			}
		}
		return edges;
	}
	/**
	 * @return result Path as String
	 * @throws Exception
	 */
	private String parseResultPath() throws Exception {
		int pos = getPosition("-t");
		if (pos < 0) {
			System.out.println("Missing -t");
			throw new Exception("Missing -t");
		} else {
			pos++;
			return input[pos];
		}
	}

	private String parseGraphPath() throws Exception {
		int pos = getPosition("-p");
		if (pos < 0) {
			throw new Exception("Missing -p");
		} else {
			pos++;
			return input[pos];
		}
	}
	
	/**
	 * @return true, if given String is in the input-arguments, false otherwise
	 * @throws Exception
	 */
	private boolean isInInput(String string) {
		if (getPosition(string) < 0)
			return false;
		return true;
	}

	/**
	 * positive, if given String is in the input arguments, -1 otherwise
	 * @param string
	 * @return the position of the given String in the input-arguments
	 */
	private int getPosition(String string) {
		int pos;
		for (pos = 0; pos < input.length; pos++) {
			if (input[pos].equals(string))
				return pos;
		}
		return -1;
	}
}
