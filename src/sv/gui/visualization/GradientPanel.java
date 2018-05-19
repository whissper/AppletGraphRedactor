package sv.gui.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
import sv.gui.additionalElements.GradientQuality;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.graphElements.Node;
import sv.gui.localization.GuiLocalization;

public class GradientPanel extends JPanel implements GradientQuality
{
	private int pixelSize = 2;
	
	private ArrayList<Integer> X = new ArrayList<Integer>();
    private ArrayList<Integer> Y = new ArrayList<Integer>();
    private ArrayList<Color> C = new ArrayList<Color>();
    private ArrayList<Double> currentV = new ArrayList<Double>();
	private ArrayList<Double> minV = new ArrayList<Double>();
	private ArrayList<Double> maxV = new ArrayList<Double>();
	private ArrayList<Double> classV = new ArrayList<Double>();
	private int count = 0;
	private double alpha=2.10;//3.50;
	
    private List<ComponentPanel> components = new LinkedList<ComponentPanel>();
	
    BufferedImage buffer;
    Timer timer;
    
	public GradientPanel(List<ComponentPanel> comps)
	{
		super(null);
		this.components = comps;
	}
	
	/**
	 * Set pixel size
	 * @param value : <code>int</code>
	 */
	public void setPixelSize(int value)
	{
		if(value == GradientQuality.ultra)
		{
			this.pixelSize = ultra;
		}
		else if(value == GradientQuality.high)
		{
			this.pixelSize = high;
		}
		else if(value == GradientQuality.medium)
		{
			this.pixelSize = medium;
		}
		else if(value == GradientQuality.low)
		{
			this.pixelSize = low;
		}
		else
		{
			this.pixelSize = veryLow;
		}
	}
	
	/**
	 * get current gradient quality (pixel size)
	 * @return <code>String</code> : type of quality
	 */
	public String getCurrentGradientQuality()
	{
		if(this.pixelSize == ultra)
		{
			return GuiLocalization.ultra;
		}
		else if(this.pixelSize == high)
		{
			return GuiLocalization.high;
		}
		else if(this.pixelSize == medium)
		{
			return GuiLocalization.medium;
		}
		else if(this.pixelSize == low)
		{
			return GuiLocalization.low;
		}
		else
		{
			return GuiLocalization.veryLow;
		}
	}
	
	/**
	 * Draw gradient
	 */
	public void drawGradient()
	{
		clearData();
		this.fillGradient();
		repaint();
	}
	
	/**
	 * Stop timer for <code>repaint()</code> action
	 */
	public void stopTimer()
	{
		timer.stop();
	}
	
