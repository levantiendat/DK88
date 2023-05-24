package com.example.dk88;

import java.util.*;

public class Graph {
    private ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
    private Map<String, List<String>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    public void addVertex(String vertex) {
        graph.put(vertex, new ArrayList<>());
    }

    public void addEdge(String startVertex, String endVertex) {
        graph.get(startVertex).add(endVertex);
    }

    public ArrayList<ArrayList<String>> printAllCycles(String startVertex) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        Set<String> onPath = new HashSet<>();
        dfs(startVertex, startVertex, visited, path, onPath, 0);
        return res;
    }

    private void dfs(String startVertex, String currentVertex, Set<String> visited, List<String> path, Set<String> onPath, int length) {
        visited.add(currentVertex);
        path.add(currentVertex);
        onPath.add(currentVertex);

        for (String neighbour : graph.get(currentVertex)) {
            if (neighbour.equals(startVertex) && length + 1 <= 5) {
                path.add(startVertex);
                res.add(new ArrayList<>(path));
                path.remove(path.size() - 1);
            } else if (!visited.contains(neighbour) && !onPath.contains(neighbour) && length + 1 <= 5) {
                dfs(startVertex, neighbour, visited, path, onPath, length + 1);
            }
        }

        onPath.remove(currentVertex);
        path.remove(path.size() - 1);
    }
}
