package generator

import java.io.ByteArrayInputStream
import java.io.InputStream
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import projectMdd.Backend
import projectMdd.Entity
import static extension generator.Helpers.*;
import projectMdd.TypeAttribute
import projectMdd.EnumAttribute
import projectMdd.RelationType

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
	public static final String PACKAGE = "de.thm.dbiGenerator";
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
		var IFolder repoFolder = project.getAndCreateFolder(COMPLETE_PATH + "/repositories");
		var IFolder pageFolder = project.getAndCreateFolder(COMPLETE_PATH + "/pages");
		var IFolder editorFolder = project.getAndCreateFolder(COMPLETE_PATH + "/editors");
		var IFolder detailsFolder = project.getAndCreateFolder(COMPLETE_PATH + "/details");

		// TODO add contents
		// create pom.xml
		createFile(sourceFolder, "pom.xml", true, backend.genPom, progressMonitor);

		// create application.properties
		createFile(resourceFolder, "application.properties", true, backend.genApplicationProperties, progressMonitor);

		// create Application.class with exemplary data
		createFile(packageFolder, backend.projectName + "Application.java", true, backend.genApplicationClass,
			progressMonitor);

		// create websecurity-class
		createFile(packageFolder, "WebSecurityConfig.java", true, backend.genWebsecurity, progressMonitor);

		// create servler-initializer
		createFile(packageFolder, "ServletInitializer.java", true, genServletInitializer, progressMonitor);

		// create base-ui
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
				createFile(repoFolder, entity.name + "Repository.java", true, entity.genEntityRepo, progressMonitor);

				if (entity.display) {
					// create page
					createFile(pageFolder, entity.name + "GridPage.java", true, entity.genEntityGridPage,
						progressMonitor);
					// TODO update method
					// create editor
					createFile(editorFolder, entity.name + "Editor.java", true, entity.genEntityEditor,
						progressMonitor);
					// create details
					createFile(detailsFolder, entity.name + "Details.java", true, entity.genEntityDetails,
						progressMonitor);
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

	def genApplicationClass(Backend backend) {
		'''
			package «PACKAGE»;
			
			«backend.getEntitiesAsImports(PACKAGE)»
			«backend.getReposAsImports(PACKAGE)»
			import org.slf4j.Logger;
			import org.slf4j.LoggerFactory;
			import org.springframework.boot.CommandLineRunner;
			import org.springframework.boot.SpringApplication;
			import org.springframework.boot.autoconfigure.SpringBootApplication;
			import org.springframework.context.annotation.Bean;
			
			import java.io.IOException;
			import java.util.HashSet;
			
			@SpringBootApplication
			public class «backend.projectName.toFirstUpper»Application {
			
			    private static final Logger log = LoggerFactory.getLogger(«backend.projectName.toFirstUpper»Application.class);
			    
			    private static final String CONTAINER_NAME = "«backend.projectName»";
			    private static final String CONTAINER_DATABASE_PASSWORD = "«backend.database.password»";
			    private static final String CONTAINER_DATABASE_NAME = "«backend.database.schema»";
			
			    public static void main(String[] args) {
			        createMySQLContainer(CONTAINER_NAME, CONTAINER_DATABASE_PASSWORD, CONTAINER_DATABASE_NAME);
			        startMySQLContainer(CONTAINER_DATABASE_NAME);
			     	SpringApplication.run(«backend.projectName.toFirstUpper»Application.class, args);
			    }
			
			    public static void createMySQLContainer(String containerName, String databasePassword, String databaseName) {
			            try {
			                log.info("Checking if container {} exists.", containerName);
			                Process check = Runtime.getRuntime().exec("docker inspect -f '{{.State.Running}}' " + containerName);
			                String res = String.valueOf(check.getInputStream());
			                log.info("Container exists: {}", res);
			                check.getOutputStream().close();
			                if (!res.contains("true")) {
			                    log.info("Creating container {}.", containerName);
			                    Process run = Runtime.getRuntime()
			                            .exec("docker run -p «backend.database.port»:3306 --name " + containerName + " -e MYSQL_ROOT_PASSWORD="
			                                    + databasePassword + " -e MYSQL_DATABASE=" + databaseName + " -d mysql:latest");
			                    run.getOutputStream().close();
			                    log.info("Created docker-container with name: {}", containerName);
			                }
			            } catch (IOException e) {
			                e.printStackTrace();
			                log.error("Could not create docker-container with name: {}", containerName);
			            }
			        }
			    
			        private static void startMySQLContainer(String containerName) {
			            try {
			                Process start = Runtime.getRuntime().exec("docker start " + containerName);
			                start.getOutputStream().close();
			                log.info("Started docker-container with name: {}", containerName);
			            } catch (IOException e) {
			                e.printStackTrace();
			                log.error("Could'nt start docker-container with name: {}", containerName);
			            }
			        }
			
			    @Bean
			    public CommandLineRunner loadData(«backend.getReposAsParams») {
			        return (args) -> {
			            «var counter = 1»
			            «FOR entity : backend.entities»
			            	«entity.createNewEntity(entity.name.toFirstLower  + counter)»
			            	«FOR attribute:entity.attributes»
			            		«entity.name.toFirstLower  + counter».set«attribute.name.toFirstUpper»(«attribute.getRandomValueForType(entity)»);
			            	«ENDFOR»
			            	«entity.saveInRepo(entity.name.toFirstLower  + counter++)»
			            	
			            «ENDFOR»
			        };
			    }
			
			}
		'''
	}

	def genWebsecurity(Backend backend) {
		'''
			package «PACKAGE»;
			
			import «PACKAGE».repositories.AdminRepository;
			import «PACKAGE».entities.Admin;
			import org.springframework.beans.factory.annotation.Autowired;
			import org.springframework.context.annotation.Bean;
			import org.springframework.context.annotation.Configuration;
			import org.springframework.http.HttpMethod;
			import org.springframework.security.authentication.AuthenticationManager;
			import org.springframework.security.config.BeanIds;
			import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
			import org.springframework.security.config.annotation.web.builders.HttpSecurity;
			import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
			import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
			import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
			import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
			
			@Configuration
			@EnableWebSecurity
			public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
				
				private AdminRepository adminRepository;
				
				@Autowired
				public WebSecurityConfig(AdminRepository adminRepository){
					this.adminRepository = adminRepository;
				}
			
			    @Override
			    protected void configure(HttpSecurity http) throws Exception {
			        http
			                .csrf().disable() // CSRF is handled by Vaadin: https://vaadin.com/framework/security
			                .exceptionHandling().accessDeniedPage("/accessDenied")
			                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
			                .and().logout().logoutSuccessUrl("/")
			                .and()
			                .authorizeRequests()
			                // allow Vaadin URLs and the login URL without authentication
			                .regexMatchers("/login.*", "/accessDenied", "/VAADIN/.*", "/favicon.ico", "/robots.txt", "/manifest.webmanifest",
			                        "/sw.js", "/offline-page.html", "/frontend/.*", "/webjars/.*", "/frontend-es5/.*", "/frontend-es6/.*").permitAll()
			                .regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
			                // deny any other URL until authenticated
			                .antMatchers("/**").fullyAuthenticated()
			            /*
			             Note that anonymous authentication is enabled by default, therefore;
			             SecurityContextHolder.getContext().getAuthentication().isAuthenticated() always will return true.
			             Look at LoginView.beforeEnter method.
			             more info: https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/anonymous.html
			             */
			        ;
			    }
			
			    @Autowired
			    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			        for(Admin admin:adminRepository.findAll()){
			        	auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
			        		.withUser(admin.getUsername()).password(admin.getPassword()).roles("ADMIN");
			        }
			    }
			
			    /**
			     * Expose the AuthenticationManager (to be used in LoginView)
			     *
			     * @return
			     * @throws Exception
			     */
			    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
			    @Override
			    public AuthenticationManager authenticationManagerBean() throws Exception {
			        return super.authenticationManagerBean();
			    }
			}
		'''
	}

	def genServletInitializer() {
		'''
		package «PACKAGE»;
		
		import org.springframework.boot.builder.SpringApplicationBuilder;
		import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
		
		public class ServletInitializer extends SpringBootServletInitializer {
		
			@Override
			protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
				return application.sources(KlostertrophyApplication.class);
			}
		
		}
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
				    MainView(HttpServletRequest request, «FOR e : backend.entities.filter[it.display] SEPARATOR ', '»«e.name.toFirstUpper»GridPage «e.name»Page«ENDFOR») {
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
	def genEntityClass(Entity entity) {
		'''
			package «PACKAGE».entities;
			
			import lombok.Getter;
			import lombok.NoArgsConstructor;
			import lombok.Setter;
			import javax.persistence.*;
			import java.util.Set;
			
			@Setter
			@Getter
			@NoArgsConstructor
			@Entity
			public class «entity.name»Gen {
				
				@Id
				@GeneratedValue
				@Column(name = "«entity.name.toFirstLower»_id")
				private Long id;
				
				// attributes
				«FOR attribute : entity.attributes»
					«IF attribute instanceof TypeAttribute»
						private «attribute.type» «attribute.name»;
					«ELSEIF attribute instanceof EnumAttribute»
						@Enumerated(EnumType.STRING)
						private «attribute.name.toFirstUpper» «attribute.name»;
					«ENDIF»
				«ENDFOR»
				
				// inward relations
				«FOR relation : entity.inwardRelations»
					«IF relation.type == RelationType.ONE_TO_ONE_VALUE»
						@OneToOne(mappedBy = "«relation.start.name.toFirstLower»")
						private «relation.start.name.toFirstUpper» «relation.start.name.toFirstLower»;
					«ELSEIF relation.type == RelationType.ONE_TO_MANY_VALUE»
						@ManyToOne
						@JoinColumn(name = "«relation.start.name.toFirstLower»_id", nullable = false)
						private «relation.start.name.toFirstUpper» «relation.start.name.toFirstLower»;
					«ELSE»
						@ManyToMany(mappedBy = "«relation.end.name.toFirstLower»s")
						private Set<«relation.start.name.toFirstUpper»> «relation.start.name.toFirstLower»s;
					«ENDIF»
				«ENDFOR»
				
				// outward relations
				«FOR relation : entity.outwardRelations»
					«IF relation.type == RelationType.ONE_TO_ONE_VALUE»
						@OneToOne(cascade = CascadeType.ALL)
						@JoinColumn(name = "«relation.end.name.toFirstLower»_id", referencedColumnName = "«relation.end.name.toFirstLower»_id")
						private «relation.end.name.toFirstUpper» «relation.end.name.toFirstLower»;
					«ELSEIF relation.type == RelationType.ONE_TO_MANY_VALUE»
						@OneToMany(mappedBy = "«relation.start.name.toFirstLower»", cascade = CascadeType.ALL)
						private Set<«relation.end.name.toFirstUpper»> «relation.end.name.toFirstLower»s;
					«ELSE»
						@ManyToMany(cascade = CascadeType.ALL)
						@JoinTable(
						name = "«relation.start.name.toFirstUpper»«relation.end.name.toFirstUpper»",
						joinColumns = {@JoinColumn(name = "«relation.start.name.toFirstLower»_id")}, 
										inverseJoinColumns = {@JoinColumn(name = "«relation.end.name.toFirstLower»_id")})
						private Set<«relation.end.name.toFirstUpper»> «relation.end.name.toFirstLower»s;
					«ENDIF»
				«ENDFOR»
				
				// enums
				«FOR attribute : entity.attributes»
					«IF attribute instanceof EnumAttribute»
						public enum «attribute.name.toFirstUpper»{
							«FOR value:attribute.values SEPARATOR ", "»
								«value.toUpperCase»
							«ENDFOR»
						}
					«ENDIF»
				«ENDFOR»
			}
		'''
	}

	def genTransientEntityClass(Entity entity) {
		'''
			package «PACKAGE».entities;
			
			import lombok.Getter;
			import lombok.NoArgsConstructor;
			import lombok.Setter;
			import javax.persistence.*;
			import java.util.Set;
			
			@Setter
			@Getter
			@NoArgsConstructor
			public class «entity.name»Gen {
				
				// attributes
				«FOR attribute : entity.attributes»
					«IF attribute instanceof TypeAttribute»
						private «attribute.type» «attribute.name»;
					«ELSEIF attribute instanceof EnumAttribute»
						private «attribute.name.toFirstUpper» «attribute.name»;
					«ENDIF»
				«ENDFOR»
				
				// inward relations
				«FOR relation : entity.inwardRelations»
					«IF relation.type == RelationType.ONE_TO_ONE_VALUE»
						private «relation.start.name.toFirstUpper» «relation.start.name.toFirstLower»;
					«ELSEIF relation.type == RelationType.ONE_TO_MANY_VALUE»
						private «relation.start.name.toFirstUpper» «relation.start.name.toFirstLower»;
					«ELSE»
						private Set<«relation.start.name.toFirstUpper»> «relation.start.name.toFirstLower»s;
					«ENDIF»
				«ENDFOR»
				
				// outward relations
				«FOR relation : entity.outwardRelations»
					«IF relation.type == RelationType.ONE_TO_ONE_VALUE»
						private «relation.end.name.toFirstUpper» «relation.end.name.toFirstLower»;
					«ELSEIF relation.type == RelationType.ONE_TO_MANY_VALUE»
						private Set<«relation.end.name.toFirstUpper»> «relation.end.name.toFirstLower»s;
					«ELSE»
						private Set<«relation.end.name.toFirstUpper»> «relation.end.name.toFirstLower»s;
					«ENDIF»
				«ENDFOR»
				
				// enums
				«FOR attribute : entity.attributes»
					«IF attribute instanceof EnumAttribute»
						public enum «attribute.name.toFirstUpper»{
							«FOR value:attribute.values SEPARATOR ", "»
								«value.toUpperCase»
							«ENDFOR»
						}
					«ENDIF»
				«ENDFOR»
			}
		'''
	}

	def genEntityExtensionClass(Entity entity) {
		'''
			package «PACKAGE».entities;
			
			import «PACKAGE».entities.«entity.name.toFirstUpper»Gen;
			
			public class «entity.name» extends «entity.name»Gen {
				
			}
		'''
	}

	def genEntityRepo(Entity entity) {
		'''
			package «PACKAGE».repositories;
			
			import «PACKAGE».entities.«entity.name.toFirstUpper»;
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

	def genEntityGridPage(Entity entity) {
		'''
				package «PACKAGE».pages;
				
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
				import «PACKAGE».entities.«entity.name.toFirstUpper»;
				import «PACKAGE».repositories.«entity.name.toFirstUpper»Repository;
				import «PACKAGE».details.«entity.name.toFirstUpper»Details;
				import «PACKAGE».editors.«entity.name.toFirstUpper»Editor;
				import org.slf4j.Logger;
				import org.slf4j.LoggerFactory;
				import org.springframework.beans.factory.annotation.Autowired;
				import org.springframework.stereotype.Component;
				import org.springframework.transaction.annotation.Transactional;
				import org.springframework.util.StringUtils;
				// Team Repository entfernt, da nur für Play verwendet
				
				@Component
				@Transactional
				@UIScope
				public class «entity.name.toFirstUpper»GridPage extends VerticalLayout {
				
			    private static final long serialVersionUID = -8733687422451328748L;
			    private static final Logger log = LoggerFactory.getLogger(«entity.name.toFirstUpper»GridPage.class);
				
				    private «entity.name.toFirstUpper»Repository «entity.name»Repository;
				    private Grid<«entity.name.toFirstUpper»> grid;
				    private «entity.name.toFirstUpper»Editor «entity.name»Editor;
				
				    private TextField filter;
				
				    private Button evaluate;
				
				    @Autowired
				    public «entity.name.toFirstUpper»GridPage(«entity.name.toFirstUpper»Repository «entity.name»Repository, «entity.name.toFirstUpper»Editor «entity.name»Editor) {
				        super();
				        this.«entity.name»Repository = «entity.name»Repository;
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
				        addNew.addClickListener(e -> this.«entity.name»Editor.edit(new «entity.name.toFirstUpper»()));
				
				        // filter
				        filter.setPlaceholder("Nach Namen filtern");
				        filter.setValueChangeMode(ValueChangeMode.EAGER);
				        filter.addValueChangeListener(e -> listValues(e.getValue()));
				
				        // editor
				        «entity.name»Editor.setChangeHandler(() -> {
				            «entity.name»Editor.close();
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
				        // add Columns
				        «FOR attribute : entity.attributes»
				        	grid.addColumn(«entity.name.toFirstUpper»::get«attribute.name.toFirstUpper»).setHeader("«attribute.name.toFirstUpper»").setSortable(true);
				        «ENDFOR»
				
				
				        // add standard-columns
			
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
				
				    }
				}
				
		'''
	}

	def genEntityEditor(Entity entity) {
		'''
			package «PACKAGE».editors;
			
			import com.vaadin.flow.component.Key;
			import com.vaadin.flow.component.KeyNotifier;
			import com.vaadin.flow.component.button.Button;
			import com.vaadin.flow.component.dialog.Dialog;
			import com.vaadin.flow.component.icon.VaadinIcon;
			import com.vaadin.flow.component.orderedlayout.FlexComponent;
			import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
			import com.vaadin.flow.component.select.Select;
			import com.vaadin.flow.component.textfield.TextField;
			import com.vaadin.flow.data.binder.Binder;
			import com.vaadin.flow.spring.annotation.UIScope;
			import «PACKAGE».entities.«entity.name.toFirstUpper»;
			import «PACKAGE».repositories.«entity.name.toFirstUpper»Repository;
			import «PACKAGE».ChangeHandler;
			import org.springframework.beans.factory.annotation.Autowired;
			import org.springframework.stereotype.Component;
			
			import java.util.ArrayList;
			import java.util.EnumSet;
			
			@Component
			@UIScope
			public class «entity.name.toFirstUpper»Editor extends Dialog implements KeyNotifier {
			
			    private «entity.name.toFirstUpper»Repository «entity.name.toFirstLower»Repository;
			    private ChangeHandler changeHandler;
			    private «entity.name.toFirstUpper» «entity.name.toFirstLower»;
			
			    //buttons
			    Button save = new Button("Speichern", VaadinIcon.CHECK.create());
			    Button cancel = new Button("Abbrechen");
			    Button delete = new Button("Löschen", VaadinIcon.TRASH.create());
			    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
			
			    //fields to edit
			    TextField name = new TextField("«entity.name.toFirstUpper»-Name");
				«FOR e : entity.attributes.filter[it instanceof EnumAttribute]»
					Select<«entity.name.toFirstUpper».«e.name.toFirstUpper»> «e.name» = new Select<>();
				«ENDFOR»
				HorizontalLayout fields = new HorizontalLayout(name, «FOR e : entity.attributes.filter[it instanceof EnumAttribute] SEPARATOR ', '»«e.name»«ENDFOR»);
				Binder<«entity.name.toFirstUpper»> binder = new Binder<>(«entity.name.toFirstUpper».class);
			
			    @Autowired
			    public «entity.name.toFirstUpper»Editor(«entity.name.toFirstUpper»Repository «entity.name.toFirstLower»Repository) {
			    	super();
			    	   this.«entity.name.toFirstLower»Repository = «entity.name.toFirstLower»Repository;
			    	   add(fields, actions);
			
			        // bind using naming convention
			        binder.bindInstanceFields(this);
			
			        //actions
			        save.getElement().getThemeList().add("primary");
			        delete.getElement().getThemeList().add("error");
			        addKeyPressListener(Key.ENTER, e -> save());
			        // wire action buttons to save, delete and reset
			        save.addClickListener(e -> save());
			        delete.addClickListener(e -> delete());
			        cancel.addClickListener(e -> changeHandler.onChange());
			
			        //fields
			        «FOR e : entity.attributes.filter[it instanceof EnumAttribute]»
			        	«e.name».setLabel("«e.name.toFirstUpper»");
			        	«e.name».setItemLabelGenerator(«entity.name.toFirstUpper».«e.name.toFirstUpper»::toString);
			        	«e.name».setItems(new ArrayList<>(EnumSet.allOf(«entity.name.toFirstUpper».«e.name.toFirstUpper».class)));
			        «ENDFOR»
			        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
			    }
			
			    public final void edit(«entity.name.toFirstUpper» «entity.name») {
			        if («entity.name» == null) {
			            close();
			            return;
			        }
			
			        final boolean persisted = «entity.name.toFirstLower».getId() != null;
			        if (persisted) {
			            // Find fresh entity for editing
			            this.«entity.name.toFirstLower» = «entity.name.toFirstLower»Repository.findById(«entity.name.toFirstLower».getId()).get();
			        }
			        else {
			            this.«entity.name.toFirstLower» = «entity.name.toFirstLower»;
			        }
			
			        this.binder.setBean(this.«entity.name.toFirstLower»);
			        open();
			        this.name.focus();
			    }
			
			    void save() {
			        if («FOR e : entity.attributes.filter[it instanceof EnumAttribute] SEPARATOR ' || '»this.«entity.name.toFirstLower».get«e.name.toFirstUpper»() == null«ENDFOR»«IF entity.attributes.filter[it instanceof EnumAttribute].size > 0» || «ENDIF»this.«entity.name.toFirstLower».getName() == null){
			            return;
			        }
			        «entity.name.toFirstLower»Repository.save(this.«entity.name.toFirstLower»);
			        this.changeHandler.onChange();
			    }
			
				void delete() {
				      «entity.name.toFirstLower»Repository.delete(this.«entity.name.toFirstLower»);
				      this.changeHandler.onChange();
				  }
			
			    public void setChangeHandler(ChangeHandler h) {
			        // ChangeHandler is notified when either save or delete is clicked
			        this.changeHandler = h;
			    }
			
			}
		'''
	}

	def genEntityDetails(Entity entity) {
		'''
		
		'''
	/*
	 * '''
	 * 	// Genrell: Für jedes relEntity eigenes Grid -> Schwachsinn??
	 * 
	 * 
	 * 	package «PACKAGE».frontend.details;
	 * 	
	 * 	import com.vaadin.flow.component.dialog.Dialog;
	 * 	import com.vaadin.flow.component.grid.Grid;
	 * 	import com.vaadin.flow.component.grid.GridSortOrder;
	 * 	import com.vaadin.flow.component.grid.GridVariant;
	 * 	import com.vaadin.flow.data.provider.SortDirection;
	 * 	import «PACKAGE».backend.entities.«entity.name.toFirstUpper»;
	 * 	«FOR relEntity : entity.relations» // TODO: Wie an Relations kommen?
	 * 		import «PACKAGE».backend.entities.«relEntity.name.toFirstUpper»;	// = import «PACKAGE».backend.entities.Team;
	 * 	«ENDFOR»
	 * 	
	 * 	import java.util.Collections;
	 * 	
	 * 	public class «entity.name.toFirstUpper»Details extends Dialog {
	 * 	
	 * 		«FOR relEntity : entity.relations» // TODO: Wie an Relations kommen?
	 * 	    	protected Grid<«relEntity.name.toFirstUpper»> grid«relEntity.name.toFirstUpper»; // = protected Grid<Team> grid;
	 * 	    «ENDFOR»
	 * 	
	 * 	    public «entity.name.toFirstUpper»Details() {
	 * 	        super();
	 * 		
	 * 			«FOR relEntity : entity.relations» // TODO: Wie an Relations kommen?
	 * 		        grid«relEntity.name.toFirstUpper» = new Grid<>(«relEntity.name.toFirstUpper».class);
	 * 		        grid«relEntity.name.toFirstUpper».setMultiSort(true);
	 * 		        // style
	 * 		        grid«relEntity.name.toFirstUpper».addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
	 * 		        //TODO: check if height scales accordingly; maybe find better method for setting size in general
	 * 		        grid«relEntity.name.toFirstUpper».setWidth("400px");
	 * 		
	 * 		        //set columns
	 * 		        grid«relEntity.name.toFirstUpper».removeAllColumns();
	 * 		        «FOR attribute : relEntity.attributes»
	 * 		        	grid.addColumn(«relEntity.name.toFirstUpper»::get«attribute.name.toFirstUpper».setHeader("«attribute.name.toFirstUpper»".setSortable(true)
	 * 		        «ENDFOR»
	 * 		
	 * 		        add(grid«relEntity.name.toFirstUpper»);
	 * 	        «ENDFOR»
	 * 	    }
	 * 	
	 * 	    public void open(«entity.name.toFirstUpper» «entity.name»){
	 * 	    	«FOR relEntity : entity.relations» // TODO: Wie an Relations kommen?
	 * 	    		grid«relEntity.name.toFirstUpper».setItems(«entity.name».getFinished());  // = grid.setItems(«entity.name».getFinished());
	 * 	    	«ENDFOR»
	 * 	        open();
	 * 	    }
	 * 	}
	 * 	
	 * '''
	 */
	}

}
