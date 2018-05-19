package sv.gui.additionalElements;

/**
 * calculated Edge results
 * @author SAV2
 *
 */
public enum EdgeResults 
{
	/**
	 * Active power flow at the beginning(<i>"From"</i>) of Edge
	 * <i>: (MW)</i>
	 */
	PF,
	
	/**
	 * Reactive power flow at the beginning(<i>"From"</i>) of Edge
	 * <i>: (MVar)</i>
	 */
	QF,
	
	/**
	 * Active power flow at the end(<i>"To"</i>) of Edge
	 * <i>: (MW)</i>
	 */
	PT,
	
	/**
	 * Reactive power flow at the end(<i>"To"</i>) of Edge
	 * <i>: (MVar)</i>
	 */
	QT
}
