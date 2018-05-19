package sv.newtonclient;

import java.util.ArrayList;

import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeTypes;
import sv.gui.additionalElements.NodesOfEdge;

/**
 * Scheme Parameters Storage
 * @author SAV2
 * @see <i>adaptive version for swing-applet</i>
 */
public class SchemeParamsStorage implements NodeTypes
{
	//Node
	private ArrayList<Integer> paramNodeRealID = new ArrayList<Integer>();
	
	private ArrayList<Double> paramNodeU = new ArrayList<Double>();
	private ArrayList<Double> paramNodeAngle = new ArrayList<Double>();
	private ArrayList<Integer> paramNodeType = new ArrayList<Integer>();
	private ArrayList<Double> paramNodePG = new ArrayList<Double>();
	private ArrayList<Double> paramNodeQG = new ArrayList<Double>();
	private ArrayList<Double> paramNodePL = new ArrayList<Double>();
	private ArrayList<Double> paramNodeQL = new ArrayList<Double>();
	
	//Edge
	private ArrayList<Integer> paramEdgeFrom=new ArrayList<Integer>();
    private ArrayList<Integer> paramEdgeTo=new ArrayList<Integer>();
    private ArrayList<Double> paramEdgeR=new ArrayList<Double>();
    private ArrayList<Double> paramEdgeX=new ArrayList<Double>();
    private ArrayList<Double> paramEdgeTap=new ArrayList<Double>();
    
    /**
     * Clear arrays
     */
    public void clearAll()
    {
    	//Node
    	paramNodeRealID.clear();
    	
    	paramNodeU.clear();
    	paramNodeAngle.clear();
    	paramNodeType.clear();
    	paramNodePG.clear();
    	paramNodeQG.clear();
    	paramNodePL.clear();
    	paramNodeQL.clear();
    	
    	//Edge
    	paramEdgeFrom.clear();
    	paramEdgeTo.clear();
    	paramEdgeR.clear();
    	paramEdgeX.clear();
    	paramEdgeTap.clear();
    }
    
    /**
     * add real ID of current Node into "SchemeParamStorage"
     * @param value : int
     */
    public void addNodeParamRealID(int value)
    {
    	this.paramNodeRealID.add(value);
    }
    
    /**
     * get real ID of current Node
     * @param index : int
     * @return real ID of current Node
     */
    public int getNodeParamRealID(int index)
    {
    	return this.paramNodeRealID.get(index);
    }
    
    /**
     * add Node parameter
     * @param nodeParam : NodeParameters
     * @param value : String
     */
    public void addNodeParameter(NodeParameters nodeParam, String value)
	{
		switch (nodeParam)
		{
		case Data_U:
			 this.paramNodeU.add( Double.parseDouble(value) );
			 break;
		case Data_Angle:
			 this.paramNodeAngle.add( Double.parseDouble(value) );
			 break;
		case Data_Type:
			 //if 3 then Slack; if 2 then PV; if others then PQ;
			 if(Integer.parseInt(value)==Slack)
			 {
				 this.paramNodeType.add(Slack);
			 }
			 else if(Integer.parseInt(value)==PV)
			 {
				 this.paramNodeType.add(PV);
			 }
			 else
			 {
				 this.paramNodeType.add(PQ);
			 }
			 break;
		case Data_PG:
			 this.paramNodePG.add( Double.parseDouble(value) );
			 break;
		case Data_QG:
			 this.paramNodeQG.add( Double.parseDouble(value) );
			 break;
		case Data_PL:
			 this.paramNodePL.add( Double.parseDouble(value) );
			 break;
		case Data_QL:
			 this.paramNodeQL.add( Double.parseDouble(value) );
			 break;
		default:
			 break;
		}
	}
	
    /**
     * add Edge parameter <i>(R/X/Tap)</i>
     * @param edgeParam : EdgeParameters
     * @param value : String
     */
    public void addEdgeParameter(EdgeParameters edgeParam, String value)
	{
		switch (edgeParam)
		{
		case Data_R:
			 this.paramEdgeR.add( Double.parseDouble(value) );
			 break;
		case Data_X:
			 this.paramEdgeX.add( Double.parseDouble(value));
			 break;
		case Data_Tap:
			 this.paramEdgeTap.add( Double.parseDouble(value) );
			 break;
		default:
			 break;
		}
	}
    
