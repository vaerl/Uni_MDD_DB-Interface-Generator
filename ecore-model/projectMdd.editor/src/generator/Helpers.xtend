package generator

import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.core.resources.IProject
import projectMdd.Backend
import projectMdd.Entity
import projectMdd.TypeAttribute
import projectMdd.Attribute
import projectMdd.DataType
import projectMdd.EnumAttribute

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

	def static createNewEntity(Entity entity, String name) {
		'''
			«entity.name.toFirstUpper» «name» = «entity.name.toFirstUpper»();
		'''
	}

	def static getRandomValueForType(Attribute attribute) {
		return switch attribute {
			TypeAttribute case attribute.type.value == DataType.BOOLEAN_VALUE: "true"
			TypeAttribute case attribute.type.value == DataType.INTEGER_VALUE: "-1"
			TypeAttribute case attribute.type.value == DataType.CHAR_VALUE: "'x'"
			TypeAttribute case attribute.type.value == DataType.DOUBLE_VALUE: "-2.0"
			TypeAttribute case attribute.type.value == DataType.STRING_VALUE: "\"asdf\""
			EnumAttribute case true: attribute.name.toFirstUpper + "." + attribute.values.get(0).toFirstUpper
			default: "null"
		}
	}

	def static setRandomValue(Attribute attribute, String entityName) {
		'''
			«entityName».set«attribute.name.toFirstUpper»(«attribute.getRandomValueForType»);
		'''
	}
	
	def static saveInRepo(Entity entity, String name){
		'''
			«entity.name.toFirstLower»Repository.save(«name»);
		'''
	}

}
