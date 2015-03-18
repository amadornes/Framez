package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.amadornes.framez.util.Graph.INode;

public class Graph<T extends INode> {

    private HashMap<T, List<T>> graph;

    /**
     * Creates a graph from the given HashMap
     *
     * @param graph
     *            Contains every vertex as the keys, and every relation of the vertices as the value of each vertex
     */
    public Graph(HashMap<T, List<T>> graph) {

        this.graph = graph;
    }

    /**
     * Creates a graph from the given list of vertices with no relations
     *
     * @param vertices
     *            Initial vertices of the graph
     */
    public Graph(List<T> vertices) {

        graph = new HashMap<T, List<T>>();
        for (T vertex : vertices) {
            graph.put(vertex, new ArrayList<T>());
        }
    }

    /**
     * Creates an empty graph
     */
    public Graph() {

        this(new HashMap<T, List<T>>());
    }

    /**
     * Adds a vertex to the graph with no relations.<br>
     * If relations want to be added immediately, skip this and use {@link #addEdge(Object, Object)} directly.<br>
     * If the vertex exists, nothing happens.
     *
     * @param vertex
     *            The vertex to be added
     */
    public void addVertex(T vertex) {

        if (!graph.containsKey(vertex)) {
            graph.put(vertex, new ArrayList<T>());
        }
    }

    /**
     * Removes the given vertex and all of its edges
     *
     * @param vertex
     *            The vertex to be removed
     */
    public void removeVertex(T vertex) {

        graph.remove(vertex);
    }

    /**
     * Creates an edge between {@code vertex1} and {@code vertex2}, which are added to the graph if they aren't already there
     *
     * @param vertex1
     * @param vertex2
     */
    public void addEdge(T vertex1, T vertex2) {

        List<T> edges;

        // Add vertex2 to the list of vertex1's relations
        edges = getRelations(vertex1);
        if (edges == null)
            edges = new ArrayList<T>();
        if (!edges.contains(vertex2))
            edges.add(vertex2);
        graph.put(vertex1, edges);

        // Add vertex1 to the list of vertex2's relations
        edges = getRelations(vertex2);
        if (edges == null)
            edges = new ArrayList<T>();
        if (!edges.contains(vertex1))
            edges.add(vertex1);
        graph.put(vertex2, edges);
    }

    /**
     * Removes the edge that connects {@code vertex1} and {@code vertex2} if it exists.<br>
     * If any of the vertices aren't in the graph, this does nothing.
     *
     * @param vertex1
     * @param vertex2
     */
    public void removeEdge(T vertex1, T vertex2) {

        if (graph.containsKey(vertex1) && graph.containsKey(vertex2)) {
            // Remove v2 from v1's list
            List<T> edges = graph.get(vertex1);
            if (edges == null)
                edges = new ArrayList<T>();
            edges.remove(vertex2);
            graph.put(vertex1, edges);

            // Remove v1 from v2's list
            edges = graph.get(vertex2);
            if (edges == null)
                edges = new ArrayList<T>();
            edges.remove(vertex1);
            graph.put(vertex2, edges);
        }
    }

    /**
     * Returns a set with all the vertices in the graph.
     *
     * @return vertices
     */
    public List<T> getVertices() {

        return new ArrayList<T>(graph.keySet());
    }

    /**
     * Returns a list with all the vertices that {@code vertex} is connected with.
     *
     * @param vertex
     * @return vertices
     */
    public List<T> getRelations(T vertex) {

        addVertex(vertex);

        return graph.get(vertex);
    }

    /**
     * True if both vertices exist an there is an edge connecting them, false otherwise.
     *
     * @param vertex1
     * @param vertex2
     * @return
     */
    public boolean edgeExsists(T vertex1, T vertex2) {

        if (!graph.containsKey(vertex1) || !graph.containsKey(vertex2))
            return false;
        return graph.get(vertex1).contains(vertex2);
    }

    public Graph<T> getMST(T start) {

        List<T> visited = new ArrayList<T>();
        Graph<T> result = new Graph<T>();

        return deepSearch(start, result, visited);
    }

    private Graph<T> deepSearch(T start, Graph<T> result, List<T> visited) {

        if (!visited.contains(start))
            visited.add(start);
        result.addVertex(start);

        int neighbours = 0;
        int maxNeighbours = start.getMaxNeighbors();

        List<T> l = new ArrayList<T>();
        l.addAll(getRelations(start));
        l.removeAll(visited);
        Collections.sort(l, new NeighbourSorter<T>(this, visited));

        for (T vertex : l) {
            if (neighbours >= maxNeighbours)
                break;

            if (visited.contains(vertex))
                continue;

            visited.add(vertex);
            result.addEdge(start, vertex);
            deepSearch(vertex, result, visited);
            neighbours++;
        }

        l.clear();

        return result;
    }

    public static interface INode {

        public int getMaxNeighbors();
    }

    private static class NeighbourSorter<T extends INode> implements Comparator<T> {

        private Graph<T> graph;
        private List<T> visited;

        public NeighbourSorter(Graph<T> graph, List<T> visited) {

            this.graph = graph;
            this.visited = visited;
        }

        @Override
        public int compare(T a, T b) {

            List<T> lA = new ArrayList<T>();
            lA.addAll(graph.getRelations(a));
            lA.removeAll(visited);

            List<T> lB = new ArrayList<T>();
            lB.addAll(graph.getRelations(b));
            lB.removeAll(visited);

            int result = Integer.compare(lA.size(), lB.size()) + Integer.compare(b.getMaxNeighbors(), a.getMaxNeighbors()) * 2;

            lA.clear();
            lB.clear();

            return result;
        }

    }

}