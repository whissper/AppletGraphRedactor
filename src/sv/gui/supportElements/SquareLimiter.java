package sv.gui.supportElements;

/**
 * simple Square limiter for drawing special componentPanel
 * 
 * @author SAV2
 *
 */
public class SquareLimiter 
{

	public enum CoordinateType 
	{
	    X,
	    Y   
	}
	
	/**
	 * Calculated result
	 */
	private int result;
	
	//constructor start:
	public SquareLimiter(){
		
	}
	//constructor end;
	
	/**
	 * Getting result
	 * @param CoordinateType: coordType -- type of coordinate
	 * @param int: sX -- start X
	 * @param int: eX -- end X
	 * @param int: sY -- start Y
	 * @param int: eY -- end Y
	 * @return int: result
	 */
	public int getResult(CoordinateType coordType, int sX, int eX, int sY, int eY)
	{
		
		//limit for X
		if (coordType == CoordinateType.X)
		{
			this.result = this.squareCoordX(sX, eX, sY, eY);
		}
		//limit for Y
		else if (coordType == CoordinateType.Y)
		{
			this.result = this.squareCoordY(sX, eX, sY, eY);
		}
		
		return result;
	}
	
    /**
     * calculate Y coordinate while drawing square component 
     * 
     * @param sX -- start X
     * @param eX -- end X
     * @param sY -- start Y
     * @param eY -- end Y
     * @return int: Y-coordinate 
     */
    private int squareCoordY(int sX, int eX, int sY, int eY)
    {
    	int Y;
    	
    	// if Height >= Width
    	if( (Math.abs(sY-eY)>=Math.abs(sX-eX)) )
    	{	
    		// end is higher than start
    		if( (sY-eY)>=0 )
    		{
    			Y = sY - Math.abs(sX-eX);
    		}
    		// end is lower than start
    		else
    		{
    			Y = sY + Math.abs(sX-eX);
    		}
    	}
    	// if Height < Width
    	else
    	{
    		Y = eY;
    	}
    	
    	return Y;
    }
    
    /**
     * calculate X coordinate while drawing square component 
     * 
     * @param sX -- start X
     * @param eX -- end X
     * @param sY -- start Y
     * @param eY -- end Y
     * @return int: X-coordinate 
     */
    private int squareCoordX(int sX, int eX, int sY, int eY)
    {
    	int X;
    	
    	// if Width >= Height
    	if( (Math.abs(sX-eX)>=Math.abs(sY-eY)) )
    	{	
    		// end is located to the left of start
    		if( (sX-eX)>=0 )
    		{
    			X = sX - Math.abs(sY-eY);
    		}
    		// end is located to the right of start
    		else
    		{
    			X = sX + Math.abs(sY-eY);
    		}
    	}
    	// if Width < Height
    	else
    	{
    		X = eX;
    	}
    	
    	return X;
    }
}
