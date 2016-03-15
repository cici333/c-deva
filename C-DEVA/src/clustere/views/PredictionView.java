package clustere.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.InitialContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;

public class PredictionView extends ViewPart{

	private static Table geneTable;
	//private static Composite mainComposite;
	public static HashMap<Node, HashMap<String, Float>> proteinMap;
	public static HashMap<Node, Float> currentResult;
	public static String EAlgorithmName;
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		SashForm sashForm = new SashForm(parent,SWT.HORIZONTAL);
		geneTable = new Table(sashForm, SWT.FULL_SELECTION);
		geneTable.setHeaderVisible(true);
		geneTable.setLinesVisible(true);
//		initTable();
		
	}
	
	
	public void initTable(){
		for(Node node : GraphInfo.nodelist){			
			proteinMap.put(node, new HashMap<String, Float>());
		}
	}
	
	public static void update(){
		
		if(geneTable==null||geneTable.isDisposed())
			return;		
		geneTable.removeAll();
		
		if (geneTable.getColumnCount() <= 1) {
			TableColumn tc1 = new TableColumn(geneTable,SWT.LEFT);
			tc1.setText("Protein");
			tc1.setWidth(80);
			proteinMap = new HashMap<Node, HashMap<String,Float>>();
			for(Node node : GraphInfo.nodelist){			
				proteinMap.put(node, new HashMap<String, Float>());
			}
		}
		
		for(Entry<Node, Float> en : currentResult.entrySet()) {			
			proteinMap.get(en.getKey()).put(EAlgorithmName, en.getValue());
		}
		
		TableColumn valueCol = new TableColumn(geneTable,SWT.LEFT);
		valueCol.setText(EAlgorithmName);
		valueCol.setWidth(80);
		
		//sort
		List<Map.Entry<Node, HashMap<String, Float>>> sortedResult = new ArrayList
				                                     <Map.Entry<Node, HashMap<String, Float>>>
		                                                (proteinMap.entrySet());
		Collections.sort(sortedResult, 
				 new Comparator<Map.Entry<Node, HashMap<String, Float>>> () {
			@Override
			public int compare(Entry<Node, HashMap<String, Float>> o1, Entry<Node, HashMap<String, Float>> o2) {
				// TODO Auto-generated method stub
				return o2.getValue().get(EAlgorithmName).compareTo(o1.getValue().get(EAlgorithmName));
			}	
		});
		
		for(Entry<Node, HashMap<String, Float>> entry : sortedResult) {
			TableItem item = new TableItem(geneTable,SWT.LEFT);
			item.setText(0, entry.getKey().getNodeID());
			for(int i = 1; i < geneTable.getColumnCount(); i++) {
				item.setText(i, entry.getValue().get(geneTable.getColumn(i).getText())+"");
			}
			
		}		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
