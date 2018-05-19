package sv.editor.interfaces;

import sv.editor.*; 

/**
 * ComponentSelectionListener
 * <p/>
 * Component selection listener interface
 *
 * @author SAV2
 * @version 1.0.0
 * @since 27.05.2012
 */
public interface ComponentSelectionListener {

    /**
     * Indicates that component passed was selected
     *
     * @param c selected component
     */
    public void componentSelected(ComponentPanel c);

}
