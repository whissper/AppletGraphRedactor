package sv.editor;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import java.util.List;
import java.util.LinkedList;

import sv.gui.additionalElements.*;
import sv.editor.interfaces.*;
import sv.gui.graphElements.*;
import sv.gui.localization.GuiLocalization;

/**
 * ComponentPanel
 * <p/>
 * Simple component panel, which delegates all painting operations to renderer object
 *
 * @author SAV2
 * @version 1.0.0
 * @since 25.07.2012
 */
public class ComponentPanel extends JPanel implements ActionListener, FocusListener, KeyListener
{
	/**
	 * Actual bounds of current component Panel
	 */
	private Rectangle actualBounds = new Rectangle();
	
	/**
	 * minimum size of ComponentPanel side
	 */
	static int COMP_MIN_SIZE = ComponentContainer.getScaledValue(40);

	/**
	 * link to the Component Container instance
	 */
	private ComponentContainer compCont;

	/**
	 * Set of selection mark components
	 */
	private ArrayList<SelectionCorner> corners;
	
    /**
     * Half size of the selection mark
     */
    private static final int HALF_MARK = 3;
    
    /**
     * Renderer component
     */
    private JComponent delegate;

    /**
     * component selection state
     */
    private boolean selected = false;

    /**
     * component selection listeners
     */
    private List<ComponentSelectionListener> listeners;
    
    /**
     * Pop-up menu of the ComponentPanel 
     */
    JPopupMenu compPanelMenu;
    
    //items of compPanelMenu
    private JMenuItem options;
    private JMenuItem voltageDiagram;
    private JMenuItem connect;
    private JMenuItem deleteComp;
    
    /**
     * creates component based on the renderer passed
     * <p/>
     * constructor for drawing of component with the mouse
     * 
     * @param delegate renderer object to use
     * @param componentCont its component container
     */
    public ComponentPanel(JComponent delegate, ComponentContainer componentCont) 
    {
        super(null);
        setOpaque(false);
        
        this.compCont = componentCont;
        this.delegate = delegate;
        
        //Node parent
        if (delegate instanceof Node)
        {
        	((Node) this.delegate).setParent(this);
        }
        
        //Edge parent
        if (delegate instanceof Edge)
        {
        	((Edge) this.delegate).setParent(this);       	
        }
        
        listeners = new LinkedList<ComponentSelectionListener>();
        MouseHandler mh = new MouseHandler();
        initCompPanelMenu();
        
        if(this.delegate instanceof Node)
        {
        	addMouseListener(mh);
        	addMouseMotionListener(mh);
        }
        
        addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {
                if (ComponentPanel.this.delegate != null)
                {
                    ComponentPanel.this.delegate.setSize(getSize());
                    repaint();
                }
            }
            
            @Override
            public void componentMoved(ComponentEvent e)
            {
            	if(ComponentPanel.this.delegate instanceof Node)
            	//if Node	
            	{
            		for(ComponentPanel comp : compCont.getComps())
            		{
            			if(comp.getDelegate() instanceof Edge)
            			//if iterated component is Edge
            			{
            				if( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID()==((Node)ComponentPanel.this.delegate).getID() 
            	    			||
            	    			((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID()==((Node)ComponentPanel.this.delegate).getID() )
            				/*
            				 * if Edge.firstNode = current Node-component
            				 * OR Edge.lastNode = current Node-component
            				 */
            				{
            					((Edge)comp.getDelegate()).rebindEdge();
            				}
            			}
            		}
            	}
            }
        });
        
        //creating of selection mark components for Node-component
        if(this.delegate instanceof Node)
        {
        	this.corners = new ArrayList<SelectionCorner>();
        	for (SelectionCornerType type : SelectionCornerType.values()) 
        	{         
        		SelectionCorner corner = new SelectionCorner(type,setCornerPoint(type));

        		corner.addMouseListener(mh);
        		corner.addMouseMotionListener(mh);
            
        		this.add(corner);
        		this.corners.add(corner);
        	}
        }

        this.add(compPanelMenu);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(true);
        addKeyListener(this);
        addFocusListener(this);
        
        //this.actualSize = new Dimension(ComponentContainer.getActualValue(getSize().width),
        								//ComponentContainer.getActualValue(getSize().height));
        
        //componentOptions = new ComponentOptionsMenu(JOptionPane.getRootFrame(), true);
  	    //componentOptions.setVisible(false);
    }
    
