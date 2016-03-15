package clustere.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;


import com.wuxuehong.bean.AlgorithmColor;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.PredictionPlugin;

public class PredictionEditor extends EditorPart {

	public static final String NAME = "disease genes predition";
	public static Composite mainComposite;
	public static SashForm sashForm;
	private static Label label;
	
	public static PredictionPlugin section = null;
	public static List<AlgorithmColor> ac = new ArrayList<AlgorithmColor>();
	public static Vector<Button> buttonlist = new Vector<Button>();
	private static Table geneTable;
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		setSite(site);
        setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		sashForm = new SashForm(parent,SWT.HORIZONTAL);
//		mainComposite = new Composite(sashForm,SWT.BORDER);
//		Composite controlComposite = new Composite(sashForm,SWT.BORDER);
	//	sashForm.setWeights(new int[]{2,1});
//		mainComposite.setLayout(new GridLayout());
		
		geneTable = new Table(sashForm, SWT.FULL_SELECTION|SWT.MULTI);
		geneTable.setHeaderVisible(true);
		geneTable.setLinesVisible(true);
		geneTable.setSize(90, 200);
		TableColumn tc1 = new TableColumn(geneTable,SWT.LEFT);
		tc1.setText("Protein");
		tc1.setWidth(80);
		for(Node node : GraphInfo.nodelist){			
			TableItem item = new TableItem(geneTable,SWT.LEFT);
			item.setText(node.getNodeID());
		}
		
		Button button1 = new Button(sashForm,SWT.NONE);
		button1.setText("Please select the disease causing genes from the left list-Confirm");
		button1.addSelectionListener(new Controllor());
		
	}
	

	public static Composite getMainComposite(){
		return sashForm;
	}
	
	public static void refresh(){
		ac.clear();
	//	algorithms.clear();
		buttonlist.clear();
		section = null;
	}
	
	public static void updateLabel(String name){
		label.setText(name);
	}
	
	
	class Controllor extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			if(geneTable.getSelection().length != 0) {
				for (TableItem ta : geneTable.getSelection()) {
					section.diseaseCasusingGenes.add(ta.getText());
				}
				section.getCandidateGenes(mainComposite);
			} else {
				MessageDialog.openWarning(geneTable.getShell(), "waning", "No disease gene was selected!");
			}
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
