package clustere.pluginAction;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import clustere.Activator;
import clustere.editors.EvaluationView;
import clustere.editors.PredictionEditor;
import clustere.editors.PredictionEditorInput;
import clustere.views.PredictionView;

import com.wuxuehong.interfaces.NewAlgorithm;
import com.wuxuehong.interfaces.PredictionPlugin;

public class PluginPredictionAction extends Action{
	private PredictionPlugin section;
    private String editorID = "CDEVA.editor4";
    private String viewID = "CDEVA.view5";
	
	public PluginPredictionAction(NewAlgorithm section) {
		this.setText("&"+section.getAlgorithmName());
		this.section = (PredictionPlugin) section;
    }
	public void run() {
		IWorkbenchPage workbenchpage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		try {
			workbenchpage.showView(viewID);
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PredictionEditor.section = this.section;
		IEditorInput editorinput = PredictionEditorInput.getInstance();
		IEditorPart editor = workbenchpage.findEditor(editorinput);
		if (editor != null) {
			workbenchpage.bringToTop(editor);
		}else {
			try {
				workbenchpage.openEditor(editorinput, editorID);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//	section.getCandidateGenes(PredictionEditor.getMainComposite());
	}
	
}
