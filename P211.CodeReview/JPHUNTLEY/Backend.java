import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;
    private List<String> nodeList;

    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
        this.nodeList = new ArrayList<>();
    }

    @Override
    public void loadGraphData(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) throw new IOException("File not found");

        for (String node : new ArrayList<>(nodeList)) {
            graph.removeNode(node);
        }
        nodeList.clear();

        try (Scanner sc = new Scanner(file)) {
            Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[minutes=(\\d+)\\]");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String u = matcher.group(1);
                    String v = matcher.group(2);
                    Double w = Double.parseDouble(matcher.group(3));

                    if (!graph.containsNode(u)) {
                        graph.insertNode(u);
                        nodeList.add(u);
                    }
                    if (!graph.containsNode(v)) {
                        graph.insertNode(v);
                        nodeList.add(v);
                    }
                    graph.insertEdge(u, v, w);
                }
            }
        }
    }

    @Override
    public List<String> getListOfAll() {
        return new ArrayList<>(this.nodeList);
    }

    @Override
    public List<String> findLocationsOnShortestPath(String start, String end) {
        try {
            return graph.shortestPathData(start, end);
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Double> findTimesOnShortestPath(String start, String end) {
        List<Double> times = new ArrayList<>();
        try {
            List<String> path = graph.shortestPathData(start, end);
            for (int i = 0; i < path.size() - 1; i++) {
                times.add(graph.getEdge(path.get(i), path.get(i + 1)));
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return times;
    }

    @Override
    public String getClosestLocationFromAll(List<String> starts) throws NoSuchElementException {
        if (starts == null || starts.isEmpty() || nodeList.isEmpty()) throw new NoSuchElementException();

        String bestNode = null;
        double minSum = Double.MAX_VALUE;

        for (String target : nodeList) {
            double currentSum = 0;
            boolean reachableFromAll = true;
            for (String s : starts) {
                try {
                    currentSum += graph.shortestPathCost(s, target);
                } catch (NoSuchElementException e) {
                    reachableFromAll = false;
                    break;
                }
            }
            if (reachableFromAll && currentSum < minSum) {
                minSum = currentSum;
                bestNode = target;
            }
        }
        if (bestNode == null) throw new NoSuchElementException();
        return bestNode;
    }
}
