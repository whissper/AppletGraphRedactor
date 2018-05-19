package sv.gui.graphElements;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;

import javax.swing.*;

import sun.java2d.loops.FillRect;
import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.additionalElements.NodeTypes;
import sv.gui.additionalElements.VoltageDiagramType;

/**
 * Node
 * <p />
 * Graph Node with unique #id
 * 
 * @version 1.0.0
 * @since 25.01.2013
 * @author SAV2
 *
 */

public class Node extends JComponent implements NodeTypes, VoltageDiagramType
{
	/**
	 * Selected state
	 */
	private boolean selected = false;
	
	/**
	 * rounding precision
	 */
	private static final double ROUND_PREC = 1000.00;
	
	/*
	 * special counters for current amount of Node connectors.
	 * conUL -- upper-left connector
	 * conUR -- upper-right connector
	 * conLL -- lower-left connector
	 * conLC -- lower-center connector
	 * conLR -- lower-right connector
	 */
	int conUL = 0;
	int conUR = 0;
	int conLL = 0;
	int conLC = 0;
	int conLR = 0;
	
	/**
	 * parent component panel
	 */
	private ComponentPanel parent;
	
	/**
	 * Default size of component which contains Node
	 */
	protected static final int DEFAULT_SIZE = 50;
	
	/**
	 * Node radius
	 */
	//public int radius;
	
	/**
	 * Node #id
	 */
	private int id;
	
	/**
	 * Node #idForRequest
	 */
	private int idForRequest = 0;
	
	/**
	 * Background color
	 */
	private Color bgColor;
	
	//------------------------------------------------------------------Node parameters-(start):
	private double paramNodeU = 0.0;
	private double paramNodeAngle = 0.0;
	private int paramNodeType = PQ;
	private double paramNodePG = 0.0;
	private double paramNodeQG = 0.0;
	private double paramNodePL = 0.0;
	private double paramNodeQL = 0.0;
	
	private double resNodeU = 0.0;
	private double resNodeAngle = 0.0;
	private double resNodePG = 0.0;
	private double resNodeQG = 0.0;
	//------------------------------------------------------------------Node parameters-(end);
	
	//-----------------------------------------------------Node voltage diagram options-(start):
	
	private int volDiagramType = numeric;
	private double volDiagramClass = 0.40;
	private double volDiagramMax = 0.44;
	private double volDiagramMin = 0.36;
	
	//-----------------------------------------------------Node voltage diagram options-(end);
	
	//constructor start:
	public Node(int id)
	{
		super();
		
		this.setOpaque(true);
		
		this.setSize(DEFAULT_SIZE, DEFAULT_SIZE);
		
		this.id = id;
		
		/*
		this.radius = this.getWidth()/2;
		
		
		this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {
                Node.this.radius = Node.this.getWidth()/2;
            }
        });
        */
        
	}
	//constructor end;
	
	/**
	 * set selected state of Node-component
	 * @param state : <code>boolean</code>
	 */
	public void setSelectedState(boolean state)
	{
		this.selected = state;
	}
	
	//---------------------------for voltage diagram------------------------(start)---->
	
	/**
	 * change VoltageDiagramClass of current Node according its Voltage lvl
	 */
	private void changeVoltageClass()
	{
		//class 0.4 = [0; 1.00]
		if( this.paramNodeU >= 0 && this.paramNodeU <= 1.00 )
		{
			volDiagramClass = 0.40;
			volDiagramMax = 0.44;
			volDiagramMin = 0.36;		
		}
		//class 10 = (1.00; 20.75)
		else if( this.paramNodeU > 1.00 && this.paramNodeU < 20.75 )
		{
			volDiagramClass = 10.00;
			volDiagramMax = 12.00;
			volDiagramMin = 8.00;		
		}
		//class 35 = [20.75; 66.80)
		else if( this.paramNodeU >= 20.75 && this.paramNodeU < 66.80 )
		{
			volDiagramClass = 35.00;
			volDiagramMax = 40.50;
			volDiagramMin = 29.50;		
		}
		//class 110 = [66.80; 157.00)
		else if( this.paramNodeU >= 66.80 && this.paramNodeU < 157.00 )
		{
			volDiagramClass = 110.00;
			volDiagramMax = 126.00;
			volDiagramMin = 94.00;		
		}
		//class 220 = [157.00; +infinite)
		else// == else if( this.paramNodeU >= 157.00 )
		{
			volDiagramClass = 220.00;
			volDiagramMax = 252.00;
			volDiagramMin = 188.00;		
		}
	}
	
	/**
	 * set voltage diagram type for current Node
	 * @param type : VoltageDiagramType
	 */
	public void setVolDiagramType(int type)
	{
		if(type == percentage)
		{
			this.volDiagramType = percentage;
		}
		else
		{
			this.volDiagramType = numeric;
		}
	}
	
	/**
	 * set voltage diagram class for current Node
	 * @param value : String
	 */
	public void setVolDiagramClass(String value)
	{
		this.volDiagramClass = Math.round(Double.parseDouble(value)*100.00)/100.00;
	}
	
