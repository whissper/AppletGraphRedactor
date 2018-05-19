package sv.gui.graphElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.*;

import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
import sv.gui.additionalElements.ConnectorTypes;
import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodesOfEdge;

/**
 * Edge
 * <p />
 * Graph Edge with unique #id
 * 
 * @version 1.0.0
 * @since 25.01.2013
 * @author SAV2
 *
 */

public class Edge extends JComponent
{		
	//connectors coordinates of each Node component (firstNode and lastNode)
	private int x0;
	private int y0; 
	private int x1; 
	private int y1;
	
	/**
	 * selected state
	 */
	private boolean selected = false;
	
	/**
	 * Selection zone thickness
	 */
	public static final int SELECTION_ZONE_THICKNESS = 10;
	
	//coordinates of start and end points of Edge's line relatively of its parent component
 	public int startEdgeParentX;
 	public int startEdgeParentY;
 	public int endEdgeParentX;
 	public int endEdgeParentY;
	
	/**
	 * shift rate of the next arrow
	 */
	private static final int NEXT_ARROW_SHIFT_RATE = 3;
	
	/**
	 * length of arrow
	 */
	private static final int ARROW_LENGTH = 8;
	
	/**
	 * generator's circle radius
	 */
	private static final int GEN_CIRCLE_RAD = 12;
	
	/**
	 * rounding precision
	 */
	private static final double ROUND_PREC = 1000.00;
	
	/**
	 * "switch-indicator"
	 */
	private boolean enabled = true;
	
	//connectors
	private ConnectorTypes firstNodeConnector;
	private ConnectorTypes lastNodeConnector;
	
	//special buffered info about "last-connectors"
	private ConnectorTypes buffFirstNodeConnector;
	private ConnectorTypes buffLastNodeConnector;
	
	//connector multipliers (0-firstNode 1-lastNode)
	private int connectorMul0;
	private int connectorMul1;
	
	//current shift of connectors (0-firstNode 1-lastNode)
	private int currentShift0;
	private int currentShift1;
	
	/**
	 * size of indicator "rectangle" side
	 */
	private final static int RECT_SIDE = 10;
	
	/**
	 * parent component panel
	 */
	private ComponentPanel parent;
	
	/**
	 * Component Shift -- shift of the component (which contains Edge element)
	 * a bit closer towards Node center.
	 */
	private static final int COMPONENT_SHIFT = 8;
	
	/**
	 * connector length
	 */
	private static final int CONNECTOR_LENGTH = 25;
	
	/**
	 * connector shift
	 */
	private static final int CONNECTOR_SHIFT = 15;
	
	/**
	 * the first bound Node
	 */
	private Node firstNode;
	
	/**
	 * the second bound Node
	 */
	private Node lastNode;
	
	/**
	 * Edge #id
	 */
	private int id;
	
	//------------------------------------------------------------------Edge parameters-(start):
	private double paramEdgeR = 0.0;
	private double paramEdgeX = 0.0;
	private double paramEdgeTap = 1;
	
	private double resEdgePF = 0.0;
	private double resEdgeQF = 0.0;
	private double resEdgePT = 0.0;
	private double resEdgeQT = 0.0;
	//------------------------------------------------------------------Edge parameters-(end);
	
	//constructor start:
	public Edge(Node firstNode, Node lastNode, int id)
	{
		super();
		
		this.setOpaque(true);
		
		this.firstNode = firstNode;
		this.lastNode = lastNode;
		this.id = id;
		
		this.bindEdge();
	}
	//constructor end;
	
	/**
	 * set selected state of Edge-component
	 * @param state : <code>boolean</code>
	 */
	public void setSelectedState(boolean state)
	{
		this.selected = state;
	}
	
