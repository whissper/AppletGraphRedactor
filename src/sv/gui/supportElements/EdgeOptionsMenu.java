package sv.gui.supportElements;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.*;

import javax.swing.JDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sv.editor.ComponentPanel;
import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodesOfEdge;
import sv.gui.graphElements.Edge;
import sv.gui.localization.GuiLocalization;

/**
 *ComponentOptions window
 *
 * @author SAV2
 * @version 1.0.0
 * @since 30.07.2012
 */

public class EdgeOptionsMenu extends JDialog implements ActionListener, KeyListener {
	
	private ComponentPanel currCompPanel;
	
	private JTabbedPane mainTabbedPane;
	
	private JPanel tabPanel1;
    private JPanel tabPanel2;
    private JPanel tabPanel3;
    
    //-----------------------------------------------Tab:"Main"-----start:
    private JLabel mainParamLabelR;
    private JLabel mainParamLabelX;
    private JLabel mainParamLabelTap;
    
    private JTextField parameterR;
    private JTextField parameterX;
    private JTextField parameterTap;
    
    private JLabel afterLabelR;
    private JLabel afterLabelX;
    //-----------------------------------------------Tab:"Main"-----end;
    
    //-----------------------------------------------Tab:"Additional"-----start:
    private JLabel offParamLabelFrom;
    private JLabel offParamLabelTo;
    
    private JTextField offParameterFrom;
    private JTextField offParameterTo;
    //-----------------------------------------------Tab:"Additional"-----end;
    
    //-----------------------------------------------Tab:"Calculated"-----start:
    private JLabel calcParamLabelPF;
    private JLabel calcParamLabelQF;
    private JLabel calcParamLabelPT;
    private JLabel calcParamLabelQT;
    
    private JTextField calcParameterPF;
    private JTextField calcParameterQF;
    private JTextField calcParameterPT;
    private JTextField calcParameterQT;
    
    private JLabel calcAfterLabelPF;
    private JLabel calcAfterLabelQF;
    private JLabel calcAfterLabelPT;
    private JLabel calcAfterLabelQT;
    //-----------------------------------------------Tab:"Calculated"-----end;
    
    private JPanel buttonPanel;
    
	private JButton okButton;
    private JButton cancelButton;
    
    

    /**
     * Creates new form Options
     */
    public EdgeOptionsMenu(Window parent, Dialog.ModalityType type) {
        super(parent, type);
        
        initComponents();
        
        addKeyListener(this);
        setFocusable(true);
        
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        parameterR.addKeyListener(this);
        parameterX.addKeyListener(this);
        parameterTap.addKeyListener(this);
        
        offParameterFrom.addKeyListener(this);
        offParameterTo.addKeyListener(this);
        
        calcParameterPF.addKeyListener(this);
        calcParameterQF.addKeyListener(this);
        calcParameterPT.addKeyListener(this);
        calcParameterQT.addKeyListener(this);
        
        okButton.addKeyListener(this);
        cancelButton.addKeyListener(this);
    }
    