    /**
     * get actual bounds of current component
     * @return its actual <code>Rectangle</code>
     */
    Rectangle getActualBounds()
    {
    	return this.actualBounds;
    }
    
    /**
     * set actual bounds of current component
     * @param value <code>Rectangle</code>
     */
    public void setActualBounds(Rectangle value)
    {
    	this.actualBounds.x = ComponentContainer.getActualValue(value.x);
    	this.actualBounds.y = ComponentContainer.getActualValue(value.y);
    	this.actualBounds.width = ComponentContainer.getActualValue(value.width);
    	this.actualBounds.height = ComponentContainer.getActualValue(value.height);
    }
    
    /**
     * change Strings
     */
    public void changeStrings()
    { 
		options.setText(GuiLocalization.options);
		voltageDiagram.setText(GuiLocalization.voltage_diagram);
		connect.setText(GuiLocalization.connect);
		deleteComp.setText(GuiLocalization.delete);
    }
    
    /**
     * construct Component Panel Pop-up menu
     */
    private void initCompPanelMenu()
    {
    	compPanelMenu = new JPopupMenu("Add Options");  
		options = new JMenuItem(GuiLocalization.options, new ImageIcon( this.getClass().getResource("img/options.png") ));
		options.setMnemonic(KeyEvent.VK_O);
		
		voltageDiagram = new JMenuItem(GuiLocalization.voltage_diagram, new ImageIcon( this.getClass().getResource("img/vol_dgrm_settings.png") ));
		voltageDiagram.setMnemonic(KeyEvent.VK_V);
		
		connect = new JMenuItem(GuiLocalization.connect, new ImageIcon( this.getClass().getResource("img/connect.png") ));
		connect.setMnemonic(KeyEvent.VK_C);
		
		//JMenuItem smth = new JMenuItem("smth else");
		deleteComp = new JMenuItem(GuiLocalization.delete, new ImageIcon( this.getClass().getResource("img/delete.png") ));
		deleteComp.setMnemonic(KeyEvent.VK_D);
		
		compPanelMenu.add(options);
		compPanelMenu.add(voltageDiagram);
		compPanelMenu.addSeparator();
		compPanelMenu.add(connect);
		//compPanelMenu.add(smth);
		compPanelMenu.add(deleteComp);
		
		options.addActionListener(this);
		voltageDiagram.addActionListener(this);
		connect.addActionListener(this);
		//smth.addActionListener(this);
		deleteComp.addActionListener(this);
		
		if(this.getDelegate() instanceof Edge)
		{
			voltageDiagram.setEnabled(false);
			connect.setEnabled(false);
		}
    }
    
    /**
     * Paints component and selection state
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
    	//Graphics2D g2d = (Graphics2D)g;
    	//g2d.setColor(Color.green);
    	//g2d.fillRect(0,0,getSize().width,getSize().height);
    	//g2d.transform(ComponentContainer.at);
    	
    	//if Node then draw its delegate first
    	if (delegate != null && delegate instanceof Node)
        {
            delegate.paint(g);
        }
        
        if (selected)
        {
            if(delegate instanceof Node)
            {
            	paintSelectionMarks(g);
            	boundSelectionMarks();
            	((Node) delegate).setSelectedState(true);
            }
            else if(delegate instanceof Edge)
            {
            	((Edge) delegate).setSelectedState(true);
            	//((Edge) delegate).repaint();
            }
        }
        else
        {
        	if(delegate instanceof Node)
            {
        		unBoundSelectionMarks();
        		((Node) delegate).setSelectedState(false);
            }
            else if(delegate instanceof Edge)
            {
            	((Edge) delegate).setSelectedState(false);
            	//((Edge) delegate).repaint();
            }
        }
        
        //if Edge then draw its delegate last
        if (delegate != null && delegate instanceof Edge)
        {
            delegate.paint(g);
        }
    }

    /**
     * Sets selection state
     *
     * @param selected state to set
     */
    void setSelected(boolean selected) 
    {
        if (this.selected == selected) return;
        
        this.selected = selected;
        
        if (selected)
        {
            fireSelectionEvent();
        }
    }

