package com.wuxuehong.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;

import com.wuxuehong.bean.Node;

/**
 * 
 * @author Tangyu
 * @date: 2015年6月2日 下午5:34:11
 * add interface for disease gene prediction plug-in
 *
 */
public interface PredictionPlugin extends NewAlgorithm{
	 /**
     * 
     * @return the candidate disease genes
     */
	ArrayList<String> diseaseCasusingGenes = new ArrayList<String>();
    abstract public HashMap<Node, Float> getCandidateGenes(Composite composite);

}
