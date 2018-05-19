package sv.gui.supportElements;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.*;

import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.additionalElements.NodeTypes;
import sv.gui.graphElements.Node;
import sv.gui.localization.GuiLocalization;

/**
 *ComponentOptions window
 *
 * @author SAV2
 * @version 1.0.0
 * @since 30.07.2012
 */

public class NodeOptionsMenu extends JDialog implements ActionListener, KeyListener {
	
	/**
	 * last selected index in JCombobox
	 */
	private int lastSelectedIndex;
	
	private ComponentContainer currCompCont;
	
	private ComponentPanel currCompPanel;
	
	private JTabbedPane mainTabbedPane;
	
	private JPanel tabPanel1;
    private JPanel tabPanel2;
    private JPanel tabPanel3;
    
    //-----------------------------------------------Tab:"Main"-----start:
    private JLabel mainParamLabelType;
    private JLabel mainParamLabelU;
    private JLabel mainParamLabelAngle;
    
    private JComboBox parameterType;
    private JTextField parameterU;
    private JTextField parameterAngle;
    
    private JLabel afterLabelU;
    private JLabel afterLabelAngle;
    //-----------------------------------------------Tab:"Main"-----end;
    
    //-----------------------------------------------Tab:"Additional"-----start:
    private JLabel offParamLabelPG;
    private JLabel offParamLabelQG;
    private JLabel offParamLabelPL;
    private JLabel offParamLabelQL;
    
    private JTextField offParameterPG;
    private JTextField offParameterQG;
    private JTextField offParameterPL;
    private JTextField offParameterQL;
    
    private JLabel offAfterLabelPG;
    private JLabel offAfterLabelQG;
    private JLabel offAfterLabelPL;
    private JLabel offAfterLabelQL;
    //-----------------------------------------------Tab:"Additional"-----end;
    
    //-----------------------------------------------Tab:"Calculated"-----start:
    private JLabel calcParamLabelU;
    private JLabel calcParamLabelAngle;
    private JLabel calcParamLabelPG;
    private JLabel calcParamLabelQG;
    
    private JTextField calcParameterU;
    private JTextField calcParameterAngle;
    private JTextField calcParameterPG;
    private JTextField calcParameterQG;
    
    private JLabel calcAfterLabelU;
    private JLabel calcAfterLabelAngle;
    private JLabel calcAfterLabelPG;
    private JLabel calcAfterLabelQG;
    //-----------------------------------------------Tab:"Calculated"-----end;
    
    private JPanel buttonPanel;
    
	private JButton okButton;
    private JButton cancelButton;
    
    

    /**
     * Creates new form Options
     */
    public NodeOptionsMenu(Window parent, Dialog.ModalityType type) {
        super(parent, type);
        initComponents();
        
        addKeyListener(this);
        setFocusable(true);
        
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        parameterType.addActionListener(this);
        
        parameterType.addKeyListener(this);
        parameterU.addKeyListener(this);
        parameterAngle.addKeyListener(this);
        
        offParameterPG.addKeyListener(this);
        offParameterQG.addKeyListener(this);
        offParameterPL.addKeyListener(this);
        offParameterQL.addKeyListener(this);
        
        calcParameterU.addKeyListener(this);
        calcParameterAngle.addKeyListener(this);
        calcParameterPG.addKeyListener(this);
        calcParameterQG.addKeyListener(this);
        
        okButton.addKeyListener(this);
        cancelButton.addKeyListener(this);
        //System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
    }
    
