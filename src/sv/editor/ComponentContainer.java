package sv.editor;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEditSupport;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sv.editor.interfaces.*;
import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.GridPanel;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.additionalElements.NodeTypes;
import sv.gui.additionalElements.NodesOfEdge;
import sv.gui.graphElements.*;
import sv.gui.localization.GuiLocalization;
import sv.gui.supportElements.EdgeOptionsMenu;
import sv.gui.supportElements.NodeVoltageMenu;
import sv.gui.supportElements.UndoableDrawEdit;
import sv.gui.supportElements.SquareLimiter;
import sv.gui.supportElements.Graph;
import sv.gui.supportElements.NodeOptionsMenu;
import sv.gui.supportElements.VisualizationMenu;
import sv.newtonclient.NewtonClient;
import sv.gui.supportElements.ServerMessage;
import sv.gui.visualization.AngleDiagram;
import sv.gui.visualization.GradientPanel;
import sv.gui.visualization.VoltageDiagram;
import sv.gui.visualization.VoltageDiagramPercent;

/**
 * Component Container
 *
 * @author SAV2
 * @version 1.0.0
 * @since 25.07.2012
 */
public class ComponentContainer extends JLayeredPane
{
	/**
	 * Gradient panel
	 */
	GradientPanel gradientPanel;
	
	/**
	 * Component container focus traversal policy
	 */
	private static CompContTraversalPolicy ccPolicy;
	
	/**
	 * Zoom rate
	 */
	public static double zoom = 1;
	
	/**
	 * greed state
	 */
	boolean gridEnabled = false;
	
	/**
	 * Grid Panel
	 */
	GridPanel gridPanel = new GridPanel(this);
	
	/**
	 * right distance from component to expand current size of Component Container
	 */
	private static final int RIGHT_DISTANCE_FROM_COMP = 180;
	
	/**
	 * bottom distance from component to expand current size of Component Container
	 */
	private static final int BOTTOM_DISTANCE_FROM_COMP = 50;
	
	/**
	 * Gradient layer
	 */
	private static final int GRADIENT_LAYER = new Integer(2); 
	
	/**
	 * Grid's layer
	 */
	private static final Integer GRID_LAYER = new Integer(1);
	
	/**
	 * Edge's layer
	 */
	private static final Integer EDGE_LAYER = new Integer(11); 
	
	/**
	 * Node's layer
	 */
	private static final Integer NODE_LAYER = new Integer(12);
	
	/**
	 * drawing state
	 */
	boolean drawingState = true;
	
	/**
	 * minimum size of ComponentPanel side
	 */
	static int COMP_MIN_SIZE = getScaledValue(40);
	
	/**
	 * possibility of request to the server
	 */
	private boolean requestPossible = true;
	
	private String edgesWithZeroR = "";
	
	/**
	 * reference to parent AppGraphRedactor
	 */
	AppGraphRedactor appGraphRed;
	
	/**
	 * Option menu for setting visualization option
	 */
	VisualizationMenu visualizationMenu;
	
	/**
	 * Option menu for setting Node Voltage diagram options
	 */
	NodeVoltageMenu componentNodeVoltage;
	
	/**
	 * Server message window (with text area)
	 */
	private  ServerMessage srvMsg;
	
	/**
	 * Newton Client
	 */
	private NewtonClient newtonClnt;
	
	/**
	 * Option menu for Node component
	 */
	NodeOptionsMenu componentNodeOptions;
	
	/**
	 * Option menu for Edge Component
	 */
	EdgeOptionsMenu componentEdgeOptions;
	
	//temp data for adding Edge component to the container
	Node frstNode = null;
	Node scndNode = null;
	
	/**
	 * Graph model in memory
	 */
	private Graph graphModel = new Graph();
	
	/**
	 * edge counter for numbering of edge's id
	 */
	private int edgeCounter = 0;
	
	/**
	 * Square Limiter
	 */
	//SquareLimiter squareLimiter = new SquareLimiter();
	
	/**
	 * UndoableEditSupport (Undo\Redo)
	 */
	private UndoableEditSupport undoableEditSupport = new UndoableEditSupport(this);

    /**
     * Component renderer factory instance to create renderer for the new component
     */
    private ComponentRendererFactory rendererFactory;

    /**
     * Component selection listener
     */
    private ComponentSelectionListener selectionListener;

    /**
     * Components list
     */
    private List<ComponentPanel> components;

    /**
     * Pop-up menu of the ComponentContainer 
     */
    private JPopupMenu compContainerMenu;
    
    //items for compContainerMenu
    private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem clearAllItem;
	private JMenuItem exportImg;
	private JMenuItem visualOptions;
    
    /**
     * Creates component container
     */
    public ComponentContainer(AppGraphRedactor apGrRed) {
        super();
        setLayout(null);
        setOpaque(true);

        //new=01.05.2013 start:
        this.add(gridPanel, GRID_LAYER, 0);
        gridPanel.setLocation(0, 0);
        
        this.addContainerListener(new ContainerListener() {
			
			@Override
			public void componentRemoved(ContainerEvent ce)
			{
				if(ce.getChild() instanceof ComponentPanel)
				{
					appGraphRed.nodesCountLabel.setText(GuiLocalization.nodes + getNodesCount());
					appGraphRed.edgesCountLabel.setText(GuiLocalization.edges + getEdgesCount());
				}
			}
			
			@Override
			public void componentAdded(ContainerEvent ce)
			{
				if(ce.getChild() instanceof ComponentPanel)
				{
					appGraphRed.nodesCountLabel.setText(GuiLocalization.nodes + getNodesCount());
					appGraphRed.edgesCountLabel.setText(GuiLocalization.edges + getEdgesCount());
				}
			}
		});
        
        this.addComponentListener(new ComponentAdapter() 
        {
            @Override
            public void componentResized(ComponentEvent e) 
            {
                gridPanel.setSize(ComponentContainer.this.getSize());
                gradientPanel.setSize(ComponentContainer.this.getSize());
            }
        });
        
        gridPanel.setVisible(gridEnabled);
        //new=01.05.2013 end;
        
        this.appGraphRed = apGrRed;
        
        ComponentCreator cc = new ComponentCreator();
        //ComponentRepainter cr = new ComponentRepainter();
        initCompContainerMenu();
        addMouseListener(cc);
        addMouseMotionListener(cc);
        addMouseWheelListener(cc);
        //addContainerListener(cr);
        components = new LinkedList<ComponentPanel>();
        selectionListener = new SelectionListener();
        setBackground(Color.white);
        this.add(compContainerMenu);
        
        //creat VisualizationMenu JDialog
        visualizationMenu = new VisualizationMenu(SwingUtilities.windowForComponent(this), Dialog.DEFAULT_MODALITY_TYPE, appGraphRed);
        visualizationMenu.setIconImage(new ImageIcon( this.getClass().getResource("img/visualization.png") ).getImage());
        visualizationMenu.setCurrentCc(this);
        visualizationMenu.setVisible(false);
        
        //create NodeVoltage JDialog
        componentNodeVoltage = new NodeVoltageMenu(SwingUtilities.windowForComponent(this), Dialog.DEFAULT_MODALITY_TYPE);
        componentNodeVoltage.setIconImage(new ImageIcon( this.getClass().getResource("img/vol_dgrm_settings.png") ).getImage());
        componentNodeVoltage.setVisible(false);
        
        //create NodeOptions JDialog
        componentNodeOptions = new NodeOptionsMenu(SwingUtilities.windowForComponent(this), Dialog.DEFAULT_MODALITY_TYPE);
        componentNodeOptions.setIconImage(new ImageIcon( this.getClass().getResource("img/options.png") ).getImage());
        componentNodeOptions.setVisible(false);
        componentNodeOptions.setCurrentCompCont(this);
        
        //create EdgeOption JDialog
        componentEdgeOptions = new EdgeOptionsMenu(SwingUtilities.windowForComponent(this), Dialog.DEFAULT_MODALITY_TYPE);
        componentEdgeOptions.setIconImage(new ImageIcon( this.getClass().getResource("img/options.png") ).getImage());
        componentEdgeOptions.setVisible(false);
        
        //server message window
        srvMsg = new sv.gui.supportElements.ServerMessage(SwingUtilities.windowForComponent(ComponentContainer.this), Dialog.DEFAULT_MODALITY_TYPE);
        srvMsg.setIconImage(new ImageIcon( this.getClass().getResource("img/request.png") ).getImage());
        srvMsg.setCurrentCc(this);
        srvMsg.setVisible(false);
        
        //newtonClient
        newtonClnt = new NewtonClient(ComponentContainer.this);
        
        //CompContTraversalPolicy
        ccPolicy = new CompContTraversalPolicy(components);
        
        this.setFocusCycleRoot(true);
        this.setFocusTraversalPolicy(ccPolicy);
        
        gradientPanel = new GradientPanel(this.components);
        add(gradientPanel, GRADIENT_LAYER, 0);
        gradientPanel.setLocation(0, 0);
        gradientPanel.setVisible(false);
    }
    