	/**
	 * get Edge parameter.
	 * @param edgeParam Choose one edge parameter.
	 * @return String representation of chosen edge parameter.
	 */
	public String getParameter(EdgeParameters edgeParam)
	{
		switch (edgeParam)
		{
		case Data_R:
			 return Double.toString( Math.round(this.paramEdgeR*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_X:
			 return Double.toString( Math.round(this.paramEdgeX*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_Tap:
			 return Double.toString( Math.round(this.paramEdgeTap*ROUND_PREC)/ROUND_PREC );
			 //break;
		default:
			 return "";
			 //break;
		}
	}
	
	/**
	 * get Edge calculated result.
	 * @param edgeRes Choose one edge calculated result.
	 * @return String representation of chosen edge calculated result.
	 */
	public String getResult(EdgeResults edgeRes)
	{
		switch (edgeRes)
		{
		case PF:
			 return Double.toString( Math.round(this.resEdgePF*ROUND_PREC)/ROUND_PREC );
			 //break;
		case QF:
			 return Double.toString( Math.round(this.resEdgeQF*ROUND_PREC)/ROUND_PREC );
			 //break;
		case PT:
			 return Double.toString( Math.round(this.resEdgePT*ROUND_PREC)/ROUND_PREC );
			 //break;
		case QT:
			 return Double.toString( Math.round(this.resEdgeQT*ROUND_PREC)/ROUND_PREC );
			 //break;
		default:
			 return "";
			 //break;
		}
	}
	
	/**
	 * set Edge parameter.
	 * @param edgeParam Choose one edge parameter.
	 * @param value set value for current parameter.
	 */
	public void setParameter(EdgeParameters edgeParam, String value)
	{
		switch (edgeParam)
		{
		case Data_R:
			 //[0; infinite)
			 if(Double.parseDouble(value) < 0)
			 {
				 this.paramEdgeR = 0.0;
			 }
			 else
			 {
				 this.paramEdgeR = Double.parseDouble(value);
			 }
			 break;
		case Data_X:
			 this.paramEdgeX = Double.parseDouble(value);
			 break;
		case Data_Tap:
			 //(0; 1]
			 if( (Double.parseDouble(value) <= 0) || (Double.parseDouble(value) > 1) )
			 {
				 this.paramEdgeTap = 1.00;
			 }
			 else
			 {
				 this.paramEdgeTap = Double.parseDouble(value);
			 }
			 break;
		default:
			 break;
		}
	}
	
	/**
	 * set Edge calculated result.
	 * @param edgeRes Choose one edge calculated result.
	 * @param value set value for current calculated result.
	 */
	public void setResult(EdgeResults edgeRes, Double value)
	{
		switch (edgeRes)
		{
		case PF:
			 this.resEdgePF = value;
			 break;
		case QF:
			 this.resEdgeQF = value;
			 break;
		case PT:
			 this.resEdgePT = value;
			 break;
		case QT:
			 this.resEdgeQT = value;
			 break;
		default:
			 break;
		}
	}
	
	/**
	 * reference to the parent ComponentPanel
	 * @param compPanel
	 */
	public void setParent(ComponentPanel compPanel)
	{
		this.parent = compPanel;
	}
	
	/**
	 * returns Edge id
	 * @return int: #id
	 */
	public int getID()
	{
		return this.id;
	}
	
	/**
	 * get FirstNode or LastNode, which are linked with this Edge. 
	 * 
	 * @param typeOfNode : NodesOfEdge
	 * @return Node
	 */
	public Node getNode(NodesOfEdge typeOfNode)
	{
		if (typeOfNode==NodesOfEdge.FIRST_NODE)
		{
			return this.firstNode;
		}
		else if (typeOfNode==NodesOfEdge.LAST_NODE)
		{
			return this.lastNode;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * binding of Edge to the place regarding positions of two its linked Nodes.
	 */
	@SuppressWarnings("unused") //"unused" it was made for Circle-shape nodes
	private void bindEdgeOld()
	{
		//Node radiuses
		int r0 = 0;//firstNode.radius;
		int r1 = 0;//lastNode.radius;
		
		//coordinates of center of each Node component
		int x0 = firstNode.getCenter().x;
		int y0 = firstNode.getCenter().y;
		int x1 = lastNode.getCenter().x;
		int y1 = lastNode.getCenter().y;
		
		/*
		 *  Calculating of coordinate shift from the Node center to the "line" of circumference.
		 *  i. e. intersection point of circumference and edge.
		 * 
		 *  for both linked nodes this parameter is unique, 
		 *  because Nodes are able to have different sizes.
		 *  
		 *  Edge line is characterized by two points (x0, y0) and (x1, y1).
		 *  They are centers of linked Nodes.
		 *  
		 *  R0, R1 -- radiuses of linked Nodes.
		 *  
		 *  formula is:
		 *  
		 *  (for the first Node):
		 *   Y = | sin{ arctan[ (y1-y0)/(x1-x0) ] } * R0 |;
		 *   X = | cos{ arctan[ (y1-y0)/(x1-x0) ] } * R0 |;
		 *   
		 *  (for the second Node):
		 *   Y = | sin{ arctan[ (y1-y0)/(x1-x0) ] } * R1 |;
		 *   X = | cos{ arctan[ (y1-y0)/(x1-x0) ] } * R1 |;
		 */
		
		//(for the first Node):
		int difY0 = (int) Math.abs(Math.floor(Math.sin(Math.atan((double)(y1-y0)/(double)(x1-x0)))*(2*r0-1)/2));
		int difX0 = (int) Math.abs(Math.floor(Math.cos(Math.atan((double)(y1-y0)/(double)(x1-x0)))*(2*r0-1)/2));
		
		//(for the second Node):
		int difY1 = (int) Math.abs(Math.floor(Math.sin(Math.atan((double)(y1-y0)/(double)(x1-x0)))*(2*r1-1)/2));
		int difX1 = (int) Math.abs(Math.floor(Math.cos(Math.atan((double)(y1-y0)/(double)(x1-x0)))*(2*r1-1)/2));
		
		
		/*
		* binding the Edge component regarding its linked Node positions:
		* 
		* firstNode(x0, y0) and lastNode(x1, y1)
		* 
		*/
		
		/*
		 * let's imagine that one node in the center(0,0) and 
		 * other one takes position on the North or South of it.
		 */  
		if ( (x0 == x1) || (Math.abs(x0-x1)<=COMPONENT_SHIFT*2) )
		{
			this.setBounds(Math.min(x0-COMPONENT_SHIFT, x1-COMPONENT_SHIFT), 			// x
						   Math.min(y0+r0, y1+r1) - COMPONENT_SHIFT,		 			// y
						   Math.abs( Math.abs(x0-x1)+COMPONENT_SHIFT*2 )+1,				// width
						   Math.abs( Math.abs(y0-y1)-(r0+r1) + COMPONENT_SHIFT*2 )+1	// height
						   );
		}
		/*
		 * let's imagine that one node in the center(0,0) and 
		 * other one takes position on the West or East of it.
		 */ 
		else if ( (y0 == y1) || (Math.abs(y0-y1)<=COMPONENT_SHIFT*2) )
		{
			this.setBounds(Math.min(x0+r0, x1+r1) - COMPONENT_SHIFT,		 			// x
						   Math.min(y0-COMPONENT_SHIFT, y1-COMPONENT_SHIFT), 			// y
						   Math.abs( Math.abs(x0-x1)-(r0+r1) + COMPONENT_SHIFT*2 )+1,	// width
						   Math.abs( Math.abs(y0-y1)+COMPONENT_SHIFT*2 )+1			 	// height
						   );
		}
		/*
		 * other cases, i.e. positions in NorthWestern, SouthEastern, etc... sectors.
		 */
		else
		{
			this.setBounds(Math.min(x0+difX0, x1+difX1)-COMPONENT_SHIFT,		  				// x
					       Math.min(y0+difY0, y1+difY1)-COMPONENT_SHIFT,		  				// y
					       Math.abs( Math.abs(x0 - x1) - (difX0+difX1) + COMPONENT_SHIFT*2 )+1, // width
					       Math.abs( Math.abs(y0 - y1) - (difY0+difY1) + COMPONENT_SHIFT*2 )+1  // height
					       );
		}
	}
	
	/**
	 * re-bind current Edge
	 */
	public void rebindEdge()
	{
		this.bindEdge();
		this.parent.setBounds(this.getBounds());
		this.parent.setActualBounds(this.getBounds());
	}
	
	/**
	 * binding of Edge to the place regarding positions of two its linked Nodes.
	 */
	//for the new "form"
	private void bindEdge()
	{
		//scaled connector_length, connector_shift, gen_circle_rad, component_shift
		int scaledConnectorLength = ComponentContainer.getScaledValue(CONNECTOR_LENGTH);
		int scaledConnectorShift = ComponentContainer.getScaledValue(CONNECTOR_SHIFT);
		int scaledComponentShift = ComponentContainer.getScaledValue(COMPONENT_SHIFT);
		int scaledGenCircleRad = ComponentContainer.getScaledValue(GEN_CIRCLE_RAD);
		
		if(firstNodeConnector!=null && lastNodeConnector!=null)
		{
			buffFirstNodeConnector = firstNodeConnector;
			buffLastNodeConnector = lastNodeConnector;
			
			//reduce counter info in firstNode according connector's type
			switch(firstNodeConnector)
			{
			case UL:
				firstNode.conUL--;
				break;
			case UR:
				firstNode.conUR--;
				break;
			case LL:
				firstNode.conLL--;
				break;
			case LC:
				firstNode.conLC--;
				break;
			case LR:
				firstNode.conLR--;
				break;
			}
			//reduce counter info in lastNode according connector's type
			switch(lastNodeConnector)
			{
			case UL:
				lastNode.conUL--;
				break;
			case UR:
				lastNode.conUR--;
				break;
			case LL:
				lastNode.conLL--;
				break;
			case LC:
				lastNode.conLC--;
				break;
			case LR:
				lastNode.conLR--;
				break;
			}
		}
		
		//connector range for first and last Nodes
		int connectorRange0 = firstNode.getWidth()/4;
		int connectorRange1 = lastNode.getWidth()/4;
						
		//coordinates of center of each Node component
		x0 = firstNode.getCenter().x;
		y0 = firstNode.getCenter().y; 
		x1 = lastNode.getCenter().x; 
		y1 = lastNode.getCenter().y;
		
		/*
		 * let's imagine that firstNode is in the center and
		 * lastNode takes position in the South-Western part of it
		 */
		if( (y0 < y1) && (x0 >= x1) )
		{
			x1 = lastNode.getCenter().x + connectorRange1;
			
			firstNodeConnector = ConnectorTypes.LL;
			lastNodeConnector = ConnectorTypes.UR;
		}
		/*
		 * let's imagine that firstNode is in the center and
		 * lastNode takes position in the North-Western part of it
		 */
		else if( (y0 > y1) && (x0 >= x1) )
		{
			x0 = firstNode.getCenter().x - connectorRange0;
			
			firstNodeConnector = ConnectorTypes.UL;
			lastNodeConnector = ConnectorTypes.LR;
		}
		/*
		 * let's imagine that firstNode is in the center and
		 * lastNode takes position in the South-Eastern part of it
		 */
		else if( (y0 < y1) && (x0 < x1) )
		{
			x1 = lastNode.getCenter().x - connectorRange1;
			
			firstNodeConnector = ConnectorTypes.LR;
			lastNodeConnector = ConnectorTypes.UL;
		}
		/*
		 * let's imagine that firstNode is in the center and
		 * lastNode takes position in the North-Eastern part of it
		 */
		else if( (y0 > y1) && (x0 < x1) )
		{
			x0 = firstNode.getCenter().x + connectorRange0;
			
			firstNodeConnector = ConnectorTypes.UR;
			lastNodeConnector = ConnectorTypes.LL;
		}
		//----------------------------------------------------------------------->
		
		//allocation of connectors
		//firstNodeConnector is left aside; lastNodeConnector is right aside
		if( (firstNode.getCenter().x - lastNode.getCenter().x) > Math.min(firstNode.getWidth(), lastNode.getWidth()) )
		{
			x0 = firstNode.getCenter().x - connectorRange0;
			y0 = firstNode.getCenter().y;
			x1 = lastNode.getCenter().x + connectorRange1;
			y1 = lastNode.getCenter().y;
		}
		//lastNodeConnector is left aside; firstNodeConnector is right aside
		else if( (firstNode.getCenter().x - lastNode.getCenter().x) < -Math.min(firstNode.getWidth(), lastNode.getWidth()) )
		{
			x0 = firstNode.getCenter().x + connectorRange0;
			y0 = firstNode.getCenter().y;
			x1 = lastNode.getCenter().x - connectorRange1;
			y1 = lastNode.getCenter().y;
		}
		
		//----------------------------------------------------------------------->
		
		//assignment for connectors type | LC-lower-center | LL-lower-left | LR-lower-right |
		if(x0 == firstNode.getCenter().x)
		{
			firstNodeConnector = ConnectorTypes.LC;
		}
		if(x1 == lastNode.getCenter().x)
		{
			lastNodeConnector = ConnectorTypes.LC;
		}
		
		if( (y0==y1 || Math.abs(y0-y1) <= scaledConnectorLength) && (x0>x1) )
		{
			firstNodeConnector = ConnectorTypes.LL;
			lastNodeConnector = ConnectorTypes.LR;
		}
		else if( (y0==y1 || Math.abs(y0-y1) <= scaledConnectorLength) && (x0<x1) )
		{
			firstNodeConnector = ConnectorTypes.LR;
			lastNodeConnector = ConnectorTypes.LL;
		}
		
		//----------------------------------------------------------------------->
		
		if(firstNodeConnector!=null && lastNodeConnector!=null)
		{
			//increase counter info in firstNode according connector's type
			switch(firstNodeConnector)
			{
			case UL:
				firstNode.conUL++;
				break;
			case UR:
				firstNode.conUR++;
				break;
			case LL:
				firstNode.conLL++;
				break;
			case LC:
				firstNode.conLC++;
				break;
			case LR:
				firstNode.conLR++;
				break;
			}
			//increase counter info in lastNode according connector's type
			switch(lastNodeConnector)
			{
			case UL:
				lastNode.conUL++;
				break;
			case UR:
				lastNode.conUR++;
				break;
			case LL:
				lastNode.conLL++;
				break;
			case LC:
				lastNode.conLC++;
				break;
			case LR:
				lastNode.conLR++;
				break;
			}
		}
		//----------------------------------------------------------------------->
		
		if(firstNodeConnector!=null && lastNodeConnector!=null)
		{
			//if firstNodeConnector type was changed
			if(buffFirstNodeConnector != firstNodeConnector)
			{		
				switch(firstNodeConnector)
				{
				case UL:
					currentShift0 = - scaledConnectorShift*(firstNode.conUL-1);
					connectorMul0 = firstNode.conUL;
					break;
				case UR:
					currentShift0 = + scaledConnectorShift*(firstNode.conUR-1);
					connectorMul0 = firstNode.conUR;
					break;
				case LL:
					currentShift0 = - scaledConnectorShift*(firstNode.conLL-1);
					connectorMul0 = firstNode.conLL;
					break;
				case LC:
					currentShift0 = + scaledConnectorShift*(firstNode.conLC-1);
					connectorMul0 = firstNode.conLC;
					break;
				case LR:
					currentShift0 = + scaledConnectorShift*(firstNode.conLR-1);
					connectorMul0 = firstNode.conLR;
					break;
				}
			}
			else//if firstNodeConnector type wasn't changed, then just check (and change) its currentShift
			{
				switch(firstNodeConnector)
				{
				case UL:
					if(connectorMul0 > firstNode.conUL)
					{
						connectorMul0--;
					}
					currentShift0 = - scaledConnectorShift*(Math.abs(connectorMul0-firstNode.conUL));
					break;
				case UR:
					if(connectorMul0 > firstNode.conUR)
					{
						connectorMul0--;
					}
					currentShift0 = + scaledConnectorShift*(Math.abs(connectorMul0-firstNode.conUR));
					break;
				case LL:
					if(connectorMul0 > firstNode.conLL)
					{
						connectorMul0--;
					}
					currentShift0 = - scaledConnectorShift*(Math.abs(connectorMul0-firstNode.conLL));
					break;
				case LC:
					if(connectorMul0 > firstNode.conLC)
					{
						connectorMul0--;
					}
					currentShift0 = + scaledConnectorShift*(Math.abs(connectorMul0-firstNode.conLC));
					break;
				case LR:
					if(connectorMul0 > firstNode.conLR)
					{
						connectorMul0--;
					}
					currentShift0 = + scaledConnectorShift*(Math.abs(connectorMul0-firstNode.conLR));
					break;
				}
			}
			//if lastNodeConnector type was changed
			if(buffLastNodeConnector != lastNodeConnector)
			{					
				switch(lastNodeConnector)
				{
				case UL:
					currentShift1 = - scaledConnectorShift*(lastNode.conUL-1);
					connectorMul1 = lastNode.conUL;
					break;
				case UR:
					currentShift1 = + scaledConnectorShift*(lastNode.conUR-1);
					connectorMul1 = lastNode.conUR;
					break;
				case LL:
					currentShift1 = - scaledConnectorShift*(lastNode.conLL-1);
					connectorMul1 = lastNode.conLL;
					break;
				case LC:
					currentShift1 = + scaledConnectorShift*(lastNode.conLC-1);
					connectorMul1 = lastNode.conLC;
					break;
				case LR:
					currentShift1 = + scaledConnectorShift*(lastNode.conLR-1);
					connectorMul1 = lastNode.conLR;
					break;
				}
			}
			else//if lastNodeConnector type wasn't changed, then just check (and change) its currentShift
			{
				switch(lastNodeConnector)
				{
				case UL:
					if(connectorMul1 > lastNode.conUL)
					{
						connectorMul1--;
					}
					currentShift1 = - scaledConnectorShift*(Math.abs(connectorMul1-lastNode.conUL));
					break;
				case UR:
					if(connectorMul1 > lastNode.conUR)
					{
						connectorMul1--;
					}
					currentShift1 = + scaledConnectorShift*(Math.abs(connectorMul1-lastNode.conUR));
					break;
				case LL:
					if(connectorMul1 > lastNode.conLL)
					{
						connectorMul1--;
					}
					currentShift1 = - scaledConnectorShift*(Math.abs(connectorMul1-lastNode.conLL));
					break;
				case LC:
					if(connectorMul1 > lastNode.conLC)
					{
						connectorMul1--;
					}
					currentShift1 = + scaledConnectorShift*(Math.abs(connectorMul1-lastNode.conLC));
					break;
				case LR:
					if(connectorMul1 > lastNode.conLR)
					{
						connectorMul1--;
					}
					currentShift1 = + scaledConnectorShift*(Math.abs(connectorMul1-lastNode.conLR));
					break;
				}
			}

		}
		
		x0 += currentShift0;
		x1 += currentShift1;
	
		/*
		* binding the Edge component regarding its linked Node positions:
		* 
		* firstNode(x0, y0) and lastNode(x1, y1)
		* 
		*/
		
		/*
		 * let's imagine that one node in the center(0,0) and 
		 * other one takes position on the North or South of it.
		 */  
		if ( (x0 == x1) || (Math.abs(x0-x1)<=scaledComponentShift*2) )
		{
			this.setBounds(Math.min(x0, x1) - scaledComponentShift - scaledGenCircleRad/2, 			// x
						   Math.min(y0, y1) - scaledComponentShift,		 							// y
						   Math.abs( Math.abs(x0-x1)+scaledComponentShift*2 )+1+scaledGenCircleRad,	// width
						   Math.abs( Math.abs(y0-y1) + scaledComponentShift*2 )+1-ComponentContainer.getScaledValue(6)	// height
						   );
		}
		/*
		 * let's imagine that one node in the center(0,0) and 
		 * other one takes position on the West or East of it.
		 */ 
		else if ( (y0 == y1) || (Math.abs(y0-y1)<=scaledConnectorLength) )
		{
			this.setBounds(Math.min(x0, x1) - scaledComponentShift,		 				// x
						   Math.min(y0-scaledComponentShift, y1-scaledComponentShift), 	// y
						   Math.abs( Math.abs(x0-x1) + scaledComponentShift*2 )+1,		// width
						   Math.abs( Math.abs(y0-y1)+ scaledConnectorLength*2 )+1-ComponentContainer.getScaledValue(6)	// height
						   );
		}
		/*
		 * other cases, i.e. positions in NorthWestern, SouthEastern, etc... sectors.
		 */
		else
		{
			this.setBounds(Math.min(x0, x1)-scaledComponentShift,		  			  	// x
					       Math.min(y0, y1)-scaledComponentShift,		  			  	// y
					       Math.abs( Math.abs(x0 - x1) + scaledComponentShift*2 )+1,  	// width
					       Math.abs( Math.abs(y0 - y1) + scaledComponentShift*2 )+1-ComponentContainer.getScaledValue(6) // height
					       );
		}
	}
	
	/**
	 * draw power flow arrows
	 * @param g2 : Graphics2D
	 * @param valX0 : x0 (x coordinate of "A" end point)
	 * @param valY0 : y0 (y coordinate of "A" end point)
	 * @param valX1 : x1 (x coordinate of "B" end point)
	 * @param valY1 : y1 (y coordinate of "B" end point)
	 * @param pos : current index position of the next arrow
	 * @param order : special order rate that shows arrow order:
	 * 				  <ul>
	 * 				  <li> &nbsp;1: from firstNode to lastNode</li>
	 * 				  <li>-1: from lastNode to firstNode</li>
	 * 				  </ul>	
	 */
	private void paintPowerFlow(Graphics2D g2, int valX0, int valY0, int valX1, int valY1, int pos, int order)
	{
		Graphics2D g2d = g2;
		
		//end points coordinates
		int x0 = valX0;
		int y0 = valY0;
		int x1 = valX1;
		int y1 = valY1;
		
		//current shift of the arrow on the its line according its index number
		int currentPosition = pos*NEXT_ARROW_SHIFT_RATE + 2;
		
		/*
		 * order rate 
		 * if -1 then arrows come from lastNode to firstNode
		 * if  1 then arrows come from firstNode to lastNode
		 */
		int orderRate;
		if(order == -1)
		{
			orderRate = -1;
		}
		else
		{
			orderRate = 1;
		}
		
		/*
		 * points coordinates of current arrow
		 * A and B -- points that are belonging to equal angles
		 * C -- kind of "arrowhead"
		 */
		int xA;
		int yA;
		int xB;
		int yB;
		int xC;
		int yC;
		
		//coordinates of current position point of the arrow
		int currentXpoint;
		int currentYpoint;
		
		//scaled arrow length
		//double scaledArrowLength = ARROW_LENGTH * ComponentContainer.zoom;
		
		//shift of C-point relatively of position point
		int cShiftX = (int) Math.abs( Math.round((double)ARROW_LENGTH * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) ) * orderRate;
		int cShiftY = (int) Math.abs( Math.round((double)ARROW_LENGTH * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) ) * orderRate;
		
		//shift of A-point and B-point relatively of position point
		int abShiftX = (int) Math.abs( Math.round((double)ARROW_LENGTH/2 * Math.cos(Math.atan( (double)(x0-x1)/(double)(y1-y0) ))) );
		int abShiftY = (int) Math.abs( Math.round((double)ARROW_LENGTH/2 * Math.sin(Math.atan( (double)(x0-x1)/(double)(y1-y0) ))) );
		
		
		if(x0==x1)
		{
			if(y0 > y1)//lastNode on the North of firstNode
			{
				currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				
				xA = currentXpoint + abShiftX;
				xB = currentXpoint - abShiftX;
				xC = currentXpoint + cShiftX;
				
				yA = currentYpoint + abShiftY;
				yB = currentYpoint + abShiftY;
				yC = currentYpoint - cShiftY;
			}
			else//lastNode on the South of firstNode
			{
				currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				
				xA = currentXpoint - abShiftX;
				xB = currentXpoint + abShiftX;
				xC = currentXpoint + cShiftX;
				
				yA = currentYpoint - abShiftY;
				yB = currentYpoint - abShiftY;
				yC = currentYpoint + cShiftY;
			}
		}
		else if(y0==y1)
		{
			if(x0 > x1)//lastNode on the West of firstNode
			{
				currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				
				yA = currentYpoint - abShiftY;
				yB = currentYpoint + abShiftY;
				yC = currentYpoint + cShiftY;
				
				xA = currentXpoint + abShiftX;
				xB = currentXpoint + abShiftX;
				xC = currentXpoint - cShiftX;
			}
			else//lastNode on the East of firstNode
			{
				currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
				
				yA = currentYpoint + abShiftY;
				yB = currentYpoint - abShiftY;
				yC = currentYpoint + cShiftY;
				
				xA = currentXpoint - abShiftX;
				xB = currentXpoint - abShiftX;
				xC = currentXpoint + cShiftX;
			}
		}
		else if( Math.abs(x0-x1) > Math.abs(y0-y1) ) // width > height
		{
			if(y0 > y1)
			{
				if(x0 > x1)//lastNode on the North-West of firstNode
				{
					currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					xC = currentXpoint - cShiftX;
				}
				else//lastNode on the North-East of firstNode
				{
					currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					xC = currentXpoint + cShiftX;
				}
				yA = currentYpoint - abShiftY;
				yB = currentYpoint + abShiftY;
			}
			else//(y0 <= y1)
			{
				if(x0 > x1)//lastNode on the South-West of firstNode
				{
					currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					xC = currentXpoint - cShiftX;
				}
				else//lastNode on the South-East of firstNode
				{
					currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					xC = currentXpoint + cShiftX;
				}
				yA = currentYpoint + abShiftY;
				yB = currentYpoint - abShiftY;
			}
			
			if( currentXpoint-x1==0 ) return;
			
			yC = (y1-y0)*(xC-x0)/(x1-x0) + y0;
			xA = (y1-currentYpoint)*(yA-currentYpoint)/(currentXpoint-x1) + currentXpoint;
			xB = (y1-currentYpoint)*(yB-currentYpoint)/(currentXpoint-x1) + currentXpoint;
		}
		else // width <= height
		{
			if(x0 > x1)
			{
				if(y0 > y1)//lastNode on the North-West of firstNode
				{
					currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					yC = currentYpoint - cShiftY;
				}
				else//lastNode on the South-West of firstNode
				{
					currentXpoint = x0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					yC = currentYpoint + cShiftY;
				}
				xA = currentXpoint - abShiftX;
				xB = currentXpoint + abShiftX;
			}
			else//(x0 <= x1)
			{
				if(y0 > y1)//lastNode on the North-East of firstNode
				{
					currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 - (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					yC = currentYpoint - cShiftY;
				}
				else//lastNode on the South-East of firstNode
				{
					currentXpoint = x0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					currentYpoint = y0 + (int) Math.abs( Math.round((double)ARROW_LENGTH*currentPosition * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
					
					yC = currentYpoint + cShiftY;
				}
				xA = currentXpoint + abShiftX;
				xB = currentXpoint - abShiftX;
			}
			
			if( y1-currentYpoint==0 ) return;
			
			xC = (x1-x0)*(yC-y0)/(y1-y0) + x0;
			yA = (currentXpoint-x1)*(xA-currentXpoint)/(y1-currentYpoint) + currentYpoint;
			yB = (currentXpoint-x1)*(xB-currentXpoint)/(y1-currentYpoint) + currentYpoint;
		}
		
		g2d.setColor(Color.green);
		g2d.fillPolygon(new int[]{xA, xB, xC}, new int[]{yA, yB, yC}, 3);
		g2d.setColor(new Color(0,125,200));
		g2d.drawPolygon(new int[]{xA, xB, xC}, new int[]{yA, yB, yC}, 3);
	}
	
	/**
	 * draw two crossing Circles in the midpoint of line.
	 * @param g2 : Graphics2D
	 * @param valX0 : x0 (x coordinate of "A" end point)
	 * @param valY0 : y0 (y coordinate of "A" end point)
	 * @param valX1 : x1 (x coordinate of "B" end point)
	 * @param valY1 : y1 (y coordinate of "B" end point)
	 */
	private void paintGenCircles(Graphics2D g2, int valX0, int valY0, int valX1, int valY1)
	{
		Graphics2D g2d = g2;
		int x0 = valX0;
		int y0 = valY0;
		int x1 = valX1;
		int y1 = valY1;
		
		//Midpoint of a Line Segment
		int midX = (x0 + x1)/2;
		int midY = (y0 + y1)/2;
		
		//coordinates of these two circles
		int genX1;
		int genY1;
		int genX2;
		int genY2;
		
		//scaled arrow length
		//double scaledArrowLength = ARROW_LENGTH * ComponentContainer.zoom;
				
		//scaled generator's circle radius
		int scaledGenCircleRad = ComponentContainer.getScaledValue(GEN_CIRCLE_RAD);

		// ~ midShiftX = 12/2 * cos{ arctan( (y1-y0)/(x1-x0) ) };
		int midShiftX = (int) Math.abs( Math.round((double)scaledGenCircleRad/2 * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
		
		// ~ midShiftY = 12/2 * sin{ arctan( (y1-y0)/(x1-x0) ) };
		int midShiftY = (int) Math.abs( Math.round((double)scaledGenCircleRad/2 * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
		
		//for connecting lines
		int lineMidShiftX = (int) Math.abs( Math.round((double)scaledGenCircleRad * Math.cos(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
		int lineMidShiftY = (int) Math.abs( Math.round((double)scaledGenCircleRad * Math.sin(Math.atan( (double)(y1-y0)/(double)(x1-x0) ))) );
		
		if(x0==x1)
		{
			genX1 = x0;
			genX2 = x1;
			
			if(y0 > y1)
			{
				genY1 = midY + midShiftY;
				genY2 = midY - midShiftY;
			}
			else
			{
				genY1 = midY - midShiftY;
				genY2 = midY + midShiftY;
			}
		}
		else if(y0==y1)
		{
			if(x0 > x1)
			{
				genX1 = midX + midShiftX;
				genX2 = midX - midShiftX;
			}
			else
			{
				genX1 = midX - midShiftX;
				genX2 = midX + midShiftX;
			}
			
			genY1 = y0;
			genY2 = y1;
		}
		else if( Math.abs(x0-x1) > Math.abs(y0-y1) ) // width > height
		{
			if(x0 > x1)
			{
				genX1 = midX + midShiftX;
				genX2 = midX - midShiftX;
			}
			else
			{
				genX1 = midX - midShiftX;
				genX2 = midX + midShiftX;
			}
			// ~ y = (y1-y0)*(x-x0)/(x1-x0) + y0
			genY1 = (y1-y0)*(genX1-x0)/(x1-x0) + y0;
			genY2 = (y1-y0)*(genX2-x0)/(x1-x0) + y0;
		}
		else // width <= height
		{
			if(y0 > y1)
			{
				genY1 = midY + midShiftY;
				genY2 = midY - midShiftY;
			}
			else
			{
				genY1 = midY - midShiftY;
				genY2 = midY + midShiftY;
			}
			// ~ x = (x1-x0)*(y-y0)/(y1-y0) + x0
			genX1 = (x1-x0)*(genY1-y0)/(y1-y0) + x0;
			genX2 = (x1-x0)*(genY2-y0)/(y1-y0) + x0;
		}
		
		if(x0 <= x1 && y0 <= y1)//South-East
		{
			g2d.drawLine(x0, y0, genX1-lineMidShiftX, genY1-lineMidShiftY);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (genX1-lineMidShiftX)-(x0), 2)
								   	 	  +
								   	 	  Math.pow( (genY1-lineMidShiftY)-(y0), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
					 				x0,
					 				y0,
					 				genX1-lineMidShiftX,
					 				genY1-lineMidShiftY,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
			g2d.drawLine(genX2+lineMidShiftX, genY2+lineMidShiftY, x1, y1);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (x1)-(genX2+lineMidShiftX), 2)
								   	 	  +
								   	 	  Math.pow( (y1)-(genY2+lineMidShiftY), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
									genX2+lineMidShiftX,
									genY2+lineMidShiftY,
					 				x1,
					 				y1,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
		}
		else if(x0 > x1 && y0 <= y1)//South-West
		{
			g2d.drawLine(x0, y0, genX1+lineMidShiftX, genY1-lineMidShiftY);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (genX1+lineMidShiftX)-(x0), 2)
								   	 	  +
								   	 	  Math.pow( (genY1-lineMidShiftY)-(y0), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
					 				x0,
					 				y0,
					 				genX1+lineMidShiftX,
					 				genY1-lineMidShiftY,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
			g2d.drawLine(genX2-lineMidShiftX, genY2+lineMidShiftY, x1, y1);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (x1)-(genX2-lineMidShiftX), 2)
								   	 	  +
								   	 	  Math.pow( (y1)-(genY2+lineMidShiftY), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
									genX2-lineMidShiftX,
									genY2+lineMidShiftY,
					 				x1,
					 				y1,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
		}
		else if(x0 > x1 && y0 > y1)//North-West
		{
			g2d.drawLine(x0, y0, genX1+lineMidShiftX, genY1+lineMidShiftY);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (genX1+lineMidShiftX)-(x0), 2)
								   	 	  +
								   	 	  Math.pow( (genY1+lineMidShiftY)-(y0), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
					 				x0,
					 				y0,
					 				genX1+lineMidShiftX,
					 				genY1+lineMidShiftY,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
			g2d.drawLine(genX2-lineMidShiftX, genY2-lineMidShiftY, x1, y1);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (x1)-(genX2-lineMidShiftX), 2)
								   	 	  +
								   	 	  Math.pow( (y1)-(genY2-lineMidShiftY), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
									genX2-lineMidShiftX,
									genY2-lineMidShiftY,
					 				x1,
					 				y1,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
		}
		else if(x0 <= x1 && y0 > y1)//North-East
		{
			g2d.drawLine(x0, y0, genX1-lineMidShiftX, genY1+lineMidShiftY);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (genX1-lineMidShiftX)-(x0), 2)
								   	 	  +
								   	 	  Math.pow( (genY1+lineMidShiftY)-(y0), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
					 				x0,
					 				y0,
					 				genX1-lineMidShiftX,
					 				genY1+lineMidShiftY,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
			g2d.drawLine(genX2+lineMidShiftX, genY2-lineMidShiftY, x1, y1);
			if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
			{//<--start:
				int orderR;
				if(this.resEdgePF > 0.2)
				{
					orderR = 1;
				}
				else//this.resEdgePF < -0.2
				{
					orderR = -1;
				}
			//draw power flow-----------------------------------------
			for(int i=0;
					//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
					i<( (int)Math.floor(
										(
							    Math.sqrt( 
								   	 	  Math.pow( (x1)-(genX2+lineMidShiftX), 2)
								   	 	  +
								   	 	  Math.pow( (y1)-(genY2-lineMidShiftY), 2)
								   		 )
								   		)
								   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   ) 
					  );
					i++)
			{
				this.paintPowerFlow(g2d,
									genX2+lineMidShiftX,
									genY2-lineMidShiftY,
					 				x1,
					 				y1,
					 				i,
					 				orderR );
			}
			//--------------------------------------------------------
			}//<--end;
		}
		
		g2d.drawOval(genX1-scaledGenCircleRad, genY1-scaledGenCircleRad, scaledGenCircleRad*2, scaledGenCircleRad*2);
		g2d.drawOval(genX2-scaledGenCircleRad, genY2-scaledGenCircleRad, scaledGenCircleRad*2, scaledGenCircleRad*2);
	}
	
	/**
	 * paint selected state (if Edge is selected)
	 * @param g2 : Graphics2D
	 * @param valX0 : int
	 * @param valY0 : int
	 * @param valX1 : int
	 * @param valY1 : int
	 */
	private void paintSelectedState(Graphics2D g2, int valX0, int valY0, int valX1, int valY1)
	{
		Graphics2D g2d = g2;
		int x0 = valX0;
		int y0 = valY0;
		int x1 = valX1;
		int y1 = valY1;
		
		g2d.setColor(new Color(0, 125, 200, 50));
		g2d.setStroke(new BasicStroke((float)(Edge.SELECTION_ZONE_THICKNESS * ComponentContainer.zoom)));
		g2d.drawLine(x0, y0, x1, y1);
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(new Color(0, 125, 200, 255));
	}
	
	//painting of component
	@Override
	protected void paintComponent(Graphics g)
	{	
		//re-binding the Edge component
		//rebindEdge();
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke((float)(1*ComponentContainer.zoom)));
		
		//g2d.setColor(Color.green);
		//g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(new Color(0,125,200));
		
		//scaled arrow_length, connector_length, rect_side
		//double scaledArrowLength = ARROW_LENGTH * ComponentContainer.zoom;
		int scaledConnectorLength = ComponentContainer.getScaledValue(CONNECTOR_LENGTH);
		int scaledRectSide = ComponentContainer.getScaledValue(RECT_SIDE);
		
		/*
		 * drawing the line from "firstNode" center to "lastNode" center:
		 */
		//if firstNode upper than LastNode (considering its connector length)
		if( (y0-y1) < -scaledConnectorLength )
		{
			g2d.drawLine(x0 - this.getX(),																// x0
					 	 y0 - this.getY()-ComponentContainer.getScaledValue(3),							// y0
					 	 x0 - this.getX(),																// ctrlX0
					 	 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength	// ctrlY0
					 	 );
			//draw as generator
			if(this.paramEdgeTap != 1.00)
			{
				this.paintGenCircles(g2d,
									 x0 - this.getX(),
									 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
									 x1 - this.getX(),
									 y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength);
			}
			//draw as line
			else
			{
				g2d.drawLine(x0 - this.getX(),																// ctrlX0
				 		 	 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	// ctrlY0
				 		 	 x1 - this.getX(),																// ctrlX1
				 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength	// ctrlY1
						 	 );
				if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
				{//<--start:
					int orderR;
					if(this.resEdgePF > 0.2)
					{
						orderR = 1;
					}
					else//this.resEdgePF < -0.2
					{
						orderR = -1;
					}
				//draw power flow-----------------------------------------
				for(int i=0;
						//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
						i<( (int)Math.floor(
											(
								    Math.sqrt( 
									   	 	  Math.pow( (x1 - this.getX())-(x0 - this.getX()), 2)
									   	 	  +
									   	 	  Math.pow( (y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength)-(y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength), 2)
									   		 )
									   		)
									   		/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
										   ) 
						  );
						i++)
				{
					this.paintPowerFlow(g2d,
						 				x0 - this.getX(),
						 				y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
						 				x1 - this.getX(),
						 				y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,
						 				i,
						 				orderR );
				}
				//--------------------------------------------------------
				}//<--end;
			}
			
			g2d.drawLine(x1 - this.getX(),																// ctrlX1
			 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,	// ctrlY1
			 		 	 x1 - this.getX(),																// x1
			 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)							// y1
					 	 );
			
			//"switch indicator"
			if(enabled)
			{
				g2d.setColor(Color.red);
			}
			else
			{
				g2d.setColor(Color.black);
			}
			g2d.fillRect(x0 - this.getX() - scaledRectSide/2,											//x
						 y0 - this.getY()-ComponentContainer.getScaledValue(3) + scaledRectSide,		//y
						 scaledRectSide,																//width
						 scaledRectSide);																//height
			g2d.fillRect(x1 - this.getX() - scaledRectSide/2,											//x
					 	 y1 - this.getY()-ComponentContainer.getScaledValue(3) - 2*scaledRectSide,		//y
					 	 scaledRectSide,																//width
					 	 scaledRectSide);																//height
			
			//draw selected state
			if(selected)
			{
				paintSelectedState(g2d,
								   x0 - this.getX(),						
								   y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	
								   x1 - this.getX(),						
								   y1 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength
								   );
			}
			
			startEdgeParentX = x0;
			startEdgeParentY = y0-ComponentContainer.getScaledValue(3)+scaledConnectorLength;
			endEdgeParentX = x1;
			endEdgeParentY = y1-ComponentContainer.getScaledValue(3)-scaledConnectorLength;
		}
		//if firstNode lower than LastNode (considering its connector length)
		else if( (y0-y1) > scaledConnectorLength )
		{
			g2d.drawLine(x0 - this.getX(),																// x0
					 	 y0 - this.getY()-ComponentContainer.getScaledValue(3),							// y0
					 	 x0 - this.getX(),																// ctrlX0
					 	 y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength	// ctrlY0
					 	 );
			//draw as generator
			if(this.paramEdgeTap != 1.00)
			{
				this.paintGenCircles(g2d,
									 x0 - this.getX(),
									 y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,
									 x1 - this.getX(),
									 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength);
			}
			//draw as line
			else
			{
				g2d.drawLine(x0 - this.getX(),																// ctrlX0
				 		 	 y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,	// ctrlY0
				 		 	 x1 - this.getX(),																// ctrlX1
				 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength	// ctrlY1
						 	 );
				if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
				{//<--start:
					int orderR;
					if(this.resEdgePF > 0.2)
					{
						orderR = 1;
					}
					else//this.resEdgePF < -0.2
					{
						orderR = -1;
					}
				//draw power flow-----------------------------------------
				for(int i=0;
						//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
						i<( (int)Math.floor(
											(
								    Math.sqrt( 
										   	  Math.pow( (x1 - this.getX())-(x0 - this.getX()), 2)
										   	  +
										   	  Math.pow( (y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength)-(y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength), 2)
										   	 )
										   	)/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
										   ) 
						  );
						i++)
				{
					this.paintPowerFlow(g2d,
						 				x0 - this.getX(),
						 				y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,
						 				x1 - this.getX(),
						 				y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
						 				i,
						 				orderR );
				}
				//--------------------------------------------------------
				}//<--end;
			}
			
			g2d.drawLine(x1 - this.getX(),																// ctrlX1
			 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	// ctrlY1
			 		 	 x1 - this.getX(),																// x1
			 		 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)							// y1
					 	 );
			
			//"switch indicator"
			if(enabled)
			{
				g2d.setColor(Color.red);
			}
			else
			{
				g2d.setColor(Color.black);
			}
			g2d.fillRect(x0 - this.getX() - scaledRectSide/2,											//x
					 	 y0 - this.getY()-ComponentContainer.getScaledValue(3) - 2*scaledRectSide,		//y
					 	 scaledRectSide,																//width
					 	 scaledRectSide);																//height
			g2d.fillRect(x1 - this.getX() - scaledRectSide/2,											//x
				 	  	 y1 - this.getY()-ComponentContainer.getScaledValue(3) + scaledRectSide,		//y
				 	  	 scaledRectSide,																//width
				 	  	 scaledRectSide);																//height
			
			//draw selected state
			if(selected)
			{
				paintSelectedState(g2d,
								   x0 - this.getX(),						
								   y0 - this.getY()-ComponentContainer.getScaledValue(3)-scaledConnectorLength,	
								   x1 - this.getX(),						
								   y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength
								   );
			}
			
			startEdgeParentX = x0;
			startEdgeParentY = y0-ComponentContainer.getScaledValue(3)-scaledConnectorLength;
			endEdgeParentX = x1;
			endEdgeParentY = y1-ComponentContainer.getScaledValue(3)+scaledConnectorLength;
		}
		//if firstNode and LastNode Y-coordinates are equals (considering its connector length)
		else if( y0==y1 || Math.abs(y0-y1) <= scaledConnectorLength )
		{
			g2d.drawLine(x0 - this.getX(),																// x0
						 y0 - this.getY()-ComponentContainer.getScaledValue(3),							// y0
						 x0 - this.getX(),																// ctrlX0
						 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength	// ctrlY0
				 	 	 );
			//draw as generator
			if(this.paramEdgeTap != 1.00)
			{
				this.paintGenCircles(g2d,
									 x0 - this.getX(),
									 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
									 x1 - this.getX(),
									 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength);
			}
			//draw as line
			else
			{
				g2d.drawLine(x0 - this.getX(),																// ctrlX0
			 		 	 	 y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	// ctrlY0
			 		 	 	 x1 - this.getX(),																// ctrlX1
			 		 	 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength	// ctrlY1
					 	 	 );
				if( this.resEdgePF > 0.2 || this.resEdgePF < -0.2 )
				{//<--start:
					int orderR;
					if(this.resEdgePF > 0.2)
					{
						orderR = 1;
					}
					else//this.resEdgePF < -0.2
					{
						orderR = -1;
					}
				//draw power flow-----------------------------------------
				for(int i=0;
						//i < SQRT( (x1-x0)^2 + (y1-y0)^2 ) / ARROW_LENGTH*3
						i<( (int)Math.floor(
											(
								    Math.sqrt( 
											  Math.pow( (x1 - this.getX())-(x0 - this.getX()), 2)
											  +
											  Math.pow( (y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength)-(y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength), 2)
											 )
											)/((double)ARROW_LENGTH*NEXT_ARROW_SHIFT_RATE)
									   	   ) 
						  );
						i++)
				{
					this.paintPowerFlow(g2d,
						 				x0 - this.getX(),
						 				y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
						 				x1 - this.getX(),
						 				y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,
						 				i,
						 				orderR );
				}
				//--------------------------------------------------------
				}//<--end;
			}
			
			g2d.drawLine(x1 - this.getX(),																// ctrlX1
		 		 	 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	// ctrlY1
		 		 	 	 x1 - this.getX(),																// x1
		 		 	 	 y1 - this.getY()-ComponentContainer.getScaledValue(3)							// y1
						 );
			
			//"switch indicator"
			if(enabled)
			{
				g2d.setColor(Color.red);
			}
			else
			{
				g2d.setColor(Color.black);
			}
			g2d.fillRect(x0 - this.getX() - scaledRectSide/2,											//x
						 y0 - this.getY()-ComponentContainer.getScaledValue(3) + scaledRectSide,		//y
						 scaledRectSide,																//width
						 scaledRectSide);																//height
			g2d.fillRect(x1 - this.getX() - scaledRectSide/2,											//x
					 	 y1 - this.getY()-ComponentContainer.getScaledValue(3) + scaledRectSide,		//y
					 	 scaledRectSide,																//width
					 	 scaledRectSide);																//height
			
			//draw selected state
			if(selected)
			{
				paintSelectedState(g2d,
								   x0 - this.getX(),						
								   y0 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength,	
								   x1 - this.getX(),						
								   y1 - this.getY()-ComponentContainer.getScaledValue(3)+scaledConnectorLength
								   );
			}
			
			startEdgeParentX = x0;
			startEdgeParentY = y0-ComponentContainer.getScaledValue(3)+scaledConnectorLength;
			endEdgeParentX = x1;
			endEdgeParentY = y1-ComponentContainer.getScaledValue(3)+scaledConnectorLength;
		}
		
		//re-binding the Edge component
		rebindEdge();	
	}

}
