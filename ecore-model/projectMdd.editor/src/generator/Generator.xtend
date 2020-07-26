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
import org.eclipse.emf.common.util.EList

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
		var IFolder packageFolder = project.getAndCreateFolder(COMPLETE_PATH);
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
		createFile(entityFolder, backend.projectName + "Application.java", true, backend.genApplicationClass, progressMonitor);
		
		// TODO create ui-base: login, logout and tab-switcher
		createFile(packageFolder, "MainView.java", true, backend.genMainView, progressMonitor);
		createFile(packageFolder, "ChangeHandler.java", true, genChangeHandler, progressMonitor);
		createFile(packageFolder, "AccessDeniedView.java", true, genAccessDenied, progressMonitor);
		createFile(packageFolder, "LoginView.java", true, genLoginView, progressMonitor);
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
	
	def genApplicationClass(Backend backend){
		'''
		'''
	}
	
	def genMainView(Backend backend) {
		'''
			package «PACKAGE»;
			
			import com.vaadin.flow.component.ClickEvent;
			import com.vaadin.flow.component.ComponentEventListener;
			import com.vaadin.flow.component.UI;
			import com.vaadin.flow.component.button.Button;
			import com.vaadin.flow.component.button.ButtonVariant;
			import com.vaadin.flow.component.icon.VaadinIcon;
			import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
			import com.vaadin.flow.component.orderedlayout.VerticalLayout;
			import com.vaadin.flow.component.tabs.Tab;
			import com.vaadin.flow.component.tabs.Tabs;
			import com.vaadin.flow.router.BeforeEnterListener;
			import com.vaadin.flow.router.Route;
			import com.vaadin.flow.router.RouteAlias;
			import com.vaadin.flow.spring.annotation.UIScope;
			import org.springframework.beans.factory.annotation.Autowired;
			import org.springframework.security.core.context.SecurityContextHolder;
			import org.springframework.stereotype.Component;
			«FOR e : backend.entities.filter[it.display]»
				import «PACKAGE».pages.«e.name.toFirstUpper»GridPage;
			«ENDFOR»
			
			
			import javax.servlet.http.HttpServletRequest;
			import java.util.HashMap;
			import java.util.Map;
			
			@Route("")
			@RouteAlias("main")
			@UIScope
			@Component
			public class MainView extends VerticalLayout {
				
				private HttpServletRequest request;
				
				@Autowired
				    MainView(HttpServletRequest request, «FOR e : backend.entities.filter[it.display] SEPARATOR ', '»«e.name.toFirstUpper»GridPage «e.name»page«ENDFOR») {
				    	super();
				    	this.request = request;
				    	
				    	Button logout = new Button(VaadinIcon.SIGN_OUT.create());
				    	logout.getStyle().set("font-size", "48px");
				    	logout.setHeight("96px");
				    	logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
				    	logout.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
				    		requestLogout();
				    	});
				    	
				    	HorizontalLayout logoutWrapper = new HorizontalLayout();
				    	logoutWrapper.add(logout);
				    	logoutWrapper.setWidth("8%");
				    	logoutWrapper.setJustifyContentMode(JustifyContentMode.END);
				    	«FOR e : backend.entities.filter[it.display]»
				    		Tab «e.name» = new Tab("«e.name.toFirstUpper»");
				    		        «e.name».getStyle().set("font-size", "48px");
				    	«ENDFOR»
				    	Tabs tabs = new Tabs(«FOR e : backend.entities.filter[it.display] SEPARATOR ', '»«e.name»«ENDFOR»);
				    	tabs.setSelectedTab(«backend.entities.get(0).name»);
				    	tabs.setFlexGrowForEnclosedTabs(1);
				    	tabs.setWidthFull();
				    	
				    	HorizontalLayout tabWrapper = new HorizontalLayout(tabs);
				    	tabWrapper.setWidth("92%");
				    	tabWrapper.setJustifyContentMode(JustifyContentMode.START);
				    	
				    	HorizontalLayout bar = new HorizontalLayout(tabWrapper, logoutWrapper);
				    	bar.setWidthFull();
				    	
				    	Map<Tab, VerticalLayout> tabsToPages = new HashMap<>();
				    	«FOR e : backend.entities.filter[it.display]»
				    		tabsToPages.put(«e.name», «e.name»Page);
				    	«ENDFOR»
				    	
				    	tabs.addSelectedChangeListener(event -> {
				    	     removeAll();
				    	     add(bar, tabsToPages.get(tabs.getSelectedTab()));
				    	});
				    	
				    	setSizeFull();
				    	add(bar);
				    	
				    	UI.getCurrent().addBeforeEnterListener((BeforeEnterListener) beforeEnterEvent -> {
				    	     if (beforeEnterEvent.getNavigationTarget() != AccessDeniedView.class && // This is to avoid a
				    	     // loop if DeniedAccessView is the target
				    	     !this.request.isUserInRole("ADMIN")) {
				    	     	beforeEnterEvent.rerouteTo(AccessDeniedView.class);
				    	     }
				    	});
			}
			
			void requestLogout() {
			        //https://stackoverflow.com/a/5727444/1572286
			        SecurityContextHolder.clearContext();
			        request.getSession(false).invalidate();
			
			        // And this is similar to how logout is handled in Vaadin 8:
			        // https://vaadin.com/docs/v8/framework/articles/HandlingLogout.html
			        UI.getCurrent().getSession().close();
			        UI.getCurrent().getPage().reload();// to redirect user to the login page
			    }
		'''
	}
	
	def genChangeHandler() {
		'''
		package «PACKAGE»;
		
		public interface ChangeHandler {
			void onChange();
			}
		'''
	}
	
	def genAccessDenied() {
		'''
			package «PACKAGE»;
						
			import com.vaadin.flow.component.formlayout.FormLayout;
			import com.vaadin.flow.component.html.Label;
			import com.vaadin.flow.component.orderedlayout.VerticalLayout;
			import com.vaadin.flow.router.Route;
			
			@Route("accessDenied")
			public class AccessDeniedView extends VerticalLayout {
			    AccessDeniedView() {
			        FormLayout formLayout = new FormLayout();
			        formLayout.add(new Label("Access denied!"));
			        add(formLayout);
			    }
			}
		'''
	}
	
	def genLoginView() {
		'''
		package «PACKAGE»;
		
		import com.vaadin.flow.component.ClickEvent;
		import com.vaadin.flow.component.ComponentEventListener;
		import com.vaadin.flow.component.Key;
		import com.vaadin.flow.component.KeyDownEvent;
		import com.vaadin.flow.component.applayout.AppLayout;
		import com.vaadin.flow.component.button.Button;
		import com.vaadin.flow.component.formlayout.FormLayout;
		import com.vaadin.flow.component.html.Label;
		import com.vaadin.flow.component.orderedlayout.FlexComponent;
		import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
		import com.vaadin.flow.component.orderedlayout.VerticalLayout;
		import com.vaadin.flow.component.textfield.PasswordField;
		import com.vaadin.flow.component.textfield.TextField;
		import com.vaadin.flow.router.BeforeEnterEvent;
		import com.vaadin.flow.router.BeforeEnterObserver;
		import com.vaadin.flow.router.Route;
		import org.apache.commons.lang3.StringUtils;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.security.authentication.AnonymousAuthenticationToken;
		import org.springframework.security.authentication.AuthenticationManager;
		import org.springframework.security.authentication.BadCredentialsException;
		import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.context.SecurityContext;
		import org.springframework.security.core.context.SecurityContextHolder;
		import org.springframework.security.web.savedrequest.DefaultSavedRequest;
		
		import javax.servlet.http.HttpServletRequest;
		import javax.servlet.http.HttpSession;
		
		@Route("login")
		public class LoginView extends AppLayout implements BeforeEnterObserver {
			
			private final Label label;
			private final TextField userNameTextField;
			private final PasswordField passwordField;
			
			/**
			* AuthenticationManager is already exposed in WebSecurityConfig
			*/
			@Autowired
			private AuthenticationManager authManager;
			
			@Autowired
			private HttpServletRequest req;
			
			LoginView() {
			    label = new Label("Bitte anmelden:");
			
			    userNameTextField = new TextField();
			    userNameTextField.setPlaceholder("Benutzename");
			    userNameTextField.setWidth("90%");
			    //UiUtils.makeFirstInputTextAutoFocus(Collections.singletonList(userNameTextField));
			
			    passwordField = new PasswordField();
			    passwordField.setPlaceholder("Passwort");
			    passwordField.setWidth("90%");
			    passwordField.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> authenticateAndNavigate());
			
			    Button submitButton = new Button("Anmelden");
			    submitButton.setWidth("90%");
			    submitButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
			        authenticateAndNavigate();
			    });
			
			    FormLayout formLayout = new FormLayout();
			    formLayout.add(label, userNameTextField, passwordField, submitButton);
			
			    VerticalLayout verticalLayout = new VerticalLayout(label, userNameTextField, passwordField, submitButton);
			    verticalLayout.setHeightFull();
			    verticalLayout.setMaxWidth("50%");
			    verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
			    verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
			
			    HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout);
			    horizontalLayout.setSizeFull();
			    horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
			
			    this.setContent(horizontalLayout);
		   }
			
			private void authenticateAndNavigate() {
			    /*
			    Set an authenticated user in Spring Security and Spring MVC
			    spring-security
			    */
			    UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userNameTextField.getValue(), passwordField.getValue());
			    try {
			        // Set authentication
			        Authentication auth = authManager.authenticate(authReq);
			        SecurityContext sc = SecurityContextHolder.getContext();
			        sc.setAuthentication(auth);
			
			        /*
			        Navigate to the requested page:
			        This is to redirect a user back to the originally requested URL – after they log in as we are not using
			        Spring's AuthenticationSuccessHandler.
			        */
			        HttpSession session = req.getSession(false);
			        DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
			        //String requestedURI = savedRequest != null ? savedRequest.getRequestURI() : Application.APP_URL;
			        String requestedURI = savedRequest != null ? savedRequest.getRequestURI() : "main";
			
			        this.getUI().ifPresent(ui -> ui.navigate(StringUtils.removeStart(requestedURI, "/")));
			    } catch (BadCredentialsException e) {
			        label.setText("Ungültiger Benutzername oder ungültiges Passwort. Bitte nochmal versuchen.");
			    }
			}
			
			/**
			 * This is to redirect user to the main URL context if (s)he has already logged in and tries to open /login
			 *
			 * @param beforeEnterEvent
			 */
			@Override
			public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
			    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			    //Anonymous Authentication is enabled in our Spring Security conf
			    if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			        //https://vaadin.com/docs/flow/routing/tutorial-routing-lifecycle.html
			        beforeEnterEvent.rerouteTo("");
			    }
			}
		}
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
		package «PACKAGE».backend.repos;
		
		import «PACKAGE».backend.entities.«entity.name.toFirstUpper»;
		import org.springframework.data.jpa.repository.JpaRepository;
		import org.springframework.stereotype.Repository;
		
		import java.util.List;
		
		@Repository
		public interface «entity.name.toFirstUpper»Repository extends JpaRepository<«entity.name.toFirstUpper», Long> {
		
		    List<«entity.name.toFirstUpper»> findAll();
		
		    List<«entity.name.toFirstUpper»> findByNameStartsWithIgnoreCase(String name);
		
		    List<«entity.name.toFirstUpper»> findByDoneIs(boolean b);
		
		}
		'''
	}

	def genEntityPage(Entity entity) {
		'''
		package «PACKAGE»;
		
		import com.vaadin.flow.component.button.Button;
		import com.vaadin.flow.component.grid.Grid;
		import com.vaadin.flow.component.grid.GridVariant;
		import com.vaadin.flow.component.icon.VaadinIcon;
		import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
		import com.vaadin.flow.component.orderedlayout.VerticalLayout;
		import com.vaadin.flow.component.page.Push;
		import com.vaadin.flow.component.textfield.TextField;
		import com.vaadin.flow.data.value.ValueChangeMode;
		import com.vaadin.flow.spring.annotation.UIScope;
		import «PACKAGE».KlostertrophyApplication;
		import «PACKAGE».backend.entities.«entity.name.toFirstUpper»;
		import «PACKAGE».backend.repos.«entity.name.toFirstUpper»Repository;
		// import de.klostertrophy.backend.repos.TeamRepository;	// ??
		import «PACKAGE».frontend.details.«entity.name.toFirstUpper»Details;
		import «PACKAGE».frontend.editors.«entity.name.toFirstUpper»Editor;
		import «PACKAGE».frontend.play.«entity.name.toFirstUpper»PlayDialog;		// Alle PLAY-Komponenten entfernen? (inkl. button, dialog, etc.)
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.stereotype.Component;
		import org.springframework.transaction.annotation.Transactional;
		import org.springframework.util.StringUtils;
		
		@Component
		@Transactional
		@UIScope
		public class «entity.name.toFirstUpper»GridPage extends VerticalLayout {
		
		    private static final long serialVersionUID = -8733687422451328748L;
		    private static final Logger log = LoggerFactory.getLogger(«entity.name.toFirstUpper»GridPage.class);
		
		    private «entity.name.toFirstUpper»Repository «entity.name»Repository;
		    private Grid<«entity.name.toFirstUpper»> grid;
		    private «entity.name.toFirstUpper»Editor «entity.name»Editor;
		    private «entity.name.toFirstUpper»PlayDialog «entity.name»PlayDialog;
		
		    private TextField filter;
		
		    private Button evaluate;
		
		    @Autowired
		    public «entity.name.toFirstUpper»GridPage(«entity.name.toFirstUpper»Repository «entity.name»Repository, «entity.name.toFirstUpper»PlayDialog «entity.name»PlayDialog, «entity.name.toFirstUpper»Editor «entity.name»Editor) {  // TeamRepository entfernt
		        super();
		        this.«entity.name»Repository = «entity.name»Repository;
		        this.«entity.name»PlayDialog = «entity.name»PlayDialog;
		        this.«entity.name»Editor = «entity.name»Editor;
		
		        filter = new TextField();
		        HorizontalLayout actions = new HorizontalLayout();
		
		        // grid
		        grid = new Grid<>(«entity.name.toFirstUpper».class);
		        grid.setItems(«entity.name»Repository.findAll());
		        grid.setMultiSort(true);
		        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
		                GridVariant.LUMO_ROW_STRIPES);
		        grid.asSingleSelect().addValueChangeListener(e -> this.«entity.name»Editor.edit(e.getValue()));
		        // add Columns
		        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
		        grid.asSingleSelect().addValueChangeListener(e -> this.«entity.name»Editor.edit(e.getValue()));
		        //add Columns
		        setColumns();
		
		        // actions
		        Button addNew = new Button("«entity.name.toFirstUpper» hinzufügen", VaadinIcon.PLUS.create());
		        addNew.addClickListener(e -> this.«entity.name»Editor.edit(new «entity.name.toFirstUpper»("", null, null))); 		// "", null, null noch richtig?
		
		        // filter
		        filter.setPlaceholder("Nach Namen filtern");
		        filter.setValueChangeMode(ValueChangeMode.EAGER);
		        filter.addValueChangeListener(e -> listValues(e.getValue()));
		
		        // editor
		        «entity.name»Editor.setChangeHandler(() -> {
		            «entity.name»Editor.close();
		            listValues(filter.getValue());
		        });
		
		        // playDialog
		        «entity.name»PlayDialog.setChangeHandler(() -> {
		            «entity.name»PlayDialog.close();
		            «entity.name»PlayDialog.reset();
		            listValues(filter.getValue());
		        });
		
		        actions.add(filter, addNew);
		        add(actions, grid, this.«entity.name»Editor);
		        listValues(null);
		    }
		
		    void listValues(String filterText) {
		        if (StringUtils.isEmpty(filterText)) {
		            grid.setItems(«entity.name»Repository.findAll());
		        } else {
		            grid.setItems(«entity.name»Repository.findByNameStartsWithIgnoreCase(filterText));
		        }
		    }
		
		    private void setColumns() {
		        // remove unwanted columns
		        grid.removeAllColumns();
		        // add Columns		// TODO: Mittels FOR-Schleife alle Attribute hinzufügen?
		        grid.addColumn(«entity.name.toFirstUpper»::getName).setHeader("Name").setSortable(true);
		        grid.addColumn(«entity.name.toFirstUpper»::getId).setHeader("ID").setSortable(true);
		        // grid.addColumn(«entity.name.toFirstUpper»::getInputType).setHeader("Punkt-Typ").setSortable(true);
		        // grid.addColumn(«entity.name.toFirstUpper»::isDoneString).setHeader("Status").setSortable(true);
		
		        // add standard-columns
		        grid.addComponentColumn(value -> {
		            Button play = new Button("Spielen");
		            play.addClassName("play");
		            play.addClickListener(e -> {
		                «entity.name»PlayDialog.edit(value);
		            });
		            if (value.getTeams().isEmpty()) {
		                play.setEnabled(false);
		            } else {
		                play.setEnabled(true);
		            }
		            return play;
		        });
		
		        grid.addComponentColumn(value -> {
		            Button details = new Button("Fertig");
		            details.addClassName("details");
		            details.addClickListener(e -> {
		                var «entity.name»Details = new «entity.name.toFirstUpper»Details();
		                «entity.name»Details.open(value);
		            });
		            if (value.getFinished().isEmpty()) {
		                log.info("Finished is empty.");
		                details.setEnabled(false);
		            } else {
		                log.info("Finished will be displayed.");
		                details.setEnabled(true);
		            }
		            return details;
		        });
		
		        grid.addComponentColumn(value -> {
		            Button edit = new Button("Bearbeiten");
		            edit.addClassName("edit");
		            edit.addClickListener(e -> {
		                «entity.name»Editor.edit(value);
		            });
		            return edit;
		        });
		
		        grid.addComponentColumn(value -> {			// Wie Play, auch entfernen?
		            evaluate = new Button("Auswerten");
		            evaluate.setEnabled(false);
		            evaluate.addClassName("evaluate");
		            evaluate.addClickListener(e -> {
		                System.out.println("EVALUIEREN");
		            });
		            return evaluate;
		        });
		    }
		}
		
		'''
	}

	def genEntityGrid(Entity entity) {
		'''
		'''
	}

}
