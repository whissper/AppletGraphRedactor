package sv.engine.xmlDocParser;

import javax.swing.*;

import java.awt.Rectangle;
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
import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.additionalElements.NodesOfEdge;
import sv.gui.localization.GuiLocalization;

/**
 * XML document parser
 * <p/>
 * for parsing of XML document 
 *
 * @author SAV2
 * @version 1.0.0
 * @since 28.08.2012
 */

public class XmlDocParserII 
{
	private ComponentContainer compContainer;
	private AppGraphRedactor appGraphRedactor;
	private DocumentBuilder builder;
	
	public XmlDocParserII(AppGraphRedactor appGR, ComponentContainer cc)
	{
		appGraphRedactor = appGR;
		compContainer = cc;
	}
	
    //openFile() operation start:
    public void openFile()
    {
    	//Node creation parameters
    	int nodeID = -1;
    	Rectangle nodeBounds = null;
    	
    	String nParamType = "1"; //PQ as default
    	String nParamU = "0.0";
    	String nParamAngle = "0.0";
    	String nParamPG = "0.0";
    	String nParamQG = "0.0";
    	String nParamPL = "0.0";
    	String nParamQL = "0.0";
    	
    	String nResU = "0.0";
    	String nResAngle = "0.0";
    	String nResPG = "0.0";
    	String nResQG = "0.0";
    	//------------------------>
    	
    	//Edge creation parameters
    	int edgeID = -1;
    	int firstNodeID = -1;
    	int lastNodeID = -1;
    	
    	String eParamR = "0.0";
    	String eParamX = "0.0";
    	String eParamTap ="0.0";
    	
    	String eResPF = "0.0";
    	String eResQF = "0.0";
    	String eResPT = "0.0";
    	String eResQT = "0.0";
    	//------------------------>
    	
    	JFileChooser chooserOpen = new JFileChooser();
    	chooserOpen.setDialogTitle(GuiLocalization.open_file);
    	
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
			//full clear current drawing area (current component container)
			compContainer.fullClear();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true); //<new>
			factory.setIgnoringElementContentWhitespace(true); //<new>
			builder = factory.newDocumentBuilder();
			//----------------------------------------------------->
			builder.setEntityResolver(new EntityResolver() {
				
				@Override
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
				{
					if(systemId.contains("example.dtd"))
					{
						InputStream dtdStream = appGraphRedactor.getClass().getResourceAsStream("dtdfile/example.dtd");
						
						return new InputSource(dtdStream);
					}
					else
					{
						return null;
					}
				}
			});
			//----------------------------------------------------->
			Document doc = builder.parse(f);
			
			
			Element root = doc.getDocumentElement(); //<buildDoc>
			NodeList components = root.getChildNodes(); //<Graph>, <Nodes>, <Edges>
			
