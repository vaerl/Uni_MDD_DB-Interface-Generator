/**
 */
package projectMdd;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see projectMdd.ProjectMddPackage
 * @generated
 */
public interface ProjectMddFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProjectMddFactory eINSTANCE = projectMdd.impl.ProjectMddFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Backend</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Backend</em>'.
	 * @generated
	 */
	Backend createBackend();

	/**
	 * Returns a new object of class '<em>Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity</em>'.
	 * @generated
	 */
	Entity createEntity();

	/**
	 * Returns a new object of class '<em>Relation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Relation</em>'.
	 * @generated
	 */
	Relation createRelation();

	/**
	 * Returns a new object of class '<em>Outward Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Outward Entity</em>'.
	 * @generated
	 */
	OutwardEntity createOutwardEntity();

	/**
	 * Returns a new object of class '<em>Inward Entity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Inward Entity</em>'.
	 * @generated
	 */
	InwardEntity createInwardEntity();

	/**
	 * Returns a new object of class '<em>Persistable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Persistable</em>'.
	 * @generated
	 */
	Persistable createPersistable();

	/**
	 * Returns a new object of class '<em>Displayable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Displayable</em>'.
	 * @generated
	 */
	Displayable createDisplayable();

	/**
	 * Returns a new object of class '<em>Database</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Database</em>'.
	 * @generated
	 */
	Database createDatabase();

	/**
	 * Returns a new object of class '<em>Enum Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Attribute</em>'.
	 * @generated
	 */
	EnumAttribute createEnumAttribute();

	/**
	 * Returns a new object of class '<em>Type Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Type Attribute</em>'.
	 * @generated
	 */
	TypeAttribute createTypeAttribute();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ProjectMddPackage getProjectMddPackage();

} //ProjectMddFactory
