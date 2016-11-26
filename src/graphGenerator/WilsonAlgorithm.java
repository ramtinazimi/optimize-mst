/**
 * @author Alexandre Stüben
 */
package graphGenerator;

import java.util.Random;

public class WilsonAlgorithm {
	
	static Random randomGenerator;
	static int [] maze;
	/**
	 * Generates a random tree in form of an AdjacentList
	 * @param numberOfNodes
	 * @return
	 */
	static AdjacentList wilsonAlgo(int numberOfNodes){
		randomGenerator = new Random();
		AdjacentList usTree = new AdjacentList(numberOfNodes);
		maze = new int [numberOfNodes];
		//set all elements to -1, which means that they have not yet been added 
		//the tree
		for(int i=0; i< numberOfNodes; i++)
			maze[i]= -1;
		//number of nodes added to the tree
		int nodesAdded =1;
		// set a random element to -2, which means added to the tree
		maze[randomGenerator.nextInt(numberOfNodes)] =-2;
		while(nodesAdded<numberOfNodes){
			int begin = getRandomFreeNode( numberOfNodes, nodesAdded);
			randomWalk(begin, numberOfNodes);
			nodesAdded += addPathToUST(begin, usTree);
		}
		return usTree;
	}
	/**
	 * generates a random walk in the array "maze", with a random number as source.
	 * The number written in a field is the field where next step has been taken to. 
	 * Do the random walk, until a field of the tree (indicated by -2) has been found.
	 * @param begin
	 * @param numberOfNodes
	 */
	private static void randomWalk(int begin , int numberOfNodes){
		int mazePntr = begin;
		while (maze[mazePntr] != -2){
			int next = getRandomWithoutInput(mazePntr, numberOfNodes);
			maze[mazePntr]=next;
			mazePntr=next;
		}
	}
	
	/**
	 * Ads the path to the tree.
	 * @param begin
	 * @param usTree
	 * @return
	 */
	private static int addPathToUST(int begin, AdjacentList usTree){
		int mazePntr=begin;
		int nodesAdded = 0;
		while(maze[mazePntr]!= -2){
			if (maze[mazePntr] == -1)
				System.err.println("Tried to add a node out of the current path.");
			int a = mazePntr;
			int b = maze[mazePntr];
			usTree.setTrue(a, b);
			nodesAdded++;

			maze[mazePntr]=-2;
			mazePntr = b;
		}
		return nodesAdded;
	}
	
	/**
	 * Returns a random number in a Range from 0 to rangeExcl-1, without the 
	 * number input. 
	 * @param input
	 * @param rangeExcl
	 * @return
	 */
	private static int getRandomWithoutInput(int input, int rangeExcl){
		int val =  randomGenerator.nextInt(rangeExcl-1);
		if (val >= input)
			val++;
		return val;
	}
	
	/**
	 * Returns a random node, which has not been set to -2; hence has not been
	 * added to the tree.
	 * @param numberOfNodes
	 * @param nodesAdded
	 * @return
	 */
	public static int getRandomFreeNode(int numberOfNodes, int nodesAdded){
		int freeNodes = numberOfNodes - nodesAdded;
		int val =  randomGenerator.nextInt(freeNodes);
		int mazePnt=0, freeNodesPnt=0;
		while(freeNodesPnt <= val){
			if (maze[mazePnt] !=-2 ){
				if (freeNodesPnt == val)
					return mazePnt;
				freeNodesPnt++;	
			}
			mazePnt++;
		}
		return mazePnt;
	}
}
