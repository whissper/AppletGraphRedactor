package sv.gui.visualization;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import sv.editor.ComponentContainer;

/**
 * Angle Diagram
 * @author Khayretdinov Denis
 *
 */
public class AngleDiagram 
{
	 private Graphics2D g2;
	    
	 public AngleDiagram (Graphics g2d)
	 {
		 this.g2 = (Graphics2D) g2d;
	 }
	    
	 public void paintDiagram(int value, int valX, int valY)
	    {
	        int parametr=value; // параметр, который получаем
	        int x=valX;
	        int y=valY; // x,y - координаты верхнего левого угла прямоугольника;
	        int w=ComponentContainer.getScaledValue(50); // длина
	        int h=ComponentContainer.getScaledValue(50); // высота
	        int start=180; // начало (угол, от которого отсчитывается угол самой дуги)
	        double finish=parametr; // угол дуги, отсчитывается от start'a
	        int i = (int) finish; 
	        double doubleQuarter1=255-(parametr-45)*5.66; // интенсивность зеленого цвета в 1 четверти
	        int intQuarter1 = (int) doubleQuarter1; 
	        double doubleQuarter2=(parametr)*5.66; // интенсивность красного цвета во второй четверти
	        int intQuarter2 = (int) doubleQuarter2;
	        double doubleQuarter3=255-(parametr+45)*5.66; // интенсивность синего цвета в 3 четверти
	        int intQuarter3 = (int) doubleQuarter3;
	        double doubleQuarter4=(parametr+90)*5.66; // интенсивность зеленого цвета в 4 четверти
	        int intQuarter4 = (int) doubleQuarter4;
	        int fontheight = ComponentContainer.getScaledValue(12); //высота букв
	        int quarter=0; //четверть круга
	        Font font = new Font("Tahoma",Font.PLAIN,fontheight);
	        g2.setFont(font);
	        
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        if (parametr >= 46 & parametr <= 90) {quarter=1;} 
	        	else if (parametr >= 0 & parametr <= 45) {quarter=2;}
	        		else if (parametr >= -44 & parametr <= -1) {quarter=3;}
	        			else if (parametr >= -90 & parametr <= -45) {quarter=4;}
	        switch (quarter) 
	        { 
	        case 1: {Color diagramColor = new Color(255, intQuarter1, 0); 
	        g2.setColor(diagramColor);
	        g2.fillArc(x,y,w,h, start,-i);
	        g2.drawArc(x,y,w,h,90,180);
	        g2.drawLine(x+w/2, y, x+w/2, y+h);
	        g2.setColor(Color.black);
	        g2.drawString(String.valueOf(parametr)+"°", x+ComponentContainer.getScaledValue(6), y+h/2+fontheight/2+ComponentContainer.getScaledValue(7));} break; 
	        case 2: {Color diagramColor = new Color(intQuarter2, 255, 0); 
	        g2.setColor(diagramColor);
	        g2.fillArc(x,y,w,h, start,-i);
	        g2.drawArc(x,y,w,h,90,180);
	        g2.drawLine(x+w/2, y, x+w/2, y+h);
	        g2.setColor(Color.black);
	        g2.drawString(String.valueOf(parametr)+"°", x+ComponentContainer.getScaledValue(6), y+h/2+fontheight/2);} break;
	        case 3: {Color diagramColor = new Color(0, 255, intQuarter3); 
	        g2.setColor(diagramColor);
	        g2.fillArc(x,y,w,h, start,-i);
	        g2.drawArc(x,y,w,h,90,180);
	        g2.drawLine(x+w/2, y, x+w/2, y+h);
	        g2.setColor(Color.black);
	        g2.drawString(String.valueOf(parametr)+"°", x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2);} break;
	        case 4: {Color diagramColor = new Color(0, intQuarter4, 255); 
	        g2.setColor(diagramColor);
	        g2.fillArc(x,y,w,h, start,-i);
	        g2.drawArc(x,y,w,h,90,180);
	        g2.drawLine(x+w/2, y, x+w/2, y+h);
	        g2.setColor(Color.black);
	        g2.drawString(String.valueOf(parametr)+"°", x+ComponentContainer.getScaledValue(4), y+h/2+fontheight/2-ComponentContainer.getScaledValue(7));} break;
	        
	        }
	        
	    }
	}