    /**
     * add Edge parameter <i>(From/To)</i>
     * <br /><br />
     * <i>FIRST_NODE</i> is equal to <i>From</i>
     * <br />
     * <i>LAST_NODE</i> is equal to <i>To</i>
     * @param edgeParam : NodesOfEdge
     * @param value : int
     */
    public void addEdgeParameter(NodesOfEdge edgeParam, int value)
	{
		switch (edgeParam)
		{
		case FIRST_NODE:
			 this.paramEdgeFrom.add(value);
			 break;
		case LAST_NODE:
			 this.paramEdgeTo.add(value);
			 break;
		default:
			 break;
		}
	}
    
    /**
     * get Edge parameter <i>(From/To)</i>
     * <br />
     * i.e. get idForRequest of linked nodes with current Edge
     * <br /><br />
     * <i>FIRST_NODE</i> is equal to <i>From</i>
     * <br />
     * <i>LAST_NODE</i> is equal to <i>To</i>
     * @param edgeParam : NodesOfEdge
     * @param index : int
     * @return current Index value of "paramEdgeFrom" (or "paramEdgeTo") arrays
     */
    public int getEdgeParameter(NodesOfEdge edgeParam, int index)
    {
    	int value = -1;
    	
    	switch (edgeParam)
		{
		case FIRST_NODE:
			 value = this.paramEdgeFrom.get(index);
			 break;
		case LAST_NODE:
			 value = this.paramEdgeTo.get(index);
			 break;
		default:
			 break;
		}
    	
    	return value;
    }
    
    /**
     * Check for all Node params size equality
     * @return size : int
     */
    private int getNodesCount()
    { 
        int size=paramNodeU.size();
        
        if(
                size!=paramNodeAngle.size()  ||
                size!=paramNodeType.size()   ||
                size!=paramNodePG.size()     ||
                size!=paramNodeQG.size()     ||
                size!=paramNodePL.size()     ||
                size!=paramNodeQL.size()
                )throw new RuntimeException("Node params size not equal");
        
        return size;
    }
    
    /**
     * Check for all Edge params size equality
     * @return size : int
     */
    private int getEdgesCount()
    { 
        int size=paramEdgeFrom.size();
        
        if(
                size!=paramEdgeTo.size()  ||
                size!=paramEdgeR.size()   ||
                size!=paramEdgeX.size()   ||
                size!=paramEdgeTap.size()
                )throw new RuntimeException("Edge params size not equal");
        
        return size;
    }
    
    /**
     * Prepare XML message to send to server
     * @return string without "\n" at the end
     * 
     */
    public String prepareXMLMessage()
    {
        
        int nodeCount=getNodesCount();
        int edgeCount=getEdgesCount();
        
        
        String message ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
        				"<Scheme>"+
        					"<Nodes>";
		
        for(int q=0; q<nodeCount; q++)
        {
            message+="<Node U=\"" +  paramNodeU.get(q)+
                     "\" Angle=\"" + paramNodeAngle.get(q) +
                     "\" Type=\"" +  paramNodeType.get(q) +
                     "\" Pg=\"" +    paramNodePG.get(q)+
                     "\" Qg=\"" +    paramNodeQG.get(q)+
                     "\" Pl=\"" +    paramNodePL.get(q)+
                     "\" Ql=\"" +    paramNodeQL.get(q)+
                     "\"/>";
        }

        message+="</Nodes>"+
        		 "<Branches>";
		
        for(int q=0; q<edgeCount; q++)
        {
            message+="<Branch From=\"" + paramEdgeFrom.get(q)+
                     "\" To=\""		   + paramEdgeTo.get(q)+
                     "\" R=\""		   + paramEdgeR.get(q)+
                     "\" X=\""		   + paramEdgeX.get(q)+
                     "\" Tap=\""	   + paramEdgeTap.get(q)+
                     "\"/>";
        }

        message+="</Branches>"+
        	"</Scheme>";
        
        return message;        
    }
}
