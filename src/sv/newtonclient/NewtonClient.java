package sv.newtonclient;

import sv.editor.ComponentContainer;

/**
 * swing version of NewtonClient
 * @since 01.03.2013
 * @author SAV2
 * @see <i>adaptive version of NewtonClient for swing-applet.
 * <br />
 * Original version was written by Polubotko D.V.</i>
 */
public class NewtonClient 
{
	/**
	 * current component container reference
	 */
	private ComponentContainer compCont;
	
	/**
	 * Request thread
	 */
	private Request request;
	
	//constructor start:
	public NewtonClient(ComponentContainer compCont)
	{
		this.compCont = compCont;
	}
	//constructor end:
	
	/**
	 * do request to the server
	 */
	public void doRequest()
	{
		request = new Request(compCont);

		request.execute();
		
	}
	
	/**
	 * cancel request to the server
	 */
	public void cancelRequest()
	{
		request.cancel(true);
	}
	
	
}
