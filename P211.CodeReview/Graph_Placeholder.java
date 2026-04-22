import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Stand-in graph until the real GraphADT shows up later in the semester.
 * I rewired this one so it actually holds nodes/edges and can run Dijkstra;
 * the old version couldn't insert edges, which made loading a .dot file pointless.
 */
public class Graph_Placeholder implements GraphADT<String, Double> {

    // adjacency list: city -> (neighbor -> travel time in minutes)
    private final Map<String, Map<String, Double>> neighbors = new HashMap<>();

    @Override
    public boolean insertNode(String data) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (neighbors.containsKey(data)) {
            return false;
        }
        neighbors.put(data, new HashMap<>());
        return true;
    }

    @Override
    public boolean removeNode(String data) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (!neighbors.containsKey(data)) {
            return false;
        }
        neighbors.remove(data);
        // drop incoming edges too
        for (Map<String, Double> outs : neighbors.values()) {
            outs.remove(data);
        }
        return true;
    }

    @Override
    public boolean containsNode(String data) {
        return data != null && neighbors.containsKey(data);
    }

    @Override
    public int getNodeCount() {
        return neighbors.size();
    }

    @Override
    public boolean insertEdge(String pred, String succ, Double weight) {
        if (pred == null || succ == null || weight == null) {
            throw new NullPointerException();
        }
        if (weight.doubleValue() <= 0.0) {
            return false;
        }
        if (!neighbors.containsKey(pred) || !neighbors.containsKey(succ)) {
            return false;
        }
        neighbors.get(pred).put(succ, weight.doubleValue());
        return true;
    }

    @Override
    public boolean removeEdge(String pred, String succ) {
        if (pred == null || succ == null) {
            throw new NullPointerException();
        }
        if (!neighbors.containsKey(pred)) {
            return false;
        }
        return neighbors.get(pred).remove(succ) != null;
    }

    @Override
    public boolean containsEdge(String pred, String succ) {
        if (pred == null || succ == null) {
            throw new NullPointerException();
        }
        Map<String, Double> outs = neighbors.get(pred);
        return outs != null && outs.containsKey(succ);
    }

    @Override
    public Double getEdge(String pred, String succ) {
        if (pred == null || succ == null) {
            throw new NullPointerException();
        }
        Map<String, Double> outs = neighbors.get(pred);
        if (outs == null || !outs.containsKey(succ)) {
            throw new NoSuchElementException();
        }
        return outs.get(succ);
    }

    @Override
    public int getEdgeCount() {
        int count = 0;
        for (Map<String, Double> outs : neighbors.values()) {
            count += outs.size();
        }
        return count;
    }

    @Override
    public List<String> shortestPathData(String start, String end) {
        if (start == null || end == null) {
            throw new NullPointerException();
        }
        if (!containsNode(start) || !containsNode(end)) {
            throw new NoSuchElementException();
        }
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // min-heap by current best distance; stale entries are ignored after we mark visited
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(u -> dist.get(u)));

        for (String v : neighbors.keySet()) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }
        dist.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (visited.contains(u)) {
                continue;
            }
            visited.add(u);
            if (u.equals(end)) {
                break;
            }
            for (Map.Entry<String, Double> e : neighbors.get(u).entrySet()) {
                String v = e.getKey();
                double w = e.getValue();
                double alt = dist.get(u) + w;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        if (!visited.contains(end) || dist.get(end).isInfinite()) {
            throw new NoSuchElementException();
        }

        // rebuild path start -> end
        List<String> rev = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            rev.add(cur);
            if (cur.equals(start)) {
                break;
            }
            cur = prev.get(cur);
        }
        if (rev.isEmpty() || !rev.get(rev.size() - 1).equals(start)) {
            throw new NoSuchElementException();
        }
        Collections.reverse(rev);
        return rev;
    }

    @Override
    public double shortestPathCost(String start, String end) {
        List<String> path = shortestPathData(start, end);
        double sum = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += getEdge(path.get(i), path.get(i + 1));
        }
        return sum;
    }
}