    /**
     * change strings
     */
    public void changeStrings()
    {
    	afterLabelAngle.setText(GuiLocalization.degrees);
    	mainParamLabelType.setText(GuiLocalization.type);
    	mainParamLabelU.setText(GuiLocalization.voltage_lvl);
    	afterLabelU.setText(GuiLocalization.kV);
    	mainParamLabelAngle.setText(GuiLocalization.phase_angle);
    	mainTabbedPane.setTitleAt(0, GuiLocalization.main);
    	offAfterLabelQL.setText(GuiLocalization.VAr);
    	offParamLabelPG.setText(GuiLocalization.active_Power_of_generation);
    	offParamLabelQG.setText(GuiLocalization.reactive_Power_of_generation);
    	offParamLabelPL.setText(GuiLocalization.active_Power_of_consumption);
    	offAfterLabelQG.setText(GuiLocalization.VAr);
    	offAfterLabelPL.setText(GuiLocalization.W);
    	offAfterLabelPG.setText(GuiLocalization.W);
    	offParamLabelQL.setText(GuiLocalization.reactive_Power_of_consumption);
    	mainTabbedPane.setTitleAt(1, GuiLocalization.additional);
    	calcAfterLabelQG.setText(GuiLocalization.VAr);
    	calcParamLabelU.setText(GuiLocalization.voltage_lvl);
    	calcParamLabelAngle.setText(GuiLocalization.phase_angle);
    	calcParamLabelPG.setText(GuiLocalization.active_gen_power);
    	calcAfterLabelAngle.setText(GuiLocalization.degrees);
    	calcAfterLabelPG.setText(GuiLocalization.W);
    	calcAfterLabelU.setText(GuiLocalization.kV);
    	calcParamLabelQG.setText(GuiLocalization.reactive_gen_power);
    	mainTabbedPane.setTitleAt(2, GuiLocalization.calculated);
    	cancelButton.setText(GuiLocalization.cancel);
    }
    
