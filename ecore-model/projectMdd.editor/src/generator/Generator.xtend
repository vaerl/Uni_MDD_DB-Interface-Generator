package generator

import java.io.ByteArrayInputStream
import java.io.InputStream
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.resource.Resource
import projectMdd.Backend
import projectMdd.Entity

/**
 * The generator for ecore files.
 * 
 * @author Marco Richter
 */
class Generator {

	/**
	 * The path where to generate the Java files.
	 */
	public static final String SOURCE_FOLDER_PATH = "src-gen/main";

	/**
	 * The base package name. Needs the succeeding dot!
	 */
	public static final String PACKAGE = "de.thm.dbiGenerator.";
	public static final String PACKAGE_PATH = "/" + PACKAGE.replaceAll("\\.", "/");
	public static final String COMPLETE_PATH = SOURCE_FOLDER_PATH + PACKAGE_PATH;

	// Ecore
	public static final String EXTENDED_META_DATA = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
	public static final String MAX_INCLUSIVE = "maxInclusive";
	public static final String MIN_INCLUSIVE = "minInclusive";

	// TODO get some Formatters
	// TODO do we need this?
	// var ImportStatementFinder importFinder = new ImportStatementFinder()	
	/**
	 * Creates a file (containing the content-CharSequence) within the given IFolder.
	 */
	def void createFile(IFolder folder, String fileName, boolean overrideFile, CharSequence content,
		IProgressMonitor progressMonitor) {
		if (progressMonitor.canceled) {
			throw new RuntimeException("Progress canceled");
		}
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		var IFile iFile = folder.getFile(fileName);
		// TODO, nur in der Testphase
		if (iFile.exists && true /*overrideFile*/ ) {
			iFile.delete(true, null);
		}
		if (!iFile.exists) {
			// process the code
			var String formattedCode;
			if (fileName.endsWith(".java")) {
				// organize imports
				// var String sourceWithImports = importFinder.getWithImports(content.toString);
				// format the code
				// formattedCode = JavaFormatter.format(sourceWithImports);
			} else if (fileName.endsWith(".xml")) {
				// format the code
				// formattedCode = XmlFormatter.format(content.toString)
			}

			var byte[] bytes
			if (formattedCode !== null) { // has the code been formatted?
				bytes = formattedCode.getBytes();
			} else { // code could not be formatted
				bytes = content.toString.bytes;
				System.err.println("File " + fileName + " could not be formatted.");
			}
			// save the file
			var InputStream source = new ByteArrayInputStream(bytes);
			iFile.create(source, true, null);
		}
	}

	/**
	 * Starts the generation of the given Resource file in the given IProject.
	 */
	def void doGenerate(Resource resourceEcore, IProject project, IProgressMonitor progressMonitor) {
		try {
			// begin the task with the amount of work
			progressMonitor.beginTask("Generating Java code.", 2);

			progressMonitor.subTask("Creating folders.");

			// create folders
			var path = "";
			for (String subPath : COMPLETE_PATH.split("/")) {
				path += subPath + "/";
				project.getAndCreateFolder(path);
			}

			// create application
			progressMonitor.subTask("Generating Entities");
			doGenerate(project, resourceEcore.allContents.filter(Backend).head, progressMonitor);
			makeProgressAndCheckCanceled(progressMonitor);

			// finish the progress monitor
			progressMonitor.done;
		} catch (Exception ex) {
			ex.printStackTrace;
		}
	}

	def void makeProgressAndCheckCanceled(IProgressMonitor monitor) {
		monitor.worked(1);
		if (monitor.canceled) {
			throw new RuntimeException("Progress canceled");
		}
	}

	def getAndCreateFolder(IProject project, String path) {
		var folder = project.getFolder(path);
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		return folder;
	}

