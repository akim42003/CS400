import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
		extends BaseGraph<NodeType, EdgeType>
		implements GraphADT<NodeType, EdgeType> {

	/**
	 * While searching for the shortest path between two nodes, a SearchNode
	 * contains data about one specific path between the start node and another
	 * node in the graph. The final node in this path is stored in its node
	 * field. The total cost of this path is stored in its cost field. And the
	 * predecessor SearchNode within this path is referenced by the predecessor
	 * field (this field is null within the SearchNode containing the starting
	 * node in its node field).
	 *
	 * SearchNodes are Comparable and are sorted by cost so that the lowest cost
	 * SearchNode has the highest priority within a java.util.PriorityQueue.
	 */
	protected class SearchNode implements Comparable<SearchNode> {
		public Node node;
		public double cost;
		public SearchNode pred;

		public SearchNode(Node startNode) {
			this.node = startNode;
			this.cost = 0;
			this.pred = null;
		}

		public SearchNode(SearchNode pred, Edge newEdge) {
			this.node = newEdge.succ;
			this.cost = pred.cost + newEdge.data.doubleValue();
			this.pred = pred;
		}

		public int compareTo(SearchNode other) {
			if (cost > other.cost)
				return +1;
			if (cost < other.cost)
				return -1;
			return 0;
		}
	}

	/**
	 * Constructor that sets the map that the graph uses.
	 */
	public DijkstraGraph() {
		super(new PlaceholderMap<>());
	}

	/**
	 * Insert a new directed edge with a non-negative weight into the graph. If
	 * an edge between pred and succ already exists, update the data stored in
	 * that edge to the new weight.
	 * 
	 * @param pred   is the data contained in the new edge's predecesor node
	 * @param succ   is the data contained in the new edge's succ node
	 * @param weight is the non-negative data to be stored in the new edge
	 * @return true if the edge could be inserted or updated, or false if the
	 *         pred or succ data are not found in any graph nodes or the weight
	 *         specified is negative.
	 */
	@Override
	public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
		if (weight.doubleValue() < 0)
			return false;
		return super.insertEdge(pred, succ, weight);
	}

	/**
	 * This helper method creates a network of SearchNodes while computing the
	 * shortest path between the provided start and end locations. The
	 * SearchNode that is returned by this method represents the end of the
	 * shortest path that is found: it's cost is the cost of that shortest path,
	 * and the nodes linked together through predecessor references represent
	 * all of the nodes along that shortest path (ordered from end to start).
	 *
	 * @param start the starting node for the path
	 * @param end   the destination node for the path
	 * @return SearchNode for the final end node within the shortest path
	 * @throws NoSuchElementException if either the start or the end node
	 *                                cannot be found, or there is no path from
	 *                                start node to end node
	 * @throws NullPointerException   if the start or end node are null
	 */
	protected SearchNode computeShortestPath(Node start, Node end) {
		PriorityQueue<SearchNode> pQueue = new PriorityQueue<>();
		PlaceholderMap<NodeType, SearchNode> visitedNodes = new PlaceholderMap<>();

		SearchNode startNode = new SearchNode(start);
		pQueue.add(startNode);

		while (!pQueue.isEmpty()) {
			SearchNode current = pQueue.remove();
			if (current.node.data == end.data) {
				return current;
			} else {
				visitedNodes.put(current.node.data, current);

				for (Edge edge : current.node.edgesLeaving) {
					if (!visitedNodes.containsKey(edge.succ.data)) {
						SearchNode unvisitedNeighbor = new SearchNode(current, edge);
						pQueue.add(unvisitedNeighbor);
					}

				}
			}

		}

		throw new NoSuchElementException();
	}

	/**
	 * Returns the list of data values from nodes along the shortest path
	 * from the node with the provided start value through the node with the
	 * provided end value. This list of data values starts with the start
	 * value, ends with the end value, and contains intermediary values in the
	 * order they are encountered while traversing this shortest path. This
	 * method uses Dijkstra's shortest path algorithm to find this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return list of data item from nodes along this shortest path
	 * @throws NoSuchElementException if either the start or the end node
	 *                                cannot be found, or there is no path from
	 *                                start node to end node
	 * @throws NullPointerException   if the start or end node are null
	 */
	public List<NodeType> shortestPathData(NodeType start, NodeType end) {
		List<NodeType> pathData = new ArrayList<>();

		if (this.nodes.containsKey(start) == false) {
			throw new NoSuchElementException();

		}
		if (this.nodes.containsKey(end) == false) {
			throw new NoSuchElementException();
		}

		Node startNode = this.nodes.get(start);
		Node endNode = this.nodes.get(end);

		SearchNode pathEnd = computeShortestPath(startNode, endNode);

		SearchNode currentNode = pathEnd;

		while (currentNode.pred != null) {
			pathData.add(currentNode.node.data);
			currentNode = currentNode.pred;
		}
		pathData.add(currentNode.node.data);

		return pathData.reversed();
	}

	/**
	 * Returns the cost of the path (sum over edge weights) of the shortest
	 * path from the node containing the start data to the node containing the
	 * end data. This method uses Dijkstra's shortest path algorithm to find
	 * this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return the cost of the shortest path between these nodes
	 * @throws NoSuchElementException if either the start or the end node
	 *                                cannot be found, or there is no path from
	 *                                start node to end node
	 * @throws NullPointerException   if the start or end node are null
	 */
	public double shortestPathCost(NodeType start, NodeType end) {
		if (this.nodes.containsKey(start) == false) {
			throw new NoSuchElementException();
		}
		if (this.nodes.containsKey(end) == false) {
			throw new NoSuchElementException();
		}
		Node startNode = this.nodes.get(start);
		Node endNode = this.nodes.get(end);
		SearchNode pathEnd = computeShortestPath(startNode, endNode);
		return pathEnd.cost;
	}

	@Test
	public void test1() {
		// replace with graph from lecture
		DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
		graph.insertNode("A");
		graph.insertNode("B");
		graph.insertNode("C");
		graph.insertNode("D");

		graph.insertEdge("A", "B", 2);
		graph.insertEdge("B", "D", 3);
		graph.insertEdge("A", "D", 10);
		graph.insertEdge("A", "C", 5);
		graph.insertEdge("C", "D", 1);

		List<String> path = graph.shortestPathData("A", "D");
		assertEquals(List.of("A", "B", "D"), path);
		assertEquals(5.0, graph.shortestPathCost("A", "D"));
	}

	@Test
	public void test2() {
		// case where no path exists between start and end
		DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

		graph.insertNode("A");
		graph.insertNode("B");

		assertThrows(NoSuchElementException.class, () -> {
			graph.shortestPathData("A", "B");
		});

	}

	@Test
	public void test3() {
		// end node does not exist
		DijkstraGraph<Integer, Integer> graph = new DijkstraGraph<>();

		graph.insertNode(1);
		graph.insertNode(2);

		assertThrows(NoSuchElementException.class, () -> {
			graph.shortestPathData(1, 3);
		});

	}

}