    /**
     * initialization of components
     */
    private void initComponents() {
    	
        GridBagConstraints gridBagConstraints;

        mainTabbedPane = new JTabbedPane();
        
        tabPanel1 = new JPanel();
        tabPanel2 = new JPanel();
        tabPanel3 = new JPanel();
        //-----------------------------------------------Tab:"Main"-----start:
        mainParamLabelType = new JLabel();
        mainParamLabelU = new JLabel();
        mainParamLabelAngle = new JLabel();
        
        parameterType = new JComboBox(new Object[]{"PQ", "PV", "Slack"});
        parameterU = new JTextField();
        parameterAngle = new JTextField();
        
        afterLabelU = new JLabel();
        afterLabelAngle = new JLabel();
        //-----------------------------------------------Tab:"Main"-----end;
        
        //-----------------------------------------------Tab:"Additional"-----start:
        offParamLabelPG = new JLabel();
        offParamLabelQG = new JLabel();
        offParamLabelPL = new JLabel();
        offParamLabelQL = new JLabel();
         
        offParameterPG = new JTextField();
        offParameterQG = new JTextField();
        offParameterPL = new JTextField();
        offParameterQL = new JTextField();
        
        offAfterLabelPG = new JLabel();
        offAfterLabelQG = new JLabel();
        offAfterLabelPL = new JLabel();
        offAfterLabelQL = new JLabel();
        //-----------------------------------------------Tab:"Additional"-----end;
        
        //-----------------------------------------------Tab:"Calculated"-----start:
        calcParamLabelU = new JLabel();
        calcParamLabelAngle = new JLabel();
        calcParamLabelPG = new JLabel();
        calcParamLabelQG = new JLabel();
        
        calcParameterU = new JTextField();
        calcParameterAngle = new JTextField();
        calcParameterPG = new JTextField();
        calcParameterQG = new JTextField();
        
        calcAfterLabelU = new JLabel();
        calcAfterLabelAngle = new JLabel();
        calcAfterLabelPG = new JLabel();
        calcAfterLabelQG = new JLabel();
        //-----------------------------------------------Tab:"Calculated"-----end;
        
        buttonPanel = new JPanel();
        
        okButton = new JButton();
        cancelButton = new JButton();

        //setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); //// --> freeze occurs in browser
        setTitle(GuiLocalization.node_Options);
        //setAlwaysOnTop(true); // --> freeze occurs in browser
        setMinimumSize(new Dimension(400, 300));
        //setModal(true); changed for setModality
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        //setModalityType(Dialog.ModalityType.MODELESS);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        tabPanel1.setLayout(new java.awt.GridBagLayout());

        afterLabelAngle.setText(GuiLocalization.degrees);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel1.add(afterLabelAngle, gridBagConstraints);

        mainParamLabelType.setText(GuiLocalization.type);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel1.add(mainParamLabelType, gridBagConstraints);

        mainParamLabelU.setText(GuiLocalization.voltage_lvl);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel1.add(mainParamLabelU, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel1.add(parameterAngle, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel1.add(parameterType, gridBagConstraints);
        parameterType.setSelectedIndex(0);
        parameterType.setPreferredSize(new Dimension(100, 20));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel1.add(parameterU, gridBagConstraints);

        afterLabelU.setText(GuiLocalization.kV);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel1.add(afterLabelU, gridBagConstraints);

        mainParamLabelAngle.setText(GuiLocalization.phase_angle);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel1.add(mainParamLabelAngle, gridBagConstraints);

        mainTabbedPane.addTab(GuiLocalization.main, tabPanel1);

        tabPanel2.setLayout(new java.awt.GridBagLayout());

        offAfterLabelQL.setText(GuiLocalization.VAr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel2.add(offAfterLabelQL, gridBagConstraints);

        offParamLabelPG.setText(GuiLocalization.active_Power_of_generation);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel2.add(offParamLabelPG, gridBagConstraints);

        offParamLabelQG.setText(GuiLocalization.reactive_Power_of_generation);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel2.add(offParamLabelQG, gridBagConstraints);
        
        offParamLabelPL.setText(GuiLocalization.active_Power_of_consumption);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel2.add(offParamLabelPL, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel2.add(offParameterQL, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel2.add(offParameterPG, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel2.add(offParameterQG, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel2.add(offParameterPL, gridBagConstraints);

        offAfterLabelQG.setText(GuiLocalization.VAr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel2.add(offAfterLabelQG, gridBagConstraints);
        
        offAfterLabelPL.setText(GuiLocalization.W);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel2.add(offAfterLabelPL, gridBagConstraints);

        offAfterLabelPG.setText(GuiLocalization.W);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 8, 16);
        tabPanel2.add(offAfterLabelPG, gridBagConstraints);

        offParamLabelQL.setText(GuiLocalization.reactive_Power_of_consumption);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel2.add(offParamLabelQL, gridBagConstraints);

        mainTabbedPane.addTab(GuiLocalization.additional, tabPanel2);
        
        //--
        tabPanel3.setLayout(new java.awt.GridBagLayout());

        calcAfterLabelQG.setText(GuiLocalization.VAr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelQG, gridBagConstraints);

        calcParamLabelU.setText(GuiLocalization.voltage_lvl);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel3.add(calcParamLabelU, gridBagConstraints);

        calcParamLabelAngle.setText(GuiLocalization.phase_angle);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelAngle, gridBagConstraints);
        
        calcParamLabelPG.setText(GuiLocalization.active_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelPG, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterQG, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel3.add(calcParameterU, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterAngle, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterPG, gridBagConstraints);

        calcAfterLabelAngle.setText(GuiLocalization.degrees);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelAngle, gridBagConstraints);
        
        calcAfterLabelPG.setText(GuiLocalization.W);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelPG, gridBagConstraints);

        calcAfterLabelU.setText(GuiLocalization.kV);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 8, 16);
        tabPanel3.add(calcAfterLabelU, gridBagConstraints);

        calcParamLabelQG.setText(GuiLocalization.reactive_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelQG, gridBagConstraints);

        mainTabbedPane.addTab(GuiLocalization.calculated, tabPanel3);
        //--

        getContentPane().add(mainTabbedPane, java.awt.BorderLayout.CENTER);

        buttonPanel.setRequestFocusEnabled(false);
        buttonPanel.setLayout(new java.awt.GridBagLayout());

        okButton.setText("OK");
        okButton.setPreferredSize(new Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        buttonPanel.add(okButton, gridBagConstraints);

        cancelButton.setText(GuiLocalization.cancel);
        cancelButton.setPreferredSize(new Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        buttonPanel.add(cancelButton, gridBagConstraints);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        
        calcParameterU.setEditable(false);
        calcParameterAngle.setEditable(false);
        calcParameterPG.setEditable(false);
        calcParameterQG.setEditable(false);
        
        //pack();
       
    } 
    
    /**
     * Set reference to current Component Container
     * @param currCompCont : ComponentContainer
     */
    public void setCurrentCompCont(ComponentContainer currCompCont)
    {
    	this.currCompCont = currCompCont;
    }
    
    /**
     * show data of current component
     * @param compPan current selected component (Node)
     */
    public void representData(ComponentPanel compPan)
    {
    	this.currCompPanel = compPan;
    	
    	this.setTitle(GuiLocalization.node+": "+ ((Node)currCompPanel.getDelegate()).getID() +" "+GuiLocalization.options);
    	
    	if (currCompPanel.getDelegate() instanceof Node)
    	{
    		parameterType.setSelectedIndex( Integer.parseInt( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_Type) )-1 );
    		parameterU.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_U) );
    		parameterAngle.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_Angle) );
    		offParameterPG.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_PG) );
    		offParameterQG.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_QG) );
    		offParameterPL.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_PL) );
    		offParameterQL.setText( ((Node)currCompPanel.getDelegate()).getParameter(NodeParameters.Data_QL) );
    		calcParameterU.setText( ((Node)currCompPanel.getDelegate()).getResult(NodeResults.Res_U) );
    		calcParameterAngle.setText( ((Node)currCompPanel.getDelegate()).getResult(NodeResults.Res_Angle) );
    		calcParameterPG.setText( ((Node)currCompPanel.getDelegate()).getResult(NodeResults.Res_PG) );
    		calcParameterQG.setText( ((Node)currCompPanel.getDelegate()).getResult(NodeResults.Res_QG) );
    	}
    	
    	//focus last selected index of selection list (parameterType)
    	lastSelectedIndex = this.parameterType.getSelectedIndex();
    }
    
    /**
     * change data of current component.
     * <br />
     * For "OK" button usage.
     */
    private void changeData()
    {
    	if ( !(this.currCompPanel==null) && (currCompPanel.getDelegate() instanceof Node) )
    	{
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_Type, Integer.toString(parameterType.getSelectedIndex()+1));
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_U, parameterU.getText());
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_Angle, parameterAngle.getText());
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_PG, offParameterPG.getText());
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_QG, offParameterQG.getText());
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_PL, offParameterPL.getText());
    		((Node)currCompPanel.getDelegate()).setParameter(NodeParameters.Data_QL, offParameterQL.getText());
    		
    		currCompPanel.repaint();
    	}
    }
    
    public void refresh()
    {
    	this.mainTabbedPane.setSelectedIndex(0);
    }
    
    //button actions
    @Override
    public void actionPerformed(ActionEvent e)
    {
    	String s = e.getActionCommand();

    	if (s.equals("OK"))
    	{
    		try
    		{
    			this.changeData();
    			this.dispose();
    			currCompPanel.requestFocusInWindow();
    			//System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
    		}
    		catch (NumberFormatException exc)
    		{
    			JOptionPane.showMessageDialog(this,
  					  						  exc.toString()+'\n'+GuiLocalization.you_have_entered_value_using_wrong_number_format,
  					  						  GuiLocalization.wrong_number_format,
  					  						  JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	else if(s.equals(GuiLocalization.cancel))
    	{
    		this.dispose();
    		currCompPanel.requestFocusInWindow();
    	}
    	else if (s.equals("comboBoxChanged"))
    	{	
    		
    		if( ((JComboBox) e.getSource()).getSelectedItem().equals("PQ") )
    		{
    			this.offParameterPG.setText("0.0");
    			this.offParameterQG.setText("0.0");
    			this.offParamLabelPG.setEnabled(false);
    			this.offParamLabelQG.setEnabled(false);
    			this.offParameterPG.setEnabled(false);
    			this.offParameterQG.setEnabled(false);
    			this.offAfterLabelPG.setEnabled(false);
    			this.offAfterLabelQG.setEnabled(false);
    			//---------------------------------------------------------------
    			this.offParamLabelPL.setEnabled(true);
    			this.offParamLabelQL.setEnabled(true);
    			this.offParameterPL.setEnabled(true);
    			this.offParameterQL.setEnabled(true);
    			this.offAfterLabelPL.setEnabled(true);
    			this.offAfterLabelQL.setEnabled(true);
    		}
    		else if( ((JComboBox) e.getSource()).getSelectedItem().equals("PV") )
    		{
    			this.offParameterPL.setText("0.0");
    			this.offParameterQL.setText("0.0");
    			this.offParamLabelPL.setEnabled(false);
    			this.offParamLabelQL.setEnabled(false);
    			this.offParameterPL.setEnabled(false);
    			this.offParameterQL.setEnabled(false);
    			this.offAfterLabelPL.setEnabled(false);
    			this.offAfterLabelQL.setEnabled(false);
    			//---------------------------------------------------------------
    			this.offParamLabelPG.setEnabled(true);
    			this.offParamLabelQG.setEnabled(true);
    			this.offParameterPG.setEnabled(true);
    			this.offParameterQG.setEnabled(true);
    			this.offAfterLabelPG.setEnabled(true);
    			this.offAfterLabelQG.setEnabled(true);
    		}
    		else if( ((JComboBox) e.getSource()).getSelectedItem().equals("Slack") )
    		{
    			//check out already existing Node of type "Slack"
    			for(ComponentPanel comp: currCompCont.getComps())
    			{
    				if (comp.getDelegate() instanceof Node)
    				{
    					if(
    						( Integer.parseInt(((Node) comp.getDelegate()).getParameter(NodeParameters.Data_Type)) == NodeTypes.Slack )
    						&&
    						( !comp.equals(currCompPanel) )
    					  )
    					{
    						JOptionPane.showMessageDialog(this,
			  						  					  GuiLocalization.there_is_another_Node_Node+ ((Node) comp.getDelegate()).getID() +GuiLocalization.that_already_has_such_type,
			  						  					  GuiLocalization.excess_Node_of_type_Slack,
			  						  					  JOptionPane.WARNING_MESSAGE);
    						
    						this.parameterType.setSelectedIndex(lastSelectedIndex);
    						return;
    					}
    				}
    			}
    			
    			this.offParameterPG.setText("0.0");
    			this.offParameterQG.setText("0.0");
    			this.offParamLabelPG.setEnabled(false);
    			this.offParamLabelQG.setEnabled(false);
    			this.offParameterPG.setEnabled(false);
    			this.offParameterQG.setEnabled(false);
    			this.offAfterLabelPG.setEnabled(false);
    			this.offAfterLabelQG.setEnabled(false);
    			//---------------------------------------------------------------
    			this.offParameterPL.setText("0.0");
    			this.offParameterQL.setText("0.0");
    			this.offParamLabelPL.setEnabled(false);
    			this.offParamLabelQL.setEnabled(false);
    			this.offParameterPL.setEnabled(false);
    			this.offParameterQL.setEnabled(false);
    			this.offAfterLabelPL.setEnabled(false);
    			this.offAfterLabelQL.setEnabled(false);
    		}
    	}
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
	public void keyReleased(KeyEvent ke)
	{
		int key = ke.getKeyCode();
		
		switch (key)
		{
		case KeyEvent.VK_ENTER :
			try
    		{
    			this.changeData();
    			this.dispose();
    			currCompPanel.requestFocusInWindow();
    			//System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
    		}
    		catch (NumberFormatException exc)
    		{
    			JOptionPane.showMessageDialog(this,
  					  						  exc.toString()+'\n'+GuiLocalization.you_have_entered_value_using_wrong_number_format,
  					  						  GuiLocalization.wrong_number_format,
  					  						  JOptionPane.ERROR_MESSAGE);
    		}
			break;
		case KeyEvent.VK_ESCAPE :
			this.dispose();
    		currCompPanel.requestFocusInWindow();
			break;
		}	
	}
}

