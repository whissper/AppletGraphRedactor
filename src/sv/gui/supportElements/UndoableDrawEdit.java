package sv.gui.supportElements;

import java.util.LinkedList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import sv.editor.ComponentContainer;
import sv.editor.ComponentPanel;

public class UndoableDrawEdit extends AbstractUndoableEdit
{
	  private ComponentContainer compContPanel;

	  private List<ComponentPanel> compPanelList;
	  private List<ComponentPanel> savedCompPanelList;

	  public UndoableDrawEdit(ComponentContainer compContPanel)
	  {
		  this.compContPanel = compContPanel;
		  compPanelList = new LinkedList<ComponentPanel>();
		  savedCompPanelList = new LinkedList<ComponentPanel>();
	    
		  compPanelList.clear();
		  for (ComponentPanel cp : compContPanel.getComps())
		  {
			  compPanelList.add(cp);
		  }
	  }
	  
	  //redo
	  public void redo() throws CannotRedoException
	  {
		  super.redo();
	    
		  if (savedCompPanelList == null)
		  {
			  // Should never get here, as super() doesn't permit redoing
			  throw new CannotRedoException();
		  }
		  else 
		  {
			  compContPanel.resetCompPanels(savedCompPanelList);
			  savedCompPanelList.clear();
		  }
	  }
	  
	  //undo
	  public void undo() throws CannotUndoException
	  {
		  super.undo();
	    
		  savedCompPanelList.clear();
		  for (ComponentPanel cp : compContPanel.getComps())
		  {
			  savedCompPanelList.add(cp);
		  }
	    
		  compContPanel.resetCompPanels(compPanelList);
	  }
}
