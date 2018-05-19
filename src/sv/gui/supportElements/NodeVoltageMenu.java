package sv.gui.supportElements;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sv.editor.ComponentPanel;
import sv.gui.graphElements.Node;
import sv.gui.localization.GuiLocalization;

public class NodeVoltageMenu extends JDialog implements ActionListener, KeyListener
{
	private ComponentPanel currCompPanel;
	
	//------------------------------------------------------------------->
	private JPanel contentPane;
	
	private JComboBox valueType;
    private JComboBox voltageClass;
    
    private JLabel valueTypeLabel;
    private JLabel voltageClasslabel;
    private JLabel maxValueLabel;
    private JLabel minValueLabel;
    private JLabel maxValueAfterLabel;
    private JLabel minValueAfterLabel;
    
    private JSlider maxValue;
    private JSlider minValue;
    
    private JTextField textMaxValue;
    private JTextField textMinValue;
    
    private JButton buttonOK;
    private JButton buttonCancel;
    
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    //------------------------------------------------------------------->
    
	/**
     * Creates new form Options
     */
    public NodeVoltageMenu(Window parent, Dialog.ModalityType type)
    {
        super(parent, type);
        initComponents();
        
        addKeyListener(this);
        setFocusable(true);
        
        voltageClass.addActionListener(this);
        
        maxValue.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if( voltageClass.getSelectedItem().equals("110") ||
					voltageClass.getSelectedItem().equals("220") )
				{
					textMaxValue.setText( Integer.toString( ((JSlider) e.getSource()).getValue() ) );
				}
				else if( voltageClass.getSelectedItem().equals("10") ||
						 voltageClass.getSelectedItem().equals("35") )
				{
					textMaxValue.setText( Double.toString( ((JSlider) e.getSource()).getValue()/10.00 ) );
				}
				else if( voltageClass.getSelectedItem().equals("0.4") )
				{
					textMaxValue.setText( Double.toString( ((JSlider) e.getSource()).getValue()/100.00 ) );
				}
			}
		});
        
        minValue.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
					if( voltageClass.getSelectedItem().equals("110") ||
						voltageClass.getSelectedItem().equals("220") )
					{
						textMinValue.setText( Integer.toString( ((JSlider) e.getSource()).getValue() ) );
					}
					else if( voltageClass.getSelectedItem().equals("10") ||
							 voltageClass.getSelectedItem().equals("35") )
					{
						textMinValue.setText( Double.toString( ((JSlider) e.getSource()).getValue()/10.00 ) );
					}
					else if( voltageClass.getSelectedItem().equals("0.4") )
					{
						textMinValue.setText( Double.toString( ((JSlider) e.getSource()).getValue()/100.00 ) );
					}
			}
		});
        
        buttonCancel.addActionListener(this);
        buttonOK.addActionListener(this);
        
        valueType.addKeyListener(this);
        voltageClass.addKeyListener(this);
        
        maxValue.addKeyListener(this);
        minValue.addKeyListener(this);
        
        textMaxValue.addKeyListener(this);
        textMinValue.addKeyListener(this);
        
        buttonOK.addKeyListener(this);
        buttonCancel.addKeyListener(this);
    }
	
    public void changeStrings()
    {
    	valueTypeLabel.setText(GuiLocalization.diagram);
    	voltageClasslabel.setText(GuiLocalization.vol_class);
    	maxValueLabel.setText(GuiLocalization.max_value);
    	minValueLabel.setText(GuiLocalization.min_value);
    	valueType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { GuiLocalization.numeric, GuiLocalization.percentage }));
    	buttonCancel.setText(GuiLocalization.cancel);
    	maxValueAfterLabel.setText(GuiLocalization.kV);
    	minValueAfterLabel.setText(GuiLocalization.kV);
    }
    
    /**
     * initialization of components
     */
    private void initComponents()
    {
    	GridBagConstraints gridBagConstraints;

        valueTypeLabel = new JLabel();
        voltageClasslabel = new JLabel();
        maxValueLabel = new JLabel();
        minValueLabel = new JLabel();
        maxValueAfterLabel = new JLabel();
        minValueAfterLabel = new JLabel();
        
        valueType = new JComboBox();
        voltageClass = new JComboBox();
        
        maxValue = new JSlider();
        minValue = new JSlider();
        
        textMaxValue = new JTextField();
        textMinValue = new JTextField();
        
        buttonOK = new JButton();
        buttonCancel = new JButton();
        
        jSeparator1 = new JSeparator();
        jSeparator2 = new JSeparator();
    	
    	contentPane = new JPanel();
    	this.setContentPane(contentPane);
    	
        this.setTitle(GuiLocalization.node_voltage_diagram_options);
        setMinimumSize(new Dimension(325, 225));
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        setResizable(false);
        getContentPane().setLayout(new GridBagLayout());
        
        valueTypeLabel.setText(GuiLocalization.diagram);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 8, 8);
        add(valueTypeLabel, gridBagConstraints);

        voltageClasslabel.setText(GuiLocalization.vol_class);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 8);
        add(voltageClasslabel, gridBagConstraints);

        maxValueLabel.setText(GuiLocalization.max_value);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 8);
        add(maxValueLabel, gridBagConstraints);

        minValueLabel.setText(GuiLocalization.min_value);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 10, 8);
        add(minValueLabel, gridBagConstraints);

        valueType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { GuiLocalization.numeric, GuiLocalization.percentage }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 5);
        add(valueType, gridBagConstraints);

        voltageClass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.4", "10", "35", "110", "220" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(voltageClass, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 3);
        add(maxValue, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 2, 3);
        add(minValue, gridBagConstraints);

        textMaxValue.setMinimumSize(new java.awt.Dimension(75, 20));
        textMaxValue.setPreferredSize(new java.awt.Dimension(75, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 4);
        add(textMaxValue, gridBagConstraints);

        textMinValue.setMinimumSize(new java.awt.Dimension(75, 20));
        textMinValue.setPreferredSize(new java.awt.Dimension(75, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 7, 4);
        add(textMinValue, gridBagConstraints);

        buttonOK.setText("OK");
        buttonOK.setMaximumSize(new java.awt.Dimension(80, 26));
        buttonOK.setMinimumSize(new java.awt.Dimension(80, 26));
        buttonOK.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 60);
        add(buttonOK, gridBagConstraints);

        buttonCancel.setText(GuiLocalization.cancel);
        buttonCancel.setMaximumSize(new java.awt.Dimension(80, 26));
        buttonCancel.setMinimumSize(new java.awt.Dimension(80, 26));
        buttonCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 5);
        add(buttonCancel, gridBagConstraints);

        jSeparator1.setPreferredSize(new java.awt.Dimension(6, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jSeparator1, gridBagConstraints);

        jSeparator2.setPreferredSize(new java.awt.Dimension(6, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jSeparator2, gridBagConstraints);

        maxValueAfterLabel.setText(GuiLocalization.kV);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 8, 8);
        add(maxValueAfterLabel, gridBagConstraints);

        minValueAfterLabel.setText(GuiLocalization.kV);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 2, 9, 9);
        add(minValueAfterLabel, gridBagConstraints);
        
        textMaxValue.setEditable(false);
        textMinValue.setEditable(false);
        
    }
    
    /**
     * show voltage diagram options of current component
     * @param compPan : current selected component (Node)
     */
    public void representData(ComponentPanel compPan)
    {
    	this.currCompPanel = compPan;
    	
    	this.setTitle(GuiLocalization.node+": "+ ((Node)currCompPanel.getDelegate()).getID() +" "+GuiLocalization.voltage_diagram_options);
    	
    	if (currCompPanel.getDelegate() instanceof Node)
    	{
    		valueType.setSelectedIndex( ((Node)currCompPanel.getDelegate()).getVolDiagramType() );
    		
    		//----------------------------------------------------------------------------->
    		//class 0.40
    		if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 0.40 )
    		{
    			voltageClass.setSelectedIndex(0);
    		}
    		//class 10.00
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 10.00 )
    		{
    			voltageClass.setSelectedIndex(1);
    		}
    		//class 35.00
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 35.00 )
    		{
    			voltageClass.setSelectedIndex(2);
    		}
    		//class 110.00
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 110.00 )
    		{
    			voltageClass.setSelectedIndex(3);
    		}
    		//class 220.00
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 220.00 )
    		{
    			voltageClass.setSelectedIndex(4);
    		}
    		//----------------------------------------------------------------------------->
    		//class 110.00 or 220.00
    		if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 110.00
    			|| 
    			((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 220.00 )
    		{
    			maxValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMax()) );
    			minValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMin()) );
    		}
    		//class 10.00 or 35.00
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 10.00
    				 || 
    				 ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 35.00 )
    		{
    			maxValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMax()*10.00) );
    			minValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMin()*10.00) );
    		}
    		//class 0.40 
    		else if( ((Node)currCompPanel.getDelegate()).getVolDiagramClass() == 0.40 )
    		{
    			maxValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMax()*100.00) );
    			minValue.setValue( (int)Math.round(((Node)currCompPanel.getDelegate()).getVolDiagramMin()*100.00) );
    		}
    	}
    }
    
    /**
     * change voltage diagram options of current component.
     * <br />
     * For "OK" button usage.
     */
    private void changeData()
    {
    	if ( (this.currCompPanel!=null) && (currCompPanel.getDelegate() instanceof Node) )
    	{
    		((Node)currCompPanel.getDelegate()).setVolDiagramType(valueType.getSelectedIndex());
    		((Node)currCompPanel.getDelegate()).setVolDiagramClass(voltageClass.getSelectedItem().toString());
    		((Node)currCompPanel.getDelegate()).setVolDiagramMax(textMaxValue.getText());
    		((Node)currCompPanel.getDelegate()).setVolDiagramMin(textMinValue.getText());
    		
    		currCompPanel.repaint();
    	}
    }
    
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();
		
		//set maximum and minimum for maxValue and minValue
		if (s.equals("comboBoxChanged"))
		{
    		//class 0.40
			if( ((JComboBox) e.getSource()).getSelectedItem().equals("0.4") )
			{
				maxValue.setMaximum(44);
    			maxValue.setMinimum(41);
    			maxValue.setValue(44);
    			
    			minValue.setMaximum(39);
    			minValue.setMinimum(36);
    			minValue.setValue(36);
			}
			//class 10.00
			else if( ((JComboBox) e.getSource()).getSelectedItem().equals("10") )
			{
				maxValue.setMaximum(120);
    			maxValue.setMinimum(105);
    			maxValue.setValue(120);
    			
    			minValue.setMaximum(95);
    			minValue.setMinimum(80);
    			minValue.setValue(80);
			}
			//class 35.00
			else if( ((JComboBox) e.getSource()).getSelectedItem().equals("35") )
			{
				maxValue.setMaximum(405);
    			maxValue.setMinimum(360);
    			maxValue.setValue(405);
    			
    			minValue.setMaximum(340);
    			minValue.setMinimum(295);
    			minValue.setValue(295);
			}
			//class 110.00
			else if( ((JComboBox) e.getSource()).getSelectedItem().equals("110") )
			{
				maxValue.setMaximum(126);
    			maxValue.setMinimum(115);
    			maxValue.setValue(126);
    			
    			minValue.setMaximum(105);
    			minValue.setMinimum(94);
    			minValue.setValue(94);
			}
			//class 220.00
			else if( ((JComboBox) e.getSource()).getSelectedItem().equals("220") )
			{
				maxValue.setMaximum(252);
    			maxValue.setMinimum(230);
    			maxValue.setValue(252);
    			
    			minValue.setMaximum(210);
    			minValue.setMinimum(188);
    			minValue.setValue(188);
			}
		}
		//"OK" button
		else if (s.equals("OK"))
    	{
    		this.changeData();
    		this.dispose();
    		currCompPanel.requestFocusInWindow();
    		currCompPanel.getParent().repaint();
    	}
		//"Cancel" button
		else if(s.equals(GuiLocalization.cancel))
    	{
    		this.dispose();
    		currCompPanel.requestFocusInWindow();
    		currCompPanel.getParent().repaint();
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
			this.changeData();
    		this.dispose();
    		currCompPanel.requestFocusInWindow();
    		currCompPanel.getParent().repaint();
			break;
		case KeyEvent.VK_ESCAPE :
			this.dispose();
    		currCompPanel.requestFocusInWindow();
    		currCompPanel.getParent().repaint();
			break;
		}	
	}

}
