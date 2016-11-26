/**
 * @Author: Alexandre Stüben, Ramtin Azimi, Florian Gl�ser
 */
package mst;

public class Edge implements Comparable<Edge>, Prim.hasID{
	public double weight;
	public final int a, b;
	public int priority;

	private static int timestamp = 0;

	public Edge(int a, int b, double w) {
		assert a != b;
		if ((a < 0) || (b < 0))
			throw new IndexOutOfBoundsException("Node name must be positive");
		if (Double.isNaN(w))
			throw new IllegalArgumentException("Weight is NaN");

		this.a = a;
		this.b = b;

		this.weight = w;
		this.priority = timestamp;
		timestamp++;
	}

	public int getA() {
		return this.a;
	}

	public int getB() {
		return this.b;
	}

	public double getWeight() {
		return weight;
	}

	public int priority() {
		return priority;
	}

	public void setWeight(double weight) {
		if (weight >= 0) {
			this.weight = weight;
		} else {
			this.weight = 0;
		}
	}

	/**
	 * This is for getting the other vertex which is different from the input
	 * vertex.
	 * 
	 * @param vertex
	 * @return other vertex
	 */
	public int other(int vertex) {
		if (vertex == a)
			return b;
		if (vertex == b)
			return a;
		else
			throw new IllegalArgumentException("illegal endpoint");
	}

	/**
	 * Returns one of the vertexes.
	 * 
	 * @return
	 */
	public int either() {
		return a;
	}

	/**
	 * Compares edges regarding their weights.
	 */
	public int compareTo(Edge that) {
		if (this.getWeight() < that.getWeight())
			return -1;
		else if (this.getWeight() > that.weight)
			return 1;
		else {
			if (this.priority() < that.priority()) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	/**
	 * Outputs the vertex of the edges and the edge weight.
	 */
	public String toString() {
		return String.format("%d-%d %.5f", a, b, weight);
	}

	public Edge clone() {
		System.out.println("false");
		return new Edge(this.a, this.b, this.weight);
	}
	
	public int getID(){
		return timestamp;
	}
}
