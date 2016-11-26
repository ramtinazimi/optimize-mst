/**
 * @author Alexandre Stüben
 */
package mst;

import testing.Tests;

public class Main {
	/*
	 *case 1: open from path
	 *   1. -p path string
	 *   2. -t target file, default "result.txt"
	 *   3. -a the algorithms used, see below
	 *  
	 *case 2: Generate random graphs 
	 *	1. -t target file, default "result.txt"
	 *  2. -a the algorithms used, see below
	 *  3. -n settings, which defines the number of nodes, see below
	 *  4. -l number of iteration for a graph given size, 
	 *   	  results stores the average time of the graphs of a given size
	 *  	case 2.1 Generate random graphs given certain densities
	 *		5. -d setting for the densities, see below
	 *	 *  case 2.2 Generate random graphs given certain number of edges
	 *		5. -d setting for the edges, see below
	 *   3. -o options for the graph-generation, see below
	 *
	 * 	More specific:
	 *   -a <algorithm-abbreviation>*
	 *   	normal Kruskal: 	"nK"	 
     *   	filter Kruskal: 	"fK"
     *   	filter Kruskal plus:"fKp"
     *   	q Kruskal: 			"qK"
     *   	binaryheap Prim: 	"bP"
     *   	quickheap Prim: 	"qP"
     *   	or
     *   	all of the above: 	"all"
	 *
     *   -n case 1: one parameter sets the amount of nodes
     *   	case 2: three parameter, first and second for minimum/maximum 
     *   		number of nodes, third for the amount added in every iteration
     *   
     * 	 -d case 1: one parameter sets the density, all between 0.0 and 1.0
     *   	case 2: three parameter, first and second for minimum/maximum 
     *   		density, third for the amount added in every iteration
     *   		(Third can be a number to which the current density is multi-
     *   		plied in every iteration, if -log has been set)
     *   
     * 	 -e case 1: one parameter sets the amount of edges
     *   	case 2: three parameter, first and second for minimum/maximum 
     *   		number of edges, third for the amount added in every iteration
     *   		(Third can be a number to which the current amount of edges  is 
     *   		multiplied in every iteration, if -log has been set)
     */
	private static Tests tests;  
	public static void main(String[] args) {	
		InputParser input = new InputParser(args);
		try{
			tests = input.parseInput();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Parameter not set correctly.");
		    return;
		}
		tests.start();
		System.out.println("Finished.");
	}
}