	def void doGenerate(IProject project, EObject rootElement, IProgressMonitor progressMonitor) {
		// setup
		var Backend backend = rootElement as Backend;
		var IFolder sourceFolder = project.getAndCreateFolder(SOURCE_FOLDER_PATH);
		var IFolder resourceFolder = project.getAndCreateFolder(SOURCE_FOLDER_PATH + "/resources");
		var IFolder entityFolder = project.getAndCreateFolder(COMPLETE_PATH + "/entities");
		var IFolder repoFolder = project.getAndCreateFolder(COMPLETE_PATH + "/repos");
		var IFolder pageFolder = project.getAndCreateFolder(COMPLETE_PATH + "/pages");
		var IFolder gridFolder = project.getAndCreateFolder(COMPLETE_PATH + "/grids");

		// TODO add contents, update outputFolder
		// create pom.xml
		createFile(sourceFolder, "pom.xml", true, backend.genPom, progressMonitor);

		// create application.properties
		createFile(resourceFolder, "application.properties", true, backend.genApplicationProperties, progressMonitor);

		// create Application.class with exemplary data
		// TODO create ui-base: login, logout and tab-switcher
		// create entity-classes
		for (Entity entity : backend.entities) {
			// create extension-file
			createFile(entityFolder, entity.name + ".java", true, entity.genEntityExtensionClass, progressMonitor);

			if (!entity.transient) {
				// create entity-gen-file
				createFile(entityFolder, entity.name + "Gen.java", true, entity.genEntityClass, progressMonitor);

				// create repositories
				createFile(repoFolder, entity.name + "Repo.java", true, entity.genEntityRepo, progressMonitor);

				if (entity.display) {
					// create page, f.e. gamesGridPage in klostertrophy
					createFile(pageFolder, entity.name + "Page.java", true, entity.genEntityPage, progressMonitor);
					// TODO update method
					// create editor
					createFile(gridFolder, entity.name + "Grid.java", true, entity.genEntityGrid, progressMonitor);
				}
			} else {
				// create entity-gen-file
				createFile(entityFolder, entity.name + "Gen.java", true, entity.genTransientEntityClass,
					progressMonitor);
			}
		}
	}

	def genPom(Backend backend) {
		'''
		'''
	}

	def genApplicationProperties(Backend backend) {
		'''
		'''
	}

	// TODO we currently don't have inheritance - we probably should.
	// TODO use Lombok for Getters and setters
	// TODO update this
	def genEntityClass(Entity entity) {
		"fix implementation"
//		'''
//			package «PACKAGE»entities;
//			
//			/**
//			* This is the {@link «entity.name»} gen-entity class.
//			*
//			*@generated
//			*/
//			public class «entity.name»Gen «IF !e.EAllSuperTypes.empty» extends «e.EAllSuperTypes.head.name» «ENDIF» {
//			
//				// attributes
//				«FOR a : e.EAllAttributes»
//					private «a.EType.instanceTypeName» «a.name»;
//				«ENDFOR»
//				
//				// references
//				«FOR a : e.EAllReferences.filter[!many]»
//					private «a.EReferenceType.name» «a.name»;
//				«ENDFOR»
//				«FOR a : e.EAllReferences.filter[many]»
//					private java.util.ArrayList<«a.EReferenceType.name»> «a.name»;
//				«ENDFOR»
//				
//				/**
//				* Default constructor.
//				*/
//				public «entity.name»Gen() {
//				}
//				
//				«IF !e.EAllAttributes.empty»
//					/**
//					* Constructor for all attributes.
//					*/
//					public «e.name»Gen(«FOR a : e.EAllAttributes SEPARATOR ', '» «a.EType.instanceTypeName» «a.name» «ENDFOR») {
//						«FOR a : e.EAllAttributes»
//							this.«a.name» = «a.name»;
//						«ENDFOR»
//					}
//				«ENDIF»
//				
//				«IF !e.EAllAttributes.empty && !e.EAllReferences.empty»
//					/**
//					* Full constructor.
//					*/
//					public «e.name»Gen(
//					«FOR a : e.EAllAttributes SEPARATOR ', '» «a.EType.instanceTypeName» «a.name» «ENDFOR» 
//					«FOR a : e.EAllReferences.filter[!many] BEFORE ', ' SEPARATOR ', '» «a.EReferenceType.name» «a.name» «ENDFOR»
//					«FOR a : e.EAllReferences.filter[many] BEFORE ', ' SEPARATOR ', '» java.util.ArrayList<«a.EReferenceType.name»> «a.name» «ENDFOR») {
//					«FOR a : e.EAllAttributes + e.EAllReferences»
//						this.«a.name» = «a.name»;
//					«ENDFOR»
//					}
//				«ENDIF»
//				
//				//TODO getter setter
//				
//				@Override
//				public String toString() {
//					«IF e.EAllAttributes.exists[string]»
//						StringBuilder builder = new StringBuilder();
//						«FOR a : e.EAllAttributes.filter[string]»
//							builder.append(«a.name»);
//							builder.append(" - ");
//						«ENDFOR»
//						return builder.toString();
//					«ELSE»
//						return null; //TODO
//					«ENDIF»
//				}
//			
//			}
//		'''
	}

	def genTransientEntityClass(Entity entity) {
		'''
		'''
	}

	def genEntityExtensionClass(Entity entity) {
		'''
			package «PACKAGE»entities;
			
			public class «entity.name» extends «entity.name»Gen {
				
			}
		'''
	}

	def genEntityRepo(Entity entity) {
		'''
		'''
	}

	def genEntityPage(Entity entity) {
		'''
		'''
	}

	def genEntityGrid(Entity entity) {
		'''
		'''
	}

}
