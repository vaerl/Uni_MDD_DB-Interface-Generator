package generator;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import generator.Generator;

public class GenerationHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelectionService service = (ISelectionService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISelectionService.class);
		ISelection sel = service.getSelection();
		if(! (sel instanceof StructuredSelection))
			return null;
		StructuredSelection structSel = (StructuredSelection)sel;
		
		IFile fileEcore = (IFile) structSel.getFirstElement();
		IProject projectEcore = fileEcore.getProject();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		// load the ecore file
		URI uriEcoreFile = URI.createFileURI(fileEcore.getRawLocation()
				.toString());
		Resource resEcoreFile = resourceSet.createResource(uriEcoreFile);
		try {
			resEcoreFile.load(Collections.emptyMap());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Generating Java code now.");
		GenerateJob job = new GenerateJob("Generating Java code", resEcoreFile,
				projectEcore);
		job.setUser(true);
		job.schedule();
		
//		TreeIterator<EObject> contents = resEcoreFile.getAllContents();
//		
//		Company rootElement = null;

	
		
//		for(EObject currentElement = null; contents.hasNext(); ) {
//			currentElement = contents.next();
//			if(currentElement instanceof Company) {
//				rootElement = (Company)currentElement;
//				break;
//			}
//		}
//		
//		
//		System.out.println("The company has " +rootElement.getCustomers().size() +"customers");
//		// TODO Auto-generated method stub
//		System.out.println("Yep!");
		return null;
	}

	private class GenerateJob extends Job implements IJobChangeListener {

		long jobBegin;
		Resource resEcoreFile;
		IProject project;

		public GenerateJob(String name, Resource resEcoreFile, IProject project) {
			super(name);
			this.resEcoreFile = resEcoreFile;
			this.project = project;
			addJobChangeListener(this);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			// start the generation
			jobBegin = System.currentTimeMillis();
			Generator generator = new Generator();
			generator.doGenerate(resEcoreFile, project, monitor);
			return Status.OK_STATUS;
		}

		@Override
		public void aboutToRun(IJobChangeEvent event) {
		}

		@Override
		public void awake(IJobChangeEvent event) {
		}

		@Override
		public void done(IJobChangeEvent event) {
			long jobEnd = System.currentTimeMillis();
			if (event.getResult().isOK()) {
				System.out.println("Generating Java code has finished (took "
						+ (jobEnd - jobBegin) + " ms).");
			} else if (event.getResult().matches(Status.CANCEL)) {
				System.out
						.println("Generating Java code has been cancelled (took "
								+ (jobEnd - jobBegin) + " ms).");
			}
		}

		@Override
		public void running(IJobChangeEvent event) {
		}

		@Override
		public void scheduled(IJobChangeEvent event) {
		}

		@Override
		public void sleeping(IJobChangeEvent event) {
		}

	}

	
	
}
