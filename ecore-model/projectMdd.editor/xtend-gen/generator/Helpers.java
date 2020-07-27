package generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import projectMdd.Backend;
import projectMdd.Entity;

@SuppressWarnings("all")
public class Helpers {
  public static boolean isString(final EAttribute a) {
    int _classifierID = a.getEAttributeType().getClassifierID();
    return (_classifierID == EcorePackage.ESTRING);
  }
  
  public static CharSequence getReposAsParams(final Backend backend) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Entity> _entities = backend.getEntities();
      boolean _hasElements = false;
      for(final Entity entity : _entities) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        {
          boolean _isTransient = entity.isTransient();
          boolean _not = (!_isTransient);
          if (_not) {
            String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
            _builder.append(_firstUpper);
            _builder.append("Repository ");
            String _name = entity.getName();
            _builder.append(_name);
            _builder.append("Repository");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence getReposAsImports(final Backend backend, final String PACKAGE) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Entity> _entities = backend.getEntities();
      for(final Entity entity : _entities) {
        {
          boolean _isTransient = entity.isTransient();
          boolean _not = (!_isTransient);
          if (_not) {
            _builder.append("import ");
            _builder.append(PACKAGE);
            _builder.append(".entities.");
            String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
            _builder.append(_firstUpper);
            _builder.append("Repository;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence getEntityAsParam(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
    _builder.append(_firstUpper);
    _builder.append(" ");
    String _name = entity.getName();
    _builder.append(_name);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static CharSequence getEntitiesAsImports(final Backend backend, final String PACKAGE) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Entity> _entities = backend.getEntities();
      for(final Entity entity : _entities) {
        {
          boolean _isTransient = entity.isTransient();
          boolean _not = (!_isTransient);
          if (_not) {
            _builder.append("import ");
            _builder.append(PACKAGE);
            _builder.append(".entities.");
            String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
            _builder.append(_firstUpper);
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
}
