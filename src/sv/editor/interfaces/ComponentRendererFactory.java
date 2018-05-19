package sv.editor.interfaces;

import javax.swing.JComponent;

/**
 * ComponentRendererFactory
 *
 * @author SAV2
 * @version 1.0.0
 * @since 25.07.2012
 */
public interface ComponentRendererFactory {

    /**
     * Creates component renderer
     *
     * @return renderer component
     */
    public JComponent createRenderer();
    
    /**
     * Reduces component counter
     */
    public void reduceCounter();
    
    /**
     * Sets value of component counter 
     * @param value : int
     */
    public void setCounter(int value);
    
    /**
     * get component counter
     * @return value of current counter
     */
    public int getCounter();

}
