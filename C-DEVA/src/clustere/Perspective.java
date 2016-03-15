package clustere;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		
		String editArea = layout.getEditorArea();
		IFolderLayout tablefolder = layout.createFolder("table", IPageLayout.LEFT, 0.25f, editArea);
		tablefolder.addView("CDEVA.EdgeView");
		tablefolder.addView("CDEVA.view1");
	//	tablefolder.addView("CDEVA.view5");
		
	//	layout.addView("CDEVA.view1", IPageLayout.LEFT, 0.25f, editArea);
		
		IFolderLayout leftBottom = layout.createFolder("left", IPageLayout.BOTTOM, 0.73f,  "CDEVA.view1");
		leftBottom.addView("CDEVA.view3");   //current network
		leftBottom.addView("CDEVA.view4");  // using algorithms

	}
}
