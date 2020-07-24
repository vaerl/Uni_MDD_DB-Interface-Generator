package generator

import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.core.resources.IProject

class Helpers {

	def static isString(EAttribute a) {
		a.EAttributeType.classifierID == EcorePackage.ESTRING
	}
}
