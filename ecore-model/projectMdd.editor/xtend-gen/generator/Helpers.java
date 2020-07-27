package generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import projectMdd.Attribute;
import projectMdd.Backend;
import projectMdd.DataType;
import projectMdd.Entity;
import projectMdd.EnumAttribute;
import projectMdd.TypeAttribute;

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
            String _firstLower = StringExtensions.toFirstLower(entity.getName());
            _builder.append(_firstLower);
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
            _builder.append(".repositories.");
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
  
  public static CharSequence createNewEntity(final Entity entity, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
    _builder.append(_firstUpper);
    _builder.append(" ");
    _builder.append(name);
    _builder.append(" = new ");
    String _firstUpper_1 = StringExtensions.toFirstUpper(entity.getName());
    _builder.append(_firstUpper_1);
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static String getRandomValueForType(final Attribute attribute, final Entity entity) {
    String _switchResult = null;
    boolean _matched = false;
    if (attribute instanceof TypeAttribute) {
      int _value = ((TypeAttribute)attribute).getType().getValue();
      boolean _equals = (_value == DataType.BOOLEAN_VALUE);
      if (_equals) {
        _matched=true;
        _switchResult = "true";
      }
    }
    if (!_matched) {
      if (attribute instanceof TypeAttribute) {
        int _value = ((TypeAttribute)attribute).getType().getValue();
        boolean _equals = (_value == DataType.INTEGER_VALUE);
        if (_equals) {
          _matched=true;
          _switchResult = "-1";
        }
      }
    }
    if (!_matched) {
      if (attribute instanceof TypeAttribute) {
        int _value = ((TypeAttribute)attribute).getType().getValue();
        boolean _equals = (_value == DataType.CHAR_VALUE);
        if (_equals) {
          _matched=true;
          _switchResult = "\'x\'";
        }
      }
    }
    if (!_matched) {
      if (attribute instanceof TypeAttribute) {
        int _value = ((TypeAttribute)attribute).getType().getValue();
        boolean _equals = (_value == DataType.DOUBLE_VALUE);
        if (_equals) {
          _matched=true;
          _switchResult = "-2.0";
        }
      }
    }
    if (!_matched) {
      if (attribute instanceof TypeAttribute) {
        int _value = ((TypeAttribute)attribute).getType().getValue();
        boolean _equals = (_value == DataType.STRING_VALUE);
        if (_equals) {
          _matched=true;
          _switchResult = "\"asdf\"";
        }
      }
    }
    if (!_matched) {
      if (attribute instanceof EnumAttribute) {
        if (true) {
          _matched=true;
          String _firstUpper = StringExtensions.toFirstUpper(entity.getName());
          String _plus = (_firstUpper + ".");
          String _firstUpper_1 = StringExtensions.toFirstUpper(((EnumAttribute)attribute).getName());
          String _plus_1 = (_plus + _firstUpper_1);
          String _plus_2 = (_plus_1 + ".");
          String _upperCase = ((EnumAttribute)attribute).getValues().get(0).toUpperCase();
          _switchResult = (_plus_2 + _upperCase);
        }
      }
    }
    if (!_matched) {
      _switchResult = "null";
    }
    return _switchResult;
  }
  
  public static CharSequence saveInRepo(final Entity entity, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    String _firstLower = StringExtensions.toFirstLower(entity.getName());
    _builder.append(_firstLower);
    _builder.append("Repository.save(");
    _builder.append(name);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