    /**
     * Component Container Focus traversal policy
     * @author SAV2
     * @since 07.05.2013
     */
    private static class CompContTraversalPolicy extends FocusTraversalPolicy
    {
    	List<ComponentPanel> order;

    	public CompContTraversalPolicy(List<ComponentPanel> orderParam)
    	{
    		this.order = orderParam;
    	}
    	
    	public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
    	{
    		if(order.size() == 0) return null;
    		
    		int idx = (order.indexOf(aComponent) + 1) % order.size();
    		return order.get(idx);
    	}

    	public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
    	{
    		if(order.size() == 0) return null;
    		
    		int idx = order.indexOf(aComponent) - 1;
    		if (idx < 0) 
    		{
    			idx = order.size() - 1;
    		}
    		return order.get(idx);
    	}

    	public Component getDefaultComponent(Container focusCycleRoot)
    	{
    		if(order.size() == 0) return null;
    		
    		return order.get(0);
    	}

    	public Component getLastComponent(Container focusCycleRoot)
    	{
    		if(order.size() == 0) return null;
    		
    		return order.get(order.size()-1);
    	}

    	public Component getFirstComponent(Container focusCycleRoot)
    	{
    		if(order.size() == 0) return null;
    		
    		return order.get(0);
    	}
    }
    
    /**
     * get Nodes count
     * @return current amount of nodes : <code>int</code> 
     */
    public int getNodesCount()
    {
    	return this.getComponentCountInLayer(NODE_LAYER);
    }
    
    /**
     * get Edges count
     * @return current amount of edges : <code>int</code> 
     */
    public int getEdgesCount()
    {
    	return this.getComponentCountInLayer(EDGE_LAYER);
    }
    
    /**
     * get current Applet Graph Editor instance
     * @return AppGraphRedactor
     */
    public AppGraphRedactor getAppGraphRed()
    {
    	return this.appGraphRed;
    }
    
    /**
     * change strings
     */
    public void changeStrings()
    {
    	undoItem.setText(GuiLocalization.undo);
		redoItem.setText(GuiLocalization.redo);
		clearAllItem.setText(GuiLocalization.clear_all);
		exportImg.setText(GuiLocalization.export_as);
		visualOptions.setText(GuiLocalization.visualization_settings);
		
		for(ComponentPanel comp : components)
		{
			comp.changeStrings();
		}
		
		visualizationMenu.changeStrings();
		componentNodeVoltage.changeStrings();
		componentNodeOptions.changeStrings();
		componentEdgeOptions.changeStrings();
		srvMsg.changeStrings();
    }
    
    /**
     * Set Drawing state
     * @param state : boolean
     */
    public void setDrawingState(boolean state)
    {
    	this.drawingState = state;
    }
    
    /**
     * adding of UndoableEditListener
     * @param undoableEditListener : UndoableEditListener
     */
    public void addUndoableEditListener(UndoableEditListener undoableEditListener) 
    {
    	undoableEditSupport.addUndoableEditListener(undoableEditListener);
    }
    
    /**
     * removing of UndoableEditListener
     * @param undoableEditListener : UndoableEditListener
     */
    public void removeUndoableEditListener(UndoableEditListener undoableEditListener) 
    {
    	undoableEditSupport.removeUndoableEditListener(undoableEditListener);
    }
    
    /**
     * adding of Edge Component
     */
    public void addEdgeComponent()
    {
    	if( (frstNode == null) || (scndNode == null) ) return;
    	
    	//do nothing if FirstNode == SecondNode i.e. both references point to the same object.
		if( frstNode.equals(scndNode) )
		{
			//System.out.println("the same object");
			return;
		}
    	
		//check out already existed Edge with the same data on linked Nodes
		for (ComponentPanel panel : components)
		{
			if(panel.getDelegate() instanceof Edge)
			{
				/*
				 * If there is such Edge from components List,
				 * which contains such data:
				 * (CurrentEdge.firstNode == frstNode AND CurrentEdge.lastNode == scndNode)
				 * OR
				 * (CurrentEdge.firstNode == scndNode AND CurrentEdge.lastNode == frstNode)
				 */
				if(	
					( ((Edge)panel.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).equals(frstNode) &&
					  ((Edge)panel.getDelegate()).getNode(NodesOfEdge.LAST_NODE).equals(scndNode) )
																				  ||
					( ((Edge)panel.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).equals(scndNode) &&
					  ((Edge)panel.getDelegate()).getNode(NodesOfEdge.LAST_NODE).equals(frstNode) )
				  )
				{
					return;
				}
			}
		}
		
    	undoableEditSupport.postEdit(new UndoableDrawEdit(ComponentContainer.this));//undo-redo
    	appGraphRed.undoItem.setEnabled(appGraphRed.undoManager.canUndo());
    	
    	this.setCurrEdgeCounter();
    	ComponentPanel currentEdgeComponent = new ComponentPanel(new Edge(frstNode, scndNode, ++edgeCounter), this);
    	//<new date="14.02.2013" start:>
    		//add(currentEdgeComponent);
    		add(currentEdgeComponent, EDGE_LAYER, 0);
    	//<new date="14.02.2013" end;>
    	currentEdgeComponent.setBounds(currentEdgeComponent.getDelegate().getBounds());
    	components.add(currentEdgeComponent);
    	currentEdgeComponent.setActualBounds(currentEdgeComponent.getDelegate().getBounds());
    	currentEdgeComponent.addComponentSelectionListener(selectionListener);
    	
    	//add edge element into the Graph model
    	this.graphModel.addEdge(frstNode.getID(), scndNode.getID());
    	recheckSize();
    }
    
