package clustere.pluginAction;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import clustere.dialogs.ProgressBarDialog;
import clustere.views.PredictionView;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.EProteinPlugin;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.interfaces.NewAlgorithm;

public class PluginEProteinAction extends Action{
	
	 private EProteinPlugin section;
	 private ProgressBarDialog pbd;
	 
	public PluginEProteinAction(NewAlgorithm section){
		this.setText("&"+section.getAlgorithmName());
		this.section = (EProteinPlugin)section;
	}
	
	public void run(){
		calCulate();
	}

	public void calCulate(){

		String algorithName = section.getAlgorithmName();								
		new NewThread(algorithName);
	}
	
	class NewThread implements Runnable{
		String[] result;
		String algorithmName;
		Thread t ;
		NewThread(String algorithmName){
			this.algorithmName = algorithmName;
			t = new Thread(this);
			t.start();
		}
		public void run() {
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(new Shell(), SWT.NONE,t);
					pbd.getLabel().setText("Caculating essential proteins......");
				}
			});
			final HashMap<Node, Float> sProteins = section.getEssentialProteins();
			if(GraphInfo.edgelist.size()==0||sProteins==null||sProteins.isEmpty()){
				Display.getDefault().asyncExec(new Runnable(){
					public void run() {
						pbd.dispose();
						MessageDialog.openError(new Shell(), "Error", "Paramater is not available...or no file input!");
					}
				});
				return ;
			}
			
			
			
			PredictionView.EAlgorithmName = this.algorithmName;
			PredictionView.currentResult = sProteins;
			
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd.dispose();				
					PredictionView.update();
				}
			});
		}
	}
	

	
	
}
