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

	def static getReposAsParams(Backend backend) {
		'''
			«FOR entity : backend.entities SEPARATOR ", "»
				«IF !entity.transient»
					«entity.name.toFirstUpper»Repository «entity.name»Repository
				«ENDIF»
			«ENDFOR»
		'''
	}

	def static getReposAsImports(Backend backend, String PACKAGE) {
		'''
			«FOR entity : backend.entities»
				«IF !entity.transient»
					import «PACKAGE».entities.«entity.name.toFirstUpper»Repository;
				«ENDIF»
			«ENDFOR»
		'''
	}

	def static getEntityAsParam(Entity entity) {
		'''
			«entity.name.toFirstUpper» «entity.name»
		'''
	}

	def static getEntitiesAsImports(Backend backend, String PACKAGE) {
		'''
			«FOR entity : backend.entities»
				«IF !entity.transient»
					import «PACKAGE».entities.«entity.name.toFirstUpper»;
				«ENDIF»
			«ENDFOR»
		'''
	}

}
