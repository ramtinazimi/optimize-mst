package Kruskal;

import mst.Edge;

/**
 * This class provides three different pivot strategies.
 * 
 * 
 * @author Ramtin
 * 
 */
public class PivotStrategy {

	static int rand1;
	static int rand2;
	static int rand3;

	// ***********************************************************************
	// ***************************PIVOT STRATEGIES****************************
	// ***********************************************************************
	/**
	 * Takes the median of three randomly picked numbers within the bounds. It
	 * could be possible that one number is picked twice or thice, but the
	 * larger the array the lesser the probability it is to pick one number
	 * multiple times.
	 * 
	 * @param edges
	 * @param left
	 * @param right
	 * @return
	 */
	public static int medianOfRandom3(Edge[] edges, int left, int right) {

		rand1 = PartitionStrategy.randInt(left, right);
		rand2 = PartitionStrategy.randInt(left, right);
		rand3 = PartitionStrategy.randInt(left, right);

		return middleValueOfTriple(edges, rand1, rand2, rand3);

	}

	/**
	 * Median of first and second.
	 * 
	 * @param edges
	 * @param left
	 * @param right
	 * @return
	 */
	public static int medianOf3(Edge[] edges, int left, int right) {

		return middleValueOfTriple(edges, left, right, left
				+ (right - left) / 2);
	}


	public static int randomPivot(int left, int right) {
		return PartitionStrategy.randInt(left, right);
	}

	// ***********************************************************************
	// ***********************FUNCTIONS FOR HELPING THE PIVOT STRATEGIES******
	// ***********************************************************************

	/**
	 * 
	 * This functions calculates efficiently the middle value of three numbers.
	 * 
	 * Source:
	 * http://stackoverflow.com/questions/1582356/fastest-way-of-finding-
	 * the-middle-value-of-a-triple
	 * 
	 * @param array
	 * @param randomIndexA
	 * @param randomIndexB
	 * @param randomIndexC
	 * @return
	 */
	public static int middleValueOfTriple(Edge[] array, int randomIndexA,
			int randomIndexB, int randomIndexC) {

		if (array[randomIndexA].weight > array[randomIndexB].weight) {
			if (array[randomIndexB].weight > array[randomIndexC].weight) {
				// b is the middle value
				return randomIndexB;
			} else if (array[randomIndexA].weight > array[randomIndexC].weight) {
				// c is the middle value
				return randomIndexC;
			} else {
				// a is the middle value
				return randomIndexA;
			}
		} else {
			if (array[randomIndexA].weight > array[randomIndexC].weight) {
				// a is the middle value
				return randomIndexA;
			} else if (array[randomIndexB].weight > array[randomIndexC].weight) {
				// c is the middle value
				return randomIndexC;
			} else {
				// b is the middle value
				return randomIndexB;
			}
		}
	}

	



	

}
