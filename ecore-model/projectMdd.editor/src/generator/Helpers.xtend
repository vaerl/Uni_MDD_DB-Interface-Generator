package generator

import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.EAttribute
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
					«entity.name.toFirstUpper»Repository «entity.name.toFirstLower»Repository
				«ENDIF»
			«ENDFOR»
		'''
	}

	def static getReposAsImports(Backend backend, String PACKAGE) {
		'''
			«FOR entity : backend.entities»
				«IF !entity.transient»
					import «PACKAGE».repositories.«entity.name.toFirstUpper»Repository;
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
			«entity.name.toFirstUpper» «name» = new «entity.name.toFirstUpper»();
		'''
	}

	def static getRandomValueForType(Attribute attribute, Entity entity) {
		return switch attribute {
			TypeAttribute case attribute.type.value == DataType.BOOLEAN_VALUE: "true"
			TypeAttribute case attribute.type.value == DataType.INTEGER_VALUE: "-1"
			TypeAttribute case attribute.type.value == DataType.CHAR_VALUE: "'x'"
			TypeAttribute case attribute.type.value == DataType.DOUBLE_VALUE: "-2.0"
			TypeAttribute case attribute.type.value == DataType.STRING_VALUE: "\"asdf\""
			EnumAttribute case true: entity.name.toFirstUpper + "." + attribute.name.toFirstUpper + "." + attribute.values.get(0).toUpperCase
			default: "null"
		}
	}
	
	def static saveInRepo(Entity entity, String name){
		'''
			«entity.name.toFirstLower»Repository.save(«name»);
		'''
	}

}
