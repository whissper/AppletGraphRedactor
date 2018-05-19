package sv.editor;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import java.io.*;
import java.security.KeyStore;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.*;

import sv.editor.interfaces.*;
import sv.engine.imgExport.ImgExporter;
import sv.engine.xmlDocParser.XmlDocParserII;
import sv.gui.additionalElements.GradientQuality;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.graphElements.*;
import sv.gui.localization.GuiLocalization;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.*;


/**
 * AppletGraphRedactor
 * <p/>
 * Applet Graph Redactor
 *
 * @author SAV2
 * @version 1.0.0
 * @since 25.07.2012
 */
public class AppGraphRedactor extends JApplet {
	
	/**
	 * image exporter
	 */
	ImgExporter imgExporter;
	
	/**
	 * Undo Manager
	 */
	UndoManager undoManager;

	/**
	 * XML document Parser
	 */
	private XmlDocParserII xmlDocParser;
	
	/**
	 * Component container 
	 */
	private ComponentContainer cc;
	
    /**
     * tool bar
     */
    private JToolBar toolBar;
    
    /**
     * scroll pane
     */
    JScrollPane ccScrollPane;
    
    /**
     * Node's ComponentDescriptor
     */
    private NodeComponentDescriptor nodeCompDescrpt;

    /**
     * Initialization of GUI
     */
    
    //-------------------------------Visualization menu items---(start)----------->
    public JCheckBoxMenuItem checkItemType;
    public JCheckBoxMenuItem checkItemU;
    public JCheckBoxMenuItem checkItemAngle;
    public JCheckBoxMenuItem checkItemPG;
    public JCheckBoxMenuItem checkItemQG;
    public JCheckBoxMenuItem checkItemPL;
    public JCheckBoxMenuItem checkItemQL;
    public JCheckBoxMenuItem checkItemCalcU;
    public JCheckBoxMenuItem checkItemCalcAngle;
    public JCheckBoxMenuItem checkItemCalcPG;
    public JCheckBoxMenuItem checkItemCalcQG;
    public JCheckBoxMenuItem checkItemDgrmAngle;
    public JCheckBoxMenuItem checkItemDgrmVoltage;
    //-------------------------------Visualization menu items---(end)----------->
    
    //----------------------------------Undo/Redo---------(start)--------------->
    public JMenuItem undoItem;
    public JMenuItem redoItem;
    //----------------------------------Undo/Redo---------(end)----------------->
    
    //----------------------------------toolbar button---------(start)---------->
    private JToggleButton pencilButton;
    private JToggleButton gridButton;
    private JLabel gridLabel;
    private JTextField cellSize;
    JLabel nodesCountLabel;
    JLabel edgesCountLabel;
    private JLabel zoomLabel;
    JSlider zoomSlider;
    private JLabel zoomAfterLabel;
    private JButton zoomButton;
    private JToggleButton gradientButton;
    private JLabel gradientQualityLabel;
    private JComboBox gradientQuality;
    //----------------------------------toolbat button---------(end)------------>
    
    //-------------------------------------------------------------- "File" menu start:
    private JMenu fileMenu;
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exportItem;
    private JMenu localizationMenu;
    private JMenuItem enusItem;
    private JMenuItem ruruItem;
    //-------------------------------------------------------------- "File" menu end;
    
    //-------------------------------------------------------------- "Edit" menu start:
    private JMenu editMenu;
    private JMenuItem clearAllItem;
    //-------------------------------------------------------------- "Edit" menu end:
    
    //-------------------------------------------------------------- "Visualization" menu start:
    private JMenu visualMenu;
    private JMenu curParamsMenu;
    private JMenu calcParamsMenu;
    private JMenu diagramMenu;
    private JMenuItem commonSetItem;
    //-------------------------------------------------------------- "Visualization" menu end:
    
    //-------------------------------------------------------------- "Server" menu start:
    private JMenu serverMenu;
    private JMenuItem itemDoReq;
    //-------------------------------------------------------------- "Server" menu start:
    
    public void init()
	  {
		  try
		  {
			  SwingUtilities.invokeAndWait(new Runnable() 
			  {
				@Override
				public void run()
				{
				  makeGUI();
				}
			  });
		  }
		  catch(Exception exc)
		  {
			  System.out.println(GuiLocalization.cant_create_because_of + exc);
		  }
	  }
    
