/**
 * Code is from http://algs4.cs.princeton.edu/43mst/EdgeWeightedGraph.java.html but slightly modified
 */

package Prim;

import java.util.ArrayList;

import mst.Edge;

public class EdgeWeightedGraph {
	private final int V;
	private int E;
	private ArrayList<Edge>[] adj;

	public EdgeWeightedGraph(int V) {
		if (V < 0)
			throw new IllegalArgumentException(
					"Number of vertices must be positive");
		this.V = V;
		this.E = 0;
		adj = (ArrayList<Edge>[]) new ArrayList[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<Edge>();
		}
	}

	public EdgeWeightedGraph(int V, int E) {
		this(V);
		if (E < 0)
			throw new IllegalArgumentException(
					"Number of edges must be positive");
		for (int i = 0; i < E; i++) {
			int v = (int) (Math.random() * V);
			int w = (int) (Math.random() * V);
			double weight = Math.round(100 * Math.random()) / 100.0;
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex" + v
					+ " is not between 0 and " + (V - 1));
	}

	public void addEdge(Edge e) {
		int v = e.either();
		int w = e.other(v);
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}

	public Iterable<Edge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	public Iterable<Edge> edges() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		for (int v = 0; v < V; v++) {
			int selfLoops = 0;
			for (Edge e : adj[v]) {
				if (e.other(v) > v) {
					list.add(e);
				} else if (e.other(v) == v) {
					if (selfLoops % 2 == 0)
						list.add(e);
					selfLoops++;
				}
			}
		}
		return list;
	}

        @Override
	public String toString() {
		String NEWLINE = System.getProperty("line.separator");
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge e : adj[v]) {
				s.append(e + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}
}