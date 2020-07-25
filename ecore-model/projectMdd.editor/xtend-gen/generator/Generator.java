package generator;

import com.google.common.collect.Iterators;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import projectMdd.Backend;
import projectMdd.Entity;

/**
 * The generator for ecore files.
 * 
 * @author Marco Richter
 */
@SuppressWarnings("all")
public class Generator {
  /**
   * The path where to generate the Java files.
   */
  public static final String SOURCE_FOLDER_PATH = "src-gen/main";
  
  /**
   * The base package name. Needs the succeeding dot!
   */
  public static final String PACKAGE = "de.thm.dbiGenerator.";
  
  public static final String PACKAGE_PATH = ("/" + Generator.PACKAGE.replaceAll("\\.", "/"));
  
  public static final String COMPLETE_PATH = (Generator.SOURCE_FOLDER_PATH + Generator.PACKAGE_PATH);
  
  public static final String EXTENDED_META_DATA = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
  
  public static final String MAX_INCLUSIVE = "maxInclusive";
  
  public static final String MIN_INCLUSIVE = "minInclusive";
  
  /**
   * Creates a file (containing the content-CharSequence) within the given IFolder.
   */
  public void createFile(final IFolder folder, final String fileName, final boolean overrideFile, final CharSequence content, final IProgressMonitor progressMonitor) {
    try {
      boolean _isCanceled = progressMonitor.isCanceled();
      if (_isCanceled) {
        throw new RuntimeException("Progress canceled");
      }
      boolean _exists = folder.exists();
      boolean _not = (!_exists);
      if (_not) {
        folder.create(true, true, null);
      }
      IFile iFile = folder.getFile(fileName);
      if ((iFile.exists() && true)) {
        iFile.delete(true, null);
      }
      boolean _exists_1 = iFile.exists();
      boolean _not_1 = (!_exists_1);
      if (_not_1) {
        byte[] _bytes = content.toString().getBytes();
        InputStream source = new ByteArrayInputStream(_bytes);
        iFile.create(source, true, null);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Starts the generation of the given Resource file in the given IProject.
   */
  public void doGenerate(final Resource resourceEcore, final IProject project, final IProgressMonitor progressMonitor) {
    try {
      progressMonitor.beginTask("Generating Java code.", 2);
      progressMonitor.subTask("Creating folders.");
      String path = "";
      String[] _split = Generator.COMPLETE_PATH.split("/");
      for (final String subPath : _split) {
        {
          String _path = path;
          path = (_path + (subPath + "/"));
          this.getAndCreateFolder(project, path);
        }
      }
      progressMonitor.subTask("Generating Entities");
      this.doGenerate(project, IteratorExtensions.<Backend>head(Iterators.<Backend>filter(resourceEcore.getAllContents(), Backend.class)), progressMonitor);
      this.makeProgressAndCheckCanceled(progressMonitor);
      progressMonitor.done();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        ex.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public void makeProgressAndCheckCanceled(final IProgressMonitor monitor) {
    monitor.worked(1);
    boolean _isCanceled = monitor.isCanceled();
    if (_isCanceled) {
      throw new RuntimeException("Progress canceled");
    }
  }
  
  public IFolder getAndCreateFolder(final IProject project, final String path) {
    try {
      IFolder folder = project.getFolder(path);
      boolean _exists = folder.exists();
      boolean _not = (!_exists);
      if (_not) {
        folder.create(true, true, null);
      }
      return folder;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void doGenerate(final IProject project, final EObject rootElement, final IProgressMonitor progressMonitor) {
    Backend backend = ((Backend) rootElement);
    IFolder sourceFolder = this.getAndCreateFolder(project, Generator.SOURCE_FOLDER_PATH.split("/")[0]);
    IFolder resourceFolder = this.getAndCreateFolder(project, (Generator.SOURCE_FOLDER_PATH + "/resources"));
    IFolder entityFolder = this.getAndCreateFolder(project, (Generator.COMPLETE_PATH + "/entities"));
    IFolder repoFolder = this.getAndCreateFolder(project, (Generator.COMPLETE_PATH + "/repos"));
    IFolder pageFolder = this.getAndCreateFolder(project, (Generator.COMPLETE_PATH + "/pages"));
    IFolder gridFolder = this.getAndCreateFolder(project, (Generator.COMPLETE_PATH + "/grids"));
    this.createFile(sourceFolder, "pom.xml", true, this.genPom(backend), progressMonitor);
    this.createFile(resourceFolder, "application.properties", true, this.genApplicationProperties(backend), progressMonitor);
    EList<Entity> _entities = backend.getEntities();
    for (final Entity entity : _entities) {
      {
        String _name = entity.getName();
        String _plus = (_name + ".java");
        this.createFile(entityFolder, _plus, true, this.genEntityExtensionClass(entity), progressMonitor);
        boolean _isTransient = entity.isTransient();
        boolean _not = (!_isTransient);
        if (_not) {
          String _name_1 = entity.getName();
          String _plus_1 = (_name_1 + "Gen.java");
          this.createFile(entityFolder, _plus_1, true, this.genEntityClass(entity), progressMonitor);
          String _name_2 = entity.getName();
          String _plus_2 = (_name_2 + "Repo.java");
          this.createFile(repoFolder, _plus_2, true, this.genEntityRepo(entity), progressMonitor);
          boolean _isDisplay = entity.isDisplay();
          if (_isDisplay) {
            String _name_3 = entity.getName();
            String _plus_3 = (_name_3 + "Page.java");
            this.createFile(pageFolder, _plus_3, true, this.genEntityPage(entity), progressMonitor);
            String _name_4 = entity.getName();
            String _plus_4 = (_name_4 + "Grid.java");
            this.createFile(gridFolder, _plus_4, true, this.genEntityGrid(entity), progressMonitor);
          }
        } else {
          String _name_5 = entity.getName();
          String _plus_5 = (_name_5 + "Gen.java");
          this.createFile(entityFolder, _plus_5, true, this.genTransientEntityClass(entity), progressMonitor);
        }
      }
    }
  }
  
  public CharSequence genPom(final Backend backend) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<modelVersion>4.0.0</modelVersion>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<parent>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<artifactId>spring-boot-starter-parent</artifactId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<version>2.3.1.RELEASE</version>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<relativePath/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</parent>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<groupId>");
    String _projectName = backend.getProjectName();
    _builder.append(_projectName, "\t");
    _builder.append("</groupId>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<artifactId>");
    String _projectName_1 = backend.getProjectName();
    _builder.append(_projectName_1, "\t");
    _builder.append("</artifactId>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<version>0.0.1-SNAPSHOT</version>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<packaging>war</packaging>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>");
    String _projectName_2 = backend.getProjectName();
    _builder.append(_projectName_2, "\t");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<description>");
    String _projectDescription = backend.getProjectDescription();
    _builder.append(_projectDescription, "\t");
    _builder.append("</description>");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<properties>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<java.version>14</java.version>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<vaadin.version>16.0.1</vaadin.version>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</properties>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<dependencies>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<!-- Spring -->");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>spring-boot-starter-data-jpa</artifactId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>spring-boot-starter-security</artifactId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>spring-boot-starter-tomcat</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<scope>provided</scope>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>spring-boot-starter-test</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<scope>test</scope>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<exclusions>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<exclusion>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("<groupId>org.junit.vintage</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("<artifactId>junit-vintage-engine</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</exclusion>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</exclusions>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>org.springframework.security</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>spring-security-test</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<scope>test</scope>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<!-- vaadin -->");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>com.vaadin</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>vaadin-spring-boot-starter</artifactId>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<!-- database -->");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<groupId>mysql</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<artifactId>mysql-connector-java</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<scope>runtime</scope>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<!-- miscellaneous -->");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t    ");
    _builder.append("<groupId>org.projectlombok</groupId>");
    _builder.newLine();
    _builder.append("\t\t    ");
    _builder.append("<artifactId>lombok</artifactId>");
    _builder.newLine();
    _builder.append("\t\t    ");
    _builder.append("<version>1.18.12</version>");
    _builder.newLine();
    _builder.append("\t\t    ");
    _builder.append("<scope>provided</scope>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</dependencies>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<dependencyManagement>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<dependencies>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<dependency>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<groupId>com.vaadin</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<artifactId>vaadin-bom</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<version>${vaadin.version}</version>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<type>pom</type>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<scope>import</scope>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</dependency>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</dependencies>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</dependencyManagement>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<build>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<plugins>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<plugin>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<groupId>org.springframework.boot</groupId>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<artifactId>spring-boot-maven-plugin</artifactId>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</plugin>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</plugins>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</build>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genApplicationProperties(final Backend backend) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# DATABASE");
    _builder.newLine();
    _builder.append("spring.jpa.hibernate.ddl-auto=create");
    _builder.newLine();
    _builder.append("spring.datasource.url=jdbc:mysql://");
    String _host = backend.getDatabase().getHost();
    _builder.append(_host);
    _builder.append(":");
    String _port = backend.getDatabase().getPort();
    _builder.append(_port);
    _builder.append("/");
    String _schema = backend.getDatabase().getSchema();
    _builder.append(_schema);
    _builder.append("?useSSL=false&allowPublicKeyRetrieval=true");
    _builder.newLineIfNotEmpty();
    _builder.append("spring.datasource.username=");
    String _username = backend.getDatabase().getUsername();
    _builder.append(_username);
    _builder.newLineIfNotEmpty();
    _builder.append("spring.datasource.password=");
    String _password = backend.getDatabase().getPassword();
    _builder.append(_password);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("# LOGGING");
    _builder.newLine();
    _builder.append("logging.level.root=DEBUG");
    _builder.newLine();
    _builder.append("org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG");
    _builder.newLine();
    return _builder;
  }
  
  public String genEntityClass(final Entity entity) {
    return "fix implementation";
  }
  
  public CharSequence genTransientEntityClass(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence genEntityExtensionClass(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(Generator.PACKAGE);
    _builder.append("entities;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class ");
    String _name = entity.getName();
    _builder.append(_name);
    _builder.append(" extends ");
    String _name_1 = entity.getName();
    _builder.append(_name_1);
    _builder.append("Gen {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genEntityRepo(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence genEntityPage(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence genEntityGrid(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
}