    /**
     * fires selection event
     */
    private void fireSelectionEvent() 
    {
        for (ComponentSelectionListener listener : listeners) {
            listener.componentSelected(this);
        }
    }

    /**
     * Paints selection marks
     *
     * @param g Graphics context
     */
    private void paintSelectionMarks(Graphics g) 
    {
    	int scaledHalfMark = ComponentContainer.getScaledValue(HALF_MARK);
    	
    	if(this.delegate instanceof Node)
    	{
    		g.setColor(Color.black);
        	g.fillRect(0, 0, scaledHalfMark, scaledHalfMark);
        	g.fillRect(getWidth() - scaledHalfMark, 0, scaledHalfMark, scaledHalfMark);
        	g.fillRect(0, getHeight() - scaledHalfMark, scaledHalfMark, scaledHalfMark);
        	g.fillRect(getWidth() - scaledHalfMark, getHeight() - scaledHalfMark, scaledHalfMark, scaledHalfMark);
        	g.fillRect(getWidth() / 2 - scaledHalfMark, 0, scaledHalfMark * 2, scaledHalfMark);
        	g.fillRect(getWidth() / 2 - scaledHalfMark, getHeight() - scaledHalfMark, scaledHalfMark * 2, scaledHalfMark);
        	g.fillRect(0, getHeight() / 2 - scaledHalfMark, scaledHalfMark, scaledHalfMark * 2);
        	g.fillRect(getWidth() - scaledHalfMark, getHeight() / 2 - scaledHalfMark, scaledHalfMark, scaledHalfMark * 2);
    	}
    }

    /**
     * Bound selection marks components
     * 
     */
    private void boundSelectionMarks()
    {   	
    	if(this.delegate instanceof Edge) return;
    	
    	for (SelectionCorner corner : corners)
    	{
    		// update the center point of selection corners
    		corner.setCenter(setCornerPoint(corner.getType()));
    		corner.setVisible(true);
    	}
    	
    }
   
    /**
     * unbound selection marks components
     * i.e. set their "Visible" state (visibility) to "false"
     * 
     */
    private void unBoundSelectionMarks()
    {
    	if(this.delegate instanceof Edge) return;
    	
    	for (SelectionCorner corner : corners)
    	{
    		corner.setVisible(false);
    	}
    	
    }
    
    /**
     * set position for selection mark components
     * on existing object
     * 
     * @param type SelectionCorenrType
     * @return new Point(x,y)
     */
    private Point setCornerPoint(SelectionCornerType type)
    {
    	if (type == SelectionCornerType.EAST){
    	    return new Point(getWidth(), getHeight() / 2);
    	} else if (type == SelectionCornerType.SOUTH){
    		return new Point(getWidth() / 2, getHeight());
    	} else if (type == SelectionCornerType.WEST){
    		return new Point(0, getHeight() / 2);
    	} else if (type == SelectionCornerType.NORTH){
    		return new Point(getWidth() / 2, 0);
    	} else if (type == SelectionCornerType.NORTHEAST){
    		return new Point(getWidth(), 0);
    	} else if (type == SelectionCornerType.SOUTHEAST){
    		return new Point(getWidth(), getHeight());
    	} else if (type == SelectionCornerType.NORTHWEST){
    		return new Point(0, 0);
    	} else {
    		return new Point(0, getHeight());
    	}
    }
    

    //adding of ComponentSelectionListener
    public void addComponentSelectionListener(ComponentSelectionListener listener) 
    {
        listeners.add(listener);
    }

    /**
     * Mouse handler - to drag and resize component
     */
    private class MouseHandler extends MouseAdapter 
    {
    	/**
    	 * mouse is dragged within clicked its right button: <code>BUTTON3</code> 
    	 */
    	private boolean mouseIsDragged = false;
    	
    	/**
    	 * current pressed Mouse button
    	 */
    	private int pressedButton = 0;
    	
