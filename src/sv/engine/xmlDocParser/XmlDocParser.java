package sv.engine.xmlDocParser;
/*
import javax.swing.*;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import sv.editor.AppGraphRedactor;
import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
*/

/**
 * XML document parser
 * <p/>
 * for parsing of XML document 
 *
 * @author SAV2
 * @version 1.0.0
 * @since 28.08.2012
 */

/*
public class XmlDocParser 
{
	private ComponentContainer compContainer;
	private AppGraphRedactor appGraphRedactor;
	private DocumentBuilder builder;
	
	public XmlDocParser(AppGraphRedactor appGR, ComponentContainer cc)
	{
		appGraphRedactor = appGR;
		compContainer = cc;
	}
	
    //openFile() operation start:
    public void openFile()
    {
    	int coordXInt=0, coordYInt=0, widthInt=0, heightInt=0;
    	
    	JFileChooser chooserOpen = new JFileChooser();
		chooserOpen.setCurrentDirectory(new File("."));
		
		chooserOpen.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(File f)
			{
				return f.isDirectory() ||
					   f.getName().toLowerCase().endsWith(".xml");
			}
			public String getDescription()
			{
				return "XML files";
			}
		});
		int r = chooserOpen.showOpenDialog(appGraphRedactor);
		if (r != JFileChooser.APPROVE_OPTION) return;
		File f = chooserOpen.getSelectedFile();
		try
		{
			if (builder == null)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(true); //<new>
				factory.setIgnoringElementContentWhitespace(true); //<new>
				builder = factory.newDocumentBuilder();
			}
			
			Document doc = builder.parse(f);
			
			Element root = doc.getDocumentElement(); // <buildDoc>
			NodeList MyComponents = root.getChildNodes(); // <MyComp>
			for(int i=0; i<MyComponents.getLength(); i++) // amount of <MyComp></MyComp>
			{
				Node NodeMyComp = MyComponents.item(i); // <MyComp>
				
				NodeList ChildsOfMyComp = NodeMyComp.getChildNodes(); //<size> and <location>
				
				for (int j=0; j<ChildsOfMyComp.getLength(); j++)
				{	
				Element childElement = (Element) ChildsOfMyComp.item(j);
				
				  if (childElement.getTagName().equals("size")) // <size>
				  {
					  Element widthElement = (Element) childElement.getFirstChild(); //<width> element
					  Text widthText = (Text) widthElement.getFirstChild(); //<width> with trash
					  String widthString = widthText.getData().trim(); //<width> w\o trash
					  widthInt = Integer.parseInt(widthString); // <width> to int
					
					  Element heightElement = (Element) childElement.getLastChild(); //<height> element
					  Text heightText = (Text) heightElement.getFirstChild(); //<height> with trash
					  String heightString = heightText.getData().trim(); //<height> w\o trash
					  heightInt = Integer.parseInt(heightString); // <height> to int
					  
				  }
				else // <location>
				  {
					  Element coordXElement = (Element) childElement.getFirstChild();	
					  Text coordXText = (Text) coordXElement.getFirstChild(); //<coordX> with trash
					  String coordXString = coordXText.getData().trim(); //<coordX> w\o trash
					  coordXInt = Integer.parseInt(coordXString); // <coordX> to int
					
					  Element coordYElement = (Element) childElement.getLastChild();
					  Text coordYText = (Text) coordYElement.getFirstChild(); //<coordY> with trash
					  String coordYString = coordYText.getData().trim(); //<coordY> w\o trash
					  coordYInt = Integer.parseInt(coordYString); // <coordY> to int
				  }
				}
				//compContainer.addCompFromXmlDoc(coordXInt, coordYInt, widthInt, heightInt);//!!!
			}
			
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(appGraphRedactor, e);
		}
		catch (ParserConfigurationException e)
		{
			JOptionPane.showMessageDialog(appGraphRedactor, e);
		}
		catch (SAXException e)
		{
			JOptionPane.showMessageDialog(appGraphRedactor, e);
		}
    }
    //openFile() operation end;
    
    //saveFile() operation start:
    public void saveFile() throws TransformerException, IOException
    {
    	JFileChooser chooserSave = new JFileChooser();
    	chooserSave.setCurrentDirectory(new File("."));
    	
    	chooserSave.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(File f)
			{
				return f.isDirectory() ||
					   f.getName().toLowerCase().endsWith(".xml");
			}
			public String getDescription()
			{
				return "XML files";
			}
		});
		
    	int k = chooserSave.showSaveDialog(appGraphRedactor);
    	if (k != JFileChooser.APPROVE_OPTION) return;
    	File fNew = chooserSave.getSelectedFile();
    	Document docNew = this.buildDocument();
    	Transformer t = TransformerFactory.newInstance().newTransformer();
    	t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://applet-work.net78.net/example.dtd");
    	t.setOutputProperty(OutputKeys.INDENT, "yes");
    	t.transform(new DOMSource(docNew), new StreamResult(new FileOutputStream(fNew)));
    	
    }
    //saveFile() operation end;
    
    //build Document start:
    public Document buildDocument()
    {
    	DocumentBuilderFactory factoryNew = DocumentBuilderFactory.newInstance(); //!!!!!!!
    	try
    	{
        builder = factoryNew.newDocumentBuilder();
    	}
    	catch (Exception exc)
    	{
    		exc.printStackTrace();
    	}                                                                        //!!!!!!!!
    	Document docNew = builder.newDocument();
    	
    	Element rootElement = docNew.createElement("buildDoc");
    	docNew.appendChild(rootElement);
    	for (ComponentPanel comps : compContainer.getComps())
    	{
        	Element myCompElement = docNew.createElement("MyComp");
        	Element sizeElement = docNew.createElement("size");
        	Element locationElement = docNew.createElement("location");
        	Element widthElement = docNew.createElement("width");
        	Element heightElement = docNew.createElement("height");
        	Element coordXElement = docNew.createElement("coordX");
        	Element coordYElement = docNew.createElement("coordY");
        	
        	Text textNodeWidth = docNew.createTextNode(Integer.toString(comps.getWidth()));
        	Text textNodeHeight = docNew.createTextNode(Integer.toString(comps.getHeight()));
        	Text textNodeCoordX = docNew.createTextNode(Integer.toString(comps.getX()));
        	Text textNodeCoordY = docNew.createTextNode(Integer.toString(comps.getY()));
        	
        	rootElement.appendChild(myCompElement);
        	myCompElement.appendChild(sizeElement);
        	myCompElement.appendChild(locationElement);
        	sizeElement.appendChild(widthElement);
        	sizeElement.appendChild(heightElement);
        	locationElement.appendChild(coordXElement);
        	locationElement.appendChild(coordYElement);
        	
        	widthElement.appendChild(textNodeWidth);
        	heightElement.appendChild(textNodeHeight);
        	coordXElement.appendChild(textNodeCoordX);
        	coordYElement.appendChild(textNodeCoordY);
        	
        	//myCompElement.setAttribute("type", "int");
    	}
    	
    	return docNew;
    }
    //build Document end;
			
}

*/