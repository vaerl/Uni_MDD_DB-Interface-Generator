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
			// save the file
			var InputStream source = new ByteArrayInputStream(content.toString.bytes);
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
		var IFolder sourceFolder = project.getAndCreateFolder(SOURCE_FOLDER_PATH.split("/").get(0));
		var IFolder resourceFolder = project.getAndCreateFolder(SOURCE_FOLDER_PATH + "/resources");
		var IFolder entityFolder = project.getAndCreateFolder(COMPLETE_PATH + "/entities");
		var IFolder repoFolder = project.getAndCreateFolder(COMPLETE_PATH + "/repos");
		var IFolder pageFolder = project.getAndCreateFolder(COMPLETE_PATH + "/pages");
		var IFolder gridFolder = project.getAndCreateFolder(COMPLETE_PATH + "/grids");

		// TODO add contents
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
			<?xml version="1.0" encoding="UTF-8"?>
			<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
				<modelVersion>4.0.0</modelVersion>
				<parent>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-parent</artifactId>
					<version>2.3.1.RELEASE</version>
					<relativePath/>
				</parent>
				<groupId>«backend.projectName»</groupId>
				<artifactId>«backend.projectName»</artifactId>
				<version>0.0.1-SNAPSHOT</version>
				<packaging>war</packaging>
				<name>«backend.projectName»</name>
				<description>«backend.projectDescription»</description>
			
				<properties>
					<java.version>14</java.version>
					<vaadin.version>16.0.1</vaadin.version>
				</properties>
			
				<dependencies>
					<!-- Spring -->
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-data-jpa</artifactId>
					</dependency>
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-security</artifactId>
					</dependency>
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-tomcat</artifactId>
						<scope>provided</scope>
					</dependency>
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-test</artifactId>
						<scope>test</scope>
						<exclusions>
							<exclusion>
								<groupId>org.junit.vintage</groupId>
								<artifactId>junit-vintage-engine</artifactId>
							</exclusion>
						</exclusions>
					</dependency>
					<dependency>
						<groupId>org.springframework.security</groupId>
						<artifactId>spring-security-test</artifactId>
						<scope>test</scope>
					</dependency>
					
					<!-- vaadin -->
					<dependency>
						<groupId>com.vaadin</groupId>
						<artifactId>vaadin-spring-boot-starter</artifactId>
					</dependency>
					
					<!-- database -->
					<dependency>
						<groupId>mysql</groupId>
						<artifactId>mysql-connector-java</artifactId>
						<scope>runtime</scope>
					</dependency>
					
					<!-- miscellaneous -->
					<dependency>
					    <groupId>org.projectlombok</groupId>
					    <artifactId>lombok</artifactId>
					    <version>1.18.12</version>
					    <scope>provided</scope>
					</dependency>
				</dependencies>
			
				<dependencyManagement>
					<dependencies>
						<dependency>
							<groupId>com.vaadin</groupId>
							<artifactId>vaadin-bom</artifactId>
							<version>${vaadin.version}</version>
							<type>pom</type>
							<scope>import</scope>
						</dependency>
					</dependencies>
				</dependencyManagement>
			
				<build>
					<plugins>
						<plugin>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-maven-plugin</artifactId>
						</plugin>
					</plugins>
				</build>
			
			</project>
		'''
	}

	def genApplicationProperties(Backend backend) {
		'''
		# DATABASE
		spring.jpa.hibernate.ddl-auto=create
		spring.datasource.url=jdbc:mysql://«backend.database.host»:«backend.database.port»/«backend.database.schema»?useSSL=false&allowPublicKeyRetrieval=true
		spring.datasource.username=«backend.database.username»
		spring.datasource.password=«backend.database.password»
		
		# LOGGING
		logging.level.root=DEBUG
		org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
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
