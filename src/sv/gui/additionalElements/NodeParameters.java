package sv.gui.additionalElements;

/**
 * Node Parameters
 * 
 * @author SAV2
 */
public enum NodeParameters {
	/**
	 * Voltage level, <i>(kV)</i>
	 */
	Data_U,
	
	/**
	 * Phase Angle, <i>(degrees)</i>
	 */
	Data_Angle,
	
	/**
	 * Node Type, <i>(PQ/PV/Slack)</i>
	 */
	Data_Type,
	
	/**
	 * Active Power of generation, <i>(W)</i>
	 */
	Data_PG,
	
	/**
	 * Reactive Power of generation, <i>(var)</i>
	 */
	Data_QG,
	
	/**
	 * Active Power of consumption, <i>(W)</i>
	 */
	Data_PL,
	
	/**
	 * Reactive Power of consumption, <i>(var)</i>
	 */
	Data_QL
}
