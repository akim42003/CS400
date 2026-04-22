import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Backend for the train-route webapp. Loads a Graphviz-style .dot file, then answers
 * path queries and the “meet in the middle” style question from the spec.
 */
public class Backend implements BackendInterface {

    // Matches our provided rail file: "A" -> "B" [minutes=123];
    private static final Pattern EDGE_LINE = Pattern.compile(
            "\\s*\"([^\"]*)\"\\s*->\\s*\"([^\"]*)\"\\s*\\[\\s*minutes\\s*=\\s*(\\d+)\\s*\\]\\s*;\\s*");

    private final GraphADT<String, Double> graph;
    /** Every city we've inserted on the last load — used to wipe the graph when we reload. */
    private final Set<String> citiesLoaded = new HashSet<>();

    public Backend(GraphADT<String, Double> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("GraphADT reference can't be null");
        }
        this.graph = graph;
    }

    /** Rip out whatever we built last time so loadGraphData starts clean. */
    private void clearLoadedGraph() {
        List<String> toRemove = new ArrayList<>(citiesLoaded);
        for (String city : toRemove) {
            graph.removeNode(city);
        }
        citiesLoaded.clear();
    }

    @Override
    public void loadGraphData(String filename) throws IOException {
        if (filename == null) {
            throw new IOException("filename was null");
        }

        clearLoadedGraph();

        Set<String> citiesThisPass = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()
                        || line.startsWith("digraph")
                        || line.equals("{")
                        || line.equals("}")) {
                    continue;
                }
                Matcher m = EDGE_LINE.matcher(line);
                if (!m.matches()) {
                    // not every line in a .dot is an edge; skip quietly
                    continue;
                }

                String from = m.group(1);
                String to = m.group(2);
                double minutes = Double.parseDouble(m.group(3));

                if (!graph.containsNode(from)) {
                    graph.insertNode(from);
                }
                if (!graph.containsNode(to)) {
                    graph.insertNode(to);
                }
                citiesThisPass.add(from);
                citiesThisPass.add(to);

                // insert updates the weight if the edge already exists
                graph.insertEdge(from, to, minutes);
            }
        }

        citiesLoaded.addAll(citiesThisPass);
    }

    @Override
    public List<String> getListOfAll() {
        // sort alphabetically — predictable, and nicer if this ever feeds a dropdown
        return new ArrayList<>(new TreeSet<>(citiesLoaded));
    }

    @Override
    public List<String> findLocationsOnShortestPath(String start, String end) {
        try {
            return new ArrayList<>(graph.shortestPathData(start, end));
        } catch (NoSuchElementException e) {
            // our interface wants an empty list instead of bubbling “no path” exceptions upward
            return new ArrayList<>();
        }
    }

    @Override
    public String getClosestLocationFromAll(List<String> starts) throws NoSuchElementException {
        if (starts == null || starts.isEmpty()) {
            throw new NoSuchElementException("need at least one starting city");
        }
        for (String s : starts) {
            if (s == null || !graph.containsNode(s)) {
                throw new NoSuchElementException("each start has to exist in the loaded graph");
            }
        }

        String best = null;
        double bestSum = Double.POSITIVE_INFINITY;

        for (String candidate : getListOfAll()) {
            double total = 0.0;
            boolean ok = true;
            for (String start : starts) {
                try {
                    total += graph.shortestPathCost(start, candidate);
                } catch (NoSuchElementException ex) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }

            int cmp = Double.compare(total, bestSum);
            if (best == null || cmp < 0 || (cmp == 0 && candidate.compareTo(best) < 0)) {
                bestSum = total;
                best = candidate;
            }
        }

        if (best == null) {
            throw new NoSuchElementException("no city is reachable from every start");
        }
        return best;
    }
}