			for (int i=0; i<components.getLength(); i++) //amount of 1-lvl tags
			{
				//add Node from xml-doc
				if( ((Element) components.item(i)).getTagName().equals("Nodes") ) //for <Nodes>
				{
					Element nodes = (Element) components.item(i);//<Nodes>
					NodeList listOfNode = nodes.getChildNodes();//list of <Node>
					
					for(int j=0; j<listOfNode.getLength(); j++)//for each <Node>
					{
						NodeList childsOfNode = listOfNode.item(j).getChildNodes(); //<NodeID>, <NodeMemoryParameters>, <NodeGraphicParameters>
						
						for(int k=0; k<childsOfNode.getLength(); k++)
						{
							Element childOfNode = (Element) childsOfNode.item(k);
							
							//<NodeID>
							if( childOfNode.getTagName().equals("NodeID") )
							{
								nodeID = Integer.parseInt( ((Text) childOfNode.getFirstChild()).getData().trim() );
							}
							//<NodeMemoryParameters>
							else if( childOfNode.getTagName().equals("NodeMemoryParameters") )
							{
								NodeList nMain = childOfNode.getChildNodes().item(0).getChildNodes();//<nMain>
								NodeList nAdditional = childOfNode.getChildNodes().item(1).getChildNodes();//<nAdditional>
								NodeList nCalculated = childOfNode.getChildNodes().item(2).getChildNodes();//<nCalculated>
							
								//<nMain>
								nParamType = ((Text) nMain.item(0).getFirstChild()).getData().trim();
								nParamU = ((Text) nMain.item(1).getFirstChild()).getData().trim();
								nParamAngle = ((Text) nMain.item(2).getFirstChild()).getData().trim();
							
								//<nAdditional>
								nParamPG = ((Text) nAdditional.item(0).getFirstChild()).getData().trim();
								nParamQG = ((Text) nAdditional.item(1).getFirstChild()).getData().trim();
								nParamPL = ((Text) nAdditional.item(2).getFirstChild()).getData().trim();
								nParamQL = ((Text) nAdditional.item(3).getFirstChild()).getData().trim();
								
								//<nCalculated>
								nResU = ((Text) nCalculated.item(0).getFirstChild()).getData().trim();
								nResAngle = ((Text) nCalculated.item(1).getFirstChild()).getData().trim();
								nResPG = ((Text) nCalculated.item(2).getFirstChild()).getData().trim();
								nResQG = ((Text) nCalculated.item(3).getFirstChild()).getData().trim();
							}
							//<NodeGraphicParameters>
							else if( childOfNode.getTagName().equals("NodeGraphicParameters") )
							{
								NodeList nLocation = childOfNode.getFirstChild().getFirstChild().getChildNodes(); //<nBounds> --> <nLocation>
								NodeList nSize = childOfNode.getFirstChild().getLastChild().getChildNodes(); //<nBounds> --> <nSize>
								
								//<nBounds> --> <nLocation>
								int nCoordX = Integer.parseInt( ((Text) nLocation.item(0).getFirstChild()).getData().trim() );
								int nCoordY = Integer.parseInt( ((Text) nLocation.item(1).getFirstChild()).getData().trim() );
								
								//<nBounds> --> <nSize>
								int nWidth = Integer.parseInt( ((Text) nSize.item(0).getFirstChild()).getData().trim() );
								int nHeight = Integer.parseInt( ((Text) nSize.item(1).getFirstChild()).getData().trim() );
								
								nodeBounds = new Rectangle(nCoordX, nCoordY, nWidth, nHeight);
							}
						
						}
						
						compContainer.addNodeFromXmlDoc(nodeID, nodeBounds, nParamType, nParamU, nParamAngle, nParamPG, nParamQG, nParamPL, nParamQL, nResU, nResAngle, nResPG, nResQG);
					}
					
				}
				//add Edge from xml-doc
				else if( ((Element) components.item(i)).getTagName().equals("Edges") ) // for <Edges>
				{
					Element edges = (Element) components.item(i);//<Edges>
					NodeList listOfEdge = edges.getChildNodes();//list of <Edge>
					
					for(int j=0; j<listOfEdge.getLength(); j++)//for each <Edge>
					{
						edgeID = Integer.parseInt( ((Text) listOfEdge.item(j).getFirstChild().getFirstChild()).getData().trim() ); //<EdgeID> --> #PCDATA
						
						NodeList edgeMemoryParameters = listOfEdge.item(j).getLastChild().getChildNodes(); //<eMain>, <eAdditional>, <eConnection>
						//for each "edge-Memory-Parameter"
						for(int k=0; k<edgeMemoryParameters.getLength(); k++)
						{
							Element memoryParameter = (Element) edgeMemoryParameters.item(k);
							//<eMain>
							if( memoryParameter.getTagName().equals("eMain") )
							{
								eParamR = ((Text) memoryParameter.getChildNodes().item(0).getFirstChild()).getData().trim();
								eParamX = ((Text) memoryParameter.getChildNodes().item(1).getFirstChild()).getData().trim();
								eParamTap = ((Text) memoryParameter.getChildNodes().item(2).getFirstChild()).getData().trim();
							}
							//<eAdditional>
							else if( memoryParameter.getTagName().equals("eAdditional") )
							{
								
							}
							//<eConnection>
							else if( memoryParameter.getTagName().equals("eConnection") )
							{
								firstNodeID = Integer.parseInt( ((Text) memoryParameter.getFirstChild().getFirstChild()).getData().trim() );
								lastNodeID = Integer.parseInt( ((Text) memoryParameter.getLastChild().getFirstChild()).getData().trim() );
							}
							//<eCalculated>
							else if ( memoryParameter.getTagName().equals("eCalculated") )
							{
								eResPF = ((Text) memoryParameter.getChildNodes().item(0).getFirstChild()).getData().trim();
								eResQF = ((Text) memoryParameter.getChildNodes().item(1).getFirstChild()).getData().trim();
								eResPT = ((Text) memoryParameter.getChildNodes().item(2).getFirstChild()).getData().trim();
								eResQT = ((Text) memoryParameter.getChildNodes().item(3).getFirstChild()).getData().trim();
							}
						}
						
						compContainer.addEdgeFromXmlDoc(edgeID, firstNodeID, lastNodeID, eParamR, eParamX, eParamTap, eResPF, eResQF, eResPT, eResQT);
					}
					
				}
			}
			
