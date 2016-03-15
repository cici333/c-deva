package clustere.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.osgi.internal.module.GroupingChecker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.WorkbenchPage;

import clustere.Activator;
import clustere.dialogs.ProgressBarDialog;
import clustere.editors.ClusterEditor;
import clustere.editors.ClusterEditorInput;
import clustere.editors.EvaluationView;
import clustere.editors.NetworkView;
import clustere.editors.NetworkViewInput;
import clustere.pluginAction.PluginAlgorithmAction;
import clustere.pluginLoader.LoaderServer;
import clustere.treeViewer.TreeElement;
import clustere.views.AlgorithmInfoView;
import clustere.views.CurrentNetworkView;
import clustere.views.EdgeView;
import clustere.views.ViewPart1;
import clustere.views.ViewPart2;

import com.wuxuehong.bean.AlgorithmColor;
import com.wuxuehong.bean.AlgorithmInfo;
import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.bean.Paramater;
import com.wuxuehong.interfaces.GraphInfo;
import com.wuxuehong.bean.*;

public class OpenFileAction extends Action {
	
	public OpenFileAction(){
		super("&Open",Action.AS_DROP_DOWN_MENU);
		setToolTipText("Open File");
		setImageDescriptor(clustere.Activator.getImageDescriptor("/icons/file_open.ico"));
		setMenuCreator(new IMenuCreator() {
			@Override
			public Menu getMenu(Menu parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Text File");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openTextFile();					
						new ShowEditorAction();
						
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Clusters");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
//						openExistClusters();
						openSavedFile();
					}
				});
				MenuItem item3 = new MenuItem(menu,SWT.CASCADE);
				item3.setText("&Clusters from other tools");
				
				Menu toolsMenu = new Menu(item3);
				item3.setMenu(toolsMenu);
				
				MenuItem mcodeMenu = new MenuItem(toolsMenu, SWT.NONE);
				mcodeMenu.setText("&MCODE");
				mcodeMenu.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openSavedFileFromMCODE();
					}
				});
				
				MenuItem cytocluterMenu = new MenuItem(toolsMenu, SWT.NONE);
				cytocluterMenu.setText("&CytoCluster");
				cytocluterMenu.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openSavedFileFromCytoCluster();
					}
				});
				
				MenuItem cluster1Menu = new MenuItem(toolsMenu, SWT.NONE);
				cluster1Menu.setText("&CluterOne");
				cluster1Menu.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openSavedFileFromClusterOne();
					}
				});
				return menu;
			}
			
			@Override
			public Menu getMenu(Control parent) {
				// TODO Auto-generated method stub
				Menu menu = new Menu(parent);
				MenuItem item1 = new MenuItem(menu,SWT.NONE);
				item1.setText("&Text File");
				item1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						openTextFile();
						new ShowEditorAction();
					}
				});
				MenuItem item2 = new MenuItem(menu,SWT.NONE);
				item2.setText("&Clusters");
				item2.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
	//					openExistClusters();
						openSavedFile();
					}
				});
				return menu;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void run(){
		openTextFile();
	}
	
	/**
	 * �����µ��ļ���ʱ�����б���ˢ��
	 */
	public void paramRefresh(){
		GraphInfo.refresh();                //����װ������Ϣ������ ��ʼ��
    	ClusterEditor.refresh();           //��ʾ�صı༭�� �����ʼ��
    	Paramater.refresh();				//��ʼ����ϵͳ����
    	EvaluationView.refresh();			//���������༭�� �����ʼ��
    	ViewPart1.refresh();          //��ͼһ������������ͼ ��ʼ��
    	ViewPart2.refresh();            //��ͼ�����������磩��ͼ��ʼ��
    	CurrentNetworkView.refresh();  //��ǰ������Ϣ��ͼ  ��ʼ��
    	AlgorithmInfoView.refresh();      //��ǰ�㷨��Ϣ��ͼ ��ʼ��
    	LoaderServer.pluginRefresh();       //����б�����ʼ��
    	IWorkbenchPage workbenchpage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	workbenchpage.closeAllEditors(true);
	}
	/**
	 *@author TangYu
	 *@modify_date: 2014��9��1�� ����4:25:50
	 *@description: import protein interaction network file(.txt)
	 * 
	 */
	public void openTextFile(){
		if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0){
		   boolean confirm = MessageDialog.openConfirm(new Shell(), "Warning", "A file has been inputed,Do you really want to open a new ?");
		    if(!confirm)
		          return;
		}
		FileDialog openDialog = new FileDialog(new Shell(), SWT.OPEN);
		openDialog.setText("Choose the file");
		openDialog.setFilterExtensions(new String[] { "*.*","*.txt" });
		openDialog.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		openDialog.setFilterPath("c:/");
		String name = openDialog.open();
		if (name == null || name.equals("")){
			return;
		}else{
			if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0)
			paramRefresh();
		}
		BufferedReader br = null;
		String str;
		Scanner s;
		String tempstr1;
		String tempstr2;
		Node tempnode1;
		Node tempnode2;
		Edge tempedge;
		try {
			br = new BufferedReader(new FileReader(new File(name)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int i = 0, j = 0;
		try {
			str = br.readLine();
			while (str != null) {
				i++;
				str = str.toUpperCase();
				s = new Scanner(str);
				if(s.hasNext()){
					tempstr1 = s.next(); // ��һ���ڵ�
					if(s.hasNext()){
						tempstr2 = s.next(); // �ڶ����ڵ�
						if(!tempstr1.isEmpty() && !tempstr2.isEmpty()){
							if(tempstr1.equals(tempstr2)){
								if(!GraphInfo.nodemap.containsKey(tempstr1)){
									tempnode1 = new Node(tempstr1);
									GraphInfo.nodemap.put(tempstr1, tempnode1);
									GraphInfo.nodelist.add(tempnode1);						
								}else{
									tempnode1 = GraphInfo.nodemap.get(tempstr1);						
								}
								tempnode1.getNeighbours().add(tempnode1);
								tempedge = new Edge(tempnode1, tempnode1);
								
							}else{
								if(!GraphInfo.nodemap.containsKey(tempstr1)){
									tempnode1 = new Node(tempstr1);
									GraphInfo.nodemap.put(tempstr1, tempnode1);
									GraphInfo.nodelist.add(tempnode1);						
								}else{
									tempnode1 = GraphInfo.nodemap.get(tempstr1);						
								}
								if(!GraphInfo.nodemap.containsKey(tempstr2)){
									tempnode2 = new Node(tempstr2);
									GraphInfo.nodemap.put(tempstr2, tempnode2);
									GraphInfo.nodelist.add(tempnode2);		
								}else{
									tempnode2 = GraphInfo.nodemap.get(tempstr2);						
								}
								tempnode1.getNeighbours().add(tempnode2);						
								tempnode2.getNeighbours().add(tempnode1);
								tempedge = new Edge(tempnode1, tempnode2);
							}												
							/**
							 * The third string: edge weight
							 */
							if(s.hasNextDouble()) {
								tempedge.setWeight(s.nextDouble());
							}							
							GraphInfo.edgelist.add(tempedge);
							tempnode1.AddAdjacentEdges(tempedge);
							
						}
					}else if(!tempstr1.isEmpty() && !GraphInfo.nodemap.containsKey(tempstr1)){
						tempnode1 = new Node(tempstr1);
						GraphInfo.nodemap.put(tempstr1, tempnode1);
						GraphInfo.nodelist.add(tempnode1);											
					}									
				}				
				str = br.readLine();
			}
			br.close();
	
		} catch (Exception e) {
		 MessageDialog.openError(null, "ERROE", "File read exception at Line"+ i +"!");
		 GraphInfo.refresh();
		 return;
		}
		CurrentNetworkView.update();
		NetworkView.update();
		EdgeView.update();
		MessageDialog.openInformation(new Shell(), "Information", "Total Nodes:"+GraphInfo.nodelist.size()+" \n Total Edges:"+GraphInfo.edgelist.size());
	}

	public void openNetworkView(){
		String editorID = "CDEVA.editor3";
		 IEditorInput editorInput = NetworkViewInput.getInstance();
		 IWorkbenchPage workbenchPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 NetworkView editor = null;
		try {
			editor = (NetworkView)workbenchPage.openEditor(editorInput, editorID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�걣���cluster�ļ���  ��ʼ����Ӧ�ı���
	 * @param algorithmName
	 * @param v
	 * @param rootElement
	 */
	public void initVariable(String algorithmName,Vector[] v,TreeElement rootElement){
		Paramater.algorithmsResults.put(algorithmName, v);
		ViewPart1.list.add(rootElement);
		ViewPart1.updateInput();
		getAlgorithmResultInfo(v, algorithmName);   //��ʾ�㷨��������Ϣ
		EvaluationView.algorithms.add(algorithmName);
		EvaluationView.ac.add(new AlgorithmColor(algorithmName,new RGB(250,0,0)));
		EvaluationView.updateCombo(algorithmName);
		EvaluationView.updateGroup(algorithmName);
		EvaluationView.updateTable();
		CurrentNetworkView.update();    //��ǰ���� ��Ϣ ����
	}
	
	public void getAlgorithmResultInfo(Vector[] v,String name){
		int max = 0;
		int min = Integer.MAX_VALUE;
		int total = 0;
		for(int i=0;i<v.length;i++){
			int size = v[i].size();
			if(size>max)max = size;
			if(size<min)min = size;
			total+=size;
		}
		float degree = (float)total/(float)v.length;
		AlgorithmInfo ai = new AlgorithmInfo();
		ai.setName(name);
		ai.setDegree(degree);
		ai.setMax(max);
		ai.setMin(min);
		ai.setTotalCluster(v.length);
		AlgorithmInfoView.update(ai);        //�㷨��Ϣ��ͼ����
	}
	
	
	private Vector<Node>[] v;
	private String algorithmName;
	
	/**
	 * ��ȡ����ľ�����
	 */
	public void openSavedFile(){
		if(GraphInfo.nodelist.size()!=0||ViewPart1.list.size()!=0||Paramater.algorithmsResults.size()!=0){
			   boolean confirm = MessageDialog.openConfirm(new Shell(), "Warning", "A file has been inputed,Do you really want to open a new ?");
			    if(!confirm)
			          return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			System.out.println("ûѡ�κ��ļ�");
			return;
		}else {
			paramRefresh();
			System.out.println("��ʼ���ļ�");
		}
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			
			int index1 = str.indexOf(':');		
			algorithmName = str.substring(0,index1);
			int size = Integer.parseInt(str.substring(index1+1, str.length()));
			 v = new Vector[size];

			str = br.readLine();
		//	str = br.readLine();
			Scanner s  = null;
			String tempstr1,tempstr2;
			Node tempnode1,tempnode2;
			Edge tempedge;
			while (str != null) {               //��ȡԭʼ�����ļ�
				if(str.startsWith("******"))break;
				str = str.toUpperCase();
				s = new Scanner(str);
				while (s.hasNext()) {
					
					tempstr1 = s.next(); // ��һ���ڵ�
					
					tempnode1 = new Node(tempstr1);
					if (!GraphInfo.nodemap.containsKey(tempstr1)) {
						GraphInfo.nodemap.put(tempstr1, tempnode1);
						GraphInfo.nodelist.add(tempnode1);
					} else{
						tempnode1 = GraphInfo.nodemap.get(tempstr1);
					}
					
					tempstr2 = s.next(); // �ڶ����ڵ�
					
					tempnode2 = new Node(tempstr2);
					if (!GraphInfo.nodemap.containsKey(tempstr2)) {
						GraphInfo.nodemap.put(tempstr2, tempnode2);
						GraphInfo.nodelist.add(tempnode2);
					} else{
						tempnode2 = GraphInfo.nodemap.get(tempstr2);
					}
					
					tempnode1.getNeighbours().add(tempnode2);
					tempnode2.getNeighbours().add(tempnode1);
					tempedge = new Edge(tempnode1, tempnode2);
					GraphInfo.edgelist.add(tempedge);
		//			}
			//		break;
				}
				str = br.readLine();
			}
			
	//		HashMap<String ,Node> nodemap = new HashMap<String,Node>();
			int index = -1;			
			if(str!=null&&str.startsWith("******")){
				str = br.readLine();
				while(str!=null){
					
					if(str.startsWith("Cluster:")){
						index ++;
						v[index] = new Vector<Node>();
					}else{
						str = str.trim().toUpperCase();
						Node n = GraphInfo.nodemap.get(str);
						/*if(n==null){
							n = new Node(str);
							nodemap.put(str, n);
						}						
						v[index].add(n);*/
						if (n != null) {
							v[index].add(n);
						}						
						
					}
					str = br.readLine();
				}

			}
		}catch(Exception e){
			e.printStackTrace();
			 MessageDialog.openError(null, "ERROE", "File read exception!");
			 GraphInfo.refresh();
			 return;
		}
		new NewThread();
	}
	
	/**
	 * ��ȡ��������ʼ��
	 * @param algorithmName
	 * @param v
	 * @param rootElement
	 */
	public void initVariable(String algorithmName,Vector[] v){
		ViewPart1.updateInput();
		getAlgorithmResultInfo(v, algorithmName);   //��ʾ�㷨��������Ϣ
		EvaluationView.algorithms.add(algorithmName);
		EvaluationView.ac.add(new AlgorithmColor(algorithmName,new RGB(250,0,0)));
		EvaluationView.updateCombo(algorithmName);
		EvaluationView.updateGroup(algorithmName);
		EvaluationView.updateTable();
		CurrentNetworkView.update();    //��ǰ���� ��Ϣ ����
	}
	
	class NewThread implements Runnable{
		
		ProgressBarDialog pbd = null;
		Thread t ;
		NewThread(){
			t = new Thread(this);
			t.start();
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					pbd = new ProgressBarDialog(new Shell(), SWT.NONE,t);
					pbd.getLabel().setText("Calculating clusters......");
				}
			});
				buildTree(v, algorithmName);
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					
					pbd.getLabel().setText("init......");
					initVariable(algorithmName, v);
					pbd.dispose();
				}
			});
		}
		
	}
	
	
	/**
	 * ����
	 * @param v
	 * @param name
	 */
	public void buildTree(Vector<Node>[]v ,String name){
		Paramater.algorithmsResults.put(name, v);
		HashMap<String,Node> tempMap = new HashMap<String,Node>();
		TreeElement parentElement = new TreeElement(name+"("+v.length+")");
		for(int i=0;i<v.length;i++){
			Vector<Node> temp = v[i];
			TreeElement te = new TreeElement("Cluster:"+i+"("+v[i].size()+")");
			Vector<Edge> tempedge = new Vector<Edge>();
			for(int j=0;j<temp.size();j++){
				 Node n1 = temp.get(j);
				for(int k=j+1;k<temp.size();k++){
					Node n2 = temp.get(k);
					if(n1.getNeighbours().contains(n2)){
						n1.getNeighbours().add(n2);
						n2.getNeighbours().add(n1);
						tempedge.add(new Edge(n1,n2));
					}
				}
			}
			String str = "Cluster"+i;
			Node node = new Node(str);
			node.setScope(v[i].size());
			tempMap.put(str, node);
			te.setNode(node);
			te.setNodes(temp);
			te.setEdges(tempedge);
			parentElement.addChild(te);
		}
		calOvalap(v, parentElement,tempMap);      //�����֮��Ľ������
		ViewPart1.list.add(parentElement);
	}
	/**
	 * ��������֮��Ľ������
	 * @param v
	 * @param parentElement
	 * @param tempMap
	 */
	public void calOvalap(Vector[] v,TreeElement parentElement,HashMap<String,Node> tempMap){
		Vector<Node> tempNode = new Vector<Node>();
		Vector<Edge> tempEdge = new Vector<Edge>();
		tempNode.addAll(tempMap.values());
		for(int i=0;i<v.length;i++){
			Vector<Node> v1 = v[i];
			for(int j=i+1;j<v.length;j++){
				Vector<Node> v2 = v[j];
				if(CheckOvalap(v1,v2)){
					Node node1 = tempMap.get("Cluster"+i);
					Node node2 = tempMap.get("Cluster"+j);
					node1.getNeighbours().add(node2);
					node2.getNeighbours().add(node1);
					Edge edge = new Edge(node1,node2);
					tempEdge.add(edge);
				}
			}
		}
		parentElement.setNodes(tempNode);
		parentElement.setEdges(tempEdge);
	}
	
	public boolean CheckOvalap(Vector<Node> v1,Vector<Node> v2){
		for(int i=0;i<v1.size();i++){
			Node n1 = v1.get(i);
			if(v2.contains(n1))return true;
		}
		return false;
	}
	
	/**
	 * ��ȡMCODE����ľ�����
	 */
	public void openSavedFileFromMCODE(){
		if(GraphInfo.nodelist.size()==0){
			    MessageDialog.openWarning(new Shell(), "Warning", "Please input the network file first!");
			    return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			return;
		}
		try{
			algorithmName = "MCODE(Cytoscape)";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str;			
			ArrayList<Vector<Node>> tempClusters = new ArrayList<Vector<Node>>();	
			
			Scanner s;			
			int index;			
			int lineIndex = 0;
			
			while((str = br.readLine()) !=null){
				if( lineIndex++ > 9 ){
					s = new Scanner(str);
					index = 0;
					Vector<Node> cNodes = new Vector<Node>();
					while(s.hasNext()) {
						if(index++ < 4) {
							s.next();
						} else {
							String nodeName = s.next();
							if (nodeName.endsWith(",")) {
								nodeName = nodeName.substring(0, nodeName.length()-1);
							}
							System.out.println( GraphInfo.nodemap.size());
							Node n = GraphInfo.nodemap.get(nodeName.toUpperCase());
							if (n != null) {
								cNodes.add(n);
							}							
						}	
					}
					if(!cNodes.isEmpty()) {
						tempClusters.add(cNodes);
					}
					
				}				
			}		
			v = tempClusters.toArray(new Vector[tempClusters.size()]);
			MessageDialog.openWarning(new Shell(), "Warning", tempClusters.size() + " Clusters from MCODE(Cytoscape) have been input into C-DEVA");
			
			
		}catch(Exception e){
			e.printStackTrace();
			 MessageDialog.openError(null, "ERROE", "File read exception!");
			 GraphInfo.refresh();
			 return;
		}
		new NewThread();
	}
	
	
	/**
	 * ��ȡClusterOne����ľ�����
	 */
	public void openSavedFileFromClusterOne(){
		if(GraphInfo.nodelist.size()==0){
			    MessageDialog.openWarning(new Shell(), "Warning", "Please input the network file first!");
			    return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			return;
		}
		try{
			algorithmName = "ClusterOne(Cytoscape)";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str;			
			ArrayList<Vector<Node>> tempClusters = new ArrayList<Vector<Node>>();	
			
			Scanner s;		
			
			while((str = br.readLine()) !=null){
				s = new Scanner(str);
				Vector<Node> cNodes = new Vector<Node>();
				while(s.hasNext()) {
					String nodeName = s.next();
					if (nodeName.endsWith(",")) {
						nodeName = nodeName.substring(0, nodeName.length()-1);
					}
					System.out.println( GraphInfo.nodemap.size());
					Node n = GraphInfo.nodemap.get(nodeName.toUpperCase());
					if (n != null) {
						cNodes.add(n);
					}	
				}
				if(!cNodes.isEmpty()) {
					tempClusters.add(cNodes);
				}				
			}		
			v = tempClusters.toArray(new Vector[tempClusters.size()]);
			MessageDialog.openWarning(new Shell(), "Warning", tempClusters.size() + " Clusters from ClusterOne(Cytoscape) have been input into C-DEVA");
			
			
		}catch(Exception e){
			e.printStackTrace();
			 MessageDialog.openError(null, "ERROE", "File read exception!");
			 GraphInfo.refresh();
			 return;
		}
		new NewThread();
	}
	
	/**
	 * ��ȡCytoCluster����ľ�����
	 */
	public void openSavedFileFromCytoCluster(){
		if(GraphInfo.nodelist.size()==0){
			    MessageDialog.openWarning(new Shell(), "Warning", "Please input the network file first!");
			    return;
			}
		FileDialog fd = new FileDialog(new Shell(),SWT.OPEN);
		fd.setFilterExtensions(new String[] { "*.*","*.txt" });
		fd.setFilterNames(new String[] {"�����ļ�*.*", "�ı���ʽ(*.txt)" });
		String filename = fd.open();
		if(filename==null||filename.equals("")){
			return;
		}
		try{
			algorithmName = "CytoCluster(Cytoscape)";
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str;			
			ArrayList<Vector<Node>> tempClusters = new ArrayList<Vector<Node>>();	
			
			Scanner s;		
			int lineIndex = 0;
			Vector<Node> cNodes = null;
			while((str = br.readLine()) !=null){
				if( lineIndex++ > 8 ) {					
					if(str.startsWith("Complex")){
						if(cNodes != null && !cNodes.isEmpty()) {
							tempClusters.add(cNodes);
						}
						cNodes = new Vector<Node>();
						}else{
							str = str.trim().toUpperCase();
							Node n = GraphInfo.nodemap.get(str);
							if (n != null) {
								cNodes.add(n);
							}												
					}			
				}
			}	
			if(cNodes != null && !cNodes.isEmpty()) {
				tempClusters.add(cNodes);
			}
			v = tempClusters.toArray(new Vector[tempClusters.size()]);
			MessageDialog.openWarning(new Shell(), "Warning", tempClusters.size() + " Clusters from ClusterOne(Cytoscape) have been input into C-DEVA");
			
			
		}catch(Exception e){
			e.printStackTrace();
			 MessageDialog.openError(null, "ERROE", "File read exception!");
			 GraphInfo.refresh();
			 return;
		}
		new NewThread();
	}
}
