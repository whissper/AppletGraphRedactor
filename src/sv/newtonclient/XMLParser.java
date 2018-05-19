package sv.newtonclient;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodeParameters;

/**
 * XML-parser for parsing server message
 * and writing data into Result-storage.
 * @see <i>Copied from original version of NewtonClient</i>
 */
public class XMLParser extends DefaultHandler
{
	/**
	 * current Result-storage
	 */
	private ResultsStorage resultsStorage;
	
	//constructor start:
	XMLParser(ResultsStorage resultsStorage)
	{
		this.resultsStorage = resultsStorage;
	}
	//constructor end;
	
	/**
	 * parsing string of xml-message
	 * @param xmlMessage : String
	 */
	public void parse(String xmlMessage)
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		XMLReader xmlReader = null;
		
		//preparation
		try
		{
			SAXParser saxPareser = spf.newSAXParser();
			xmlReader = saxPareser.getXMLReader();
		}
		catch(Exception ex)
		{
			throw new RuntimeException("XMLParser[parse]: parser preparation error: "+ex.getMessage());
		}
		
		//parsing
		try
		{
			xmlReader.setContentHandler(this);
			xmlReader.setErrorHandler(this);
			resultsStorage.clearAll();
			
			InputSource source = new InputSource( new ByteArrayInputStream(xmlMessage.getBytes()) );
			xmlReader.parse(source);
		}
		catch(Exception ex)
		{
			throw new RuntimeException("XMLParser[parse]: parse error: "+ex.getMessage());
		}				
	}
	
	@Override
	public void startElement(String uri, String localName, String rawName, Attributes attributes)
	{
		//<Scheme>
		if( rawName.equals("Scheme") )
		{ 
            int atrCount = attributes.getLength();//amount of <Scheme> attributes
            
            for(int q=0; q<atrCount; q++)
            {
            	String AtrName = attributes.getQName(q);
            	String AtrVal = attributes.getValue(q);
            	
            	if(AtrName.equals("Ret"))//Ret=""
            	{
            		resultsStorage.setRetVal(Integer.parseInt(AtrVal));
            	}
			
            	if(AtrName.equals("Status"))//Status=""
            	{
            		resultsStorage.setStatus(AtrVal);
            	}
            }
        }
		
		//<Node>
		if(rawName.equals("Node"))
		{
            int AtrCount=attributes.getLength();//amount of <Node> attributes
            
            for(int q=0; q<AtrCount; q++)
            {
            	String AtrName = attributes.getQName(q);
            	String AtrVal = attributes.getValue(q);
			
            	if(AtrName.equals("U"))//U=""
            	{
            		resultsStorage.addResNode(NodeParameters.Data_U, AtrVal);
            	}
		    
            	if(AtrName.equals("Angle"))//Angle=""
            	{
            		resultsStorage.addResNode(NodeParameters.Data_Angle, AtrVal);
            	}
			    
            	if(AtrName.equals("Pg"))//Pg=""
            	{
            		resultsStorage.addResNode(NodeParameters.Data_PG, AtrVal);
            	}
			
            	if(AtrName.equals("Qg"))//Qg=""
            	{
            		resultsStorage.addResNode(NodeParameters.Data_QG, AtrVal);
            	}
            }
        }
		
		//<Branch>
		if(rawName.equals("Branch"))
		{
            int AtrCount = attributes.getLength(); //amount of <Branch> attributes
            
            for(int q=0; q<AtrCount; q++)
            {
            	String AtrName = attributes.getQName(q);
            	String AtrVal = attributes.getValue(q);
			
            	if(AtrName.equals("PF"))//PF=""
            	{
            		resultsStorage.addResEdge(EdgeResults.PF, AtrVal);
            	}
		    
            	if(AtrName.equals("QF"))//QF=""
            	{
            		resultsStorage.addResEdge(EdgeResults.QF, AtrVal);
            	}
			    
            	if(AtrName.equals("PT"))//PT=""
            	{
            		resultsStorage.addResEdge(EdgeResults.PT, AtrVal);
            	}
			
            	if(AtrName.equals("QT"))//QT=""
            	{
            		resultsStorage.addResEdge(EdgeResults.QT, AtrVal);
            	}
            }
        }
		
	}
	
}