	/**
	 * set voltage diagram max-value for current Node
	 * @param value : String
	 */
	public void setVolDiagramMax(String value)
	{
		this.volDiagramMax = Math.round(Double.parseDouble(value)*100.00)/100.00;
	}
	
	/**
	 * set voltage diagram min-value for current Node
	 * @param value : String
	 */
	public void setVolDiagramMin(String value)
	{
		this.volDiagramMin = Math.round(Double.parseDouble(value)*100.00)/100.00;
	}
	
	/**
	 * get voltage diagram type
	 * @return voltage diagram type of current Node
	 */
	public int getVolDiagramType()
	{
		return this.volDiagramType;
	}
	
	/**
	 * get voltage diagram class
	 * @return voltage diagram class of current Node
	 */
	public double getVolDiagramClass()
	{
		return this.volDiagramClass;
	}
	
	/**
	 * get voltage diagram max-value
	 * @return voltage diagram max-value of current Node
	 */
	public double getVolDiagramMax()
	{
		return this.volDiagramMax;
	}
	
	/**
	 * get voltage diagram min-value
	 * @return voltage diagram min-value of current Node
	 */
	public double getVolDiagramMin()
	{
		return this.volDiagramMin;
	}
	//---------------------------for voltage diagram------------------------(end)---->
	