    /**
     * creating of GUI
     */
    private void makeGUI() 
    {
    	/*
    	try {
    	      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	    } catch (Exception e) {
    	      e.printStackTrace();
    	    }
    	*/
    	setSize(800, 600);
    	
        JPanel cp = new JPanel(new BorderLayout(5, 5));
        cp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(130, 190, 250))));
        setContentPane(cp);
        
        /*
        types = new JComboBox(new Object[]{//new JButtonComponentDescriptor(),
                //new JCheckBoxComponentDescriptor(true),
                //new JCheckBoxComponentDescriptor(false),
                //new JTextFieldComponentDescriptor(),
                //new JTreeComponentDescriptor(),
                //new JTableComponentDescriptor(),
                new NodeComponentDescriptor(),
                //new MyComponentComponentDescriptor()
        });
        
        cp.add(types, BorderLayout.NORTH); //add JComboBox to the "North" part of "cp"(contentPane)
        types.setSelectedIndex(0);
        */
        
        nodeCompDescrpt = new NodeComponentDescriptor();
        
        cc = new ComponentContainer(this);
        cc.setRendererFactory(new ComponentRendererFactoryImpl());
        
        ccScrollPane = new JScrollPane(cc);
        cp.add(ccScrollPane, BorderLayout.CENTER);
        ccScrollPane.setWheelScrollingEnabled(false);
        
        //Image Exporter
        imgExporter = new ImgExporter(this, cc);
        
        //XML parser
        xmlDocParser = new XmlDocParserII(this, cc);
        
        //creating new UndoManager and adding UndoableEditListener to it
        undoManager = new UndoManager();
        cc.addUndoableEditListener(undoManager);
        
        //----------------------------tool_bar--------------------------------start--->
        toolBar = new JToolBar("Tools");
        toolBar.setPreferredSize(new Dimension(30, 30));
        cp.add(toolBar, BorderLayout.NORTH);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        
        Image pencilImg = new ImageIcon(this.getClass().getResource("img/pencil.png")).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        pencilButton = new JToggleButton( new ImageIcon(pencilImg) );
        pencilButton.setToolTipText(GuiLocalization.drawing_state);
        pencilButton.setSelected(true);
        
        gridButton = new JToggleButton(new ImageIcon( this.getClass().getResource("img/grid.png") ));
        gridButton.setToolTipText(GuiLocalization.grid_state);
        
        gridLabel = new JLabel(GuiLocalization.cell_size);
        cellSize = new JTextField(((Integer)cc.gridPanel.getCellSize()).toString());
        cellSize.setMaximumSize(new Dimension(30, 23));
        cellSize.setMinimumSize(new Dimension(30, 23));
        cellSize.setPreferredSize(new Dimension(30, 23));
        
        nodesCountLabel = new JLabel(GuiLocalization.nodes + cc.getNodesCount() );
        edgesCountLabel = new JLabel(GuiLocalization.edges + cc.getEdgesCount() );
        
        zoomLabel = new JLabel(new ImageIcon( this.getClass().getResource("img/zoom.png") ));
        
        zoomSlider = new JSlider(10, 200, 100);
        zoomSlider.setMaximumSize(new Dimension(200, 20));
        zoomSlider.setMinimumSize(new Dimension(100, 20));
        zoomSlider.setPreferredSize(new Dimension(200, 20));
        
        zoomAfterLabel = new JLabel(zoomSlider.getValue() + " %");
        
        zoomButton = new JButton("1:1");
        
        gradientButton = new JToggleButton(new ImageIcon( this.getClass().getResource("img/color.png") ));
        gradientButton.setToolTipText(GuiLocalization.gradient);
        
        gradientQualityLabel = new JLabel(GuiLocalization.quality);
        gradientQuality = new JComboBox(new String[]{GuiLocalization.ultra,GuiLocalization.high,GuiLocalization.medium,GuiLocalization.low,GuiLocalization.veryLow});
        gradientQuality.setMaximumSize(new Dimension(85, 22));
        gradientQuality.setMinimumSize(new Dimension(85, 22));
        gradientQuality.setPreferredSize(new Dimension(85, 22));
        gradientQuality.setSelectedItem(cc.gradientPanel.getCurrentGradientQuality());
        
        toolBar.addSeparator();
        toolBar.add(pencilButton);
        toolBar.add(gridButton);
        toolBar.addSeparator();
        toolBar.add(gridLabel);
        toolBar.add(cellSize);
        toolBar.addSeparator();
        toolBar.add(zoomButton);
        toolBar.add(zoomLabel);
        toolBar.add(zoomSlider);
        toolBar.add(zoomAfterLabel);
        toolBar.addSeparator();
        toolBar.add(gradientButton);
        toolBar.addSeparator();
        toolBar.add(gradientQualityLabel);
        toolBar.add(gradientQuality);
        toolBar.addSeparator();
        toolBar.add(nodesCountLabel);
        toolBar.addSeparator();
        toolBar.add(edgesCountLabel);
        toolBar.addSeparator();
        
        

        
        
        pencilButton.addItemListener(new ItemListener() {
        	
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == 1)
				{
					gradientButton.setSelected(false);
					cc.setDrawingState(true);
				}
				else
				{
					cc.setDrawingState(false);
				}
			}
		});
        
        gridButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == 1)
				{
					cc.gridEnabled = true;
					cc.gridPanel.setVisible(true);
				}
				else
				{
					cc.gridEnabled = false;
					cc.gridPanel.setVisible(false);
				}	
			}
		});
        
        cellSize.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				cc.gridPanel.setCellSize(cellSize.getText());	
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				cc.gridPanel.setCellSize(cellSize.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				//cc.gridPanel.setCellSize(cellSize.getText());	
			}
		});
        
        zoomButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				zoomSlider.setValue(100);
			}
		});
        
        zoomSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				zoomAfterLabel.setText(zoomSlider.getValue() + " %");
				cc.setZoom(zoomSlider.getValue()/100.00);
			}
		});
        
        gradientButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
				{
					@Override
					protected Void doInBackground() throws Exception
					{
						cc.gradientPanel.drawGradient();
						return null;
					}
					
					@Override
					protected void done()
					{
						cc.gradientPanel.stopTimer();
						super.done();
					}
				};
						
				if(e.getStateChange() == 1)
				{
					pencilButton.setSelected(false);
					worker.execute();
					cc.gradientPanel.setVisible(true);
				}
				else
				{
					worker.cancel(true);
					cc.gradientPanel.clearData();
					cc.gradientPanel.setVisible(false);
				}	
			}
		});
        
        gradientQuality.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if( ((JComboBox) e.getSource()).getSelectedItem().equals(GuiLocalization.ultra) )
				{
					cc.gradientPanel.setPixelSize(GradientQuality.ultra);
				}
				else if( ((JComboBox) e.getSource()).getSelectedItem().equals(GuiLocalization.high) )
				{
					cc.gradientPanel.setPixelSize(GradientQuality.high);
				}
				else if( ((JComboBox) e.getSource()).getSelectedItem().equals(GuiLocalization.medium) )
				{
					cc.gradientPanel.setPixelSize(GradientQuality.medium);
				}
				else if( ((JComboBox) e.getSource()).getSelectedItem().equals(GuiLocalization.low) )
				{
					cc.gradientPanel.setPixelSize(GradientQuality.low);
				}
				else if( ((JComboBox) e.getSource()).getSelectedItem().equals(GuiLocalization.veryLow) )
				{
					cc.gradientPanel.setPixelSize(GradientQuality.veryLow);
				}
			}
		});
        //----------------------------tool_bar----------------------------------end--->
        
        //-------------------------------------------------------------- "File" menu start:
        fileMenu = new JMenu(GuiLocalization.file);
        
        //item "New"
        newItem = new JMenuItem(GuiLocalization.new_f, new ImageIcon( this.getClass().getResource("img/new_f.png") ));
        //newItem.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK + ActionEvent.SHIFT_MASK) );
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        //item "Open" and set "ctrl+O" accelerator to him
		openItem = new JMenuItem(GuiLocalization.open_file, new ImageIcon( this.getClass().getResource("img/open.png") ));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openItem.getAccessibleContext().setAccessibleDescription("Open document");
		
		//item "Save" and set "ctrl+S" accelerator to him
		saveItem = new JMenuItem(GuiLocalization.save_f, new ImageIcon( this.getClass().getResource("img/save.png") ));
		saveItem.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK) );
		saveItem.getAccessibleContext().setAccessibleDescription("Save document");
		
		//item "Export as..."
		exportItem = new JMenuItem(GuiLocalization.export_as, new ImageIcon( this.getClass().getResource("img/export.png") ));
		exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		
		//menu-item "Localization"
		localizationMenu = new JMenu(GuiLocalization.localization);
		//item "en_US"
		enusItem = new JMenuItem("en_US", new ImageIcon( this.getClass().getResource("img/us.png") ));
		//item "ru_RU"
		ruruItem = new JMenuItem("ru_RU", new ImageIcon( this.getClass().getResource("img/ru.png") ));
		
		//"New" action
		newItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				zoomSlider.setValue(100);
				
				int userAnswer = JOptionPane.showOptionDialog(	AppGraphRedactor.this,
		  				   										GuiLocalization.do_you_want_to_save_the_current_scheme, 
		  				   										GuiLocalization.save_current_scheme, 
		  				   										JOptionPane.YES_NO_CANCEL_OPTION,
		  				   										JOptionPane.QUESTION_MESSAGE,
		  				   										null,
		  				   										new String[]{GuiLocalization.yes, GuiLocalization.no, GuiLocalization.cancel},
		  				   										"default" );
				if(userAnswer == JOptionPane.YES_OPTION)
				{
					try
					{
						xmlDocParser.saveFile();
					}
					catch (TransformerException exc)
					{
						JOptionPane.showMessageDialog(AppGraphRedactor.this, exc.toString());
					}
					catch (IOException exc)
					{
						JOptionPane.showMessageDialog(AppGraphRedactor.this, exc.toString());
					}
					
					cc.fullClear();
				}
				else if(userAnswer == JOptionPane.NO_OPTION)
				{
					cc.fullClear();
				}
			}
		});
		
        //"Open File..." action
		openItem.addActionListener(new ActionListener() 
			{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				zoomSlider.setValue(100);
				xmlDocParser.openFile();
				undoItem.setEnabled(undoManager.canUndo());
		        redoItem.setEnabled(undoManager.canRedo());
			}
		});
		
		//"Save" action
		saveItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					zoomSlider.setValue(100);
					xmlDocParser.saveFile();
				}
				catch (TransformerException exc)
				{
					JOptionPane.showMessageDialog(AppGraphRedactor.this,
		  					  					  exc.getMessage(),
		  					  					  GuiLocalization.xml_parser_failed,
		  					  					  JOptionPane.ERROR_MESSAGE);
				}
				catch (IOException exc)
				{
					JOptionPane.showMessageDialog(AppGraphRedactor.this,
							  					  exc.getMessage(),
							  					  GuiLocalization.xml_parser_failed,
							  					  JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//"Export as..." action
		exportItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					imgExporter.exportImage();
				}
				catch (IOException exc)
				{
					JOptionPane.showMessageDialog(AppGraphRedactor.this,
												  exc.getMessage(),
												  GuiLocalization.export_image_failed,
												  JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
		
		//"en_US" action
		enusItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GuiLocalization.setEnUS();
				ruruItem.setEnabled(true);
				enusItem.setEnabled(false);
				changeStrings();
			}
		});
		
		//"ru_RU" action
		ruruItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GuiLocalization.setRuRU();
				enusItem.setEnabled(true);
				ruruItem.setEnabled(false);
				changeStrings();
			}
		});
		
		//add items to the file menu
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		fileMenu.add(saveItem);
		fileMenu.add(exportItem);
		fileMenu.addSeparator();
		fileMenu.add(localizationMenu);
		
		localizationMenu.add(enusItem);
		localizationMenu.addSeparator();
		localizationMenu.add(ruruItem);
		
		enusItem.setEnabled(false);
		ruruItem.setEnabled(true);
		//-------------------------------------------------------------- "File" menu end;
		
		//-------------------------------------------------------------- "Edit" menu start:
		editMenu = new JMenu(GuiLocalization.edit);
		
		//item "Clear All"
		clearAllItem = new JMenuItem(GuiLocalization.clear_all, new ImageIcon( this.getClass().getResource("img/clear_all.png") ));
		clearAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK + ActionEvent.SHIFT_MASK));
		
		//item "Undo" and set "ctrl+Z" accelerator to him 
		undoItem = new JMenuItem(GuiLocalization.undo, new ImageIcon( this.getClass().getResource("img/undo.png") ));
		undoItem.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK) );
		undoItem.getAccessibleContext().setAccessibleDescription("Undo last operation");
		
		//item "Redo" and set "ctrl+Y" accelerator to him 
		redoItem = new JMenuItem(GuiLocalization.redo, new ImageIcon( this.getClass().getResource("img/redo.png") ));
		redoItem.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK) );
		redoItem.getAccessibleContext().setAccessibleDescription("Redo last operation");
		
		//"Clear All" action
		clearAllItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cc.fullClear();
			}
		});
		
		//"Undo" action
		undoItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				undoManager.undo();				
			}
		});
		
		//"Redo" action
        redoItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				undoManager.redo();				
			}
		});
        
		//add items to the edit menu
        editMenu.add(clearAllItem);
        editMenu.addSeparator();
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        
        undoItem.setEnabled(undoManager.canUndo());
        redoItem.setEnabled(undoManager.canRedo());
		//-------------------------------------------------------------- "Edit" menu end;
        
        //-------------------------------------------------------------- "Visualization" menu start:
        visualMenu = new JMenu(GuiLocalization.visualization);
        
        curParamsMenu = new JMenu(GuiLocalization.current_parameters);
        curParamsMenu.setIcon(new ImageIcon( this.getClass().getResource("img/current.png") ));
        
        calcParamsMenu = new JMenu(GuiLocalization.calculated_parameters);
        calcParamsMenu.setIcon(new ImageIcon( this.getClass().getResource("img/calc.png") ));
        
        diagramMenu = new JMenu(GuiLocalization.diagrams);
        diagramMenu.setIcon(new ImageIcon( this.getClass().getResource("img/diagrams.png") ));
        
        commonSetItem = new JMenuItem(GuiLocalization.common_settings, new ImageIcon( this.getClass().getResource("img/visualization.png") ));
        commonSetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
        
        //item "Show Type"
        checkItemType = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.type);
        //item "Show Voltage lvl"
        checkItemU = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.voltage_lvl);
        //item "Show Phase Angle"
        checkItemAngle = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.phase_angle);
        //item "Show Active gen. Power"
        checkItemPG = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.active_gen_power);
        //item "Show Reactive gen. Power"
        checkItemQG = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.reactive_gen_power);
        //item "Show Active cons. Power"
        checkItemPL = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.active_cons_power);
        //item "Show Reactive cons. Power"
        checkItemQL = new JCheckBoxMenuItem(GuiLocalization.show + GuiLocalization.reactive_cons_power);
        //--------------------------------------------------------------------------------
        //item "Show calc. Voltage lvl"
        checkItemCalcU = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.voltage_lvl);
        //item "Show calc. Phase Angle"
        checkItemCalcAngle = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.phase_angle);
        //item "Show calc. Active gen. Power"
        checkItemCalcPG = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.active_gen_power);
        //item "Show calc. Reactive gen. Power"
        checkItemCalcQG = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.reactive_gen_power);
        //---------------------------------------------------------------------------------
        //item  "(diagram) Show calc. Phase Angle"
        checkItemDgrmAngle = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.phase_angle);
        //item  "(diagram) Show calc. Voltage lvl"
        checkItemDgrmVoltage = new JCheckBoxMenuItem(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.voltage_lvl);
        
        //"Show Type" action
        checkItemType.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_Type, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Voltage lvl" action
        checkItemU.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_U, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Phase Angle" action
        checkItemAngle.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_Angle, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Active gen. Power" action
        checkItemPG.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_PG, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Reactive gen. Power" action
        checkItemQG.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_QG, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Active cons. Power" action
        checkItemPL.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_PL, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show Reactive cons. Power" action
        checkItemQL.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintInfo(NodeParameters.Data_QL, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show calc. Voltage lvl" action
        checkItemCalcU.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintResults(NodeParameters.Data_U, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show calc. Phase Angle" action
        checkItemCalcAngle.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintResults(NodeParameters.Data_Angle, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show calc. Active gen. Power" action
        checkItemCalcPG.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintResults(NodeParameters.Data_PG, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Show calc. Reactive gen. Power" action
        checkItemCalcQG.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintResults(NodeParameters.Data_QG, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"(diagram) Show calc. Phase Angle" action
        checkItemDgrmAngle.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintDgrm(NodeParameters.Data_Angle, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"(diagram) Show calc. Voltage lvl" action
        checkItemDgrmVoltage.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cc.paintDgrm(NodeParameters.Data_U, e.getStateChange());
				cc.repaint();
			}
		});
        
        //"Common settings" action
        commonSetItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				cc.visualizationMenu.setLocationRelativeTo(cc);
				cc.visualizationMenu.representData();
				cc.visualizationMenu.setVisible(true);
			}
		});
        
        //add items to the visualization menu
        visualMenu.add(curParamsMenu);
        visualMenu.add(calcParamsMenu);
        visualMenu.add(diagramMenu);
        visualMenu.addSeparator();
        visualMenu.add(commonSetItem);
        
        curParamsMenu.add(checkItemType);
        curParamsMenu.add(checkItemU);
        curParamsMenu.add(checkItemAngle);
        curParamsMenu.add(checkItemPG);
        curParamsMenu.add(checkItemQG);
        curParamsMenu.add(checkItemPL);
        curParamsMenu.add(checkItemQL);
        
        calcParamsMenu.add(checkItemCalcU);
        calcParamsMenu.add(checkItemCalcAngle);
        calcParamsMenu.add(checkItemCalcPG);
        calcParamsMenu.add(checkItemCalcQG);
        
        diagramMenu.add(checkItemDgrmAngle);
        diagramMenu.add(checkItemDgrmVoltage);
        //-------------------------------------------------------------- "Visualization" menu end;
		
        //-------------------------------------------------------------- "Server" menu start:
        serverMenu = new JMenu(GuiLocalization.server);
        
        //item "Do request"
        itemDoReq = new JMenuItem(GuiLocalization.do_request, new ImageIcon( this.getClass().getResource("img/request.png") ));
        itemDoReq.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK + ActionEvent.SHIFT_MASK));
        
        //"Do request" action
      	itemDoReq.addActionListener(new ActionListener() {
      			
      		@Override
      		public void actionPerformed(ActionEvent e) {
      			cc.makeRequestToServ();
      		}
      	});
      	
      	//add items to the server menu
      	serverMenu.add(itemDoReq);
        //-------------------------------------------------------------- "Server" menu end;
        
		//Common menu bar for Menus (File | Edit | ...)
		JMenuBar menuBar = new JMenuBar(); //create JMenuBar
		menuBar.add(fileMenu); //add fileMenu("File") to the current JMenuBar
		menuBar.add(editMenu); //add editMenu("Edit") to the current JMenuBar
		menuBar.add(visualMenu); //add visualMenu("Visualization") to the current JMenuBar
		menuBar.add(serverMenu); //add serverMenu("Server") to the current JMenuBar
		setJMenuBar(menuBar); //set current JMenuBar
		
        cp.setFocusable(true); //for Keyboard interaction
        cc.setFocusable(true); //for Keyboard interaction

        //<start: new data=03.09.2012>
        /*
        KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyboardManager.addKeyEventDispatcher(new KeyDispatcher());
        */
        //<end: new data=03.09.2012>
    }
    
    /**
     * change strings
     */
    private void changeStrings()
    {
    	//AppGraphRedactor
    	pencilButton.setToolTipText(GuiLocalization.drawing_state);
    	gridButton.setToolTipText(GuiLocalization.grid_state);
    	gradientButton.setToolTipText(GuiLocalization.gradient);
    	gridLabel.setText(GuiLocalization.cell_size);
    	gradientQualityLabel.setText(GuiLocalization.quality);

    	gradientQuality.setModel(new JComboBox(new String[]{GuiLocalization.ultra,GuiLocalization.high,GuiLocalization.medium,GuiLocalization.low,GuiLocalization.veryLow}).getModel());
        gradientQuality.setSelectedItem(cc.gradientPanel.getCurrentGradientQuality());
        
    	nodesCountLabel.setText(GuiLocalization.nodes + cc.getNodesCount() );
    	edgesCountLabel.setText(GuiLocalization.edges + cc.getEdgesCount() );
    	fileMenu.setText(GuiLocalization.file);
    	newItem.setText(GuiLocalization.new_f);
    	openItem.setText(GuiLocalization.open_file);
    	saveItem.setText(GuiLocalization.save_f);
    	exportItem.setText(GuiLocalization.export_as);
    	localizationMenu.setText(GuiLocalization.localization);
    	editMenu.setText(GuiLocalization.edit);
    	clearAllItem.setText(GuiLocalization.clear_all);
    	undoItem.setText(GuiLocalization.undo);
    	redoItem.setText(GuiLocalization.redo);
    	visualMenu.setText(GuiLocalization.visualization);
    	curParamsMenu.setText(GuiLocalization.current_parameters);
    	calcParamsMenu.setText(GuiLocalization.calculated_parameters);
    	diagramMenu.setText(GuiLocalization.diagrams);
    	commonSetItem.setText(GuiLocalization.common_settings);
    	checkItemType.setText(GuiLocalization.show + GuiLocalization.type);
        checkItemU.setText(GuiLocalization.show + GuiLocalization.voltage_lvl);
        checkItemAngle.setText(GuiLocalization.show + GuiLocalization.phase_angle);
        checkItemPG.setText(GuiLocalization.show + GuiLocalization.active_gen_power);
        checkItemQG.setText(GuiLocalization.show + GuiLocalization.reactive_gen_power);
        checkItemPL.setText(GuiLocalization.show + GuiLocalization.active_cons_power);
        checkItemQL.setText(GuiLocalization.show + GuiLocalization.reactive_cons_power);
        checkItemCalcU.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.voltage_lvl);
        checkItemCalcAngle.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.phase_angle);
        checkItemCalcPG.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.active_gen_power);
        checkItemCalcQG.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.reactive_gen_power);
        checkItemDgrmAngle.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.phase_angle);
        checkItemDgrmVoltage.setText(GuiLocalization.show+GuiLocalization.calc + GuiLocalization.voltage_lvl);
        serverMenu.setText(GuiLocalization.server);
        itemDoReq.setText(GuiLocalization.do_request);
        
        //ComponentContainer
        cc.changeStrings();
        
        UIManager.put("FileChooser.lookInLabelText", GuiLocalization.look_in);
        UIManager.put("FileChooser.saveInLabelText", GuiLocalization.save_in);
        UIManager.put("FileChooser.fileNameLabelText", GuiLocalization.file_name);
        UIManager.put("FileChooser.filesOfTypeLabelText", GuiLocalization.files_of_type);
        UIManager.put("FileChooser.openButtonToolTipText", GuiLocalization.open_selected_file);
        UIManager.put("FileChooser.saveButtonToolTipText", GuiLocalization.save_selected_file);
        UIManager.put("FileChooser.cancelButtonToolTipText", GuiLocalization.abort_file_chooser_dialog);
        UIManager.put("FileChooser.openButtonText", GuiLocalization.open);
        UIManager.put("FileChooser.saveButtonText", GuiLocalization.save);
        UIManager.put("FileChooser.cancelButtonText", GuiLocalization.cancel);

    }
    
    /**
     * Implementation of {@link ComponentRendererFactory}
     */
    class ComponentRendererFactoryImpl implements ComponentRendererFactory
    {
        public JComponent createRenderer() 
        {
            Object o = nodeCompDescrpt;
            if (!(o instanceof ComponentDescriptor))
            	return null;
            return ((ComponentDescriptor) o).createComponent();
        }
        
        public void reduceCounter()
        {
        	Object o = nodeCompDescrpt;
            if (!(o instanceof ComponentDescriptor))
            	return;
            ((ComponentDescriptor) o).reduceCompCounter();
        }
        
        public void setCounter(int value)
        {
        	Object o = nodeCompDescrpt;
            if (!(o instanceof ComponentDescriptor))
            	return;
            ((ComponentDescriptor) o).setCompCounter(value);
        }
        
        public int getCounter()
        {
        	Object o = nodeCompDescrpt;
        	if (!(o instanceof ComponentDescriptor))
            	return 0;
        	return ((ComponentDescriptor) o).getCompCounter();
        }
    }

    /**
     * Component descriptor and actually renderer factory
     */
    private abstract class ComponentDescriptor {

        /**
         * title to show in the component types list
         */
        private String title;

        /**
         * created component counter
         */
        protected int counter = 0;

        protected ComponentDescriptor(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
        
        /**
         * reduce counter
         */
        protected void reduceCompCounter()
        {
        	this.counter--;
        }
        
        /**
         * set counter
         * @param value : int
         */
        protected void setCompCounter(int value)
        {
        	if(value <= 0) return;
        	this.counter = value;
        }
        
        /**
         * get counter
         * @return counter : int
         */
        protected int getCompCounter()
        {
        	return this.counter;
        }
        
        /**
         * Creates renderer
         *
         * @return renderer component
         */
        abstract JComponent createComponent();
    }
    

    /**
     * Node renderer descriptor
     */
    private class NodeComponentDescriptor extends ComponentDescriptor {
    	
    	NodeComponentDescriptor()
    	{
    		super("Node");
    	}
    	
    	@Override
        JComponent createComponent() {
            return new Node(++counter);
        }
    	
    }
    
    /**
     * JButton renderer descriptor
     */
    /*-----
    private class JButtonComponentDescriptor extends ComponentDescriptor {

        JButtonComponentDescriptor() {
            super("JButton component");
        }

        @Override
        JComponent createComponent() {
            return new JButton("Button #" + counter++);
        }
    }
    -----*/
    /**
     * MyComponent renderer descriptor
     */
    /*-----
    private class MyComponentComponentDescriptor extends ComponentDescriptor {

    	MyComponentComponentDescriptor() {
            super("MyComponent component");
        }

        @Override
        JComponent createComponent() {
            return new MyComponent();
        }
    }
	-----*/
    /**
     * JCheckBox renderer descriptor
     */
    /*-----
    private class JCheckBoxComponentDescriptor extends ComponentDescriptor {

        private boolean selected;

        JCheckBoxComponentDescriptor(boolean selected) {
            super("JCheckBox component " + (selected ? "(selected)" : "(unselected)"));
            this.selected = selected;
        }

        @Override
        JComponent createComponent() {
            return new JCheckBox("CheckBox #" + counter++, selected);
        }
    }
	-----*/
    /**
     * JtextField renderer descriptor
     */
    /*-----
    private class JTextFieldComponentDescriptor extends ComponentDescriptor {

        JTextFieldComponentDescriptor() {
            super("JTextField component");
        }

        @Override
        JComponent createComponent() {
            return new JTextField("Text field #" + counter++);
        }
    }
	-----*/
    /**
     * JTree renderer descriptor
     */
    /*-----
    private class JTreeComponentDescriptor extends ComponentDescriptor {

        JTreeComponentDescriptor() {
            super("JTree component");
        }

        @Override
        JComponent createComponent() {
            Vector<Object> data = new Vector<Object>();
            data.add("Leaf 1");
            Vector<Object> node = new Vector<Object>();
            node.add("Leaf 2");
            node.add("Leaf 3");
            node.add("Leaf 4");
            data.add(node);
            JTree tree = new JTree(data);
            tree.expandRow(1);
            tree.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            return tree;
        }
    }
	-----*/
    /**
     * JTable renderer descriptor
     */
    /*-----
    private class JTableComponentDescriptor extends ComponentDescriptor {

        JTableComponentDescriptor() {
            super("JTable component");
        }

        @Override
        JComponent createComponent() {
            TableModel m = new TableModel() {
                private String columns[] = {"ID", "Name", "Active"};
                private Class classes[] = {Integer.class, String.class, Boolean.class};
                private Object data[][] = {{1001, "Administrator", true}, {1002, "Skipy", true}, {1003, "John Doe", false}};

                public int getRowCount() {
                    return 3;
                }

                public int getColumnCount() {
                    return 3;
                }

                public String getColumnName(int columnIndex) {
                    return columns[columnIndex];
                }

                public Class<?> getColumnClass(int columnIndex) {
                    return classes[columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    return data[rowIndex][columnIndex];
                }

                public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                }

                public void addTableModelListener(TableModelListener l) {
                }

                public void removeTableModelListener(TableModelListener l) {
                }
            };
            JTable table = new JTable(m) {
            };
            table.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            table.getColumnModel().getColumn(1).setMinWidth(100);
            return table;
        }
    }
    -----*/
    
    //<start: new data=03.09.2012>
    /*
    private class KeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent ke) {
        	
        	if (ke.getID() == KeyEvent.KEY_PRESSED) 
        	{
        		
            } 
        	else if (ke.getID() == KeyEvent.KEY_RELEASED) 
        	{
        		//CTRL+Z
            	if ( (ke.getKeyCode() == KeyEvent.VK_Z) && ((ke.getModifiers() & KeyEvent.CTRL_MASK) != 0) )
        		{
        			undoManager.undo();
        		}
            	//CTRL+Y
            	else if ( (ke.getKeyCode() == KeyEvent.VK_Y) && ((ke.getModifiers() & KeyEvent.CTRL_MASK) != 0) )
        		{
        			undoManager.redo();
        		}
        	} 
            else if (ke.getID() == KeyEvent.KEY_TYPED) 
            {
            	
            }
            return false;
        }
    }
    */
   //<end: new data=03.09.2012>
}
