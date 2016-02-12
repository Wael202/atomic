/*******************************************************************************
 * Copyright 2016 Friedrich-Schiller-Universität Jena
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
package org.corpus_tools.atomic.projects.salt;

import static org.junit.Assert.*;

import org.corpus_tools.atomic.projects.Document;
import org.junit.Before;
import org.junit.Test;

import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SCorpus;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;

/**
 * Unit tests for {@link DocumentCreationRunnable}.
 *
 * <p>@author Stephan Druskat <stephan.druskat@uni-jena.de>
 *
 */
public class DocumentCreationRunnableTest {
	
	private DocumentCreationRunnable fixture = null;

	/**
	 * Sets the fixture
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		SCorpus corpus = SaltFactory.eINSTANCE.createSCorpus();
		corpus.setId("12345");
		corpus.setSName("corpus");
		setFixture(new DocumentCreationRunnable(corpus, new Document("document", "sourceText")));
	}

	/**
	 * Test method for {@link org.corpus_tools.atomic.projects.salt.DocumentCreationRunnable#run()}.
	 */
	@Test
	public void testRun() {
		getFixture().run();
		SDocument document = getFixture().getsDocument();
		SCorpus corpus = getFixture().getsCorpus();
		assertNotNull(corpus);
		assertNotNull(document);
		assertEquals("12345", corpus.getSId());
		assertEquals("corpus", corpus.getSName());
		assertEquals("document", document.getSName());
		assertEquals("sourceText", document.getSDocumentGraph().getSTextualDSs().get(0).getSText());
	}

	/**
	 * @return the fixture
	 */
	private DocumentCreationRunnable getFixture() {
		return fixture;
	}

	/**
	 * @param fixture the fixture to set
	 */
	private void setFixture(DocumentCreationRunnable fixture) {
		this.fixture = fixture;
	}

}