    /**
     * change Strings
     */
    public void changeStrings()
    {
    	mainParamLabelR.setText(GuiLocalization.resistance);
    	mainParamLabelX.setText(GuiLocalization.reactance);
    	mainParamLabelTap.setText(GuiLocalization.transformation_rate);
    	afterLabelR.setText(GuiLocalization.Ohm);
    	afterLabelX.setText(GuiLocalization.Ohm);
    	mainTabbedPane.setTitleAt(0, GuiLocalization.main);
    	offParamLabelFrom.setText(GuiLocalization.from_Node);
    	offParamLabelTo.setText(GuiLocalization.to_Node);
    	mainTabbedPane.setTitleAt(1, GuiLocalization.additional);
    	calcAfterLabelQT.setText(GuiLocalization.MVAr);
    	calcParamLabelPF.setText(GuiLocalization.act_pwr_flow_begin);
    	calcParamLabelQF.setText(GuiLocalization.react_pwr_flow_begin);
    	calcParamLabelPT.setText(GuiLocalization.act_pwr_flow_end);
    	calcAfterLabelQF.setText(GuiLocalization.MVAr);
    	calcAfterLabelPT.setText(GuiLocalization.MW);
    	calcAfterLabelPF.setText(GuiLocalization.MW);
    	calcParamLabelQT.setText(GuiLocalization.react_pwr_flow_end);
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
        mainParamLabelR = new JLabel();
        mainParamLabelX = new JLabel();
        mainParamLabelTap = new JLabel();
        
        parameterR = new JTextField();
        parameterX = new JTextField();
        parameterTap = new JTextField();
        
        afterLabelR = new JLabel();
        afterLabelX = new JLabel();
        //-----------------------------------------------Tab:"Main"-----end;
        
        //-----------------------------------------------Tab:"Additional"-----start:
        offParamLabelFrom = new JLabel();
        offParamLabelTo = new JLabel();
         
        offParameterFrom = new JTextField();
        offParameterTo = new JTextField();
        //-----------------------------------------------Tab:"Additional"-----end;
        
        //-----------------------------------------------Tab:"Calculated"-----start:
        calcParamLabelPF = new JLabel();
        calcParamLabelQF = new JLabel();
        calcParamLabelPT = new JLabel();
        calcParamLabelQT = new JLabel();
        
        calcParameterPF = new JTextField();
        calcParameterQF = new JTextField();
        calcParameterPT = new JTextField();
        calcParameterQT = new JTextField();
        
        calcAfterLabelPF = new JLabel();
        calcAfterLabelQF = new JLabel();
        calcAfterLabelPT = new JLabel();
        calcAfterLabelQT = new JLabel();
        //-----------------------------------------------Tab:"Calculated"-----end;
        
        buttonPanel = new JPanel();
        
        okButton = new JButton();
        cancelButton = new JButton();
        
        //setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); //// --> freeze occurs in browser
        setTitle(GuiLocalization.edge_Options);
        //setAlwaysOnTop(true); // --> freeze occurs in browser
        setMinimumSize(new Dimension(400, 300));
        //setModal(true); changed for setModality
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        //setModalityType(Dialog.ModalityType.MODELESS);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        tabPanel1.setLayout(new java.awt.GridBagLayout());


        mainParamLabelR.setText(GuiLocalization.resistance);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel1.add(mainParamLabelR, gridBagConstraints);
        
        mainParamLabelX.setText(GuiLocalization.reactance);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel1.add(mainParamLabelX, gridBagConstraints);

        mainParamLabelTap.setText(GuiLocalization.transformation_rate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel1.add(mainParamLabelTap, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel1.add(parameterR, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel1.add(parameterX, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel1.add(parameterTap, gridBagConstraints);

        afterLabelR.setText(GuiLocalization.Ohm);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 8, 16);
        tabPanel1.add(afterLabelR, gridBagConstraints);
        
        afterLabelX.setText(GuiLocalization.Ohm);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel1.add(afterLabelX, gridBagConstraints);

        mainTabbedPane.addTab(GuiLocalization.main, tabPanel1);

        tabPanel2.setLayout(new java.awt.GridBagLayout());

        offParamLabelFrom.setText(GuiLocalization.from_Node);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel2.add(offParamLabelFrom, gridBagConstraints);

        offParamLabelTo.setText(GuiLocalization.to_Node);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel2.add(offParamLabelTo, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel2.add(offParameterFrom, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel2.add(offParameterTo, gridBagConstraints);

        mainTabbedPane.addTab(GuiLocalization.additional, tabPanel2);
        
        //--
        tabPanel3.setLayout(new java.awt.GridBagLayout());

        calcAfterLabelQT.setText(GuiLocalization.MVAr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelQT, gridBagConstraints);

        calcParamLabelPF.setText(GuiLocalization.act_pwr_flow_begin);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 8, 8);
        tabPanel3.add(calcParamLabelPF, gridBagConstraints);

        calcParamLabelQF.setText(GuiLocalization.react_pwr_flow_begin);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelQF, gridBagConstraints);
        
        calcParamLabelPT.setText(GuiLocalization.act_pwr_flow_end);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelPT, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterQT, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        tabPanel3.add(calcParameterPF, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterQF, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        tabPanel3.add(calcParameterPT, gridBagConstraints);

        calcAfterLabelQF.setText(GuiLocalization.MVAr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelQF, gridBagConstraints);
        
        calcAfterLabelPT.setText(GuiLocalization.MW);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 16);
        tabPanel3.add(calcAfterLabelPT, gridBagConstraints);

        calcAfterLabelPF.setText(GuiLocalization.MW);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 8, 16);
        tabPanel3.add(calcAfterLabelPF, gridBagConstraints);

        calcParamLabelQT.setText(GuiLocalization.react_pwr_flow_end);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 8, 8);
        tabPanel3.add(calcParamLabelQT, gridBagConstraints);

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

        offParameterFrom.setEditable(false);
        offParameterTo.setEditable(false);
        //parameterTap.setEditable(false);
        
        calcParameterPF.setEditable(false);
        calcParameterQF.setEditable(false);
        calcParameterPT.setEditable(false);
        calcParameterQT.setEditable(false);
        
        //pack();
    }
    
    /**
     * show data of current component
     * @param compPan current selected component (Edge)
     */
    public void representData(ComponentPanel compPan)
    {
    	this.currCompPanel = compPan;
    	
    	this.setTitle(GuiLocalization.edge+": "+ ((Edge)currCompPanel.getDelegate()).getID() +" "+GuiLocalization.options);
    	
    	if (currCompPanel.getDelegate() instanceof Edge)
    	{
    		parameterR.setText( ((Edge)currCompPanel.getDelegate()).getParameter(EdgeParameters.Data_R) );
    		parameterX.setText( ((Edge)currCompPanel.getDelegate()).getParameter(EdgeParameters.Data_X) );
    		parameterTap.setText( ((Edge)currCompPanel.getDelegate()).getParameter(EdgeParameters.Data_Tap) );
    		offParameterFrom.setText( Integer.toString( ((Edge)currCompPanel.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID() ) );
    		offParameterTo.setText( Integer.toString( ((Edge)currCompPanel.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() ) );
    		calcParameterPF.setText( ((Edge)currCompPanel.getDelegate()).getResult(EdgeResults.PF) );
    		calcParameterQF.setText( ((Edge)currCompPanel.getDelegate()).getResult(EdgeResults.QF) );
    		calcParameterPT.setText( ((Edge)currCompPanel.getDelegate()).getResult(EdgeResults.PT) );
    		calcParameterQT.setText( ((Edge)currCompPanel.getDelegate()).getResult(EdgeResults.QT) );
    	}
    }
    
    /**
     * change data of current component.
     * <br />
     * For "OK" button usage.
     */
    private void changeData()
    {
    	if ( !(this.currCompPanel==null) && (currCompPanel.getDelegate() instanceof Edge) )
    	{
    		((Edge)currCompPanel.getDelegate()).setParameter(EdgeParameters.Data_R, parameterR.getText());
    		((Edge)currCompPanel.getDelegate()).setParameter(EdgeParameters.Data_X, parameterX.getText());
    		((Edge)currCompPanel.getDelegate()).setParameter(EdgeParameters.Data_Tap, parameterTap.getText());
    		
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


