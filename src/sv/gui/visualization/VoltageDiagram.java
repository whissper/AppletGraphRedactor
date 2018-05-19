package sv.gui.visualization;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sv.editor.ComponentContainer;
import sv.gui.localization.GuiLocalization;

/**
 * Voltage Diagram
 * @author Khayretdinov Denis
 *
 */
public class VoltageDiagram 
{
	 private Graphics2D g2;
	 
	 private int x;
	 private int y;
	 private int w;
	 private int h;
	 private int start;
	 private int fontheight;
	    
	 public VoltageDiagram (Graphics g2d)
	 {
		 this.g2 = (Graphics2D) g2d;
	 }
	 
	 /**
	  * Если произошло несоответствие текущего напряжения и
	  * опций, вытсавленных в Voltage Diagram options у текущего Узла
	  */
	 private void mismatchParam()
	 {
		g2.setColor(Color.lightGray);
		g2.fillArc(x,y,w,h,start,360);
 		g2.setColor(Color.gray);
 		g2.drawOval(x,y,w,h);
 		g2.setColor(Color.black);
 		g2.setFont(new Font("Tahoma", Font.PLAIN, fontheight));
		g2.drawString("mismatch", x+4, y+h/2+fontheight/2);
	 }
	 
	 public void paintDiagram(double currentValue, double maxValueIn, double normalValueIn, double minValueIn, int valX, int valY)
	    {
		 	x=valX;
	        y=valY; // x,y - координаты верхнего левого угла прямоугольника;
	        w=ComponentContainer.getScaledValue(50); // длина
	        h=ComponentContainer.getScaledValue(50); // высота
	        double normalValue=normalValueIn; // класс напряжения
	        double parametr=currentValue; // параметр, который получаем из свойств объекта
	        double maxValue=maxValueIn; // максимальное значение
	        double minValue=minValueIn; // минимальное напряжение
	        
	        //g2.drawString(Double.toString(parametr), x, y);
	        if(normalValue==0.4)
	        {
	        	 normalValue = normalValueIn*100.00; // класс напряжения
	        	 parametr=currentValue*100.00; // параметр, который получаем из свойств объекта
		         maxValue = maxValueIn*100.00; // максимальное значение
		         minValue = minValueIn*100.00; // минимальное напряжение
		        
		    }
	        else if(normalValue==10.00 || normalValue==35.00)
	        {
	        	 normalValue = normalValueIn*10.00; // класс напряжения
	        	 parametr=currentValue*10.00; // параметр, который получаем из свойств объекта
		         maxValue = maxValueIn*10.00; // максимальное значение
		         minValue = minValueIn*10.00; // минимальное напряжение
		        
	        }
	        
	        if(parametr > maxValue)
	        {
	        	parametr = maxValue;
	        }
	        else if(parametr < minValue)
	        {
	        	parametr = minValue;
	        }
	        
	        double rightMid = (normalValue-minValue)/2.00;
	        double leftMid = (maxValue-normalValue)/2.00;
	        double rightStep = 255.00/rightMid;
	        double leftStep = 255.00/leftMid;
	        
	        start=90; // начало (угол, от которого отсчитывается угол самой дуги)
	                    
	        Double doubleIncrease1=(-minValue+parametr)*rightStep; // интенсивность зеленого цвета в 1 четверти
	        int intIncrease1 = doubleIncrease1.intValue();
	        Double doubleDecrease2=255-(parametr-minValue-rightMid)*rightStep; // интенсивность синего цвета во второй четверти
	        int intDecrease2 = doubleDecrease2.intValue();
	        Double doubleIncrease3=(parametr-normalValue)*leftStep; // интенсивность красного цвета в 3 четверти
	        int intIncrease3 = doubleIncrease3.intValue();
	        Double doubleDecrease4=255-(parametr-normalValue-leftMid)*leftStep; // интенсивность зеленого цвета в 4 четверти
	        int intDecrease4 = doubleDecrease4.intValue();
	        fontheight = ComponentContainer.getScaledValue(11); //высота букв
	        int quarter=0; //четверть круга
	        Font font = new Font("Tahoma",Font.PLAIN,fontheight);
	        g2.setFont(font); 
	        //System.out.println(intIncrease1 +" "+ intDecrease2 +" "+ intIncrease3 +" "+ intDecrease4);	        
	        
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        if (parametr >= minValue & parametr < minValue+rightMid) {quarter=1;} 
	    	else if (parametr >= minValue+rightMid & parametr < normalValue) {quarter=2;}
	    		else if (parametr >= normalValue & parametr < normalValue+leftMid) {quarter=3;}
	    			else if (parametr >= normalValue+leftMid & parametr <= maxValue) {quarter=4;}

	        switch (quarter) 
	        { 
	        case 1:
	        		if( intIncrease1 > 255 || intIncrease1 < 0 )
	        		{
	        			mismatchParam();	
	        		}
	        		else
	        		{
	        			g2.setColor(new Color(0, intIncrease1, 255));
	        			double temp = (parametr-minValue)*(90/rightMid);
	        			int finish=(int)temp; // угол дуги, отсчитывается от start'a
	        			g2.fillArc(x,y,w,h, start,-finish);
	        			g2.drawOval(x,y,w,h);
	        			g2.setColor(Color.black);
	        			g2.drawString(String.valueOf(currentValue)+" "+GuiLocalization.kV, x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2);
	        		}
	        		break;
	        case 2: 
	        		if( intDecrease2 > 255 || intDecrease2 < 0 )
	        		{
	        			mismatchParam();
	        		}
	        		else
	        		{
	        			g2.setColor(new Color(0, 255, intDecrease2));
	        			double temp = (parametr-minValue)*(90/rightMid);
	        			int finish=(int)temp; // угол дуги, отсчитывается от start'a
	        			g2.fillArc(x,y,w,h, start,-finish);
	        			g2.drawOval(x,y,w,h);
	        			g2.setColor(Color.black);
	        			g2.drawString(String.valueOf(currentValue)+" "+GuiLocalization.kV, x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2);
	        		}
	        		break;
	        case 3:
	        		if( intIncrease3 > 255 || intIncrease3 < 0 )
	        		{
	        			mismatchParam();
	        		}
	        		else
	        		{
	        			g2.setColor(new Color(intIncrease3, 255, 0));
	        			double temp = 180+(parametr-normalValue)*(90/leftMid);
	        			int finish=(int)temp; // угол дуги, отсчитывается от start'a
	        			g2.fillArc(x,y,w,h, start,-finish);
	        			g2.drawOval(x,y,w,h);
	        			g2.setColor(Color.black);
	        			g2.drawString(String.valueOf(currentValue)+" "+GuiLocalization.kV, x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2);
	        		} 
	        		break;
	        case 4: 
	        		if( intDecrease4 > 255 || intDecrease4 < 0 )
	        		{
	        			mismatchParam();
	        		}
	        		else
	        		{
	        			g2.setColor(new Color(255, intDecrease4, 0));
	        			double temp = 180+(parametr-normalValue)*(90/leftMid);
	        			int finish=(int)temp; // угол дуги, отсчитывается от start'a
	        			g2.fillArc(x,y,w,h, start,-finish);
	        			g2.drawOval(x,y,w,h);
	        			g2.setColor(Color.black);
	        			g2.drawString(String.valueOf(currentValue)+" "+GuiLocalization.kV, x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2);
	        		}
	        		break;
	        default:
	        		mismatchParam();
	        		break;
	        }

	    }
	}
