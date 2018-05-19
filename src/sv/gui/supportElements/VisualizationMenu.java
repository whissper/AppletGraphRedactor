package sv.gui.supportElements;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import sv.editor.AppGraphRedactor;
import sv.editor.ComponentContainer;
import sv.gui.localization.GuiLocalization;

public class VisualizationMenu extends JDialog implements ItemListener
{
	private AppGraphRedactor appGraphRedactor;
	private ComponentContainer currentCc;
	
	//------------------------------------------------------>
	private JPanel contentPane;
	
	private JCheckBox chBoxType;
    private JCheckBox chBoxCalcAGenPow;
    private JCheckBox chBoxCalcRGenPow;
    private JCheckBox chBoxPhAngleDgrm;
    private JCheckBox chBoxVolLvlDgrm;
    private JCheckBox chBoxVolLvl;
    private JCheckBox chBoxPhAngle;
    private JCheckBox chBoxAGenPow;
    private JCheckBox chBoxRGenPow;
    private JCheckBox chBoxAConsPow;
    private JCheckBox chBoxRConsPow;
    private JCheckBox chBoxCalcVolLvl;
    private JCheckBox chBoxCalcPhAngle;
    
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JSeparator jSeparator3;
    
    private JButton closeButton;
    //------------------------------------------------------>
    