	/**
	 * Fill gradient algorithm
	 */
	private void fillGradient()
	{
		//timer for repainting (start)
		timer = new Timer(25, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				repaint();
				//System.out.println("painting");
			}
		});
		timer.setInitialDelay(100);
		timer.start();
		//timer for repainting (end)
		
		if(this.components.size() == 0) return;
		
		int maxX = 0;
    	int maxY = 0;
    	
    	for(ComponentPanel comp : this.components)
    	{
    		maxX = Math.max( maxX, ComponentContainer.getActualValue(comp.getX()+comp.getSize().width) );
    		maxY = Math.max( maxY, ComponentContainer.getActualValue(comp.getY()+comp.getSize().height) );
    	}
    	
    	maxX += 180; //private static final int ComponentContainer.RIGHT_DISTANCE_FROM_COMP
    	maxY += 50; //private static final int ComponentContainer.BOTTOM_DISTANCE_FROM_COMP
    	
		buffer = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = buffer.createGraphics();
		
		double normalValue;
        double minValue;
        double maxValue;
        double currentValue;

		for (ComponentPanel comp : components)
		{
			if(comp.getDelegate() instanceof Node)
			{
				X.add( ComponentContainer.getActualValue(((Node)comp.getDelegate()).getCenter().x) );
				Y.add( ComponentContainer.getActualValue(((Node)comp.getDelegate()).getCenter().y) );
				
				currentV.add(Double.parseDouble( ((Node)comp.getDelegate()).getResult(NodeResults.Res_U) ) );
				//currentV.add(Double.parseDouble( ((Node)comp.getDelegate()).getParameter(NodeParameters.Data_U) ) );
				
				maxV.add( ((Node)comp.getDelegate()).getVolDiagramMax() );
				minV.add( ((Node)comp.getDelegate()).getVolDiagramMin() );
				classV.add( ((Node)comp.getDelegate()).getVolDiagramClass() );
				
				normalValue = classV.get(classV.size()-1);
				minValue = minV.get(minV.size()-1);
				maxValue = maxV.get(maxV.size()-1);
				currentValue = currentV.get(currentV.size()-1);
				
				if(normalValue == 0.4)
				{
					normalValue = normalValue*100.00; // класс напряжения
					currentValue=currentValue*100.00; // параметр, который получаем из свойств объекта
					maxValue = maxValue*100.00; // максимальное значение
					minValue = minValue*100.00; // минимальное напряжение
	        
				}
				else if(normalValue==10.00 || normalValue==35.00)
				{
					normalValue = normalValue*10.00; // класс напряжения
					currentValue=currentValue*10.00; // параметр, который получаем из свойств объекта
					maxValue = maxValue*10.00; // максимальное значение
					minValue = minValue*10.00; // минимальное напряжение
	        
				}
        
				if(currentValue > maxValue)
				{
					currentValue = maxValue;
				}
				else if(currentValue < minValue)
				{
					currentValue = minValue;
				}
        
				double rightMid = (normalValue-minValue)/2.00;
				double leftMid = (maxValue-normalValue)/2.00;
				double rightStep = 255.00/rightMid;
				double leftStep = 255.00/leftMid;
                          
				Double doubleIncrease1=(-minValue+currentValue)*rightStep; // интенсивность зеленого цвета в 1 четверти
				int intIncrease1 = doubleIncrease1.intValue();
				Double doubleDecrease2=255-(currentValue-minValue-rightMid)*rightStep; // интенсивность синего цвета во второй четверти
				int intDecrease2 = doubleDecrease2.intValue();
				Double doubleIncrease3=(currentValue-normalValue)*leftStep; // интенсивность красного цвета в 3 четверти
				int intIncrease3 = doubleIncrease3.intValue();
				Double doubleDecrease4=255-(currentValue-normalValue-leftMid)*leftStep; // интенсивность зеленого цвета в 4 четверти
				int intDecrease4 = doubleDecrease4.intValue();
				int quarter=0; //четверть круга
         
           
				if (currentValue >= minValue & currentValue < minValue+rightMid) {quarter=1;} 
				else if (currentValue >= minValue+rightMid & currentValue < normalValue) {quarter=2;}
				else if (currentValue >= normalValue & currentValue < normalValue+leftMid) {quarter=3;}
    			else if (currentValue >= normalValue+leftMid & currentValue <= maxValue) {quarter=4;}
         
  
        
				switch (quarter) 
				{ 
				case 1:
					if( intIncrease1 > 255 || intIncrease1 < 0 )
					{
						break;	
					}
					else
					{
						C.add(new Color(0,intIncrease1,255));       			        			
					}
					break;
				case 2: 
					if( intDecrease2 > 255 || intDecrease2 < 0 )
					{
						break;
					}
					else
					{
						C.add(new Color(0,255,intDecrease2));
        			
        			}
					break;
				case 3:
					if( intIncrease3 > 255 || intIncrease3 < 0 )
					{
						break;
					}
					else
					{
						C.add(new Color(intIncrease3,255,0));
        	       	} 
					break;
				case 4: 
					if( intDecrease4 > 255 || intDecrease4 < 0 )
					{
						break;
					}
					else
					{
						C.add(new Color(255,intDecrease4,0));	
					}
					break;
               }
				
				count++;	
			}	
		}
		
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (int q=0; q<buffer.getWidth(); q+=pixelSize)
	       	for (int w=0; w<buffer.getHeight(); w+=pixelSize)
	       	{
	       		int R,G,B;
	       		double r=0.00,g=0.00,b=0.00;
	       		double SumA_R=0.00;
	       		double SumA_G=0.00;
	       		double SumA_B=0.00;
	       		double SumB_R=0.00;
	       		double SumB_G=0.00;
	       		double SumB_B=0.00;
	   	 
	       		for (int e=0; e<count; e++)
	       		{
	       			double Distance = Math.sqrt(Math.pow((q-X.get(e)),2)+Math.pow((w-Y.get(e)),2));
			
	       			if(Distance!=0) {
	       				SumA_R+=C.get(e).getRed()*(1/Math.pow(Distance,alpha));
	       				SumA_G+=C.get(e).getGreen()*(1/Math.pow(Distance,alpha));
	       				SumA_B+=C.get(e).getBlue()*(1/Math.pow(Distance,alpha));

	       				SumB_R+=(1/Math.pow(Distance,alpha));
	       				SumB_G+=(1/Math.pow(Distance,alpha));
	       				SumB_B+=(1/Math.pow(Distance,alpha));      
	       			}
	       			else
	       			{
	       				SumA_R=C.get(e).getRed();
	       				SumA_G=C.get(e).getGreen();
	       				SumA_B=C.get(e).getBlue();
	       				SumB_R=1;
	       				SumB_G=1;
	       				SumB_B=1;
	       			}
	       		}
	       		
	       		if(SumB_R!=0)
	       			r=SumA_R/SumB_R;

	       		if(SumB_G!=0)
	       			g=SumA_G/SumB_G;

	       		if(SumB_B!=0)
	       			b=SumA_B/SumB_B;

	       		R=(int) (r);
	       		G=(int)(g);
	       		B=(int)(b);

	       		g2d.setColor(new Color(R,G,B));
	       		g2d.fillRect(q, w, pixelSize, pixelSize);

	       	}

	}
	
	/**
	 * Clear Data
	 */
	public void clearData()
	{
		X.clear();
		Y.clear();
		C.clear();
		
		currentV.clear();
		minV.clear();
		maxV.clear();
		classV.clear();

		count = 0;
		
		buffer = null;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.scale(ComponentContainer.zoom, ComponentContainer.zoom);
		
		if(buffer != null)
		{
			g2d.drawImage(buffer, 0, 0, this);
		}
	}

}

