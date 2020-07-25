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
		var IFolder packageFolder = project.getAndCreateFolder(COMPLETE_PATH);
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
		'''
	}

	def genApplicationProperties(Backend backend) {
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
			«FOR e : backend.entities»
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
				    MainView(HttpServletRequest request, «FOR e : backend.entities SEPARATOR ', '»«e.name.toFirstUpper»GridPage «e.name»page«ENDFOR») {
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
				    	«FOR e : backend.entities»
				    		Tab «e.name» = new Tab("«e.name.toFirstUpper»");
				    		        «e.name».getStyle().set("font-size", "48px");
				    	«ENDFOR»
				    	Tabs tabs = new Tabs(«FOR e : backend.entities SEPARATOR ', '»«e.name»«ENDFOR»);
				    	tabs.setSelectedTab(«backend.entities.get(0).name»);
				    	tabs.setFlexGrowForEnclosedTabs(1);
				    	tabs.setWidthFull();
				    	
				    	HorizontalLayout tabWrapper = new HorizontalLayout(tabs);
				    	tabWrapper.setWidth("92%");
				    	tabWrapper.setJustifyContentMode(JustifyContentMode.START);
				    	
				    	HorizontalLayout bar = new HorizontalLayout(tabWrapper, logoutWrapper);
				    	bar.setWidthFull();
				    	
				    	Map<Tab, VerticalLayout> tabsToPages = new HashMap<>();
				    	«FOR e : backend.entities»
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
