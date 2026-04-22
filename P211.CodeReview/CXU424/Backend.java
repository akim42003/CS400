import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Backend implements BackendInterface {
	
		private GraphADT<String,Double> graph = null;
		private List<String> nodes;
	
		/** Implementing classes should support the constructor below.
	     * @param graph object to store the backend's graph data
	     */
		public Backend(GraphADT<String,Double> graph) {
	    	this.graph = graph;
	    	nodes = new ArrayList<>();
	    }

	    /**
	     * Loads graph data from a dot file. If a graph was previously loaded, this
	     * method should first delete the contents (nodes and edges) of the existing
	     * graph before loading a new one.
	     * @param filename the path to a dot file to read graph data from
	     * @throws IOException if there was any problem reading from this file
	     */
		@Override
	    public void loadGraphData(String filename) throws IOException {
			//clear existing graph
			for(String node : nodes) {
				graph.removeNode(node);
			}
			nodes.clear();
			//read
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(filename));
				String line;
				while((line = reader.readLine()) != null) {
					line = line.trim();
					//pass empty line
					if(line.isEmpty() || line.startsWith("//") || line.startsWith("#")) {
						continue;
					}
					//verify whether it is a node
					if(line.contains("->")) {
						int arrowPos = line.indexOf("->");
						String before = extractNodeFromLine(line, 0, arrowPos);
						if(before == null) continue;
						int afterArrow = arrowPos + 2;
						int destEnd = findNodeEnd(line, afterArrow);
						if(destEnd == -1) continue;
						String dest = extractNodeFromLine(line, afterArrow, destEnd);
						if(dest == null) continue;
						double weight = 1.0; //default weight: 1.0
						int bracketStart = line.indexOf('[', destEnd);
						if(bracketStart != -1) {
							int bracketEnd = line.indexOf(']', bracketStart);
							if(bracketEnd != -1) {
								String attrPart = line.substring(bracketStart + 1, bracketEnd);
								weight = extractWeightFromAttr(attrPart);
							}
						}
						//insert node and edge
						if(graph.insertNode(before)) {
							nodes.add(before);
						}
						if(graph.insertNode(dest)) {
							nodes.add(dest);
						}
						if(weight > 0) {
							graph.insertEdge(before, dest, weight);
						}
					} else {
						continue;
					}
				}
			} finally {
				if(reader != null) {
					reader.close();
				}
			}
	    }
		
		private String extractNodeFromLine(String line, int start, int end) {
			int i = start;
			while(i < end && Character.isWhitespace(line.charAt(i))) {
				i++;
			}
			if(i >= end) return null;
			if(line.charAt(i) == '"') {
				int quoteEnd = line.indexOf('"', i + 1);
				if(quoteEnd == -1 || quoteEnd >= end) return null;
				return line.substring(i + 1, quoteEnd);
			} else {
				int j = i;
				while(j < end && !Character.isWhitespace(line.charAt(j)) && line.charAt(j) != '[' && line.charAt(j) != ';' && !(j + 1 < end && line.charAt(j) == '-' && line.charAt(j + 1) == '>')) {
					j++;
				}
				return line.substring(i, j);
			}
		}
		
		private int findNodeEnd(String line, int start) {
			int i = start;
			int len = line.length();
			while(i < len) {
				char c = line.charAt(i);
				if(c == '"') {
					i = line.indexOf('"', i + 1);
					if(i == -1) return -1;
					i++;
					continue;
				}
				if(Character.isWhitespace(c) || c == '[' || c == ';') {
					return i;
				}
				i++;
			}
			return len;
		}
		
		private double extractWeightFromAttr(String attrPart) {
			int weightIndex = attrPart.indexOf("weight=");
			if(weightIndex == -1) return 1.0;
			int start = weightIndex + 7;
			while(start < attrPart.length() && Character.isWhitespace(attrPart.charAt(start))) {
				start++;
			}
			int end = start;
			while(end < attrPart.length()) {
				char c = attrPart.charAt(end);
				if(c == ',' || Character.isWhitespace(c)) {
					break;
				}
				end++;
			}
			String numStr = attrPart.substring(start, end).trim();
			try {
				return Double.parseDouble(numStr);
			} catch(Exception e) {
				return 1.0;
			}
		}

	    /**
	     * Returns a list of all locations in the graph.
	     * @return list of all location names
	     */
		@Override
	    public List<String> getListOfAll() {
	    	return nodes;
	    }

	    /**
	     * Return the sequence of locations along the shortest path from start to 
	     * end, or an empty list if no such path exists.
	     * @param start the start of the path
	     * @param end the end of the path
	     * @return a list with the nodes along the shortest path from start to end,
	     *         or an empty list if no such path exists
	     */
		@Override
	    public List<String> findLocationsOnShortestPath(String start, String end) {
	    	return graph.shortestPathData(start, end);
	    }

	    /**
	     * Returns the location that can be reached from all of the specified start 
	     * locations in the shortest time: minimizing the sum of the times from 
	     * each start location.
	     * @param starts the list of locations to minimize times from
	     * @return the location that can be reached in the shortest total time 
	     *         from all of the specified start locations
	     * @throws NoSuchElementException if there is no location that can be 
	     *         reached from all start locations, or if any start locations does
	     *         not exist within the graph
	     */
		@Override
	    public String getClosestLocationFromAll(List<String> starts) throws NoSuchElementException {
	    	for(String s : starts) {
	    		if(!graph.containsNode(s)) {
	    			throw new NoSuchElementException("No such start element exists in the graph.");
	    		}
	    	}
	    	String bestNode = null;
    		double bestTotal = Double.POSITIVE_INFINITY;
    		for(String candidate : nodes) {
    			double total = 0.0;
    			boolean reachable = true;
    			for(String start : starts) {
    				try {
    					total += graph.shortestPathCost(start, candidate);
    				} catch(NoSuchElementException nsee) {
    					reachable = false;
    					break;
    				}
    			}
    			if(reachable && total < bestTotal) {
    				bestTotal = total;
    				bestNode = candidate;
    			}
    		}
    		if(bestNode == null) {
    			throw new NoSuchElementException("No such start element exists in the graph.");
    		}
    		return bestNode;
	    }

}