    /**
     * Creates new form Options
     */
    public VisualizationMenu(Window parent, Dialog.ModalityType type, AppGraphRedactor apGrRed)
    {
        super(parent, type);
        this.appGraphRedactor = apGrRed;
        initComponents();
        
        chBoxType.addItemListener(this);
        chBoxCalcAGenPow.addItemListener(this);
        chBoxCalcRGenPow.addItemListener(this);
        chBoxPhAngleDgrm.addItemListener(this);
        chBoxVolLvlDgrm.addItemListener(this);
        chBoxVolLvl.addItemListener(this);
        chBoxPhAngle.addItemListener(this);
        chBoxAGenPow.addItemListener(this);
        chBoxRGenPow.addItemListener(this);
        chBoxAConsPow.addItemListener(this);
        chBoxRConsPow.addItemListener(this);
        chBoxCalcVolLvl.addItemListener(this);
        chBoxCalcPhAngle.addItemListener(this);
        
        closeButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				VisualizationMenu.this.dispose();
				currentCc.requestFocusInWindow();
			}
		});
    }
    
    /**
     * change strings
     */
    public void changeStrings()
    {
    	this.setTitle(GuiLocalization.visualization_settings);
    	chBoxType.setText(GuiLocalization.type);
    	chBoxVolLvl.setText(GuiLocalization.voltage_lvl);
    	chBoxPhAngle.setText(GuiLocalization.phase_angle);
    	chBoxAGenPow.setText(GuiLocalization.active_gen_power);
    	chBoxRGenPow.setText(GuiLocalization.reactive_gen_power);
    	chBoxAConsPow.setText(GuiLocalization.active_cons_power);
    	chBoxRConsPow.setText(GuiLocalization.reactive_cons_power);
    	chBoxCalcVolLvl.setText(GuiLocalization.calc+GuiLocalization.voltage_lvl);
    	chBoxCalcPhAngle.setText(GuiLocalization.calc+GuiLocalization.phase_angle);
    	chBoxCalcAGenPow.setText(GuiLocalization.calc+GuiLocalization.active_gen_power);
    	chBoxCalcRGenPow.setText(GuiLocalization.calc+GuiLocalization.reactive_gen_power);
    	chBoxPhAngleDgrm.setText(GuiLocalization.phase_angle_diagram);
    	chBoxVolLvlDgrm.setText(GuiLocalization.voltage_lvl_diagram);
    	jLabel1.setText(GuiLocalization.current_parameters+":");
    	jLabel2.setText(GuiLocalization.calculated_parameters+":");
    	jLabel3.setText(GuiLocalization.diagrams+":");
    	closeButton.setText(GuiLocalization.close);
    }
    
    /**
     * initialization of components
     */
    private void initComponents()
    {
    	GridBagConstraints gridBagConstraints;
    	
    	chBoxType = new JCheckBox();
        chBoxCalcAGenPow = new JCheckBox();
        chBoxCalcRGenPow = new JCheckBox();
        chBoxPhAngleDgrm = new JCheckBox();
        chBoxVolLvlDgrm = new JCheckBox();
        chBoxVolLvl = new JCheckBox();
        chBoxPhAngle = new JCheckBox();
        chBoxAGenPow = new JCheckBox();
        chBoxRGenPow = new JCheckBox();
        chBoxAConsPow = new JCheckBox();
        chBoxRConsPow = new JCheckBox();
        chBoxCalcVolLvl = new JCheckBox();
        chBoxCalcPhAngle = new JCheckBox();
        
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        
        jSeparator1 = new JSeparator();
        jSeparator2 = new JSeparator();
        jSeparator3 = new JSeparator();
        
        closeButton = new JButton();
        
    	contentPane = new JPanel();
    	this.setContentPane(contentPane);
    	
        this.setTitle(GuiLocalization.visualization_settings);
        setMinimumSize(new Dimension(380, 350));
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        setResizable(false);
        getContentPane().setLayout(new GridBagLayout());
        
        chBoxType.setText(GuiLocalization.type);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxType, gridBagConstraints);

        chBoxVolLvl.setText(GuiLocalization.voltage_lvl);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxVolLvl, gridBagConstraints);

        chBoxPhAngle.setText(GuiLocalization.phase_angle);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxPhAngle, gridBagConstraints);

        chBoxAGenPow.setText(GuiLocalization.active_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(chBoxAGenPow, gridBagConstraints);

        chBoxRGenPow.setText(GuiLocalization.reactive_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(chBoxRGenPow, gridBagConstraints);

        chBoxAConsPow.setText(GuiLocalization.active_cons_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(chBoxAConsPow, gridBagConstraints);

        chBoxRConsPow.setText(GuiLocalization.reactive_cons_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(chBoxRConsPow, gridBagConstraints);

        chBoxCalcVolLvl.setText(GuiLocalization.calc+GuiLocalization.voltage_lvl);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxCalcVolLvl, gridBagConstraints);

        chBoxCalcPhAngle.setText(GuiLocalization.calc+GuiLocalization.phase_angle);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxCalcPhAngle, gridBagConstraints);

        chBoxCalcAGenPow.setText(GuiLocalization.calc+GuiLocalization.active_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(chBoxCalcAGenPow, gridBagConstraints);

        chBoxCalcRGenPow.setText(GuiLocalization.calc+GuiLocalization.reactive_gen_power);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(chBoxCalcRGenPow, gridBagConstraints);

        chBoxPhAngleDgrm.setText(GuiLocalization.phase_angle_diagram);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxPhAngleDgrm, gridBagConstraints);

        chBoxVolLvlDgrm.setText(GuiLocalization.voltage_lvl_diagram);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chBoxVolLvlDgrm, gridBagConstraints);

        jLabel1.setText(GuiLocalization.current_parameters+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText(GuiLocalization.calculated_parameters+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        add(jLabel2, gridBagConstraints);

        jLabel3.setText(GuiLocalization.diagrams+":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 5, 10);
        add(jLabel3, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jSeparator1, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jSeparator2, gridBagConstraints);
        
        closeButton.setText(GuiLocalization.close);
        closeButton.setMinimumSize(new java.awt.Dimension(90, 23));
        closeButton.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(closeButton, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jSeparator3, gridBagConstraints);
    }
    
    /**
     * represent current paint info
     */
    public void representData()
    {
    	chBoxType.setSelected( appGraphRedactor.checkItemType.isSelected() );
    	chBoxVolLvl.setSelected( appGraphRedactor.checkItemU.isSelected() );
    	chBoxPhAngle.setSelected( appGraphRedactor.checkItemAngle.isSelected() );
    	chBoxAGenPow.setSelected( appGraphRedactor.checkItemPG.isSelected() );
    	chBoxRGenPow.setSelected( appGraphRedactor.checkItemQG.isSelected() );
    	chBoxAConsPow.setSelected( appGraphRedactor.checkItemPL.isSelected() );
    	chBoxRConsPow.setSelected( appGraphRedactor.checkItemQL.isSelected() );
    	
    	chBoxCalcVolLvl.setSelected( appGraphRedactor.checkItemCalcU.isSelected() );
    	chBoxCalcPhAngle.setSelected( appGraphRedactor.checkItemCalcAngle.isSelected() );
    	chBoxCalcAGenPow.setSelected( appGraphRedactor.checkItemCalcPG.isSelected() );
    	chBoxCalcRGenPow.setSelected( appGraphRedactor.checkItemCalcQG.isSelected() );
    	
    	chBoxPhAngleDgrm.setSelected( appGraphRedactor.checkItemDgrmAngle.isSelected() );
    	chBoxVolLvlDgrm.setSelected( appGraphRedactor.checkItemDgrmVoltage.isSelected() );
    }
    
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		String s = ((JCheckBox)e.getItem()).getText();
		
		if(s.equals(GuiLocalization.type))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemType.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemType.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.voltage_lvl))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemU.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemU.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.phase_angle))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemAngle.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemAngle.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.active_gen_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemPG.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemPG.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.reactive_gen_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemQG.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemQG.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.active_cons_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemPL.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemPL.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.reactive_cons_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemQL.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemQL.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.calc+GuiLocalization.voltage_lvl))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemCalcU.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemCalcU.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.calc+GuiLocalization.phase_angle))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemCalcAngle.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemCalcAngle.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.calc+GuiLocalization.active_gen_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemCalcPG.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemCalcPG.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.calc+GuiLocalization.reactive_gen_power))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemCalcQG.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemCalcQG.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.phase_angle_diagram))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemDgrmAngle.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemDgrmAngle.setSelected(false);
			}
		}
		else if(s.equals(GuiLocalization.voltage_lvl_diagram))
		{
			if(e.getStateChange()==1)
			{
				appGraphRedactor.checkItemDgrmVoltage.setSelected(true);
			}
			else
			{
				appGraphRedactor.checkItemDgrmVoltage.setSelected(false);
			}
		}
	}
	
	/**
     * set current Component Container
     * @param curCc : ComponentContainer
     */
    public void setCurrentCc(ComponentContainer curCc)
    {
    	this.currentCc = curCc;
    }
}
