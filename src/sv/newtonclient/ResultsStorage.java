package sv.newtonclient;

import java.util.ArrayList;

import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodeParameters;

/**
 * Result Storage
 * <br />
 * <i>(storage for calculated results)</i>
 * @author SAV2
 * @see <i>adaptive version for swing-applet</i>
 */
public class ResultsStorage 
{
	//Node results (start):
	/**
	 * calculated Voltage level <i>(data)</i>
	 */
	private ArrayList<Double> resNodeU = new ArrayList<Double>();
	
	/**
	 * calculated phase Angle <i>(data)</i>
	 */
    private ArrayList<Double> resNodeAngle = new ArrayList<Double>();
    
    /** 
     * calculated Active Power of generation <i>(data)</i>
     */
    private ArrayList<Double> resNodePG = new ArrayList<Double>();
    
    /**
     * calculated Reactive Power of generation <i>(data)</i>
     */
    private ArrayList<Double> resNodeQG = new ArrayList<Double>();
    //Node results (end);
    
    //Edge results (start):
    /**
     * calculated Active power flow at the beginning of Edge <i>(data)</i>
     */
    private ArrayList<Double> resEdgePF = new ArrayList<Double>();
    
    /**
     * calculated Reactive power flow at the beginning of Edge <i>(data)</i>
     */
    private ArrayList<Double> resEdgeQF = new ArrayList<Double>();
    
    /**
     * calculated Active power flow at the end of Edge <i>(data)</i>
     */
    private ArrayList<Double> resEdgePT = new ArrayList<Double>();
    
    /**
     * calculated Reactive power flow at the end of Edge <i>(data)</i>
     */
    private ArrayList<Double> resEdgeQT = new ArrayList<Double>();
    //Edge results (end);
    
    /**
     * Status of the calculation, that
     * contains short explanation of performed analysis
     */
    private String status = new String();
    
    /**
     * Return value of the calculation
     * -1 - not inited
     *  0 - ok
     */
    private int retVal=-1;
    
    /**
     * Clear arrays
     * 
     */
    public void clearAll()
    {   
        status="";
        retVal=-1;
        
        resNodeU.clear();
        resNodeAngle.clear();
        resNodePG.clear();
        resNodeQG.clear();
        
        resEdgePF.clear();
        resEdgeQF.clear();
        resEdgePT.clear();
        resEdgeQT.clear();
    }
    
    /**
     * add calculated Node parameter in result storage
     * @param resNodeParam : NodeParameters
     * @param value : String
     */
    public void addResNode(NodeParameters resNodeParam, String value)
	{
		switch (resNodeParam)
		{
		case Data_U:
			 this.resNodeU.add( Double.parseDouble(value) );
			 break;
		case Data_Angle:
			 this.resNodeAngle.add( Double.parseDouble(value) );
			 break;
		case Data_PG:
			 this.resNodePG.add( Double.parseDouble(value) );
			 break;
		case Data_QG:
			 this.resNodeQG.add( Double.parseDouble(value) );
			 break;
		default:
			 break;
		}
	}
    
    /**
     * add calculated Edge parameter in result storage
     * @param resEdgeParam : EdgeResults
     * @param value : String
     */
    public void addResEdge(EdgeResults resEdgeParam, String value)
    {
    	switch(resEdgeParam)
    	{
    	case PF:
    		this.resEdgePF.add( Double.parseDouble(value) );
    		break;
    	case QF:
    		this.resEdgeQF.add( Double.parseDouble(value) );
    		break;
    	case PT:
    		this.resEdgePT.add( Double.parseDouble(value) );
    		break;
    	case QT:
    		this.resEdgeQT.add( Double.parseDouble(value) );
    		break;
    	default:
    		break;
    	}
    }
    
    //get arrays (start):
    /**
     * get calculated Voltage level <i>(data)</i>
     * @return resNodeU : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResNodeU()
    {
    	return this.resNodeU;
    }
    
    /**
     * get calculated phase Angle <i>(data)</i>
     * @return resNodeAngle : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResNodeAngle()
    {
    	return this.resNodeAngle;
    }
    
    /**
     * get calculated Active Power of generation <i>(data)</i>
     * @return resNodePG : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResNodePG()
    {
    	return this.resNodePG;
    }
    
    /**
     * get calculated Reactive Power of generation <i>(data)</i>
     * @return resNodeQG : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResNodeQG()
    {
    	return this.resNodeQG;
    }
    
    /**
     * get calculated Active power flow at the beginning of Edge <i>(data)</i>
     * @return resEdgePF : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResEdgePF()
    {
    	return this.resEdgePF;
    }
    
    /**
     * get calculated Reactive power flow at the beginning of Edge <i>(data)</i>
     * @return resEdgePF : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResEdgeQF()
    {
    	return this.resEdgeQF;
    }
    
    /**
     * get calculated Active power flow at the end of Edge <i>(data)</i>
     * @return resEdgePF : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResEdgePT()
    {
    	return this.resEdgePT;
    }
    
    /**
     * get calculated Reactive power flow at the end of Edge <i>(data)</i>
     * @return resEdgePF : ArrayList&lt;Double&gt;
     */
    public ArrayList<Double> getResEdgeQT()
    {
    	return this.resEdgeQT;
    }
    //get arrays (end);
    
    /**
     * set Return value of the calculation
     * @param val : int
     */
    public void setRetVal(int val){
        this.retVal = val;
    }
    
    /**
     * get Return value of the calculation
     * @return retVal : int
     */
    public int getRetVal(){
        return this.retVal;
    }
    
    /**
     * set Status of the calculation
     * @param status : String
     */
    public void setStatus(String status){
        this.status = status;
    }
    
    /**
     * get Status of the calculation
     * @return status : String
     */
    public String getStatus(){
        return this.status;
    }
    
}