    //<new date="16.02.2013" start:>
    /**
     * adding of Node component from XML-document
     * 
     * @param nodeID : int
     * @param nodeBounds : Rectangle
     * @param nParamType : String
     * @param nParamU : String
     * @param nParamAngle : String
     * @param nParamPG : String
     * @param nParamQG : String
     * @param nParamPL : String
     * @param nParamQL : String
     * @param nResU : String
     * @param nResAngle : String
     * @param nResPG : String
     * @param nResQG : String
     */
    public void addNodeFromXmlDoc(int nodeID,
    							  Rectangle nodeBounds,
    							  String nParamType,
    							  String nParamU,
    							  String nParamAngle,
    							  String nParamPG,
    							  String nParamQG,
    							  String nParamPL,
    							  String nParamQL,
    							  String nResU,
    							  String nResAngle,
    							  String nResPG,
    							  String nResQG)
    {
    	ComponentPanel currentNodeOfXmlDoc = new ComponentPanel(new Node(nodeID), this);
    	add(currentNodeOfXmlDoc, NODE_LAYER, 0);
    	currentNodeOfXmlDoc.setBounds(nodeBounds);
    	currentNodeOfXmlDoc.getDelegate().setSize((int)nodeBounds.getWidth(), (int)nodeBounds.getHeight());
    	components.add(currentNodeOfXmlDoc);
    	currentNodeOfXmlDoc.setActualBounds(currentNodeOfXmlDoc.getBounds());
    	currentNodeOfXmlDoc.addComponentSelectionListener(selectionListener);
    	
    	//add node element into the Graph model
        graphModel.addNode( ((Node)currentNodeOfXmlDoc.getDelegate()).getID() );
        
    	//setting parameters
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_Type, nParamType);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_U, nParamU);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_Angle, nParamAngle);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_PG, nParamPG);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_QG, nParamQG);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_PL, nParamPL);
    	((Node) currentNodeOfXmlDoc.getDelegate()).setParameter(NodeParameters.Data_QL, nParamQL);
    	//setting results
    	((Node) currentNodeOfXmlDoc.getDelegate()).setResult(NodeResults.Res_U, Double.parseDouble(nResU));
    	((Node) currentNodeOfXmlDoc.getDelegate()).setResult(NodeResults.Res_Angle, Double.parseDouble(nResAngle));
    	((Node) currentNodeOfXmlDoc.getDelegate()).setResult(NodeResults.Res_PG, Double.parseDouble(nResPG));
    	((Node) currentNodeOfXmlDoc.getDelegate()).setResult(NodeResults.Res_QG, Double.parseDouble(nResQG));
    	
    	recheckSize();
    }
    
    /**
     * adding of Edge component from XML-document
     * 
     * @param edgeID : int
     * @param firstNodeID : int
     * @param lastNodeID : int
     * @param eParamR : String
     * @param eParamX : String
     * @param eParamTap : String
     * @param eResPF : String
     * @param eResQF : String
     * @param eResPT : String
     * @param eResQT : String
     */
    public void addEdgeFromXmlDoc(int edgeID,
    							  int firstNodeID,
    							  int lastNodeID,
    							  String eParamR,
    							  String eParamX,
    							  String eParamTap,
    							  String eResPF,
    							  String eResQF,
    							  String eResPT,
    							  String eResQT)
    {
    	
    	ComponentPanel currentEdgeOfXmlDoc = new ComponentPanel(new Edge( this.getNodeByID(firstNodeID), this.getNodeByID(lastNodeID), edgeID ), this);
    	add(currentEdgeOfXmlDoc, EDGE_LAYER, 0);
    	currentEdgeOfXmlDoc.setBounds(currentEdgeOfXmlDoc.getDelegate().getBounds());
    	components.add(currentEdgeOfXmlDoc);
    	currentEdgeOfXmlDoc.setActualBounds(currentEdgeOfXmlDoc.getDelegate().getBounds());
    	currentEdgeOfXmlDoc.addComponentSelectionListener(selectionListener);
    	
    	//add edge element into the Graph model
    	this.graphModel.addEdge(firstNodeID, lastNodeID);
    	
    	//setting parameters
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setParameter(EdgeParameters.Data_R, eParamR);
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setParameter(EdgeParameters.Data_X, eParamX);
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setParameter(EdgeParameters.Data_Tap, eParamTap);
    	//setting results
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setResult(EdgeResults.PF, Double.parseDouble(eResPF));
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setResult(EdgeResults.QF, Double.parseDouble(eResQF));
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setResult(EdgeResults.PT, Double.parseDouble(eResPT));
    	((Edge) currentEdgeOfXmlDoc.getDelegate()).setResult(EdgeResults.QT, Double.parseDouble(eResQT));
    	
    	recheckSize();
    }
    //<new date="16.02.2013" end;>
    
    /**
     * re-check size of component container and set its new Preferred size
     */
    public void recheckSize()
    {
    	ComponentContainer cc = ComponentContainer.this;
    	int maxX = 0;
    	int maxY = 0;
    	
    	for(ComponentPanel comp : cc.components)
    	{
    		maxX = Math.max( maxX, comp.getX()+comp.getSize().width);
    		maxY = Math.max( maxY, comp.getY()+comp.getSize().height);
    	}
    	
    	cc.setPreferredSize( new Dimension(maxX+getScaledValue(RIGHT_DISTANCE_FROM_COMP), maxY+getScaledValue(BOTTOM_DISTANCE_FROM_COMP)) );
    	
    	cc.revalidate();
    }
    
    /**
     * returns components list 
     * 
     * @return Components list
     */
    public List<ComponentPanel> getComps()
    {
    	return components;
    }
    
    /**
     * returns graph model of current component container.
     * @return graph model.
     */
    public Graph getGrpah()
    {
    	return this.graphModel;
    }

    /**
     * Construct Component Container Pop-up Menu
     */
    private void initCompContainerMenu()
    {
    	compContainerMenu = new JPopupMenu("Options");
		  
		undoItem = new JMenuItem(GuiLocalization.undo, new ImageIcon( this.getClass().getResource("img/undo.png") ));
		undoItem.setMnemonic(KeyEvent.VK_U);
		
		redoItem = new JMenuItem(GuiLocalization.redo, new ImageIcon( this.getClass().getResource("img/redo.png") ));
		redoItem.setMnemonic(KeyEvent.VK_R);
		
		clearAllItem = new JMenuItem(GuiLocalization.clear_all, new ImageIcon( this.getClass().getResource("img/clear_all.png") ));
		clearAllItem.setMnemonic(KeyEvent.VK_C);
		
		exportImg = new JMenuItem(GuiLocalization.export_as,new ImageIcon( this.getClass().getResource("img/export.png") ));
		exportImg.setMnemonic(KeyEvent.VK_X);
		
		visualOptions = new JMenuItem(GuiLocalization.visualization_settings, new ImageIcon( this.getClass().getResource("img/visualization.png") )); 
		visualOptions.setMnemonic(KeyEvent.VK_V);
		
		compContainerMenu.add(undoItem); 
		compContainerMenu.add(redoItem); 
		compContainerMenu.add(clearAllItem);
		compContainerMenu.addSeparator();
		compContainerMenu.add(exportImg);
		compContainerMenu.addSeparator();  
		compContainerMenu.add(visualOptions);
		
		//undo
		undoItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				appGraphRed.undoManager.undo();	
			}
		});
		
		//redo
		redoItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				appGraphRed.undoManager.redo();	
			}
		});
		
		//clear All
		clearAllItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ComponentContainer.this.fullClear();	
			}
		});
		
		//Export as...
		exportImg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					appGraphRed.imgExporter.exportImage();
				}
				catch (IOException exc)
				{
					JOptionPane.showMessageDialog(appGraphRed,
												  exc.getMessage(),
												  GuiLocalization.export_image_failed,
												  JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
		
		//visualization settings
		visualOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				visualizationMenu.setLocationRelativeTo(appGraphRed);
				visualizationMenu.representData();
				visualizationMenu.setVisible(true);
			}
		});
		
    }
    
    /**
     * Sets renderer factory
     *
     * @param rendererFactory renderer factory instance
     */
    public void setRendererFactory(ComponentRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    //<new>
    /*
    private class ComponentRepainter extends ContainerAdapter
    {
    	@Override
		public void componentRemoved(ContainerEvent e) {
			repaint();
		}
		
		@Override
		public void componentAdded(ContainerEvent e) {
			repaint();
			System.out.println("done!");
		}
    }
    */
    //<new>
    
    /**
     * Check the click if its point is contained in the current line
     * @param x : current x-coordinate
     * @param y : current y-coordinate
     * @param startX : x-coordinate of start point of line
     * @param startY : y-coordinate of start point of line
     * @param endX : x-coordinate of end point of line
     * @param endY : y-coordinate of end point of line
     * @param lineThickness : thickness of line
     * @return <b>true</b> if the click is within lineThickness/2 of the center of the line
     */
    private boolean containsLine(int x, int y, int startX, int startY, int endX, int endY, int lineThickness)
    {
        // Check if line is a point
        if(startX == endX && startY == endY)
        {
            if(Math.abs(startY - y) <= lineThickness / 2 && Math.abs(startX - x) <= lineThickness / 2)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        int x1, x2, y1, y2;

        if(startX < endX)
        {
            x1 = startX;
            y1 = startY;
            x2 = endX;
            y2 = endY;
        }
        else
        {
            x1 = endX;
            y1 = endY;
            x2 = startX;
            y2 = startY;
        }


        /**** USING MATRIX TRANSFORMATIONS ****/

        double r_numerator = (x-x1)*(x2-x1) + (y-y1)*(y2-y1);
        double r_denominator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
        double r = r_numerator / r_denominator;

        // s is the position of the perpendicular projection of the point along
        // the line: s < 0 = point is left of the line; s > 0 = point is right of
        // the line; s = 0 = the point is along the line
        double s =  ((y1-y)*(x2-x1)-(x1-x)*(y2-y1) ) / r_denominator;

        double distance = Math.abs(s)*Math.sqrt(r_denominator);

        // Point is along the length of the line
        if ( (r >= 0) && (r <= 1) )
        {
                if(Math.abs(distance) <= lineThickness / 2)
                {
                    return true;
                }
                else
                {
                    return false;
                }
        }
        // else point is at one end of the line
        else
        {
            double dist1 = (x-x1)*(x-x1) + (y-y1)*(y-y1); // distance to start of line
            double dist2 = (x-x2)*(x-x2) + (y-y2)*(y-y2); // distance to end of line
            
            if (dist1 < dist2)
            {
                distance = Math.sqrt(dist1);
            }
            else
            {
                distance = Math.sqrt(dist2);
            }
            
            if(distance <= lineThickness / 2)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        /**** END USING MATRIX TRANSFORMATIONS****/

    }
    
    /**
     * Mouse actions handler
     */
    private class ComponentCreator extends MouseAdapter {
    	
    	/**
    	 * mouse is dragged within clicked its right button: <code>BUTTON3</code> 
    	 */
    	private boolean mouseIsDragged = false;
    	
        private ComponentPanel currentComponent = null;
        
        /**
         * X coordinate of point where mouse was pressed on current component container
         */
        private int startX;
        
        /**
         * Y coordinate of point where mouse was pressed on current component container
         */
        private int startY;
        
        /**
    	 * current pressed Mouse button
    	 */
        private int pressedButton = 0;
        
        
        @Override
        public void mousePressed(MouseEvent e)
        {
        	pressedButton = e.getButton();
        	
        	//Point p = e.getPoint();
			//at.transform(p, p);
        	
        	//wipe temp data for adding Edge component to the container
        	frstNode = null;
   		 	scndNode = null;
   		 	
   		 	startX = e.getX();
   		 	startY = e.getY();
   		 	
   		 	if(gridEnabled)
   		 	{
   		 		startX -= startX % getScaledValue(gridPanel.getCellSize());
   		 		startY -= startY % getScaledValue(gridPanel.getCellSize());
   		 	}
   		 	
            if (rendererFactory == null) return;
            
            //------------------------------------------------------------------------>
        	for(ComponentPanel comp : ComponentContainer.this.components)
        	{
        		if(comp.getDelegate() instanceof Edge)
        		{
        			if( containsLine(e.getX(),
        							 e.getY(),
        							 ((Edge)comp.getDelegate()).startEdgeParentX,
        							 ((Edge)comp.getDelegate()).startEdgeParentY,
        							 ((Edge)comp.getDelegate()).endEdgeParentX,
        							 ((Edge)comp.getDelegate()).endEdgeParentY,
        							 Edge.SELECTION_ZONE_THICKNESS)
        			  )
        			{
        				ComponentContainer.this.moveToFront(comp);
        				comp.setSelected(true);
        				currentComponent = null;
        				return;
        			}
        		}
        	}
        	//------------------------------------------------------------------------>
            
            if(pressedButton == MouseEvent.BUTTON1 && drawingState)//left mouse button
            {
            	ComponentContainer.this.setCurrNodeCounter();
            	currentComponent = new ComponentPanel(rendererFactory.createRenderer(), ComponentContainer.this);
            
            	//<new date="14.02.2013" start:>
            	//add(currentComponent);
            	ComponentContainer.this.add(currentComponent, NODE_LAYER, 0);
            	//<new date="14.02.2013" end;>

            	fetchCurrentComponent(e);
            	currentComponent.setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        	ComponentContainer.this.requestFocusInWindow();
        	repaint();
        	
        	if(mouseIsDragged)//if mouse with clicked right button was dragged
        	{
        		mouseIsDragged = false;
        		ComponentContainer.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        		return;
        	}
        	
        	//------------------------------------------------------------------------>
        	for(ComponentPanel comp : ComponentContainer.this.components)
        	{
        		if(comp.getDelegate() instanceof Edge)
        		{
        			if( containsLine(e.getX(),
        							 e.getY(),
        							 ((Edge)comp.getDelegate()).startEdgeParentX,
        							 ((Edge)comp.getDelegate()).startEdgeParentY,
        							 ((Edge)comp.getDelegate()).endEdgeParentX,
        							 ((Edge)comp.getDelegate()).endEdgeParentY,
        							 Edge.SELECTION_ZONE_THICKNESS)
        			  )
        			{
        				//set KeyBoard focus for selected component
        	        	comp.requestFocusInWindow();
        			}
        		}
        	}
        	//------------------------------------------------------------------------>
        	
        	if(e.getButton() == MouseEvent.BUTTON1 && drawingState)//left mouse button
        	{
        		if(currentComponent == null) return;
        		
        		if ( Math.abs(e.getX()-startX) < COMP_MIN_SIZE || Math.abs(e.getY()-startY) < COMP_MIN_SIZE )
            	{
            		rendererFactory.reduceCounter();
                	remove(currentComponent);
                	repaint();
                	return;
            	}
        		
            	undoableEditSupport.postEdit(new UndoableDrawEdit(ComponentContainer.this));//undo-redo
            	appGraphRed.undoItem.setEnabled(appGraphRed.undoManager.canUndo());
            	
            	fetchCurrentComponent(e);
            	components.add(currentComponent);
            	currentComponent.setBorder(null);
            	currentComponent.setActualBounds(currentComponent.getBounds());
            	currentComponent.addComponentSelectionListener(selectionListener);
            	currentComponent.setSelected(true);
            	currentComponent.requestFocusInWindow();
            
            	//add node element into the Graph model
            	if (currentComponent.getDelegate() instanceof Node)
            	{
            		graphModel.addNode( ((Node)currentComponent.getDelegate()).getID() );
            	}
            
            	ComponentContainer.this.recheckSize();
        	}
        	else if(e.getButton() == MouseEvent.BUTTON3)//right mouse button
        	{
        		//------------------------------------------------------------------------>
            	for(ComponentPanel comp : ComponentContainer.this.components)
            	{
            		if(comp.getDelegate() instanceof Edge)
            		{
            			if( containsLine(e.getX(),
            							 e.getY(),
            							 ((Edge)comp.getDelegate()).startEdgeParentX,
            							 ((Edge)comp.getDelegate()).startEdgeParentY,
            							 ((Edge)comp.getDelegate()).endEdgeParentX,
            							 ((Edge)comp.getDelegate()).endEdgeParentY,
            							 Edge.SELECTION_ZONE_THICKNESS)
            			  )
            			{
            				comp.compPanelMenu.show(e.getComponent(), startX, startY);
            				return;
            			}
            		}
            	}
            	//------------------------------------------------------------------------>
        		compContainerMenu.show(e.getComponent(), startX, startY);
        		undoItem.setEnabled(appGraphRed.undoManager.canUndo());
        		redoItem.setEnabled(appGraphRed.undoManager.canRedo());
        	}
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
        	if(pressedButton == MouseEvent.BUTTON1 && drawingState)
        	{
        		if(currentComponent == null) return;
        		fetchCurrentComponent(e);
            	repaint();
        	}
        	
        	if(pressedButton == MouseEvent.BUTTON3)
        	{
        		mouseIsDragged = true;
        		ComponentContainer.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        		
        		//View Position
        		Point vp = appGraphRed.ccScrollPane.getViewport().getViewPosition();
        		//Viewport Size
        		Dimension vps = appGraphRed.ccScrollPane.getViewport().getSize();
        		
        		vp.translate(startX-e.getX(), startY-e.getY());
        		
        		ComponentContainer.this.scrollRectToVisible(new Rectangle(vp, vps));
        	}
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
        	
        	//------------------------------------------------------------------------>
            for(ComponentPanel comp : ComponentContainer.this.components)
            {
            	if(comp.getDelegate() instanceof Edge)
                {
                	if( containsLine(e.getX(),
                					 e.getY(),
                					 ((Edge)comp.getDelegate()).startEdgeParentX,
                					 ((Edge)comp.getDelegate()).startEdgeParentY,
                					 ((Edge)comp.getDelegate()).endEdgeParentX,
                					 ((Edge)comp.getDelegate()).endEdgeParentY,
                					 Edge.SELECTION_ZONE_THICKNESS)
                	   )
                	   {
                			if( comp.getDelegate() instanceof Edge )
                	        {
                				//DblClick START
                		    	if((e.getButton() == MouseEvent.BUTTON1) && (e.getSource() instanceof ComponentContainer))
                		    	{
                		    		if (e.getClickCount() == 2 && !e.isConsumed()) 
                		    	  	{
                		    			e.consume();
                		    	            
                		    	        componentEdgeOptions.setLocationRelativeTo(appGraphRed);
                		    	        componentEdgeOptions.representData(comp);
                		    	        componentEdgeOptions.refresh();
                		    	        componentEdgeOptions.setVisible(true);
                		    	  	}
                		    	}
                		    	//DblClick END
                	         }
                			 return;
                	   }
                }
            }
            //------------------------------------------------------------------------>
    	  	
        	//System.out.println(drawingState);
        	//System.out.println( components.size() );
        	//System.out.println(ComponentContainer.this.graphModel.getConnections());
        	//System.out.println( ComponentContainer.this.graphModel.getConnections() );
        	//System.out.println( ComponentContainer.this.components );
        	//System.out.println( ComponentContainer.this.getComponentCount() );
        	//System.out.println(Double.toString( Math.round(21.31218921*1000.00)/1000.00 ));
        	//System.out.println(Double.toString(41/100.00));
            //at.scale(0.5, 0.5);
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
        	if(e.getWheelRotation() == 1)
        	{
        		//System.out.println("down");
        		appGraphRed.zoomSlider.setValue( appGraphRed.zoomSlider.getValue()-10 );
        	}
        	else if(e.getWheelRotation() == -1)
        	{
        		//System.out.println("up");
        		appGraphRed.zoomSlider.setValue( appGraphRed.zoomSlider.getValue()+10 );
        	}
        }
        
        //fetch == set bounds of current component
        private void fetchCurrentComponent(MouseEvent e) {
        	
            int endX = e.getX();
            int endY = e.getY();
            
            if(endX <= 0)
            {
            	endX = 1;
            }
            
            if(endY <= 0)
            {
            	endY = 1;
            }
            
            if(gridEnabled)
            {
            	endX -= endX % getScaledValue(gridPanel.getCellSize());
            	endY -= endY % getScaledValue(gridPanel.getCellSize());
            }
            
            //for Node (square)
            if(currentComponent.getDelegate() instanceof Node)
            {
            	/*
            	currentComponent.setBounds(Math.min(startX, squareLimiter.getResult(CoordinateType.X, startX, endX, startY, endY)),
            							   Math.min(startY, squareLimiter.getResult(CoordinateType.Y, startX, endX, startY, endY)),
            							   Math.min(Math.abs(startY - endY), Math.abs(startX - endX)),
                                           Math.min(Math.abs(startY - endY), Math.abs(startX - endX)));
            	*/
            	currentComponent.setBounds(Math.min(startX, endX),
            							   Math.min(startY, endY),
            							   Math.abs(startX - endX),
            							   Math.abs(startY - endY));
            }
            //for others (non-square)
            else
            {
            	currentComponent.setBounds(Math.min(startX, endX),
            							   Math.min(startY, endY),
            							   Math.abs(startX - endX),
            							   Math.abs(startY - endY));
            }
        }

    }
    
    /**
     * Component selection listener implementation
     */
    private class SelectionListener implements ComponentSelectionListener
    {
        public void componentSelected(ComponentPanel c) 
        {
            for (ComponentPanel panel : components) 
            {
                if (panel != c)
                {
                    panel.setSelected(false);
                }
            }
            repaint();
        }
    }
    
    /**
     * removing of selected\focused component from container
     * 
     * @param deletedComp instance of ComponentPanel
     */
    public void removeExistingComponent(ComponentPanel deletedComp)
    {
    	if(!drawingState) return;
    	
    	undoableEditSupport.postEdit(new UndoableDrawEdit(ComponentContainer.this));//undo-redo
    	appGraphRed.redoItem.setEnabled(appGraphRed.undoManager.canRedo());
    	
    	//Node removing action
    	if (deletedComp.getDelegate() instanceof Node)
    	{
    		/*
    		for (ComponentPanel panel : components)
    		{
    			if(panel.getDelegate() instanceof Edge)
    			{
    				if( ((Edge)panel.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID() == ((Node)deletedComp.getDelegate()).getID()
    					||
    					((Edge)panel.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() == ((Node)deletedComp.getDelegate()).getID()	)
    				{
    					this.components.remove(panel);
    					this.remove(panel);
    				}
    			}
    		}
    		*/
    		/*
    		 * the same method which was commented above, but using
    		 * special "Iterator" Interface to avoid java.util.ConcurrentModificationException.
    		 * Because "Iterator.remove()" is the only safe way to modify a collection during iteration.
    		 */
    		for( Iterator<ComponentPanel> iter = components.iterator(); iter.hasNext(); )
    		{
    			ComponentPanel currPanel = iter.next();
    			if( currPanel.getDelegate() instanceof Edge)
    			{
    				if( 
    					((Edge)currPanel.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).equals( ((Node)deletedComp.getDelegate()) )
        				||
        				((Edge)currPanel.getDelegate()).getNode(NodesOfEdge.LAST_NODE).equals( ((Node)deletedComp.getDelegate()) )
    				  )
        			  {
        				iter.remove();
        				this.remove(currPanel);
        			  }
    			}
    		}

    		this.graphModel.removeNode( ((Node)deletedComp.getDelegate()).getID() );
    	}
    	//Edge removing action
    	else if (deletedComp.getDelegate() instanceof Edge)
    	{
    		this.graphModel.removeEdge( ((Edge)deletedComp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID(),
    									((Edge)deletedComp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() );
    	}
    	
    	this.components.remove(deletedComp);
    	this.remove(deletedComp);
    	recheckSize();
    	repaint();
    }
   
    /**
     * reset state of whole of ComponentPanels in the ComponentContainer
     * <br /><br />
     * special for Undo\Redo (UndoableDrawEdit) usage
     * 
     * @param resetedPanels : List&#60;ComponentPanel&#62;
     */
    public void resetCompPanels(List<ComponentPanel> resetedPanels)
    {  	
    	this.components.clear();
    	this.removeAll();
    	this.graphModel.getConnections().clear();
    	
    	this.add(gridPanel, GRID_LAYER, 0);
        gridPanel.setLocation(0, 0);
        
        this.add(gradientPanel, GRADIENT_LAYER, 0);
        gradientPanel.setLocation(0, 0);
    	
    	for (ComponentPanel cp : resetedPanels)
    	{
    		this.add(cp);
    		
    		cp.setBounds(getScaledValue(cp.getActualBounds().x),
					     getScaledValue(cp.getActualBounds().y),
					     getScaledValue(cp.getActualBounds().width),
					     getScaledValue(cp.getActualBounds().height) );
					     
    		components.add(cp);
    		cp.setSelected(false);
    		
    		//Node
    		if( cp.getDelegate() instanceof Node )
    		{
    			this.graphModel.addNode( ((Node)cp.getDelegate()).getID() );
    		}
    		//Edge
    		else if(cp.getDelegate() instanceof Edge)
    		{
    			this.graphModel.addEdge( ((Edge)cp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID(), 
    									 ((Edge)cp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() );
    		}
    		
    		/*
    		System.out.println("values of component: "+ cp.getX() + " "
													  + cp.getY() + " "
													  + cp.getWidth() + " "
													  + cp.getHeight() + " ");
			*/
    	}
    	
    	appGraphRed.undoItem.setEnabled(appGraphRed.undoManager.canUndo());
    	appGraphRed.redoItem.setEnabled(appGraphRed.undoManager.canRedo());
    	
    	recheckSize();
    	repaint();
    	this.requestFocusInWindow();
    }
    
    /**
     * Clear component container ("drawing area")
     */
    public void fullClear()
    {
    	undoableEditSupport.postEdit(new UndoableDrawEdit(ComponentContainer.this));//undo-redo
    	this.graphModel.getConnections().clear();
    	this.components.clear();
    	this.removeAll();
    	
    	this.add(gridPanel, GRID_LAYER, 0);
        gridPanel.setLocation(0, 0);
        
        this.add(gradientPanel, GRADIENT_LAYER, 0);
        gradientPanel.setLocation(0, 0);
        
        this.recheckSize();
    	this.repaint();
    	this.requestFocusInWindow();
    }
    
    /**
     * Get Node by its ID.
     * @param nodeID : int
     * @return Node that has the same ID, which is equal to entered parameter
     */
    private Node getNodeByID(int nodeID)
    {
    	Node currentNode = null;
    	
    	for (ComponentPanel comp : components)
    	{
    		if( !(comp.getDelegate() instanceof Node) ) continue;
    		
    		if( ((Node) comp.getDelegate()).getID()==nodeID )
    		{
    			currentNode = (Node) comp.getDelegate();
    		}	
    	}
    	
    	return currentNode;
    }
    
    /**
     * Get Node by its idForRequest.
     * @param nodeIDforReq : int 
     * @return Node that has the same idForRequest, which is equal to entered parameter
     */
    public Node getNodeByIDforReq(int nodeIDforReq)
    {
    	Node currentNode = null;
    	
    	for (ComponentPanel comp : components)
    	{
    		if( !(comp.getDelegate() instanceof Node) ) continue;
    		
    		if( ((Node) comp.getDelegate()).getIDforRequest()==nodeIDforReq )
    		{
    			currentNode = (Node) comp.getDelegate();
    		}	
    	}
    	
    	return currentNode;
    }
    
    /**
     * get Edge by its connected nodes
     * @param firstNodeID : int
     * @param lastNodeID : int
     * @return Edge object
     */
    public Edge getEdgeByItsConnectedNodes(int firstNodeID, int lastNodeID)
    {
    	Edge edgeByItsNodes = null;
    	
    	for(ComponentPanel comp : this.components)
    	{
    		if(comp.getDelegate() instanceof Edge)
    		{
    			if( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID()==firstNodeID 
    				&&
    				((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID()==lastNodeID )
    			{
    				edgeByItsNodes = (Edge)comp.getDelegate();
    			}
    		}
    	}
    	
    	return edgeByItsNodes;
    }
    
    /**
     * Check Scheme parameters before sending data to the server.
     */
    private void checkSchemeParams()
    {
    	//counter for edges with zero values of their resistance
    	int counter = 0;
    	
    	/*
    	 * check quotient of firstNode and lastNode Voltage lvls
    	 * according to transformation rate of their Edge
    	 */
    	for(ComponentPanel comp : this.components)
    	{
    		if(comp.getDelegate() instanceof Edge )
    		{
    			if(//if firstNode.Data_U > lastNode.Data_U
    			   Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getParameter(NodeParameters.Data_U))
    			   >
    			   Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getParameter(NodeParameters.Data_U) )
    			  )
    			{
    				((Edge)comp.getDelegate())
    				.setParameter(EdgeParameters.Data_Tap,
    							  Double.toString(
    							  Math.round(
    			    				Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getParameter(NodeParameters.Data_U))
    			    				/
    			    				Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getParameter(NodeParameters.Data_U) )
    			    				* 1000.00
    			    				)/1000.00
    			    				)
    							  );
    				//currentEdge.Data_Tap = lastNode.Data_U / firstNode.Data_U	
    			}
    			else//if firstNode.Data_U <= lastNode.Data_U
    			{
    				((Edge)comp.getDelegate())
    				.setParameter(EdgeParameters.Data_Tap,
    							  Double.toString(
    							  Math.round(
    			    				Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getParameter(NodeParameters.Data_U))
    			    				/
    			    				Double.parseDouble( ((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getParameter(NodeParameters.Data_U) )
    			    				* 1000.00
    			    				)/1000.00
    			    				)
    							  );
    				//currentEdge.Data_Tap = firstNode.Data_U / lastNode.Data_U	
    			}
    		}
    	}
    	
    	//check out are there Edges with Resistance that are equal to 0
    	for(ComponentPanel comp : this.components)
    	{
    		if(comp.getDelegate() instanceof Edge )
    		{
    			if( Double.parseDouble( ((Edge)comp.getDelegate()).getParameter(EdgeParameters.Data_R) )==0 )
    			//if currentEdge.Data_R == 0
    			{
    				this.requestPossible = false;
    				
    				if(counter==12)
    				{
    					this.edgesWithZeroR += "\n";
    					counter = 0;
    				}
    				
    				this.edgesWithZeroR += ((Edge)comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID() + "-->" +
    									   ((Edge)comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() + " ; ";
    				counter++;
    			}
    		}
    	}
    }
    
    /**
     * make request to the server
     */
    public void makeRequestToServ()
    {
    	boolean slackTypeIsAssigned = false;
    	
    	this.checkSchemeParams();
    	
    	if(requestPossible)
    	{
    		for(ComponentPanel comp : this.components)
    		{
    			if(comp.getDelegate() instanceof Node)
    			{
    				if( Integer.parseInt( ((Node) comp.getDelegate()).getParameter(NodeParameters.Data_Type) ) == NodeTypes.Slack )
    				{
    					slackTypeIsAssigned = true;
    				}
    			}
    		}
    		
    		if(slackTypeIsAssigned)
    		{
    			srvMsg.setLocationRelativeTo(appGraphRed);
        		srvMsg.setVisible(true);
    			newtonClnt.doRequest();
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(appGraphRed,
	  					  					  GuiLocalization.cant_do_request_because_of_Slack_type_wasnt_assigned_for_a_Node,
	  					  					  GuiLocalization.warning,
	  					  					  JOptionPane.WARNING_MESSAGE);
    		}
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(appGraphRed,
					  					  GuiLocalization.client_isnt_able_to_do_request_because_of_zero_values_of_resistance_in_current_edges +":\n" +
					  					  edgesWithZeroR,
					  					  GuiLocalization.warning,
					  					  JOptionPane.WARNING_MESSAGE);
    		this.requestPossible = true;
    		this.edgesWithZeroR = "";
    	}
    }
    
    /**
     * Set text from server message into text-area of serverMessage(dialog window)
     * @param value : String
     */
    public void setSrvMsg(String value)
    {
    	this.srvMsg.setMessage(value);
    }
    
    /**
     * get Newton Client
     * @return current newtonClnt of this Component container
     */
    public NewtonClient getNewtonClnt()
    {
    	return newtonClnt;
    }
    
    //<new date="05.03.2013" start:>
    /**
     * set value of current "edgeCounter"
     */
    private void setCurrEdgeCounter()
    {
    	for (ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Edge)
    		{
    			edgeCounter = Math.max(edgeCounter, ((Edge) comp.getDelegate()).getID() );
    		}
    	}
    }
    
    /**
     * set value of current "nodeCounter"
     */
    private void setCurrNodeCounter()
    {
    	for (ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			rendererFactory.setCounter( Math.max(rendererFactory.getCounter(), ((Node) comp.getDelegate()).getID() ) );
    		}
    	}
    }
    //<new date="05.03.2013" end;>
    
    //paint info statements
    private int paintID = 1;
    private int paintType;
    private int paintU;
    private int paintAngle;
    private int paintPG;
    private int paintQG;
    private int paintPL;
    private int paintQL;
    
    private int paintCalcU;
    private int paintCalcAngle;
    private int paintCalcPG;
    private int paintCalcQG;
    
    private int paintDgrmAngle;
    private int paintDgrmVoltage;
    
    /**
     * set current paintInfo statement.
     * @param param : NodeParameters
     * @param value : int
     */
    public void paintInfo(NodeParameters param, int value)
    {
    	switch(param)
    	{
    	case Data_Type:
    		paintType = value;
    		break;
    	case Data_U:
    		paintU = value;
    		break;
    	case Data_Angle:
    		paintAngle = value;
    		break;
    	case Data_PG:
    		paintPG = value;
    		break;
    	case Data_QG:
    		paintQG = value;
    		break;
    	case Data_PL:
    		paintPL = value;
    		break;
    	case Data_QL:
    		paintQL = value;
    		break;
    	}
    }
    
    /**
     * set current paintResults statement
     * @param param : NodeParameters
     * @param value : int
     */
    public void paintResults(NodeParameters param, int value)
    {
    	switch(param)
    	{
    	case Data_U:
    		paintCalcU = value;
    		break;
    	case Data_Angle:
    		paintCalcAngle = value;
    		break;
    	case Data_PG:
    		paintCalcPG = value;
    		break;
    	case Data_QG:
    		paintCalcQG = value;
    		break;
    	}
    }
    
    /**
     * set current paintDgrm statement
     * @param param : NodeParameters
     * @param value : int
     */
    public void paintDgrm(NodeParameters param, int value)
    {
    	switch(param)
    	{
    	case Data_Angle:
    		paintDgrmAngle = value;
    		break;
    	case Data_U:
    		paintDgrmVoltage = value;
    		break;
    	}
    }
    
    @Override
	public void paint(Graphics g)
    {
    	//super.paint(g);
    	
    	Graphics2D g2d = (Graphics2D) g;
    	
    	//g2d.translate(getWidth() / 2, getHeight() / 2);
    	//g2d.transform(at);
    	//g2d.translate(getWidth() * -0.5, getHeight() * -0.5);
    	
    	super.paint(g);
    	
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	g2d.setStroke(new BasicStroke((float)(1*zoom)));
    	g2d.setFont(new Font("Arial", Font.PLAIN, getScaledValue(12)));
    	
    	g2d.setColor(Color.gray);
    	
    	for(ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			comp.getDelegate().setSize(comp.getSize());
    		}
    	}
    	
    	for(ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			int multiple = 0;
    			int newLine = getScaledValue(14);
    			int nodeXplusRad = ((Node )comp.getDelegate()).getCenter().x+(((Node )comp.getDelegate()).getWidth()/2)+getScaledValue(4);
    			int nodeYminusRad = ((Node )comp.getDelegate()).getCenter().y-(((Node )comp.getDelegate()).getHeight())-1;
    			
    			if(paintDgrmVoltage == 1)
    			{
    				nodeXplusRad = ((Node )comp.getDelegate()).getCenter().x+(((Node )comp.getDelegate()).getWidth()/2)+getScaledValue(4)+getScaledValue(50+5);
    			}
    			
    			//ID
    			if(paintID==1)
    			{
    				g2d.drawString("id:"+((Node )comp.getDelegate()).getID(),
    								((Node )comp.getDelegate()).getCenter().x+(((Node )comp.getDelegate()).getWidth()/2)+getScaledValue(4),
    								nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//Type
    			if(paintType==1)
    			{
    				switch( Integer.parseInt( ((Node)comp.getDelegate()).getParameter(NodeParameters.Data_Type) ) )
    				{
    				case 3:
    					g2d.drawString( GuiLocalization.type + ": Slack", nodeXplusRad, nodeYminusRad + newLine*multiple);
    					break;
    				case 2:
    					g2d.drawString( GuiLocalization.type + ": PV", nodeXplusRad, nodeYminusRad + newLine*multiple);
    					break;
    				default :
    					g2d.drawString( GuiLocalization.type + ": PQ", nodeXplusRad, nodeYminusRad + newLine*multiple);
    					break;
    				}
    				multiple++;
    			}
    			//U
    			if(paintU==1)
    			{
    				g2d.drawString(GuiLocalization.voltage+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_U), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//Angle
    			if(paintAngle==1)
    			{
    				g2d.drawString(GuiLocalization.ph_Angle+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_Angle), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//PG
    			if(paintPG==1)
    			{
    				g2d.drawString(GuiLocalization.a_gen_Power+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_PG), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//QG
    			if(paintQG==1)
    			{
    				g2d.drawString(GuiLocalization.r_gen_Power+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_QG), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//PL
    			if(paintPL==1)
    			{
    				g2d.drawString(GuiLocalization.a_cons_Power+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_PL), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			//QL
    			if(paintQL==1)
    			{
    				g2d.drawString(GuiLocalization.r_cons_Power+": "+((Node )comp.getDelegate()).getParameter(NodeParameters.Data_QL), nodeXplusRad, nodeYminusRad + newLine*multiple);
    				multiple++;
    			}
    			
    		}
    	}
    	
    	g2d.setColor(new Color(150, 200, 230));
    	
    	for(ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			int multiple = 0;
    			int newLine = getScaledValue(14);
    			int nodeXminusRad = ((Node )comp.getDelegate()).getCenter().x-(((Node )comp.getDelegate()).getWidth()/2)-getScaledValue(100);
    			int nodeYminusRad = ((Node )comp.getDelegate()).getCenter().y-(((Node )comp.getDelegate()).getHeight())-getScaledValue(4);
    			//calcQG
    			if(paintCalcQG==1)
    			{
    				g2d.drawString(GuiLocalization.r_gen_Power+": "+((Node )comp.getDelegate()).getResult(NodeResults.Res_QG), nodeXminusRad, nodeYminusRad + newLine*multiple);
    				multiple--;
    			}
    			//calcPG
    			if(paintCalcPG==1)
    			{
    				g2d.drawString(GuiLocalization.a_gen_Power+": "+((Node )comp.getDelegate()).getResult(NodeResults.Res_PG), nodeXminusRad, nodeYminusRad + newLine*multiple);
    				multiple--;
    			}
    			//calcAngle
    			if(paintCalcAngle==1)
    			{
    				g2d.drawString(GuiLocalization.ph_Angle+": "+((Node )comp.getDelegate()).getResult(NodeResults.Res_Angle), nodeXminusRad, nodeYminusRad + newLine*multiple);
    				multiple--;
    			}
    			//calcU
    			if(paintCalcU==1)
    			{
    				g2d.drawString(GuiLocalization.voltage+": "+((Node )comp.getDelegate()).getResult(NodeResults.Res_U), nodeXminusRad, nodeYminusRad + newLine*multiple);
    				multiple--;
    			}
    		}
    	}
    	
    	for(ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			int multiple = 0;
    			int newLine = getScaledValue(14);
    			int nodeXminusRad = ((Node )comp.getDelegate()).getCenter().x-(((Node )comp.getDelegate()).getWidth()/2)-getScaledValue(30);
    			int nodeYminusRad = ((Node )comp.getDelegate()).getCenter().y-(((Node )comp.getDelegate()).getHeight());
    			//Angle diagram
    			if(paintDgrmAngle == 1)
    			{
    				new AngleDiagram(g2d).paintDiagram( (int)Math.round( Double.parseDouble( ((Node )comp.getDelegate()).getResult(NodeResults.Res_Angle) ) ),
    													 nodeXminusRad,
    													 nodeYminusRad + newLine*multiple );
    			}
    		}
    	}
    	
    	for(ComponentPanel comp : components)
    	{
    		if(comp.getDelegate() instanceof Node)
    		{
    			int multiple = 0;
    			int newLine = getScaledValue(14);
    			int nodeXminusRad = ((Node )comp.getDelegate()).getCenter().x+(((Node )comp.getDelegate()).getWidth()/2)+getScaledValue(5);
    			int nodeYminusRad = ((Node )comp.getDelegate()).getCenter().y-(((Node )comp.getDelegate()).getHeight())+getScaledValue(3);
    			//Voltage diagram
    			if(paintDgrmVoltage == 1)
    			{
    				//numeric type
    				if ( ((Node )comp.getDelegate()).getVolDiagramType() == 0 )
    				{
    					new VoltageDiagram(g2d).paintDiagram( Double.parseDouble( ((Node )comp.getDelegate()).getResult(NodeResults.Res_U) ),
    													  	  ((Node )comp.getDelegate()).getVolDiagramMax(),
    													  	  ((Node )comp.getDelegate()).getVolDiagramClass(),
    													  	  ((Node )comp.getDelegate()).getVolDiagramMin(),
    													  	  nodeXminusRad,
    													  	  nodeYminusRad + newLine*multiple );
    				}
    				//percentage type
    				else
    				{
    					new VoltageDiagramPercent(g2d).paintDiagram( Double.parseDouble( ((Node )comp.getDelegate()).getResult(NodeResults.Res_U) ),
    																 ((Node )comp.getDelegate()).getVolDiagramMax(),
    																 ((Node )comp.getDelegate()).getVolDiagramClass(),
    																 ((Node )comp.getDelegate()).getVolDiagramMin(),
    																 nodeXminusRad,
    																 nodeYminusRad + newLine*multiple );
    				}
    			}
    		}
    	}
    	
    }
    
    /**
	 * get Scaled Value
	 * @param value
	 * @return <code>int</code> scaled value
	 */
	public static int getScaledValue(int value)
	{
		int scaledValue = (int)Math.round((double)value * ComponentContainer.zoom);
		
		if(scaledValue < 1)
		{
			return 1;
		}
		else
		{
			return scaledValue;
		}
	}
    
	/**
	 * get Actual value
	 * @param value
	 * @return <code>int</code> actual value
	 */
	public static int getActualValue(int value)
	{
		int actualValue = (int)Math.round((double)value / ComponentContainer.zoom);
		
		if(actualValue < 1)
		{
			return 1;
		}
		else
		{
			return actualValue;
		}
	}
	
	/**
	 * set Zoom rate
	 * @param zoomValue <code>double</code>
	 */
	void setZoom(double zoomValue)
	{
		ComponentContainer.zoom = zoomValue;
		
		ComponentContainer.COMP_MIN_SIZE = ComponentContainer.getScaledValue(40);
		ComponentPanel.COMP_MIN_SIZE = ComponentContainer.getScaledValue(40);
		
		for(ComponentPanel comp : components)
		{
			comp.setBounds(getScaledValue(comp.getActualBounds().x),
						   getScaledValue(comp.getActualBounds().y),
						   getScaledValue(comp.getActualBounds().width),
						   getScaledValue(comp.getActualBounds().height) );
		}
		
		recheckSize();
		repaint();
	}
}
