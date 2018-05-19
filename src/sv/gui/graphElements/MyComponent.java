package sv.gui.graphElements;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * MyComponent
 * <p/>
 * SImple test of MyComponent
 * @author SAV2
 * @since 18.07.2012
 * @version 1.0
 */

public class MyComponent extends JComponent
{
	
	public MyComponent()
	{
		this.setOpaque(true);
	}
	
	
	protected void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(new Color(0,0,0));
		g2.drawLine(getX()+getWidth()/2, getY()+getHeight(), getX()+getWidth()/2, getY());
		
		//-- draw consumption start:
		int[] xPoints = {getX()+getWidth()/2, getX()+getWidth()/2-getHeight()/3, getX()+getWidth()/2+getHeight()/3};
		int[] yPoints = {getY(),              getY()+getHeight()/3,              getY()+getHeight()/3};
		int num = 3;
		
		g2.fillPolygon(xPoints, yPoints, num);
		//-- draw consumption end;
		
		/*-- draw generation start:
		g2.setColor(Color.white);
		g2.fillOval(getX()+getWidth()/2-getHeight()/4, getY(), getHeight()/2, getHeight()/2);
		g2.setColor(Color.black);
		g2.drawOval(getX()+getWidth()/2-getHeight()/4, getY(), getHeight()/2, getHeight()/2);
		
		QuadCurve2D q = new QuadCurve2D.Double(getX()+getWidth()/2, getY()+getHeight()/4, getX()+getWidth()/2+getHeight()/16, getY()+getHeight()/4-getHeight()/8, getX()+getWidth()/2+getHeight()/8, getY()+getHeight()/4);
		g2.draw(q);
		q.setCurve(getX()+getWidth()/2, getY()+getHeight()/4, getX()+getWidth()/2-getHeight()/16, getY()+getHeight()/4+getHeight()/8, getX()+getWidth()/2-getHeight()/8, getY()+getHeight()/4);
		g2.draw(q);
		-- draw generation end; */
		
		g2.setColor(new Color(0,125,200));
		g2.fillRect(getX(), getY()+getHeight()-6, getWidth(), 6);
	}	
    
}