			builder = null;			
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
    	chooserSave.setDialogTitle(GuiLocalization.save_f);
    	
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
    	
    	if( !fNew.getName().toLowerCase().endsWith(".xml") )
    	{
    		fNew = new File( chooserSave.getSelectedFile() + ".xml" );
    	}
    	
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
    	}                                                                        
    	Document docNew = builder.newDocument();								 //!!!!!!!!
    	
    	Element rootElement = docNew.createElement("buildDoc"); //<buildDoc>
    	docNew.appendChild(rootElement);
    	
    	Element graphElement = docNew.createElement("Graph"); //<Graph>
    	Element nodesElement = docNew.createElement("Nodes"); //<Nodes>
    	Element edgesElement = docNew.createElement("Edges"); //<Edges>
    	
    	rootElement.appendChild(graphElement);
    	rootElement.appendChild(nodesElement);
    	rootElement.appendChild(edgesElement);
    	
    	//<Graph> start:
    	//for each <graphNode>
    	//<Graph>-->
    	for (int graphNodeId : compContainer.getGrpah().getConnections().keySet())
    	{
    		Element graphNodeElement = docNew.createElement("graphNode"); //<graphNode>
    		graphElement.appendChild(graphNodeElement);
    		
    		//<graphNode>-->
    		Element graphNodeIDElement = docNew.createElement("graphNodeID"); //<graphNodeID>
    		Element linkedNodesElement = docNew.createElement("linkedNodes"); //<linkedNodes>
    		
    		graphNodeElement.appendChild(graphNodeIDElement);
    		graphNodeElement.appendChild(linkedNodesElement);
    		
    		//<graphNodeID>-->
    		Text textGraphNodeID = docNew.createTextNode( Integer.toString(graphNodeId) ); //<graphNodeID> #PCDATA
    		graphNodeIDElement.appendChild(textGraphNodeID);
    		
    		//<linkedNodes>-->
    		for (int graphLinkedNodeID : compContainer.getGrpah().getConnections().get(graphNodeId))
    		{
    			Element graphLinkedNodeIdElement = docNew.createElement("linkedNodeID"); //<linkedNodeID>
    			linkedNodesElement.appendChild(graphLinkedNodeIdElement);
    			
    			Text textLinkedNodeId = docNew.createTextNode( Integer.toString(graphLinkedNodeID) ); //<linkedNodeID> #PCDATA
    			graphLinkedNodeIdElement.appendChild(textLinkedNodeId);
    		}
    	}
    	//<Graph> end;
    	
    	//<Nodes>\<Edges> start:
    	for (ComponentPanel comp : compContainer.getComps())
    	{	
    		//<Nodes>-->
    		if(comp.getDelegate() instanceof sv.gui.graphElements.Node)
    		{
    			Element nodeElement = docNew.createElement("Node"); //<Node>
    			nodesElement.appendChild(nodeElement);
    			
    			//<Node>-->
    			Element nodeIDElement = docNew.createElement("NodeID"); //<NodeID>
    			Element nodeMemoryParamsElement = docNew.createElement("NodeMemoryParameters"); //<NodeMemoryParameters>
    			Element nodeGraphicParamsElement = docNew.createElement("NodeGraphicParameters"); //<NodeGraphicParameters>
    			
    			nodeElement.appendChild(nodeIDElement);
    			nodeElement.appendChild(nodeMemoryParamsElement);
    			nodeElement.appendChild(nodeGraphicParamsElement);
    			
    			//<NodeID>-->
    			Text textNodeId = docNew.createTextNode( Integer.toString( ((sv.gui.graphElements.Node) comp.getDelegate()).getID() ) ); //<NodeID> #PCDATA
    			nodeIDElement.appendChild(textNodeId);
    			
    			//<NodeMemoryParameters>-->
    			Element nMainElement = docNew.createElement("nMain"); //<nMain>
    			Element nAdditionalElement = docNew.createElement("nAdditional"); //<nAdditional>
    			Element nCalculatedElement = docNew.createElement("nCalculated"); //<nCalculated>
    			
    			nodeMemoryParamsElement.appendChild(nMainElement);
    			nodeMemoryParamsElement.appendChild(nAdditionalElement);
    			nodeMemoryParamsElement.appendChild(nCalculatedElement);
    			
    			//<nMain>-->
    			Element nParamTypeElement = docNew.createElement("nParamType"); //<nParamType>
    			Element nParamUElement = docNew.createElement("nParamU"); //<nParamU>
    			Element nParamAngleElement = docNew.createElement("nParamAngle"); //<nParamAngle>
    			
    			nMainElement.appendChild(nParamTypeElement);
    			nMainElement.appendChild(nParamUElement);
    			nMainElement.appendChild(nParamAngleElement);
    			
    			//<nParamType>-->
    			Text textNParamType = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_Type) ); //<nParamType> #PCDATA
    			nParamTypeElement.appendChild(textNParamType);
    			
    			//<nParamU>-->
    			Text textNParamU = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_U) ); //<nParamU> #PCDATA
    			nParamUElement.appendChild(textNParamU);
    			
    			//<nParamAngle>-->
    			Text textNParamAngle = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_Angle) ); //<nParamAngle> #PCDATA
    			nParamAngleElement.appendChild(textNParamAngle);
    			
    			//<nAdditional>-->
    			Element nParamPGElement = docNew.createElement("nParamPG"); //<nParamPG>
    			Element nParamQGElement = docNew.createElement("nParamQG"); //<nParamQG>
    			Element nParamPLElement = docNew.createElement("nParamPL"); //<nParamPL>
    			Element nParamQLElement = docNew.createElement("nParamQL"); //<nParamQL>
    			
    			nAdditionalElement.appendChild(nParamPGElement);
    			nAdditionalElement.appendChild(nParamQGElement);
    			nAdditionalElement.appendChild(nParamPLElement);
    			nAdditionalElement.appendChild(nParamQLElement);
    			
    			//<nParamPG>-->
    			Text textNParamPG = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_PG) ); //<nParamPG> #PCDATA
    			nParamPGElement.appendChild(textNParamPG);
    			
    			//<nParamQG>-->
    			Text textNParamQG = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_QG) ); //<nParamQG> #PCDATA
    			nParamQGElement.appendChild(textNParamQG);
    			
    			//<nParamPL>-->
    			Text textNParamPL = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_PL) ); //<nParamPL> #PCDATA
    			nParamPLElement.appendChild(textNParamPL);
    			
    			//<nParamQL>-->
    			Text textNParamQL = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getParameter(NodeParameters.Data_QL) ); //<nParamQL> #PCDATA
    			nParamQLElement.appendChild(textNParamQL);
    			
    			//<nCalculated>-->
    			Element nResUElement = docNew.createElement("nResU"); //<nResU>
    			Element nResAngleElement = docNew.createElement("nResAngle"); //<nResAngle>
    			Element nResPGElement = docNew.createElement("nResPG"); //<nResPG>
    			Element nResQGElement = docNew.createElement("nResQG"); //<nResQG>
    			
    			nCalculatedElement.appendChild(nResUElement);
    			nCalculatedElement.appendChild(nResAngleElement);
    			nCalculatedElement.appendChild(nResPGElement);
    			nCalculatedElement.appendChild(nResQGElement);
    			
    			//<nResU>-->
    			Text textNResU = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getResult(NodeResults.Res_U) ); //<nResU> #PCDATA
    			nResUElement.appendChild(textNResU);
    			
    			//<nResAngle>-->
    			Text textNResAngle = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getResult(NodeResults.Res_Angle) ); //<nResAngle> #PCDATA
    			nResAngleElement.appendChild(textNResAngle);
    			
    			//<nResPG>-->
    			Text textNResPG = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getResult(NodeResults.Res_PG) ); //<nResPG> #PCDATA
    			nResPGElement.appendChild(textNResPG);
    			
    			//<nResQG>-->
    			Text textNResQG = docNew.createTextNode( ((sv.gui.graphElements.Node) comp.getDelegate()).getResult(NodeResults.Res_QG) ); //<nResQG> #PCDATA
    			nResQGElement.appendChild(textNResQG);
    			
    			//<NodeGraphicParameters>-->
    			Element nBoundsElement = docNew.createElement("nBounds"); //<nBounds>
    			nodeGraphicParamsElement.appendChild(nBoundsElement);
    			
    			//<nBounds>-->
    			Element nLocationElement = docNew.createElement("nLocation"); //<nLocation>
    			Element nSizeElement = docNew.createElement("nSize"); //<nSize>
    			
    			nBoundsElement.appendChild(nLocationElement);
    			nBoundsElement.appendChild(nSizeElement);
    			
    			//<nLocation>-->
    			Element nCoordXElement = docNew.createElement("nCoordX"); //<nCoordX>
    			Element nCoordYElement = docNew.createElement("nCoordY"); //<nCoordY>
    			
    			nLocationElement.appendChild(nCoordXElement);
    			nLocationElement.appendChild(nCoordYElement);
    			
    			//<nCoordX>-->
    			Text textNCoordX = docNew.createTextNode( Integer.toString( comp.getX() ) ); //<nCoordX> #PCDATA
    			nCoordXElement.appendChild(textNCoordX);
    			
    			//<nCoordY>-->
    			Text textNCoordY = docNew.createTextNode( Integer.toString( comp.getY() ) ); //<nCoordY> #PCDATA
    			nCoordYElement.appendChild(textNCoordY);
    			
    			//<nSize>-->
    			Element nWidthElement = docNew.createElement("nWidth"); //<nWidth>
    			Element nHeightElement = docNew.createElement("nHeight"); //<nHeight>
    			
    			nSizeElement.appendChild(nWidthElement);
    			nSizeElement.appendChild(nHeightElement);
    			
    			//<nWidth>-->
    			Text textNWidth = docNew.createTextNode( Integer.toString( comp.getWidth() ) ); //<nWidth> #PCDATA
    			nWidthElement.appendChild(textNWidth);
    			
    			//<nHeight>-->
    			Text textNHeight = docNew.createTextNode( Integer.toString( comp.getHeight() ) ); //<nHeight> #PCDATA
    			nHeightElement.appendChild(textNHeight);
    			
    		}
    		//<Edges>-->
    		else if(comp.getDelegate() instanceof sv.gui.graphElements.Edge)
    		{
    			Element edgeElement = docNew.createElement("Edge"); //<Edge>
    			edgesElement.appendChild(edgeElement);
    			
    			//<Edge>-->
    			Element edgeIDElement = docNew.createElement("EdgeID"); //<EdgeID>
    			Element edgeMemoryParamsElement = docNew.createElement("EdgeMemoryParameters"); //<EdgeMemoryParameters>
    			
    			edgeElement.appendChild(edgeIDElement);
    			edgeElement.appendChild(edgeMemoryParamsElement);
    			
    			//<EdgeID>-->
    			Text textEdgeId = docNew.createTextNode( Integer.toString( ((sv.gui.graphElements.Edge) comp.getDelegate()).getID() ) ); //<EdgeID> #PCDATA
    			edgeIDElement.appendChild(textEdgeId);
    			
    			//<EdgeMemoryParameters>-->
    			Element eMainElement = docNew.createElement("eMain"); //<eMain>
    			Element eAdditionalElement = docNew.createElement("eAdditional"); //<eAdditional>
    			Element eConnectionElement = docNew.createElement("eConnection"); //<eConnection>
    			Element eCalculatedElement = docNew.createElement("eCalculated"); //<eCalculated>
    			
    			edgeMemoryParamsElement.appendChild(eMainElement);
    			edgeMemoryParamsElement.appendChild(eAdditionalElement);
    			edgeMemoryParamsElement.appendChild(eConnectionElement);
    			edgeMemoryParamsElement.appendChild(eCalculatedElement);
    			
    			//<eMain>-->
    			Element eParamRElement = docNew.createElement("eParamR"); //<eParamR>
    			Element eParamXElement = docNew.createElement("eParamX"); //<eParamX>
    			Element eParamTapElement = docNew.createElement("eParamTap"); //<eParamTap>
    			
    			eMainElement.appendChild(eParamRElement);
    			eMainElement.appendChild(eParamXElement);
    			eMainElement.appendChild(eParamTapElement);
    			
    			//<eParamR>-->
    			Text textEParamR = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getParameter(EdgeParameters.Data_R) ); //<eParamR> #PCDATA
    			eParamRElement.appendChild(textEParamR);
    			
    			//<eParamX>-->
    			Text textEParamX = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getParameter(EdgeParameters.Data_X) ); //<eParamX> #PCDATA
    			eParamXElement.appendChild(textEParamX);
    			
    			//<eParamTap>-->
    			Text textEParamTap = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getParameter(EdgeParameters.Data_Tap) ); //<eParamTap> #PCDATA
    			eParamTapElement.appendChild(textEParamTap);
    			
    			//<eAdditional>-->
    			//Element eParamDElement = docNew.createElement("eParamD"); //<eParamD>
    			//Element eParamEElement = docNew.createElement("eParamE"); //<eParamE>
    			
    			//eAdditionalElement.appendChild(eParamDElement);
    			//eAdditionalElement.appendChild(eParamEElement);
    			
    			//<eParamD>-->
    			//Text textEParamD = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getParameter(EdgeParameters.D) ); //<eParamD> #PCDATA
    			//eParamDElement.appendChild(textEParamD);
    			
    			//<eParamE>-->
    			//Text textEParamE = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getParameter(EdgeParameters.E) ); //<eParamE> #PCDATA
    			//eParamEElement.appendChild(textEParamE);
    			
    			//<eConnection>-->
    			Element firstNodeIDElement = docNew.createElement("firstNodeID"); //<firstNodeID>
    			Element lastNodeIDElement = docNew.createElement("lastNodeID"); //<lastNodeID>
    			
    			eConnectionElement.appendChild(firstNodeIDElement);
    			eConnectionElement.appendChild(lastNodeIDElement);
    			
    			//<firstNodeID>-->
    			Text textFirstNodeId = docNew.createTextNode( Integer.toString( ((sv.gui.graphElements.Edge) comp.getDelegate()).getNode(NodesOfEdge.FIRST_NODE).getID() ) ); //<firstNodeID> #PCDATA
    			firstNodeIDElement.appendChild(textFirstNodeId);
    			
    			//<lastNodeID>-->
    			Text textLastNodeId = docNew.createTextNode( Integer.toString( ((sv.gui.graphElements.Edge) comp.getDelegate()).getNode(NodesOfEdge.LAST_NODE).getID() ) ); //<lastNodeID> #PCDATA
    			lastNodeIDElement.appendChild(textLastNodeId);
    			
    			//<eCalculated>-->
    			Element eResPFElement = docNew.createElement("eResPF");//<eResPF>
    			Element eResQFElement = docNew.createElement("eResQF");//<eResQF>
    			Element eResPTElement = docNew.createElement("eResPT");//<eResPT>
    			Element eResQTElement = docNew.createElement("eResQT");//<eResQT>
    			
    			eCalculatedElement.appendChild(eResPFElement);
    			eCalculatedElement.appendChild(eResQFElement);
    			eCalculatedElement.appendChild(eResPTElement);
    			eCalculatedElement.appendChild(eResQTElement);
    			
    			//<eResPF>-->
    			Text textEResPF = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getResult(EdgeResults.PF) ); //<eResPF> #PCDATA
    			eResPFElement.appendChild(textEResPF);
    			
    			//<eResQF>-->
    			Text textEResQF = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getResult(EdgeResults.QF) ); //<eResQF> #PCDATA
    			eResQFElement.appendChild(textEResQF);
    			
    			//<eResPT>-->
    			Text textEResPT = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getResult(EdgeResults.PT) ); //<eResPT> #PCDATA
    			eResPTElement.appendChild(textEResPT);
    			
    			//<eResQT>-->
    			Text textEResQT = docNew.createTextNode( ((sv.gui.graphElements.Edge) comp.getDelegate()).getResult(EdgeResults.QT) ); //<eResQT> #PCDATA
    			eResQTElement.appendChild(textEResQT);
    		}
    		
    	}
    	//<Nodes>\<Edges> end;
    	
    	builder = null;
    	return docNew;
    }
    //build Document end;
			
}

