package sv.gui.visualization;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sv.editor.ComponentContainer;

/**
 * Voltage Diagram
 * @author Khayretdinov Denis
 *
 */
public class VoltageDiagramPercent 
{
	 private Graphics2D g2;
	 
	 private int x;
	 private int y;
	 private int w;
	 private int h;
	 private int start;
	    
	 public VoltageDiagramPercent (Graphics g2d)
	 {
		 this.g2 = (Graphics2D) g2d;
	 }
	 
	 /**
	  * ���� ��������� �������������� �������� ���������� �
	  * �����, ������������ � Voltage Diagram options � �������� ����
	  */
	 private void mismatchParam()
	 {
		g2.setColor(Color.lightGray);
		g2.fillArc(x,y,w,h,start,360);
 		g2.setColor(Color.gray);
 		g2.drawOval(x,y,w,h);
 		g2.setColor(Color.black);
 		g2.setFont(new Font("Tahoma", Font.PLAIN, ComponentContainer.getScaledValue(11)));
		g2.drawString("mismatch", x+4, y+h/2+ComponentContainer.getScaledValue(11)/2);
	 }
	 
	 public void paintDiagram(double currentValue, double maxValueIn, double normalValueIn, double minValueIn, int valX, int valY)
	    {
		 	x=valX;
	        y=valY; // x,y - ���������� �������� ������ ���� ��������������;
	        w=ComponentContainer.getScaledValue(50); // �����
	        h=ComponentContainer.getScaledValue(50); // ������
	        double normalValue=normalValueIn; // ����� ����������
	        double parametr=currentValue; // ��������, ������� �������� �� ������� �������
	        double maxValue=maxValueIn; // ������������ ��������
	        double minValue=minValueIn; // ����������� ����������
	        double currentPercent = 100.00;
	        
	        
	        if(normalValue==0.4)
	        {
	        	 normalValue = normalValueIn;//*100.00; // ����� ����������
	        	 parametr=currentValue;//*100.00; // ��������, ������� �������� �� ������� �������
		         maxValue = maxValueIn;//*100.00; // ������������ ��������
		         minValue = minValueIn;//*100.00; // ����������� ����������
		        
		    }
	        else if(normalValue==10 || normalValue==35)
	        {
	        	 normalValue = normalValueIn;//*10.00; // ����� ����������
	        	 parametr=currentValue;//*10.00; // ��������, ������� �������� �� ������� �������
		         maxValue = maxValueIn;//*10.00; // ������������ ��������
		         minValue = minValueIn;//*10.00; // ����������� ����������
		        
	        }
	        
	        if(parametr > maxValue)
	        {
	        	parametr = maxValue;
	        }
	        else if(parametr < minValue)
	        {
	        	parametr = minValue;
	        }	        
	        
	        double rightOnePercent = (normalValue-minValue)/100.00;
	        double leftOnePercent = (maxValue-normalValue)/100.00;
	        if (parametr<=normalValue) currentPercent=(parametr-minValue)/rightOnePercent;
	        	else currentPercent=(parametr-normalValue)/leftOnePercent+100.00;
	        double step = 5.1;
	        start=90; // ������ (����, �� �������� ������������� ���� ����� ����)
	        double temp = currentPercent*1.8;
	                    
	        Double doubleIncrease1=currentPercent*step; // ������������� �������� ����� � 1 ��������
	        int intIncrease1 = doubleIncrease1.intValue(); 
	        Double doubleDecrease2=255-(currentPercent-50)*step; // ������������� ������ ����� �� ������ ��������
	        int intDecrease2 = doubleDecrease2.intValue();
	        Double doubleIncrease3=(currentPercent-100)*step; // ������������� �������� ����� � 3 ��������
	        int intIncrease3 = doubleIncrease3.intValue();
	        Double doubleDecrease4=255-(currentPercent-150)*step; // ������������� �������� ����� � 4 ��������
	        int intDecrease4 = doubleDecrease4.intValue();
	        int fontheight = ComponentContainer.getScaledValue(12); //������ ����
	        int quarter=0; //�������� �����
	        Font font = new Font("Tahoma",Font.PLAIN,fontheight);
	        g2.setFont(font); 
	        //System.out.println(intIncrease1 +" "+ intDecrease2 +" "+ intIncrease3 +" "+ intDecrease4);
	        //System.out.println(currentPercent);
	        
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        if (currentPercent >= 0 & currentPercent < 50) {quarter=1;} 
	    	else if (currentPercent >= 50 & currentPercent < 100) {quarter=2;}
	    		else if (currentPercent >= 100 & currentPercent < 150) {quarter=3;}
	    			else if (currentPercent >= 150 & currentPercent <= 200) {quarter=4;}
	    
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
	        		int finish=(int)temp; // ���� ����, ������������� �� start'a
	        		g2.fillArc(x,y,w,h, start,-finish);
	        		g2.drawOval(x,y,w,h);
	        		g2.setColor(Color.black);
	        		g2.drawString(String.format("%8.1f", currentPercent)+"%", x-ComponentContainer.getScaledValue(5), y+h/2+fontheight/2);
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
	        		int finish=(int)temp; // ���� ����, ������������� �� start'a
	        		g2.fillArc(x,y,w,h, start,-finish);
	        		g2.drawOval(x,y,w,h);
	        		g2.setColor(Color.black);
	        		g2.drawString(String.format("%8.1f", currentPercent)+"%", x-ComponentContainer.getScaledValue(5), y+h/2+fontheight/2);
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
	        		int finish=(int)temp; // ���� ����, ������������� �� start'a
	        		g2.fillArc(x,y,w,h, start,-finish);
	        		g2.drawOval(x,y,w,h);
	        		g2.setColor(Color.black);
	        		g2.drawString(String.format("%8.1f", currentPercent)+"%", x-ComponentContainer.getScaledValue(8), y+h/2+fontheight/2);
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
	        		int finish=(int)temp; // ���� ����, ������������� �� start'a
	        		g2.fillArc(x,y,w,h, start,-finish);
	        		g2.drawOval(x,y,w,h);
	        		g2.setColor(Color.black);
	        		g2.drawString(String.format("%8.1f", currentPercent)+"%", x-ComponentContainer.getScaledValue(8), y+h/2+fontheight/2);
	        	}
	        	break;
	        default:
	        	mismatchParam();
	        	break;
	        }
	        
	    }
	}
