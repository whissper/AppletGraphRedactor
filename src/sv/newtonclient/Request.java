package sv.newtonclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;
import sv.gui.additionalElements.EdgeParameters;
import sv.gui.additionalElements.EdgeResults;
import sv.gui.additionalElements.NodeParameters;
import sv.gui.additionalElements.NodeResults;
import sv.gui.additionalElements.NodesOfEdge;
import sv.gui.graphElements.*;
import sv.gui.localization.GuiLocalization;

/**
 * Request thread
 * @author SAV2
 * @see <i>adaptive version for swing-applet</i>
 *
 */
public class Request extends SwingWorker<String, String>
{
	/**
	 * start-time of current request
	 */
	private long startTime;
	
	/**
	 * current Component Container
	 */
	private ComponentContainer cc;	
	
	/**
	 * current Scheme parameters Storage
	 */
	private SchemeParamsStorage schemeParamsStorage;
	
	/**
	 * current Result Storage
	 */
	private ResultsStorage resultsStorage;
	
	/**
	 * client Socket
	 */
	private Socket clientSocket = null;
    
    /**
     * ServerPort
     */
    static final int serverPort=1099;
    
    /**
     * Server Address
     */
    static final String serverAddress="195.19.158.25";    
    
    /**
     * Open socket timeout in milliseconds
     */
    static final int openTimeout=20*1000;    
    
    /**
     * Read timeout in milliseconds
     */
    static final int readTimeout=10*1000;
     
    /**
     * Data writing stream
     */
    private DataOutputStream outToServer = null;    
    
    /**
     * Reading data stream
     */
    private BufferedReader inFromServer = null; 
	
    //constructor start:
    public Request(ComponentContainer cc) 
    {
		this.cc = cc;
	}
    //constructor end;
    
    //Swingworker's methods (start):
	@Override
	protected String doInBackground() throws Exception 
	{
		//long time = System.currentTimeMillis();
		
		try //connect
		{
			startTime=System.currentTimeMillis();
			
			String message = prepareXMLMessage();
            
			publish(GuiLocalization.connecting);
            openSocket();
            setTimeOut();
            openStreams();
            
            sendMessage(message);
            //Thread.sleep(2000);
		}
		catch(Exception ex)
		{
			//System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog( cc.getAppGraphRed(),
					   					  GuiLocalization.exception_occurred+ex.getMessage(),
					   					  GuiLocalization.connect_failed,
					   					  JOptionPane.ERROR_MESSAGE);
			if(!this.isCancelled())
			{
				this.cancel(true);
			}
		}
		
