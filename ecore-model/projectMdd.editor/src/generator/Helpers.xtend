package generator

import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.core.resources.IProject
import projectMdd.Backend
import projectMdd.Entity

class Helpers {

	def static isString(EAttribute a) {
		a.EAttributeType.classifierID == EcorePackage.ESTRING
	}
	
}
