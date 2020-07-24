package generator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcorePackage;

@SuppressWarnings("all")
public class Helpers {
  public static boolean isString(final EAttribute a) {
    int _classifierID = a.getEAttributeType().getClassifierID();
    return (_classifierID == EcorePackage.ESTRING);
  }
}
