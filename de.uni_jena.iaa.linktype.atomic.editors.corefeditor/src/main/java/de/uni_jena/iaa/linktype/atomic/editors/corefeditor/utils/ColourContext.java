/**
 * Copyright (c) 2014 Stephan Druskat. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Stephan Druskat - initial API and implementation
 */
 package de.uni_jena.iaa.linktype.atomic.editors.corefeditor.utils;

/**
 * @author Stephan Druskat
 *
 */
public class ColourContext {
	
	private ColourStrategy strategy;
	 
    public ColourContext(ColourStrategy strategy) {
        this.strategy = strategy;
    }
    
    public float executeStrategy(float f) {
        return this.strategy.execute(f);
    }

	public float executeStrategy(float f, double d) {
		return this.strategy.execute(f, d);
	}

}
