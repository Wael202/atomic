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
package org.corpus_tools.atomic.projects.wizard;

import java.util.ArrayList; 
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.corpus_tools.atomic.projects.Corpus;
import org.corpus_tools.atomic.projects.Document;
import org.corpus_tools.atomic.projects.ProjectNode;
import org.corpus_tools.atomic.ui.api.ExtendedViewerSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.swt.widgets.Group;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * A wizard page for the user to construct the structure of a project.
 * <p>
 * FIXME: SWTBot test this class!
 * 
 * @author Stephan Druskat <stephan.druskat@uni-jena.de>
 */
public class NewAtomicProjectWizardPageProjectStructure extends WizardPage {
	private DataBindingContext bindingContext;
	private Text nameText;
	private Text addSubCorpusNameText;
	private Text addDocumentNameText;
	private Text documentNameText;
	private Text sourceTextText;
	private Corpus model = createNewProject();
	private Text projectNameText;
	private Set<Control> corpusConstrols = new HashSet<>(), documentControls = new HashSet<>();
	private TreeViewer projectTreeViewer;
	private Button btnRemoveElement;
	private Button btnNewDocument;
	private Button btnNewSubCorpus;
	private Group grpDocument;
	private Label lblSourceText;
	private Button browseSourceTextBtn;
	private Label lblName;

	/**
	 * Default constructor calling the constructor {@link #NewAtomicProjectWizardPageProjectStructure(String)} with the default page name.
	 */
	public NewAtomicProjectWizardPageProjectStructure() {
		super("Create the project structure");
		setTitle("Create the project structure");
		setDescription("Create the structure of the new project by adding corpora, subcorpora, and documents.");
		/*
		 * FIXME TODO: Add context-sensitive help to Atomic, the the "?" button will show in the wizard. Add the following description to a help "window" of sorts: Every corpus must have a name and can contain n (sub-) corpora and n
		 * documents. Every document must have a name and must contain one source text. Must include Eclipse Help plugin for this.
		 */
	}

	/**
	 * TODO: Description
	 *
	 * @return
	 */
	private Corpus createNewProject() {
		Corpus project = new Corpus();
		project.setName("Project");
		Corpus root = new Corpus();
		root.setName("Root corpus");
//		Document d = new Document();
//		d.setName("Document");
//		root.addChild(d);
		project.addChild(root);
		return project;
	}

	/*
	 * @copydoc @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		// Calculate and set good size and position for dialog
		Monitor[] monitors = getShell().getDisplay().getMonitors();
		Monitor activeMonitor = null;

		Rectangle r = getShell().getBounds();
		for (int i = 0; i < monitors.length; i++) {
			if (monitors[i].getBounds().intersects(r)) {
				activeMonitor = monitors[i];
			}
		}
		Rectangle bounds = activeMonitor.getClientArea();
		int boundsWidth = bounds.width;
		int boundsHeight = bounds.height;
		Point size = getShell().computeSize((int) (boundsWidth * (80.0f / 100.0f)), (int) (boundsHeight * (80.0f / 100.0f)));

		int x = bounds.x + ((bounds.width - size.x) / 2);
		getShell().setSize(size);
		getShell().setLocation(x, 0);

		// Create controls
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);

		// Project name
		Group projectGroup = new Group(container, SWT.NONE);
		projectGroup.setText("Project");
		projectGroup.setLayout(new GridLayout(2, false));
		projectGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblName_2 = new Label(projectGroup, SWT.NONE);
		lblName_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName_2.setText("Name:");

		projectNameText = new Text(projectGroup, SWT.BORDER);
		projectNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// Project contents
		SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);
		sashForm.setLocation(0, 0);
		GridData gridDataSashForm = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gridDataSashForm.horizontalAlignment = SWT.FILL;
		gridDataSashForm.verticalAlignment = SWT.FILL;
		sashForm.setLayoutData(gridDataSashForm);

		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout(4, false));

		final Button btnNewRootCorpus = new Button(leftComposite, SWT.NONE);
		btnNewRootCorpus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String numberOfExistingRootCorpora = (getModel().getChildren().size() > 0) ? " " + String.valueOf(getModel().getChildren().size() + 1) : "";
				Corpus newRootCorpus = new Corpus();
				newRootCorpus.setName("Root corpus" + numberOfExistingRootCorpora);
				getModel().addChild(newRootCorpus);
				nameText.selectAll();
				nameText.setFocus();
				projectTreeViewer.refresh();
				projectTreeViewer.setSelection(new StructuredSelection(newRootCorpus));
			}
		});
		btnNewRootCorpus.setText("Add root corpus");
		
		btnNewSubCorpus = new Button(leftComposite, SWT.NONE);
		btnNewSubCorpus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Can only be corpus, otherwise button will be disabled due to extra binding
				Corpus parent = (Corpus) getSelectedElement();
				Corpus newSubCorpus = new Corpus();
				newSubCorpus.setName("New subcorpus");
				parent.addChild(newSubCorpus);
				projectTreeViewer.setSelection(new StructuredSelection(newSubCorpus));
				nameText.selectAll();
				nameText.setFocus();
				projectTreeViewer.refresh();
				projectTreeViewer.expandAll();
				projectTreeViewer.setSelection(new StructuredSelection(newSubCorpus));			}
		});
		btnNewSubCorpus.setText("Add subcorpus");

		
		btnNewDocument = new Button(leftComposite, SWT.NONE);
		btnNewDocument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Can only be corpus, otherwise button will be disabled due to extra binding
				Corpus parent = (Corpus) getSelectedElement();
				Document newDocument = new Document();
				newDocument.setName("New document");
				parent.addChild(newDocument);
				projectTreeViewer.setSelection(new StructuredSelection(newDocument));
				nameText.selectAll();
				nameText.setFocus();
				projectTreeViewer.refresh();
				projectTreeViewer.expandAll();
				projectTreeViewer.setSelection(new StructuredSelection(newDocument));
			}
		});
		btnNewDocument.setText("Add document");
		
		btnRemoveElement = new Button(leftComposite, SWT.NONE);
		btnRemoveElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem selectedItem = projectTreeViewer.getTree().getSelection()[0];
				TreeItem parentItem = selectedItem.getParentItem();
				Corpus parent;
				int index;
				if (parentItem == null) {
					parent = getModel();
					index = projectTreeViewer.getTree().indexOf(selectedItem);
				}
				else {
					parent = (Corpus) parentItem.getData();
					index = parentItem.indexOf(selectedItem);
				}
				List<ProjectNode> list = new ArrayList<ProjectNode>(parent.getChildren());
				list.remove(index);
				parent.setChildren(list);
			}
		});
		btnRemoveElement.setText("Remove element");

		projectTreeViewer = new TreeViewer(leftComposite, SWT.SINGLE | SWT.BORDER);
		new Label(leftComposite, SWT.NONE);
		projectTreeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		projectTreeViewer.expandAll();

		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new GridLayout(2, false));
		
		lblName = new Label(rightComposite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		nameText = new Text(rightComposite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		grpDocument = new Group(rightComposite, SWT.NONE);
		grpDocument.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpDocument.setLayout(new GridLayout(2, false));
		grpDocument.setText("Document");

		lblSourceText = new Label(grpDocument, SWT.NONE);
		lblSourceText.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblSourceText.setText("Source text:");

		sourceTextText = new Text(grpDocument, SWT.BORDER | SWT.MULTI);
		sourceTextText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		new Label(grpDocument, SWT.NONE);

		browseSourceTextBtn = new Button(grpDocument, SWT.NONE);
		browseSourceTextBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		browseSourceTextBtn.setText("Browse");

		sashForm.setWeights(new int[] { 1, 1 });
		bindingContext = initDataBindings();
		initExtraBindings(bindingContext);
	}

	/**
	 * Returns the selected element in the project tree viewer.
	 *
	 * @return the selected element in the project tree viewer
	 */
	protected ProjectNode getSelectedElement() {
		IStructuredSelection selection = (IStructuredSelection) projectTreeViewer.getSelection();
		if (selection.isEmpty())
			return null;
		return (ProjectNode) selection.getFirstElement();
	}