	/**
	 * get Node parameter.
	 * @param nodeParam Choose one node parameter.
	 * @return String representation of chosen node parameter.
	 */
	public String getParameter(NodeParameters nodeParam)
	{
		switch (nodeParam)
		{
		case Data_U:
			 return Double.toString( Math.round(this.paramNodeU*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_Angle:
			 return Double.toString( Math.round(this.paramNodeAngle*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_Type:
			 return Integer.toString(this.paramNodeType);
			 //break;
		case Data_PG:
			 return Double.toString( Math.round(this.paramNodePG*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_QG:
			 return Double.toString( Math.round(this.paramNodeQG*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_PL:
			 return Double.toString( Math.round(this.paramNodePL*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Data_QL:
			 return Double.toString( Math.round(this.paramNodeQL*ROUND_PREC)/ROUND_PREC );
			 //break;
		default:
			 return "";
			 //break;
		}
	}
	
	/**
	 * get Node calculated result
	 * @param nodeRes Choose one Node calculated result
	 * @return String representation of chosen Node calculated result
	 */
	public String getResult(NodeResults nodeRes)
	{
		switch (nodeRes)
		{
		case Res_U:
			 return Double.toString( Math.round(this.resNodeU*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Res_Angle:
			 return Double.toString( Math.round(this.resNodeAngle*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Res_PG:
			 return Double.toString( Math.round(this.resNodePG*ROUND_PREC)/ROUND_PREC );
			 //break;
		case Res_QG:
			 return Double.toString( Math.round(this.resNodeQG*ROUND_PREC)/ROUND_PREC );
			 //break;
		default:
			 return "";
			 //break;
		}
	}
	
	/**
	 * set Node parameter.
	 * @param nodeParam Choose one node parameter.
	 * @param value set value for current parameter.
	 */
	public void setParameter(NodeParameters nodeParam, String value)
	{
		switch (nodeParam)
		{
		case Data_U:
			 //[0; infinite)
			 if(Double.parseDouble(value) < 0)
			 {
				 this.paramNodeU = 0.0;
				 //set voltage class automatically for current Node
				 this.changeVoltageClass();
			 }
			 else
			 {
				 if( this.paramNodeU != Double.parseDouble(value) )
				 {
					 this.paramNodeU = Double.parseDouble(value);
					 //set voltage class automatically for current Node
					 this.changeVoltageClass();
				 }
			 }
			 break;
		case Data_Angle: 
			 //[-90; 90]
			 if(Double.parseDouble(value) < -90.00)
			 {
				 this.paramNodeAngle = -90.00;
			 }
			 else if(Double.parseDouble(value) > 90.00)
			 {
				 this.paramNodeAngle = 90.00;
			 }
			 else
			 {
				 this.paramNodeAngle = Double.parseDouble(value);
			 }
			 break;
		case Data_Type:
			 //if 3 then Slack; if 2 then PV; if others then PQ;
			 if(Integer.parseInt(value)==Slack)
			 {
				 this.paramNodeType = Slack;
			 }
			 else if(Integer.parseInt(value)==PV)
			 {
				 this.paramNodeType = PV;
			 }
			 else
			 {
				 this.paramNodeType = PQ;
			 }
			 break;
		case Data_PG:
			 //[0; infinite)
			 if(Double.parseDouble(value) < 0)
			 {
				 this.paramNodePG = 0.0;
			 }
			 else
			 {
				 this.paramNodePG = Double.parseDouble(value);
			 }
			 break;
		case Data_QG:
			 this.paramNodeQG = Double.parseDouble(value);
			 break;
		case Data_PL:
			 //[0; infinite)
			 if(Double.parseDouble(value) < 0)
			 {
				 this.paramNodePL = 0.0;
			 }
			 else
			 {
				 this.paramNodePL = Double.parseDouble(value);
			 }
			 break;
		case Data_QL:
			 this.paramNodeQL = Double.parseDouble(value);
			 break;
		default:
			 break;
		}
	}
	
	/**
	 * set Node calculated result
	 * @param nodeRes Choose one node calculated result
	 * @param value set value of current calculated result
	 */
	public void setResult(NodeResults nodeRes, Double value)
	{
		switch (nodeRes)
		{
		case Res_U:
			 this.resNodeU = value;
			 break;
		case Res_Angle:
			 this.resNodeAngle = value;
			 break;
		case Res_PG:
			 this.resNodePG = value;
			 break;
		case Res_QG:
			 this.resNodeQG = value;
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
	 * returns Node id
	 * @return int: #id
	 */
	public int getID()
	{
		return this.id;
	}
	
	/**
	 * set special Node #id for Request action
	 * @param value : int
	 */
	public void setIDforRequest(int value)
	{
		this.idForRequest = value;
	}
	
	/**
	 * returns special Node id for Request action
	 * @return int: #idForRequest
	 */
	public int getIDforRequest()
	{
		return this.idForRequest;
	}
	
	/**
	 * get Center of Node
	 * @return Point: center
	 */
	public Point getCenter()
	{
		//Point center = new Point(this.parent.getX()+this.radius, this.parent.getY()+this.radius);
		Point center = new Point(this.parent.getX()+this.parent.getWidth()/2, this.parent.getY()+this.parent.getHeight());
		return center;
	}
	
	/**
	 * set Background of Node
	 * @param Color: c
	 */
	@Override
	public void setBackground(Color c)
	{
		bgColor = c;
	}
	
	/**
	 * get Background of Node
	 * @return Color: bgColor
	 */
	@Override
	public Color getBackground()
	{
		if(bgColor == null) {return Color.white;}
		else
		{
			return bgColor;
		}
	}
	
	//painting of component
	@Override
	protected void paintComponent(Graphics g)
	{
		//this.setSize(getWidth(), getWidth());
		/*
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(this.getBackground());
		g2d.fillOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.drawOval(0, 0, getWidth()-1, getHeight()-1);
		g2d.drawString(Integer.toString(id), getWidth()/2-4, getHeight()/2+4);
		g2d.setColor(Color.lightGray);
		g2d.drawString("      "+Integer.toString(this.radius), getWidth()/2-4, getHeight()/2+4);
		*/
		
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke((float)(1*ComponentContainer.zoom)));
		
		if(paramNodeType==PQ && (paramNodePL!=0 || paramNodeQL!=0))
		{
			g2d.setColor(new Color(0,0,0));
			g2d.drawLine(getX()+getWidth()/2, getY()+getHeight(), getX()+getWidth()/2, getY()+getHeight()/3);
		
			//-- draw consumption start:
			int[] xPoints = {getX()+getWidth()/2, getX()+getWidth()/2-getHeight()/3, getX()+getWidth()/2+getHeight()/3};
			int[] yPoints = {getY(),              getY()+getHeight()/3,              getY()+getHeight()/3};
			int num = 3;
		
			g2d.fillPolygon(xPoints, yPoints, num);
			//-- draw consumption end;
		}
		else if(paramNodeType==PV || paramNodeType==Slack)
		{
			g2d.setColor(new Color(0,0,0));
			g2d.drawLine(getX()+getWidth()/2, getY()+getHeight(), getX()+getWidth()/2, getY()+getHeight()/2);
			//-- draw generation start:
			//g2d.setColor(Color.white);
			//g2d.fillOval(getX()+getWidth()/2-getHeight()/4, getY(), getHeight()/2, getHeight()/2);
			g2d.setColor(Color.black);
			g2d.drawOval(getX()+getWidth()/2-getHeight()/4, getY(), getHeight()/2, getHeight()/2);
		
			QuadCurve2D q = new QuadCurve2D.Double(getX()+getWidth()/2, getY()+getHeight()/4, getX()+getWidth()/2+getHeight()/16, getY()+getHeight()/4-getHeight()/8, getX()+getWidth()/2+getHeight()/8, getY()+getHeight()/4);
			g2d.draw(q);
			q.setCurve(getX()+getWidth()/2, getY()+getHeight()/4, getX()+getWidth()/2-getHeight()/16, getY()+getHeight()/4+getHeight()/8, getX()+getWidth()/2-getHeight()/8, getY()+getHeight()/4);
			g2d.draw(q);
			//-- draw generation end;
		}
		
		g2d.setColor(new Color(0,125,200));
		g2d.fillRect(getX(), getY()+getHeight()-ComponentContainer.getScaledValue(6), getWidth(), ComponentContainer.getScaledValue(6));
		
		if(selected)
		{
			g2d.setColor(new Color(0, 125, 200, 30));
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.setColor(new Color(0, 125, 200, 255));
		}
	}
	
	
}
