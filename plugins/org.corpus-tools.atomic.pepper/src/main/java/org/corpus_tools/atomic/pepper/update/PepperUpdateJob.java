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
 *     Martin Klotz - nested class {@link ModuleTableReader} initial API
 *     					and implementation
 *******************************************************************************/
package org.corpus_tools.atomic.pepper.update;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.corpus_tools.atomic.pepper.AtomicPepperStarter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * TODO Description
 * <p>
 * 
 * @author Stephan Druskat <stephan.druskat@uni-jena.de>
 */
public class PepperUpdateJob extends Job {

	/**
	 * Defines a static logger variable so that it references the XML{@link org.apache.logging.log4j.Logger} instance named "PepperUpdateJob".
	 */
	private static final Logger log = LogManager.getLogger(PepperUpdateJob.class);
	
	/**
	 * @param name
	 */
	public PepperUpdateJob(String name) {
		super(name);
	}

	/**
	 * Handles the actual update process.
	 * 
	 * @copydoc @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		AtomicPepperStarter pepperStarter = new AtomicPepperStarter();
		pepperStarter.startPepper();

//		update();
		return Status.OK_STATUS;
	}

	/**
	 * TODO: Description
	 *
	 */
	private void update() {
		// TODO Auto-generated method stub
		
	}
}