		if(!this.isCancelled())//if not cancelled
		{
			try //receive message
			{
				publish(GuiLocalization.receiving_message);
				String serversentence = recieveMessage();         
				resultsStorage = new ResultsStorage();
				new XMLParser(resultsStorage).parse(serversentence);
				//Thread.sleep(2000);

			}
			catch(Exception ex)
			{
				//System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog( cc.getAppGraphRed(),
					   					  	  GuiLocalization.exception_occurred+ex.getMessage(),
					   					  	  GuiLocalization.receiving_of_message_failed,
					   					  	  JOptionPane.ERROR_MESSAGE);
			}
		}
		
		//System.out.format("Thread time=%.3f ms thread = %s\n", (System.currentTimeMillis()-time)/1000.0f,Thread.currentThread().getName());
		return getResults();
	}
	
	@Override
	protected void process(List<String> strs)
	{
		for (String str : strs)
		{
			cc.setSrvMsg(str);
		}
	}
	
	@Override
	protected void done()
	{
		//closeStreams
		try
		{
			closeStreams();
        }
		catch(Exception ex)
		{
            //System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog( cc.getAppGraphRed(),
					   					  GuiLocalization.exception_occurred+ex.getMessage(),
					   					  GuiLocalization.closing_of_streams_failed,
					   					  JOptionPane.ERROR_MESSAGE);
        }
		//closeConnection
		try
		{
			closeConnection();
        }
		catch(Exception ex)
		{
            //System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog( cc.getAppGraphRed(),
					   					  GuiLocalization.exception_occurred+ex.getMessage(),
					   					  GuiLocalization.closing_of_connection_failed,
					   					  JOptionPane.ERROR_MESSAGE);
        }
		//setSrvMsg
		if(!this.isCancelled())//if not cancelled
		{
			try
			{
				setResults();
				cc.setSrvMsg(get());
			}
			catch(Exception ex)
			{
				//System.out.println(ex.getMessage());
				JOptionPane.showMessageDialog( cc.getAppGraphRed(),
										  	  GuiLocalization.exception_occurred+ex.getMessage(),
										  	  GuiLocalization.setting_of_server_message_failed,
										  	  JOptionPane.ERROR_MESSAGE);
			}
		}
		//repaint component container
		cc.repaint();
	}
	//Swingworker's methods (end);
	
	/**
     * Try to open socket
     * 
     */    
    private void openSocket()
    {
        try
        {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(serverAddress), serverPort);
            clientSocket = new Socket();
            clientSocket.connect(socketAddress, openTimeout);
        }
        catch (SocketTimeoutException ex)
        {
            throw new RuntimeException("Requiest[openSocket]: socket timeout open error: "+ex.getMessage());           
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Requiest[openSocket]: socket open error: "+ex.getMessage());
        }
    }
    
    /**
     * Set socket time out
     * readTimeout is in milliseconds
     * 
     */    
    private void setTimeOut()
    { 
        if(clientSocket == null)
        {
            throw new RuntimeException("Requiest[setTimeOut]: clientSocket == null");
        }
        
        try
        {
        	clientSocket.setSoTimeout(readTimeout);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[setTimeOut]: timeout setting error: "+ex.getMessage());
        }        
    }
    
    /**
     * Open in- and out- streams
     * 
     */
    private void openStreams()
    {     
        if(clientSocket == null)
        {
            throw new RuntimeException("Requiest[openStreams]: clientSocket == null");
        }
        
        try //out-to
        {
        	outToServer =	new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[openStreams]: output stream opening error: "+ex.getMessage());
        }
        
        try //in-from
        {
        	inFromServer =	new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));        
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[openStreams]: input stream opening error: "+ex.getMessage());
        }    
    }
    
    /**
     * Prepare XML message to send to server
     * @return string without "\n" at the end
     */
    private String prepareXMLMessage()
    {	
    	schemeParamsStorage = new SchemeParamsStorage();

    	int counter = 0;
    	for(ComponentPanel comp : cc.getComps())
    	{
    		JComponent delegate = comp.getDelegate();
    		
    		//Node
    		if(delegate instanceof Node)
    		{
    			((Node)delegate).setIDforRequest(++counter);
    			
    			schemeParamsStorage.addNodeParamRealID( ((Node)delegate).getID() );
    			
    			schemeParamsStorage.addNodeParameter(NodeParameters.Data_U, ((Node)delegate).getParameter(NodeParameters.Data_U));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_Angle, ((Node)delegate).getParameter(NodeParameters.Data_Angle));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_Type, ((Node)delegate).getParameter(NodeParameters.Data_Type));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_PG, ((Node)delegate).getParameter(NodeParameters.Data_PG));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_QG, ((Node)delegate).getParameter(NodeParameters.Data_QG));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_PL, ((Node)delegate).getParameter(NodeParameters.Data_PL));
        		schemeParamsStorage.addNodeParameter(NodeParameters.Data_QL, ((Node)delegate).getParameter(NodeParameters.Data_QL));
    		}
    		//Edge
    		else if(delegate instanceof Edge)
    		{
    			schemeParamsStorage.addEdgeParameter(NodesOfEdge.FIRST_NODE, ((Edge)delegate).getNode(NodesOfEdge.FIRST_NODE).getIDforRequest());
        		schemeParamsStorage.addEdgeParameter(NodesOfEdge.LAST_NODE, ((Edge)delegate).getNode(NodesOfEdge.LAST_NODE).getIDforRequest());
        		schemeParamsStorage.addEdgeParameter(EdgeParameters.Data_R, ((Edge)delegate).getParameter(EdgeParameters.Data_R));
        		schemeParamsStorage.addEdgeParameter(EdgeParameters.Data_X, ((Edge)delegate).getParameter(EdgeParameters.Data_X));
        		schemeParamsStorage.addEdgeParameter(EdgeParameters.Data_Tap, ((Edge)delegate).getParameter(EdgeParameters.Data_Tap));
    		}
    	}
    	
    	return schemeParamsStorage.prepareXMLMessage();
    }
    
    /**
     * Sending message to server
     * @param message string without "\n" character
     * 
     */
    private void sendMessage(String message)
    {
        if(outToServer == null)
        {
            throw new RuntimeException("Requiest[sendMessage]: outToServer == null");
        }
        
        try
        {
            outToServer.writeBytes(message+"\n");
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[sendMessage]: sending message error: "+ex.getMessage());
        }    
    }
    
    /**
     * Trying to get message with timeout enabled
     * @return could not be null
     */
    private String recieveMessage()
    {
        String serversentence = null;
        
        if(inFromServer == null)
        {
            throw new RuntimeException("Requiest[recieveMessage]: inFromServer == null"); 
        }
        
        try
        {
            serversentence = inFromServer.readLine();
        
            if(serversentence == null)
            {
                throw new RuntimeException("Requiest[recieveMessage]: no data in recieved message");
            }   
        }
        catch(SocketTimeoutException ex)
        {
                throw new RuntimeException("Requiest[recieveMessage]: recieve timeout: "+ex.getMessage());
        }
        catch(Exception ex)
        {
                throw new RuntimeException("Requiest[recieveMessage]: recieve error: "+ex.getMessage());
        }
        
        return serversentence;
    }
    
    /**
     * Close in/out streams
     * 
     */
    private void closeStreams()
    {
        try
        {
            if(inFromServer != null)
            inFromServer.close();
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[closeStreams]: error in closing In stream: "+ex.getMessage());
        }
        
        try
        {
            if(outToServer != null)
            outToServer.close();
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[closeStreams]: error in closing Out stream: "+ex.getMessage());
        }
    }
    
    /**
     * Close client socket
     * 
     */
    private void closeConnection()
    {
        try
        {
            if(clientSocket != null)
            clientSocket.close();
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Requiest[closeConnection]: error in closing socket: "+ex.getMessage());
        }
    }
	
    /**
     * get results from current ResultStorage
     * @return resultStr : String
     */
    private String getResults()
	{
		if(resultsStorage == null) return null;
		
		String resultStr = "Ret=" + resultsStorage.getRetVal() + "\n" +
						   "Status=" + resultsStorage.getStatus() + "\n";
						   //Nodes	
						   for(int i=0; i<resultsStorage.getResNodeU().size(); i++)
						   {
							   resultStr += String.format("NodeID=%2d %10.2f(U) %10.2f(Angle) %10.2f(PG) %10.2f(QG)\n",
									   					  schemeParamsStorage.getNodeParamRealID(i), 
									   					  resultsStorage.getResNodeU().get(i),
									   					  resultsStorage.getResNodeAngle().get(i),
									   					  resultsStorage.getResNodePG().get(i),
									   					  resultsStorage.getResNodeQG().get(i)
									   					  ) + "\n";
						   }
						   //Edges
						   for(int i=0; i<resultsStorage.getResEdgePF().size(); i++)
						   {
							   resultStr += String.format("EdgeID=%2d %10.2f(PF) %10.2f(QF) %10.2f(PT) %10.2f(QT)\n",
					   					  				  cc.getEdgeByItsConnectedNodes(cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.FIRST_NODE, i)).getID(),
					   					  						  						cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.LAST_NODE, i)).getID()
					   					  						  						).getID(), 
					   					  				  resultsStorage.getResEdgePF().get(i),
					   					  				  resultsStorage.getResEdgeQF().get(i),
					   					  				  resultsStorage.getResEdgePT().get(i),
					   					  				  resultsStorage.getResEdgeQT().get(i)
					   					  				  ) + "\n";
						   }
		resultStr += String.format("Time=%dms\n",System.currentTimeMillis()-startTime);
				
		return resultStr;
	}
    
    /**
     * set current results (of "resultsStorage") into real Nodes\Edges on scheme,
     * according to its real-ID.
     */
	private void setResults()
    {	
		if(resultsStorage == null || schemeParamsStorage == null) return;
		int counter = 0; //counter for setting Node #idForRequest 
		
		//Nodes
		 for(int i=0; i<this.resultsStorage.getResNodeU().size(); i++)
		 {
			 counter++;//because if Node #idForRequest == 0 then there was no request action yet
			 cc.getNodeByIDforReq(counter).setResult(NodeResults.Res_U, resultsStorage.getResNodeU().get(i));
			 cc.getNodeByIDforReq(counter).setResult(NodeResults.Res_Angle, resultsStorage.getResNodeAngle().get(i));
			 cc.getNodeByIDforReq(counter).setResult(NodeResults.Res_PG, resultsStorage.getResNodePG().get(i));
			 cc.getNodeByIDforReq(counter).setResult(NodeResults.Res_QG, resultsStorage.getResNodeQG().get(i));
		 }
		 //Edges
		 for(int i=0; i<this.resultsStorage.getResEdgePF().size(); i++)
		 {
			 cc.getEdgeByItsConnectedNodes(cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.FIRST_NODE, i)).getID(),
					 					   cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.LAST_NODE, i)).getID()
	  									    ).setResult(EdgeResults.PF, resultsStorage.getResEdgePF().get(i));
			 
			 cc.getEdgeByItsConnectedNodes(cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.FIRST_NODE, i)).getID(),
					 					   cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.LAST_NODE, i)).getID()
					   					    ).setResult(EdgeResults.QF, resultsStorage.getResEdgeQF().get(i));
			 
			 cc.getEdgeByItsConnectedNodes(cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.FIRST_NODE, i)).getID(),
					 					   cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.LAST_NODE, i)).getID()
					   					    ).setResult(EdgeResults.PT, resultsStorage.getResEdgePT().get(i));
			 
			 cc.getEdgeByItsConnectedNodes(cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.FIRST_NODE, i)).getID(),
					 					   cc.getNodeByIDforReq(schemeParamsStorage.getEdgeParameter(NodesOfEdge.LAST_NODE, i)).getID()
					   					    ).setResult(EdgeResults.QT, resultsStorage.getResEdgeQT().get(i));
		 }
    }
    
   
}
