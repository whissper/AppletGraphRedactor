package sv.gui.supportElements;

import java.util.*;

/**
 * Graph that consist of "Collection" of connections.
 * <br />
 * Each connection is represented by "Current Node", 
 * and Nodes that are connected to the current one.
 * <br /><br />
 * <i>Graph representation in memory</i>
 * 
 * @author SAV2
 * @since 06.02.2013
 */
public class Graph 
{
  /**
   * "Map" of connections
   */
  private Map<Integer, Set<Integer>> connections = new TreeMap<Integer, Set<Integer>>();
  
  /**
   * add a new Node.
   * <br /><br />
   * NodeID = []
   * 
   * @param nodeID : int
   */
  public void addNode(int nodeID) 
  {
    if (!connections.containsKey(nodeID)) 
    {
      connections.put(nodeID, new TreeSet<Integer>());
    }
  }
  
  /**
   * remove current Node and whole info about connected Nodes with it
   * 
   * @param currNodeID : int
   */
  public void removeNode(int currNodeID) 
  {
    if (!connections.containsKey(currNodeID)) 
    {
      return;
    }
    
    /*
     *  connections.get(currNodeID) == currNodeID=[1, 2, 3, ...]
     *  i.e. Set of connected Nodes of current Node.
     *  
     *  for each ("connected Node of current Node")
     *  {
     *  	remove currNodeID form each Set of connected Nodes of current Node
     *  }
     *  remove current Node from "Map" of connections (i.e. remove from the Graph);
     *  
     */
    for (int connectedNodeID : connections.get(currNodeID)) 
    {
      connections.get(connectedNodeID).remove(currNodeID);
    }
    connections.remove(currNodeID);
  }
  
  /**
   * add new Edge.
   * 
   * @param currNodeID : int
   * @param connectedNodeID : int
   */
  public void addEdge(int currNodeID, int connectedNodeID)
  {
    addNode(currNodeID);
    addNode(connectedNodeID);
    connections.get(currNodeID).add(connectedNodeID);
    connections.get(connectedNodeID).add(currNodeID);
  }
  
  /**
   * remove current Edge that connects "CurrentNode" and "ConnectedNode"
   * 
   * @param currNodeID : int
   * @param connectedNodeID : int
   */
  public void removeEdge(int currNodeID, int connectedNodeID)
  {
	  //if ( CurrentNode is nonexistent OR ConnectedNode is nonexistent ) 
	  if( (!connections.containsKey(currNodeID)) || (!connections.containsKey(connectedNodeID)) )
	  {
		  return;
	  }
	  /*
	   *  if ( CurrentNode is nonexistent in the ConnectedNode Set 
	   *  	   OR 
	   *  	   ConnectedNode is nonexistent in the CurrentNode Set )
	   */
	  if( (!connections.get(currNodeID).contains(connectedNodeID)) ||
		  (!connections.get(connectedNodeID).contains(currNodeID)) )
	  {
		  return;
	  }
	  //removing "data" from Sets of both Nodes
	  connections.get(currNodeID).remove(connectedNodeID);
	  connections.get(connectedNodeID).remove(currNodeID);
  }
  
  public Map<Integer, Set<Integer>> getConnections()
  {
	  return this.connections;
  }
  
}