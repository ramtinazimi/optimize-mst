/**
 * Code is based on http://algs4.cs.princeton.edu/43mst/PrimMST.java.html
 */

package Prim;

import java.util.LinkedList;
import java.util.Queue;

import mst.Edge;
import mst.Graph;

public class PrimQH {

    private int V;
    private Edge[] edgeTo;
    private QuickHeap<WeightedVertex> qh;
    private WeightedVertex[] vertices;
    private boolean[] marked;

    private EdgeWeightedGraph ewGraph;
    public PrimQH(Graph graph) {

    	graphToEdgeWeightGraph(graph);
        V = graph.getNumOfNodes();
        edgeTo = new Edge[V];
        qh = new QuickHeap<>(V);
        vertices = new WeightedVertex[V];
        marked = new boolean[V];
        
      
    }
    
    private void graphToEdgeWeightGraph(Graph graph){
    	ewGraph = new EdgeWeightedGraph(graph.getNumOfNodes());
    	for(Edge e : graph.edges)
    		ewGraph.addEdge(e);
    }
    
    public void start(){
    	
    	for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                prim(ewGraph, v);
            }
        }
    }
    private void prim(EdgeWeightedGraph g, int s) {
        for (int v = 0; v < V; v++) {
            vertices[v] = new WeightedVertex(v, Double.POSITIVE_INFINITY);
        }
        vertices[s].setDistance(0);
        qh.add(vertices[s]);
        while (!qh.isEmpty()) {
            WeightedVertex u = qh.extractMin();
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
                if (qh.contains(vertices[v])) {
                    qh.decreaseKey(temp, v);
                } else {
                    qh.add(temp);
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

    public static void main(String[] args){
    	
    }
}
