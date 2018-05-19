package sv.gui.additionalElements;

import java.awt.*;
import javax.swing.JComponent;

/**
 * SelectionCorner
 *
 * @author SAV2
 * @version 1.0.0
 * @since 25.07.2012
 */
public class SelectionCorner extends JComponent
{
    
    private SelectionCornerType type;
    
    private Point center = null;
    
    public static final int DEFAULT_SIZE=8;

    public SelectionCorner(SelectionCornerType type,Point center)
    {
    	super();
        setType(type);
        setCenter(center);
    }
    
    
    public void setType(SelectionCornerType type) 
    {
        if (type == null) 
        {
            throw new IllegalArgumentException("SelectionCorner[setType]: type == null");
        }
        this.type = type;
    }    
    
    public SelectionCornerType getType() 
    {
        return type;
    }    
    
    public Point getCenter() 
    {
        return new Point(center);
    }    
    
    public void setCenter(Point center) 
    {
        
        if (center == null) {
            throw new IllegalArgumentException("SelectionCorner[setCenter]: center should not be null");
        }          
        
        this.center = new Point(center);
        
        // update the location of this JComponent
        setLocation(center.x - DEFAULT_SIZE/2, center.y - DEFAULT_SIZE/2);
        
        setSize(new Dimension(DEFAULT_SIZE, DEFAULT_SIZE));
        // the size of this JComponent remains
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        if(g==null)return;
        /*
        Graphics2D g2=(Graphics2D)g;
        
        g2.setColor(Color.gray);
        g2.fillRect(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
        g2.setColor(Color.black);
        g2.drawRect(0, 0, DEFAULT_SIZE-1, DEFAULT_SIZE-1);
        */
    }

}

