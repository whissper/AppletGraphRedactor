package sv.gui.additionalElements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import sv.editor.ComponentContainer;

/**
 * Grid Panel
 * @author SAV2
 * @since 01.05.2013
 */
public class GridPanel extends JPanel
{
	private ComponentContainer compC;
	
	private int cellSize = 25;
	private static final int DOT_SIZE = 2;
	
	public GridPanel(ComponentContainer cc)
	{
		super(null);
		this.compC = cc;
	}
	
	/**
	 * set cell's size
	 * @param value : <code>String</code>
	 */
	public void setCellSize(String value)
	{
		try
		{
			if(Integer.parseInt(value) <= 0)
			{
				this.cellSize = 1;
			}
			else
			{
				this.cellSize = Integer.parseInt(value);
			}
			compC.repaint();
		}
		catch(NumberFormatException ex)
		{
			
		}
	}
	
	/**
	 * get cell size of current grid panel
	 * @return <code>int</code> : cell size
	 */
	public int getCellSize()
	{
		return this.cellSize;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.lightGray);
		
		int scaledDotSize = ComponentContainer.getScaledValue(DOT_SIZE);
		int scaledCellSize = ComponentContainer.getScaledValue(cellSize);
		
		for(int i=0; i<this.getWidth(); i+=scaledCellSize)
		{
			for(int j=0; j<this.getHeight(); j+=scaledCellSize)
			{
				g2d.fillRect(i-scaledDotSize/2, j-scaledDotSize/2, scaledDotSize, scaledDotSize);
			}
		}
	}
}