	@Override
	public void performHelp() {
		Shell shell = new Shell(getShell());
		shell.setText("My Custom Help !!");
		shell.setLayout(new GridLayout());
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Browser browser = new Browser(shell, SWT.NONE);
		browser.setUrl("http://stackoverflow.com/questions/7322489/cant-put-content-behind-swt-wizard-help-button");
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		shell.open();
	}
	
	/**
	 * @return the model
	 */
	public Corpus getModel() {
		return model;
	}

	/**
	 * Initializes the data bindings from model to widgets and returns the binding context.
	 *
	 * @return bindingContext the binding context
	 */
	private DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		
		IObservableValue treeViewerSelectionObserveSelection = ViewersObservables.observeSingleSelection(projectTreeViewer);
		IObservableValue textTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(nameText);//SWTObservables.observeText(beanText, SWT.Modify);
		IObservableValue treeViewerValueObserveDetailValue = BeanProperties.value("name").observeDetail(treeViewerSelectionObserveSelection);
		bindingContext.bindValue(textTextObserveWidget, treeViewerValueObserveDetailValue);
		
		return bindingContext;
	}
	
	/**
	 * Adds extra bindings that are not "real" data bindings (i.e., enabled/disabled buttons, etc.)
	 *
	 * @param bindingContext2
	 */
	private void initExtraBindings(DataBindingContext dbc) {
		// Observable value for selected element in tree viewer
		final IObservableValue projectTreeViewerSelection = ViewersObservables.observeSingleSelection(projectTreeViewer);
		
		// Enable the "Add document" and "Add subcorpus" buttons only if the currently selected element in the tree is a Corpus
		IObservableValue corpusSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(projectTreeViewerSelection.getValue() != null && projectTreeViewerSelection.getValue() instanceof Corpus);
			}
		};
		dbc.bindValue(WidgetProperties.enabled().observe(btnNewDocument), corpusSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(btnNewSubCorpus), corpusSelected);
		
		// Enable the "Remove element" button only if the currently selected element in the tree is not null
		IObservableValue anythingSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(projectTreeViewerSelection.getValue() != null);
			}
		};
		dbc.bindValue(WidgetProperties.enabled().observe(btnRemoveElement), anythingSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(lblName), anythingSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(nameText), anythingSelected);
		
		// Disable all document-relevant widgets when selected element is not of type Document
		IObservableValue documentSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(projectTreeViewerSelection.getValue() != null && projectTreeViewerSelection.getValue() instanceof Document);
			}
		};
		dbc.bindValue(WidgetProperties.enabled().observe(grpDocument), documentSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(sourceTextText), documentSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(lblSourceText), documentSelected);
		dbc.bindValue(WidgetProperties.enabled().observe(browseSourceTextBtn), documentSelected);
		
		// Bind model to the project tree viewer
		ExtendedViewerSupport.bind(projectTreeViewer, getModel(), BeanProperties.list("children", Corpus.class), BeanProperties.value(ProjectNode.class, "name"), ProjectTreeWizardLabelProvider.class);
	}


}
