package clustere.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import clustere.editors.NetworkView;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;

public class EdgeView extends ViewPart{

	private static Table edgesTable;
	public static HashMap<Edge, Double> edgesMap;
	public static List<String> formerSelectedEdges; // save the locations of selected nodes
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		SashForm sashForm = new SashForm(parent,SWT.HORIZONTAL);
		edgesTable = new Table(sashForm, SWT.FULL_SELECTION | SWT.MULTI);
		edgesTable.setHeaderVisible(true);
		edgesTable.setLinesVisible(true);
		edgesTable.addSelectionListener(new Controllor());
		
	}
	
	
	public static void update(){
		
		if(edgesTable==null||edgesTable.isDisposed())
			return;		
		edgesTable.removeAll();
		
		if (edgesTable.getColumnCount() <= 1) {
			TableColumn tc1 = new TableColumn(edgesTable,SWT.LEFT);
			tc1.setText("Edge");
			tc1.setWidth(80);
			edgesMap = new HashMap<Edge, Double> ();
			for(Edge edge: GraphInfo.edgelist){			
				edgesMap.put(edge, edge.getWeight());
			}
		}
		
				
		TableColumn valueCol = new TableColumn(edgesTable,SWT.LEFT);
		valueCol.setText("Weight");
		valueCol.setWidth(80);
		
		//sort
		List<Map.Entry<Edge, Double>> sortedResult = new ArrayList<Map.Entry<Edge, Double>>
		                                                                        (edgesMap.entrySet());
		Collections.sort(sortedResult, 
				 new Comparator<Map.Entry<Edge, Double>> () {
			@Override
			public int compare(Entry<Edge, Double> o1, Entry<Edge, Double> o2) {
				// TODO Auto-generated method stub
				return o2.getValue().compareTo(o1.getValue());
			}	
		});
		
		for(Entry<Edge, Double> entry : sortedResult) {
			TableItem item = new TableItem(edgesTable,SWT.LEFT);
			item.setText(0, entry.getKey().getNode1().getNodeID()+"-"+entry.getKey().getNode2().getNodeID());
			item.setText(1, entry.getValue()+"");
			
			
		}		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	class Controllor extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			
			GC gc = new GC(NetworkView.getCanvas());
			
			if(formerSelectedEdges != null && !formerSelectedEdges.isEmpty()) {
				/**
				 * cover the red lines
				 */
				for(String locs :  formerSelectedEdges) {
					String[] locPair = locs.split("-");	
					/**
					 * redraw the black lines
					 */					
					gc.drawLine(Integer.parseInt(locPair[0]), Integer.parseInt(locPair[1]),
							Integer.parseInt(locPair[2]), Integer.parseInt(locPair[3]));
				}			
			}
			formerSelectedEdges = new ArrayList<String>();
			TableItem[] items = edgesTable.getSelection();
			gc.setForeground(new Color(Display.getDefault(),new RGB(255,0,0)));
			for(TableItem item : items) {
				String[] nodes = item.getText(0).split("-");
				Node node1 = GraphInfo.nodemap.get(nodes[0]);
				Node node2 = GraphInfo.nodemap.get(nodes[1]);
				
				gc.drawLine((int) node1.getMx(), (int) node1.getMy(),
							(int) node2.getMx(), (int) node2.getMy());	
				formerSelectedEdges.add(node1.getMx()+"-"+node1.getMy()+"-"+node2.getMx()+"-"+node2.getMy());
				
			}
			gc.setForeground(Paramater.FORE_COLOR);
		}
	}
	
	

}
