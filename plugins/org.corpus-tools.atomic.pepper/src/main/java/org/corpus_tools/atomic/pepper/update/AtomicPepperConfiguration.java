/*******************************************************************************
 * Copyright 2015 Friedrich-Schiller-Universität Jena
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Stephan Druskat - initial API and implementation
 *******************************************************************************/
package org.corpus_tools.atomic.pepper.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import de.hu_berlin.german.korpling.saltnpepper.pepper.common.PepperConfiguration;

/**
 * TODO Description
 * <p>
 * @author Stephan Druskat <stephan.druskat@uni-jena.de>
 */
public class AtomicPepperConfiguration extends PepperConfiguration {

	/**
	 * Defines a static logger variable so that it references the {@link org.apache.logging.log4j.Logger} instance named "AtomicPepperConfiguration".
	 */
	private static final Logger log = LogManager.getLogger(AtomicPepperConfiguration.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3772390133156698540L;
	/** Folder containing all Pepper configuration files. */
	private static final String FILE_PEPPER_CONF = "config";
	/** Name of the files containing all Pepper properties. */
	public static final String FILE_PEPPER_PROP = "pepper.properties";
	private static final Bundle pepperBundle = Platform.getBundle("org.corpus_tools.atomic.pepper");

	/**
	 * Load Pepper properties from the file {@link #FILE_PEPPER_PROP}, located in folder {@link #FILE_PEPPER_CONF}.
	 */
	public void load() {
		log.trace("Finding Pepper home.");
		File pepperHome = findPepperHome();
		File propFile = new File(pepperHome.getAbsolutePath() + "/" + FILE_PEPPER_CONF + "/" + FILE_PEPPER_PROP + "/");
		log.trace("Constructed Pepper properties file: {}.", propFile.toString());
		log.trace("Loading Pepper properties file.");
		load(propFile);
	}

	/**
	 * Extracts and returns the path of the Pepper home directory within the Atomic folder structure at runtime.
	 *
	 * @return {@link java.io.File} representation of the Pepper home directory.
	 */
	private File findPepperHome() {
		File pepperHome = null;
		URL url = FileLocator.find(pepperBundle, new Path("/"), null);
		URL pepperHomeURL = null;
		try {
			pepperHomeURL = FileLocator.resolve(url);
			pepperHome = new File(pepperHomeURL.getFile());
		}
		catch (IOException e) {
			log.error("Could not resolve pepper home URL!", e);
		}
		return pepperHome;
	}

}
