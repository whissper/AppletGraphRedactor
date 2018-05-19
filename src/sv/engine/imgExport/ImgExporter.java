package sv.engine.imgExport;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import sv.editor.AppGraphRedactor;
import sv.editor.ComponentContainer;
import sv.gui.localization.GuiLocalization;

/**
 * Image Exporter
 * @author SAV2
 * @since 15.04.2013
 */
public class ImgExporter
{
	private ComponentContainer compCont;
	private AppGraphRedactor appGraphRedactor;
	
	/**
	 * ImgExporter constructor
	 * @param appGrphRdct : AppGraphRedactor -- current applet graph editor
	 * @param cc : ComponentContainer -- current component container (draw field)
	 */
	public ImgExporter(AppGraphRedactor appGrphRdct, ComponentContainer cc)
	{
		this.compCont = cc;
		this.appGraphRedactor = appGrphRdct;
	}
	
	/**
	 * export Image
	 * @throws IOException
	 */
	public void exportImage() throws IOException
	{
		JFileChooser chooserExport = new JFileChooser();
		chooserExport.setDialogTitle(GuiLocalization.export_as);
		
		chooserExport.setCurrentDirectory(new File("."));
		
		chooserExport.setAcceptAllFileFilterUsed(false);
		
		//JPEG filter
		chooserExport.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory() ||
					   f.getName().toLowerCase().endsWith(".jpg") ||
					   f.getName().toLowerCase().endsWith(".jpeg");
			}
			
			@Override
			public String getDescription()
			{
				return "JPEG";
			}
		});
		//GIF filter
		chooserExport.addChoosableFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory() ||
					   f.getName().toLowerCase().endsWith(".gif");
			}
			
			@Override
			public String getDescription()
			{
				return "GIF";
			}
		});
		//PNG filter
		chooserExport.addChoosableFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory() ||
					   f.getName().toLowerCase().endsWith(".png");
			}
					
			@Override
			public String getDescription()
			{
				return "PNG";
			}
		});
		
		int k = chooserExport.showSaveDialog(appGraphRedactor);
    	if (k != JFileChooser.APPROVE_OPTION) return;
    	
    	File imgFile = chooserExport.getSelectedFile();
    	
    	//JPEG
    	if(chooserExport.getFileFilter().getDescription().equals("JPEG"))
    	{
    		if( !imgFile.getName().toLowerCase().endsWith(".jpg") )
        	{
        		imgFile = new File( chooserExport.getSelectedFile() + ".jpg" );
        	}
        	
        	saveImage(imgFile, "JPEG");
    	}
    	//GIF
    	else if(chooserExport.getFileFilter().getDescription().equals("GIF"))
    	{
    		if( !imgFile.getName().toLowerCase().endsWith(".gif") )
        	{
        		imgFile = new File( chooserExport.getSelectedFile() + ".gif" );
        	}
        	
        	saveImage(imgFile, "GIF");
    	}
    	//PNG
    	else if(chooserExport.getFileFilter().getDescription().equals("PNG"))
    	{
    		if( !imgFile.getName().toLowerCase().endsWith(".png") )
        	{
        		imgFile = new File( chooserExport.getSelectedFile() + ".png" );
        	}
        	
        	saveImage(imgFile, "PNG");
    	}
	}
	
	/**
	 * save image file
	 * @param file : File (current image file)
	 * @param filterDescription : String (file filter description)
	 * @throws IOException
	 */
	private void saveImage(File file, String filterDescription) throws IOException
	{
		BufferedImage image = new BufferedImage(compCont.getWidth(), compCont.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = image.createGraphics();
	    compCont.paint(graphics2D);
	    
	    if( filterDescription.equals("JPEG") )
	    {
	    	ImageIO.write(image,"jpeg", file);
	    }
	    else if( filterDescription.equals("GIF") )
	    {
	    	ImageIO.write(image,"gif", file);
	    }
	    else if( filterDescription.equals("PNG") )
	    {
	    	ImageIO.write(image,"png", file);
	    }
	}
}
