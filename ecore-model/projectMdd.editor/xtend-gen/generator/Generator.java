package generator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import projectMdd.Backend;

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
  public static final String SOURCE_FOLDER_PATH = "src-gen/";
  
  /**
   * The base package name.
   */
  public static final String PACKAGE = "de.thm.mdd.testapp.";
  
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
        String formattedCode = null;
        boolean _endsWith = fileName.endsWith(".java");
        if (_endsWith) {
        } else {
          boolean _endsWith_1 = fileName.endsWith(".xml");
          if (_endsWith_1) {
          }
        }
        byte[] bytes = null;
        if ((formattedCode != null)) {
          bytes = formattedCode.getBytes();
        } else {
          bytes = content.toString().getBytes();
          System.err.println((("File " + fileName) + " could not be formatted."));
        }
        InputStream source = new ByteArrayInputStream(bytes);
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
      progressMonitor.beginTask("Generating Java code", 2);
      progressMonitor.subTask("Creating folders");
      IFolder folder = project.getFolder(Generator.SOURCE_FOLDER_PATH);
      boolean _exists = folder.exists();
      boolean _not = (!_exists);
      if (_not) {
        folder.create(true, true, null);
      }
      folder = project.getFolder((Generator.SOURCE_FOLDER_PATH + "de"));
      boolean _exists_1 = folder.exists();
      boolean _not_1 = (!_exists_1);
      if (_not_1) {
        folder.create(true, true, null);
      }
      folder = project.getFolder((Generator.SOURCE_FOLDER_PATH + "/de/thm"));
      boolean _exists_2 = folder.exists();
      boolean _not_2 = (!_exists_2);
      if (_not_2) {
        folder.create(true, true, null);
      }
      folder = project.getFolder((Generator.SOURCE_FOLDER_PATH + "/de/thm/mdd"));
      boolean _exists_3 = folder.exists();
      boolean _not_3 = (!_exists_3);
      if (_not_3) {
        folder.create(true, true, null);
      }
      folder = project.getFolder((Generator.SOURCE_FOLDER_PATH + "/de/thm/mdd/testapp"));
      boolean _exists_4 = folder.exists();
      boolean _not_4 = (!_exists_4);
      if (_not_4) {
        folder.create(true, true, null);
      }
      folder = project.getFolder(((Generator.SOURCE_FOLDER_PATH + Generator.PACKAGE_PATH) + "entities"));
      boolean _exists_5 = folder.exists();
      boolean _not_5 = (!_exists_5);
      if (_not_5) {
        folder.create(true, true, null);
      }
      this.makeProgressAndCheckCanceled(progressMonitor);
      IFolder entityFolder = project.getFolder(((Generator.SOURCE_FOLDER_PATH + Generator.PACKAGE_PATH) + "entities"));
      progressMonitor.subTask("Generating Entities");
      this.doGenerate(entityFolder, IteratorExtensions.<Backend>head(Iterators.<Backend>filter(resourceEcore.getAllContents(), Backend.class)), progressMonitor);
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
  
  public void doGenerate(final IFolder outputFolder, final EObject rootElement, final IProgressMonitor progressMonitor) {
    Backend mc = ((Backend) rootElement);
  }
  
  public void makeProgressAndCheckCanceled(final IProgressMonitor monitor) {
    monitor.worked(1);
    boolean _isCanceled = monitor.isCanceled();
    if (_isCanceled) {
      throw new RuntimeException("Progress canceled");
    }
  }
  
  public CharSequence compileEntitiesGen(final EClass e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(Generator.PACKAGE);
    _builder.append("entities;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append("* This is the {@link ");
    String _name = e.getName();
    _builder.append(_name);
    _builder.append("} entity class.");
    _builder.newLineIfNotEmpty();
    _builder.append("*");
    _builder.newLine();
    _builder.append("*@generated");
    _builder.newLine();
    _builder.append("*/");
    _builder.newLine();
    _builder.append("public class ");
    String _name_1 = e.getName();
    _builder.append(_name_1);
    _builder.append("Gen ");
    {
      boolean _isEmpty = e.getEAllSuperTypes().isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append(" extends ");
        String _name_2 = IterableExtensions.<EClass>head(e.getEAllSuperTypes()).getName();
        _builder.append(_name_2);
        _builder.append(" ");
      }
    }
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// attributes");
    _builder.newLine();
    {
      EList<EAttribute> _eAllAttributes = e.getEAllAttributes();
      for(final EAttribute a : _eAllAttributes) {
        _builder.append("\t");
        _builder.append("private ");
        String _instanceTypeName = a.getEType().getInstanceTypeName();
        _builder.append(_instanceTypeName, "\t");
        _builder.append(" ");
        String _name_3 = a.getName();
        _builder.append(_name_3, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// references");
    _builder.newLine();
    {
      final Function1<EReference, Boolean> _function = (EReference it) -> {
        boolean _isMany = it.isMany();
        return Boolean.valueOf((!_isMany));
      };
      Iterable<EReference> _filter = IterableExtensions.<EReference>filter(e.getEAllReferences(), _function);
      for(final EReference a_1 : _filter) {
        _builder.append("\t");
        _builder.append("private ");
        String _name_4 = a_1.getEReferenceType().getName();
        _builder.append(_name_4, "\t");
        _builder.append(" ");
        String _name_5 = a_1.getName();
        _builder.append(_name_5, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      final Function1<EReference, Boolean> _function_1 = (EReference it) -> {
        return Boolean.valueOf(it.isMany());
      };
      Iterable<EReference> _filter_1 = IterableExtensions.<EReference>filter(e.getEAllReferences(), _function_1);
      for(final EReference a_2 : _filter_1) {
        _builder.append("\t");
        _builder.append("private java.util.ArrayList<");
        String _name_6 = a_2.getEReferenceType().getName();
        _builder.append(_name_6, "\t");
        _builder.append("> ");
        String _name_7 = a_2.getName();
        _builder.append(_name_7, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/**");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("* Default constructor.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _name_8 = e.getName();
    _builder.append(_name_8, "\t");
    _builder.append("Gen() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _isEmpty_1 = e.getEAllAttributes().isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder.append("\t");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("* Constructor for all attributes.");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ");
        String _name_9 = e.getName();
        _builder.append(_name_9, "\t");
        _builder.append("Gen(");
        {
          EList<EAttribute> _eAllAttributes_1 = e.getEAllAttributes();
          boolean _hasElements = false;
          for(final EAttribute a_3 : _eAllAttributes_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append(" ");
            String _instanceTypeName_1 = a_3.getEType().getInstanceTypeName();
            _builder.append(_instanceTypeName_1, "\t");
            _builder.append(" ");
            String _name_10 = a_3.getName();
            _builder.append(_name_10, "\t");
            _builder.append(" ");
          }
        }
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        {
          EList<EAttribute> _eAllAttributes_2 = e.getEAllAttributes();
          for(final EAttribute a_4 : _eAllAttributes_2) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("this.");
            String _name_11 = a_4.getName();
            _builder.append(_name_11, "\t\t");
            _builder.append(" = ");
            String _name_12 = a_4.getName();
            _builder.append(_name_12, "\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      if (((!e.getEAllAttributes().isEmpty()) && (!e.getEAllReferences().isEmpty()))) {
        _builder.append("\t");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("* Full constructor.");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ");
        String _name_13 = e.getName();
        _builder.append(_name_13, "\t");
        _builder.append("Gen(");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          EList<EAttribute> _eAllAttributes_3 = e.getEAllAttributes();
          boolean _hasElements_1 = false;
          for(final EAttribute a_5 : _eAllAttributes_3) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append(" ");
            String _instanceTypeName_2 = a_5.getEType().getInstanceTypeName();
            _builder.append(_instanceTypeName_2, "\t");
            _builder.append(" ");
            String _name_14 = a_5.getName();
            _builder.append(_name_14, "\t");
            _builder.append(" ");
          }
        }
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          final Function1<EReference, Boolean> _function_2 = (EReference it) -> {
            boolean _isMany = it.isMany();
            return Boolean.valueOf((!_isMany));
          };
          Iterable<EReference> _filter_2 = IterableExtensions.<EReference>filter(e.getEAllReferences(), _function_2);
          boolean _hasElements_2 = false;
          for(final EReference a_6 : _filter_2) {
            if (!_hasElements_2) {
              _hasElements_2 = true;
              _builder.append(", ", "\t");
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append(" ");
            String _name_15 = a_6.getEReferenceType().getName();
            _builder.append(_name_15, "\t");
            _builder.append(" ");
            String _name_16 = a_6.getName();
            _builder.append(_name_16, "\t");
            _builder.append(" ");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          final Function1<EReference, Boolean> _function_3 = (EReference it) -> {
            return Boolean.valueOf(it.isMany());
          };
          Iterable<EReference> _filter_3 = IterableExtensions.<EReference>filter(e.getEAllReferences(), _function_3);
          boolean _hasElements_3 = false;
          for(final EReference a_7 : _filter_3) {
            if (!_hasElements_3) {
              _hasElements_3 = true;
              _builder.append(", ", "\t");
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append(" java.util.ArrayList<");
            String _name_17 = a_7.getEReferenceType().getName();
            _builder.append(_name_17, "\t");
            _builder.append("> ");
            String _name_18 = a_7.getName();
            _builder.append(_name_18, "\t");
            _builder.append(" ");
          }
        }
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        {
          EList<EAttribute> _eAllAttributes_4 = e.getEAllAttributes();
          EList<EReference> _eAllReferences = e.getEAllReferences();
          Iterable<EStructuralFeature> _plus = Iterables.<EStructuralFeature>concat(_eAllAttributes_4, _eAllReferences);
          for(final EStructuralFeature a_8 : _plus) {
            _builder.append("\t");
            _builder.append("this.");
            String _name_19 = a_8.getName();
            _builder.append(_name_19, "\t");
            _builder.append(" = ");
            String _name_20 = a_8.getName();
            _builder.append(_name_20, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//TODO getter setter");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String toString() {");
    _builder.newLine();
    {
      final Function1<EAttribute, Boolean> _function_4 = (EAttribute it) -> {
        return Boolean.valueOf(this.isString(it));
      };
      boolean _exists = IterableExtensions.<EAttribute>exists(e.getEAllAttributes(), _function_4);
      if (_exists) {
        _builder.append("\t\t");
        _builder.append("StringBuilder builder = new StringBuilder();");
        _builder.newLine();
        {
          final Function1<EAttribute, Boolean> _function_5 = (EAttribute it) -> {
            return Boolean.valueOf(this.isString(it));
          };
          Iterable<EAttribute> _filter_4 = IterableExtensions.<EAttribute>filter(e.getEAllAttributes(), _function_5);
          for(final EAttribute a_9 : _filter_4) {
            _builder.append("\t\t");
            _builder.append("builder.append(");
            String _name_21 = a_9.getName();
            _builder.append(_name_21, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("builder.append(\" - \");");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
        _builder.append("return builder.toString();");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("return null; //TODO");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compileEntities(final EClass e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(Generator.PACKAGE);
    _builder.append("entities;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class ");
    String _name = e.getName();
    _builder.append(_name);
    _builder.append(" extends ");
    String _name_1 = e.getName();
    _builder.append(_name_1);
    _builder.append("Gen {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public boolean isString(final EAttribute a) {
    int _classifierID = a.getEAttributeType().getClassifierID();
    return (_classifierID == EcorePackage.ESTRING);
  }
}