        /**
         * x of mouse pointer displacement from top-left corner
         * <br />
         * i.e. X coordinate of point where mouse was pressed on current component
         */
        private int dx = 0;

        /**
         * y of mouse pointer displacement from top-left corner
         * <br />
         * i.e. Y coordinate of point where mouse was pressed on current component
         */
        private int dy = 0;

        /**
         * starting X coordinate of current component 
         */
        private int startX;
        /**
         * starting Y coordinate of current component 
         */
        private int startY;
        /**
         * the initial value of the component Width
         */
        private int startWidth;
        /**
         * the initial value of the component Height
         */
        private int startHeight;
        /**
         * starting cursor position on X 
         * when mouse is pressed at Selection mark component 
         */
        private int startCursorX;
        /**
         * starting cursor position on Y 
         * when mouse is pressed at Selection mark component 
         */
        private int startCursorY;

        @Override
        public void mousePressed(MouseEvent e) 
        {
        	/*
        	if(ComponentPanel.this.delegate instanceof Edge)
        	{
        		MouseEvent parentEvent = new MouseEvent(e.getComponent().getParent(),
        												MouseEvent.MOUSE_PRESSED,
        												e.getWhen(),
        												e.getModifiers(),
        												e.getComponent().getX() + e.getX(),
        												e.getComponent().getY() + e.getY(),
        												e.getClickCount(),
        												false );
        	
        		e.getComponent().getParent().dispatchEvent(parentEvent);
        	}
        	*/
        	
        	pressedButton = e.getButton();
        	
        	setSelected(true);
        	
            dx = e.getX();
            dy = e.getY();
            
            if(e.getSource() instanceof SelectionCorner)
            {
            	startCursorX = e.getXOnScreen(); 
            	startCursorY = e.getYOnScreen();
                startX = getX();
                startY = getY();
                startWidth = getWidth();
                startHeight = getHeight();        
                
                // set cursor according the type of selection corner
                SelectionCorner corner = (SelectionCorner)e.getSource();
                setCursor(corner);
            }
            //<new date="14.02.2013" start:>
            if (ComponentPanel.this.delegate instanceof Node)
            {
            	compCont.moveToFront(ComponentPanel.this);
            	
            	for(ComponentPanel comp : compCont.getComps())
            	{
            		if(comp.getDelegate() instanceof Edge)
            		{
            			if( 
            				((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).equals(ComponentPanel.this.delegate)
            				||
            				((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).equals(ComponentPanel.this.delegate)
            			  )
            			{
            				compCont.moveToFront(comp);
            			}
            		}
            	}
            }
            //compCont.setLayer(c, layer)
            //<new date="14.02.2013" end;>
            
            if(pressedButton == MouseEvent.BUTTON1)//left mouse button
            {
            	//if frstNode != null AND delegate instance of Node
            	if (!(compCont.frstNode == null) && (ComponentPanel.this.delegate instanceof Node))
            	{
            		compCont.scndNode = (Node)ComponentPanel.this.delegate;
        			compCont.addEdgeComponent();
        			//compCont.repaint();
            	}
            }
            
        	//wipe temp data for adding Edge component to the container
        	compCont.frstNode = null;
    		compCont.scndNode = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            if(e == null) return;
            
            if (!(e.getSource() instanceof SelectionCorner))return;
            
            SelectionCorner corner = (SelectionCorner)e.getSource();
            setCursor(corner);
        }
        

        @Override
        public void mouseExited(MouseEvent e) 
        {
            if(e == null) return;
            
            if (!(e.getSource() instanceof SelectionCorner))return;
            
            SelectionCorner corner = (SelectionCorner)e.getSource();
            corner.setCursor(Cursor.getDefaultCursor());
        }
        
        @Override
        public void mouseDragged(MouseEvent e) 
        {
        	if(pressedButton != MouseEvent.BUTTON1 && pressedButton != MouseEvent.BUTTON3) return;
        	if(ComponentPanel.this.delegate instanceof Edge) return;
        	if(!compCont.drawingState) return;
        	
        	if(pressedButton == MouseEvent.BUTTON1)
        	{
        		int currentX = getX() + e.getX() - dx;
        		int currentY = getY() + e.getY() - dy;

        		if(currentX <= 0)
        		{
        			currentX = 1;
        		}
        	
        		if(currentY <= 0)
        		{
        			currentY = 1;
        		}
        	
        		if(compCont.gridEnabled)
        		{
        			currentX -= currentX % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			currentY -= currentY % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        		}
        	
        		setLocation(currentX, currentY);
           
        		if(e.getSource() instanceof SelectionCorner)
        		{
        			SelectionCorner corner = (SelectionCorner)e.getSource();
        			setCursor(corner);
        			changeComponent(e, corner.getType());
        			repaint();
        		}
            
        		compCont.repaint(); //for repaint visualization info above nodes
        	}
        	else if(pressedButton == MouseEvent.BUTTON3)
        	{
        		mouseIsDragged = true;
        		
        		ComponentPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        		compCont.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        		
        		//View Position
        		Point vp = compCont.appGraphRed.ccScrollPane.getViewport().getViewPosition();
        		//Viewport Size
        		Dimension vps = compCont.appGraphRed.ccScrollPane.getViewport().getSize();
        		
        		vp.translate( dx-e.getX(), dy-e.getY() );
        		
        		compCont.scrollRectToVisible(new Rectangle(vp, vps));
        	}
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
        	//DblClick START
    	  	if((e.getButton() == MouseEvent.BUTTON1) && (e.getSource() instanceof ComponentPanel))
    	  	{
    	  		if (e.getClickCount() == 2 && !e.isConsumed()) 
    	  		{
    	            e.consume();
    	            
    	            if( ComponentPanel.this.delegate instanceof Node )
    	            {
    	            	compCont.componentNodeOptions.setLocationRelativeTo(compCont.appGraphRed);
    	            	compCont.componentNodeOptions.representData(ComponentPanel.this);
    	            	compCont.componentNodeOptions.refresh();
    	            	compCont.componentNodeOptions.setVisible(true);
    	            }
    	            /*
    	            else if( ComponentPanel.this.delegate instanceof Edge )
    	            {
    	            	compCont.componentEdgeOptions.setLocationRelativeTo(e.getComponent());
    	            	compCont.componentEdgeOptions.representData(ComponentPanel.this);
    	            	compCont.componentEdgeOptions.refresh();
    	            	compCont.componentEdgeOptions.setVisible(true);
    	            }
    	            */
    	  		}
    	  	}
        	//DblClick END
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
        	//set KeyBoard focus for selected component
        	ComponentPanel.this.requestFocusInWindow();
        	
        	if(mouseIsDragged)//if mouse with clicked right button was dragged
        	{
        		mouseIsDragged = false;
        		ComponentPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        		compCont.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        		return;
        	}
        	
        	if(e.getButton() == MouseEvent.BUTTON1)//left mouse button
        	{
        		compCont.recheckSize();
        	}
        	else if (e.getButton() == MouseEvent.BUTTON3 )//right mouse button
    	  	{    
        		compPanelMenu.show(e.getComponent(), dx, dy);
    	  	}
        	
        	setActualBounds(getBounds());
        }
        
        /**
         * change size of the selected component 
         * according its pressed selection mark
         * 
         * @param e MouseEvent
         * @param type SelectionCornerType
         */
        private void changeComponent(MouseEvent e, SelectionCornerType type) 
        {
        	int changedX;
        	int changedY;
        	int changedWidth;
        	int changedHeight;
        	/*
        	if (delegate instanceof Node)
        	{
        		switch (type)
        		{
        		case EAST:	
        			 setBounds(	startX,
        					 	startY,
        					 	startWidth + (e.getXOnScreen()-startCursorX),
        					 	startHeight + (e.getXOnScreen()-startCursorX) );
        			 break;
        		case SOUTH:
        			 setBounds( startX - (e.getYOnScreen()-startCursorY),
        					 	startY,
        					 	startWidth + (e.getYOnScreen()-startCursorY),
        					 	startHeight + (e.getYOnScreen()-startCursorY) );
        			 break;
        		case WEST:
        			 setBounds( getX(),
        					 	startY - (startX - getX()),
        					 	startWidth + (startX - getX()),
        					 	startHeight + (startX - getX()) );
        			 break;
        		case NORTH:
        			 setBounds( startX,
        					 	getY(),
        					 	startWidth + (startY - getY()),
        					 	startHeight + (startY - getY()) );
        			 break;
        		case NORTHEAST:
        			 setBounds( startX,
     					 		compCont.squareLimiter.getResult( CoordinateType.Y, startX, (getX()+startWidth) + (e.getXOnScreen()-startCursorX), startY+startHeight, getY() ),
     					 		startWidth + Math.min(startCursorY-e.getYOnScreen(), e.getXOnScreen() - startCursorX),
     					 		startHeight + Math.min(startCursorY-e.getYOnScreen(), e.getXOnScreen() - startCursorX) );
        			 break;
        		case SOUTHEAST:
        			 setBounds( startX,
        					 	startY,
        					 	startWidth + Math.min(e.getXOnScreen()-startCursorX, e.getYOnScreen()-startCursorY),
        					 	startHeight + Math.min(e.getYOnScreen()-startCursorY, e.getXOnScreen()-startCursorX) );
        			 break;
        		case NORTHWEST:
        			 setBounds( compCont.squareLimiter.getResult( CoordinateType.X, startX+startWidth, getX(), startY+startHeight, getY() ),
        					 	compCont.squareLimiter.getResult( CoordinateType.Y, startX+startWidth, getX(), startY+startHeight, getY() ),
        					 	startWidth + Math.min(startX - getX(), startY - getY()),
        					 	startHeight + Math.min(startX - getX(), startY - getY()) );
        			 break;
        			 
        		default : //SOUTHWEST
        			 setBounds( compCont.squareLimiter.getResult( CoordinateType.X, startX+startWidth, getX(), startY, (getY()+startHeight) + (e.getYOnScreen() - startCursorY) ),
        					 	startY,
        					 	startWidth + Math.min(startCursorX-e.getXOnScreen(), e.getYOnScreen()-startCursorY),
        					 	startHeight + Math.min(startCursorX-e.getXOnScreen(), e.getYOnScreen()-startCursorY) );
        			 break;
        		}
        	}
        	else
        	{
        	*/
        		switch (type)
        		{
        		case EAST:
        			 changedWidth = startWidth+(e.getXOnScreen()-startCursorX);
        			 if(compCont.gridEnabled)
        			 {
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        			 }
        			 setBounds(	startX,
        					 	startY,
        					 	changedWidth,
        					 	startHeight );
        			 break;
        		case SOUTH:
        			 changedHeight = startHeight+(e.getYOnScreen()-startCursorY);
        			 if(compCont.gridEnabled)
        			 {
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        			 }
        			 setBounds( startX,
        					 	startY,
        					 	startWidth,
        					 	changedHeight );
        			 break;
        		case WEST:
        			 changedX = getX();
        			 changedWidth = startWidth+(startX - getX());
        			 if(compCont.gridEnabled)
        			 {
        				 changedX -= changedX % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if( changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        				 changedX = startX + startWidth - COMP_MIN_SIZE;
        			 }
        			 setBounds( changedX,
        					 	startY,
        					 	changedWidth,
        					 	startHeight );
        			 break;
        		case NORTH:
        			 changedY = getY();
        			 changedHeight = startHeight+(startY - getY());
        			 if(compCont.gridEnabled)
        			 {
        				 changedY -= changedY % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        				 changedY = startY + startHeight - COMP_MIN_SIZE;
        			 }
        			 setBounds( startX,
        					 	changedY,
        					 	startWidth,
        					 	changedHeight );
        			 break;
        		case NORTHEAST:
        			 changedY = getY();
        			 changedWidth = startWidth+(e.getXOnScreen()-startCursorX);
        			 changedHeight = startHeight+(startY - getY());
        			 if(compCont.gridEnabled)
        			 {
        				 changedY -= changedY % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        				 changedY = startY + startHeight - COMP_MIN_SIZE;
        			 }
        		 	 setBounds( startX,
        		 			 	changedY,
        		 			 	changedWidth,
        		 			 	changedHeight );
        			 break;
        		case SOUTHEAST:
        			 changedWidth = startWidth+(e.getXOnScreen()-startCursorX);
        			 changedHeight = startHeight+(e.getYOnScreen()-startCursorY);
        			 if(compCont.gridEnabled)
        			 {
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        			 }
        			 setBounds( startX,
        					 	startY,
        					 	changedWidth,
        					 	changedHeight );
        			 break;
        		case NORTHWEST:
        			 changedX = getX();
        			 changedY = getY();
        			 changedWidth = startWidth+(startX - getX());
        			 changedHeight = startHeight+(startY - getY());
        			 if(compCont.gridEnabled)
        			 {
        				 changedX -= changedX % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedY -= changedY % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if( changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        				 changedX = startX + startWidth - COMP_MIN_SIZE;
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        				 changedY = startY + startHeight - COMP_MIN_SIZE;
        			 }
        			 setBounds( changedX,
        					 	changedY,
        					 	changedWidth,
        					 	changedHeight );
        			 break;
        			 
        		default : //SOUTHWEST
        			 changedX = getX();
        			 changedWidth = startWidth+(startX - getX());
        			 changedHeight = startHeight+(e.getYOnScreen()-startCursorY);
        			 if(compCont.gridEnabled)
        			 {
        				 changedX -= changedX % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedWidth -= changedWidth % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        				 changedHeight -= changedHeight % ComponentContainer.getScaledValue(compCont.gridPanel.getCellSize());
        			 }
        			 if(changedHeight < COMP_MIN_SIZE)
        			 {
        				 changedHeight = COMP_MIN_SIZE;
        			 }
        			 if( changedWidth < COMP_MIN_SIZE)
        			 {
        				 changedWidth = COMP_MIN_SIZE;
        				 changedX = startX + startWidth - COMP_MIN_SIZE;
        			 }
        			 setBounds( changedX,
        					 	startY,
        					 	changedWidth,
        					 	changedHeight );
        			 break;
        		//}
        		
        	/*
        	// the old way -->
        	if (type == SelectionCornerType.EAST){
        		setBounds(startX, startY,  startWidth+(e.getXOnScreen()-startCursorX), startHeight);
        	} else if (type == SelectionCornerType.SOUTH){
        		setBounds(startX, startY,  startWidth, startHeight+(e.getYOnScreen()-startCursorY));
        	} else if (type == SelectionCornerType.WEST){
        		setBounds(getX(), startY,  startWidth+(startX - getX()), startHeight);
        	} else if (type == SelectionCornerType.NORTH){
        		setBounds(startX, getY(),  startWidth, startHeight+(startY - getY()));
        	} else if (type == SelectionCornerType.NORTHEAST){
        		setBounds(startX, getY(),  startWidth+(e.getXOnScreen()-startCursorX), startHeight+(startCursorY-e.getYOnScreen()));
        	} else if (type == SelectionCornerType.SOUTHEAST){
        		setBounds(startX, startY,  startWidth+(e.getXOnScreen()-startCursorX), startHeight+(e.getYOnScreen()-startCursorY));
        	} else if (type == SelectionCornerType.NORTHWEST){
        		setBounds(getX(), getY(),  startWidth+(startX - getX()), startHeight+(startY - getY()));
        	} else {
        		setBounds(getX(), startY,  startWidth+(startCursorX-e.getXOnScreen()), startHeight+(e.getYOnScreen()-startCursorY));
        	}
        	*/
        	}
        		setActualBounds(getBounds());
        }
        
        /**
         * set Cursor for selection mark components
         * according its type
         * 	
         * @param corner SelectionCorner
         */
        private void setCursor(SelectionCorner corner)
        {
        	     if (corner.getType() == SelectionCornerType.EAST) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.SOUTH) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.WEST) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.NORTH) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.SOUTHEAST) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.SOUTHWEST) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        	        } else if (corner.getType() == SelectionCornerType.NORTHEAST) {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        	        } else {
        	            corner.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        	        }
        }

    }

	@Override
	public void actionPerformed(ActionEvent e) 
		{
			String s = e.getActionCommand();
			
			//calls Component Option menu
			if (s.equals(GuiLocalization.options)) 
		  	{
				if( this.delegate instanceof Node )
				{
					compCont.componentNodeOptions.setLocationRelativeTo(this.compCont.appGraphRed);
					compCont.componentNodeOptions.representData(this);
					compCont.componentNodeOptions.refresh();
	            	compCont.componentNodeOptions.setVisible(true);
				}
				else if( this.delegate instanceof Edge )
				{
					compCont.componentEdgeOptions.setLocationRelativeTo(this.compCont.appGraphRed);
					compCont.componentEdgeOptions.representData(this);
					compCont.componentEdgeOptions.refresh();
	            	compCont.componentEdgeOptions.setVisible(true);
				}
		  	}
			//voltage diagram
			else if (s.equals(GuiLocalization.voltage_diagram))
			{
				if( this.delegate instanceof Node )
				{
					compCont.componentNodeVoltage.setLocationRelativeTo(this.compCont.appGraphRed);
					compCont.componentNodeVoltage.representData(this);
	            	compCont.componentNodeVoltage.setVisible(true);
				}
			}
			//connect
			else if (s.equals(GuiLocalization.connect))
			{
				if (this.getDelegate() instanceof Node)
				{
					compCont.frstNode = (Node)ComponentPanel.this.delegate;
				}
			}
			//deletes current component
			else if (s.equals(GuiLocalization.delete))
			{	
				compCont.removeExistingComponent(this);
			}
			//another actions
			else
			{
				JOptionPane.showMessageDialog( compCont.appGraphRed,
					    					  "under development",
					    					  "Error",
					    					  JOptionPane.ERROR_MESSAGE);
				//System.out.println(compCont.getComponentCount());
				/*
				int circle = 0;
				for (ComponentPanel comps : compCont.getComps())
				{
					System.out.println(comps.getName());
					circle+=1;
				}
				System.out.println("number of components is: " + circle);
				*/
			}
        }

	@Override
	public void focusGained(FocusEvent fe) {
		// TODO Auto-generated method stub
		this.setSelected(true);
		//compCont.repaint();
	}

	@Override
	public void focusLost(FocusEvent fe) {
		// TODO Auto-generated method stub
		this.setSelected(false);
		//compCont.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		int key = ke.getKeyCode();
		//DELETE
		switch (key)
		{
		case KeyEvent.VK_DELETE :
			compCont.removeExistingComponent(this);
			break;
		case KeyEvent.VK_D :
			compCont.removeExistingComponent(this);
			break;
		case KeyEvent.VK_C :
			if (this.getDelegate() instanceof Node)
			{
				compCont.frstNode = (Node)ComponentPanel.this.delegate;
			}
			break;
		case KeyEvent.VK_O :
			if( this.delegate instanceof Node )
			{
				compCont.componentNodeOptions.setLocationRelativeTo(this.compCont.appGraphRed);
				compCont.componentNodeOptions.representData(this);
				compCont.componentNodeOptions.refresh();
            	compCont.componentNodeOptions.setVisible(true);
			}
			else if( this.delegate instanceof Edge )
			{
				compCont.componentEdgeOptions.setLocationRelativeTo(this.compCont.appGraphRed);
				compCont.componentEdgeOptions.representData(this);
				compCont.componentEdgeOptions.refresh();
            	compCont.componentEdgeOptions.setVisible(true);
			}
			break;
		case KeyEvent.VK_V :
			if( this.delegate instanceof Node )
			{
				compCont.componentNodeVoltage.setLocationRelativeTo(this.compCont.appGraphRed);
				compCont.componentNodeVoltage.representData(this);
            	compCont.componentNodeVoltage.setVisible(true);
			}
			break;
		}
		
		// shift+D
		//if ( (ke.getKeyCode() == KeyEvent.VK_DELETE) && ((ke.getModifiers() & KeyEvent.SHIFT_MASK) != 0) )
		/*
		if ( (ke.getKeyCode() == KeyEvent.VK_D) && (ke.getModifiers() == KeyEvent.SHIFT_MASK) )
		{
			compCont.removeExistingComponent(this);
		}
		*/
	}
	
	/**
	 * return delegate of current ComponentPanel
	 * @return JComponent: delegate
	 */
	public JComponent getDelegate()
	{
		return this.delegate;
	}

}

