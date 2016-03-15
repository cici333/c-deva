package com.wuxuehong.interfaces;

import java.util.HashMap;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Composite;

import com.wuxuehong.bean.Node;

/**
 * 
 * @author Tangyu
 * @date: 2015年6月2日 下午5:34:11
 * add interface for disease gene prediction plug-in
 *
 */
public interface EProteinPlugin extends NewAlgorithm{
	 /**
     * 
     * @return the essential proteins
     */
    abstract public HashMap<Node, Float> getEssentialProteins();

}
