package sv.gui.supportElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sv.editor.ComponentContainer;
import sv.gui.localization.GuiLocalization;

public class ServerMessage extends JDialog
{
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton cancelButton;
	private ComponentContainer currentCc;
	
	/**
     * Creates new form Server Message
     */
    public ServerMessage(Window parent, Dialog.ModalityType type) {
        super(parent, type);
        initComponents();
    }
    
    /**
     * change strings
     */
    public void changeStrings()
    {
    	setTitle(GuiLocalization.server_message);
    	cancelButton.setText(GuiLocalization.cancel);
    }
    
    private void initComponents()
    {
    	setTitle(GuiLocalization.server_message);
    	setMinimumSize(new Dimension(400, 300));
        setModalityType(Dialog.ModalityType.MODELESS);
        setResizable(false);
        
    	panel = new JPanel();
    	textArea = new JTextArea();
    	scrollPane = new JScrollPane(textArea);
    	cancelButton = new JButton(GuiLocalization.cancel);
    	
    	panel.setLayout(new BorderLayout());
    	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	setContentPane(panel);
    	
    	getContentPane().add(scrollPane, BorderLayout.CENTER);
    	getContentPane().add(cancelButton, BorderLayout.SOUTH);
    	
    	scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	textArea.setEditable(false);
    	//textArea.setText("\n" + "YO" + "\n" + "World!");
    	
    	cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				currentCc.getNewtonClnt().cancelRequest();
				ServerMessage.this.dispose();
				currentCc.requestFocusInWindow();
			}
		});
    	
    }
    
    /**
     * set message
     * @param value : String
     */
    public void setMessage(String value)
    {
    	textArea.setText(value);
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
