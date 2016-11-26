/**
 * Code is based on http://algs4.cs.princeton.edu/43mst/PrimMST.java.html
 */

package Prim;

import java.util.LinkedList;
import java.util.Queue;

import mst.Edge;
import mst.Graph;

public class PrimBH {

    private int V;
    private Edge[] edgeTo;
    private BinaryHeap<WeightedVertex> bh;
    private WeightedVertex[] vertices;
    private boolean[] marked;
    EdgeWeightedGraph ewGraph;

    public PrimBH(Graph graph) {
    	graphToEdgeWeightGraph(graph);
        V = graph.getNumOfNodes();
        edgeTo = new Edge[V];
        bh = new BinaryHeap<>(V);
        vertices = new WeightedVertex[V];
        marked = new boolean[V];

    }
    
    public void start(){
    	for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                prim(ewGraph, v);
            }
        }
    }
    
    private void graphToEdgeWeightGraph(Graph graph){
    	ewGraph = new EdgeWeightedGraph(graph.getNumOfNodes());
    	for(Edge e : graph.edges)
    		ewGraph.addEdge(e);
    }

    private void prim(EdgeWeightedGraph g, int s) {
        for (int v = 0; v < V; v++) {
            vertices[v] = new WeightedVertex(v, Double.POSITIVE_INFINITY);
        }
        vertices[s].setDistance(0);
        bh.insert(vertices[s]);
        while (!bh.isEmpty()) {
            WeightedVertex u = bh.delMin();
            scan(g, u.getV());
        }

    }

    private void scan(EdgeWeightedGraph g, int u) {
        marked[u] = true;
        for (Edge e : g.adj(u)) {
            int v = e.other(u);
            if (marked[v]) {
                continue;
            }
            if (e.getWeight() < vertices[v].getDistance()) {
                edgeTo[v] = e;
                WeightedVertex temp = new WeightedVertex(v, e.getWeight());
                if (bh.contains(vertices[v])) {
                    bh.decreaseKey(vertices[v], temp);
                } else {
                    bh.insert(temp);
                }
                vertices[v] = temp;
            }
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new LinkedList<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }

    public double weight() {
        double weight = 0.0;
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                weight += e.getWeight();
            }
        }
        return weight;
    }

}
