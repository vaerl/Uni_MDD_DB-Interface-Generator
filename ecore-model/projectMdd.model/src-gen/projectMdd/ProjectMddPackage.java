/**
 */
package projectMdd;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see projectMdd.ProjectMddFactory
 * @model kind="package"
 * @generated
 */
public interface ProjectMddPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "projectMdd";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/projectMdd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "projectMdd";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProjectMddPackage eINSTANCE = projectMdd.impl.ProjectMddPackageImpl.init();

	/**
	 * The meta object id for the '{@link projectMdd.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see projectMdd.impl.EntityImpl
	 * @see projectMdd.impl.ProjectMddPackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 1;

	/**
	 * The meta object id for the '{@link projectMdd.impl.AssociationImpl <em>Association</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see projectMdd.impl.AssociationImpl
	 * @see projectMdd.impl.ProjectMddPackageImpl#getAssociation()
	 * @generated
	 */
	int ASSOCIATION = 2;

	/**
	 * The meta object id for the '{@link projectMdd.impl.BackendImpl <em>Backend</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see projectMdd.impl.BackendImpl
	 * @see projectMdd.impl.ProjectMddPackageImpl#getBackend()
	 * @generated
	 */
	int BACKEND = 0;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BACKEND__ENTITIES = 0;

	/**
	 * The feature id for the '<em><b>Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BACKEND__ASSOCIATIONS = 1;

	/**
	 * The number of structural features of the '<em>Backend</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BACKEND_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Backend</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BACKEND_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Association</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__ASSOCIATION = 0;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ENTITIES = 0;

	/**
	 * The number of structural features of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '{@link projectMdd.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see projectMdd.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the reference list '{@link projectMdd.Entity#getAssociation <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Association</em>'.
	 * @see projectMdd.Entity#getAssociation()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_Association();

	/**
	 * Returns the meta object for class '{@link projectMdd.Association <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association</em>'.
	 * @see projectMdd.Association
	 * @generated
	 */
	EClass getAssociation();

	/**
	 * Returns the meta object for the reference list '{@link projectMdd.Association#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Entities</em>'.
	 * @see projectMdd.Association#getEntities()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Entities();

	/**
	 * Returns the meta object for class '{@link projectMdd.Backend <em>Backend</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Backend</em>'.
	 * @see projectMdd.Backend
	 * @generated
	 */
	EClass getBackend();

	/**
	 * Returns the meta object for the containment reference list '{@link projectMdd.Backend#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entities</em>'.
	 * @see projectMdd.Backend#getEntities()
	 * @see #getBackend()
	 * @generated
	 */
	EReference getBackend_Entities();

	/**
	 * Returns the meta object for the containment reference list '{@link projectMdd.Backend#getAssociations <em>Associations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Associations</em>'.
	 * @see projectMdd.Backend#getAssociations()
	 * @see #getBackend()
	 * @generated
	 */
	EReference getBackend_Associations();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ProjectMddFactory getProjectMddFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link projectMdd.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see projectMdd.impl.EntityImpl
		 * @see projectMdd.impl.ProjectMddPackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Association</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__ASSOCIATION = eINSTANCE.getEntity_Association();

		/**
		 * The meta object literal for the '{@link projectMdd.impl.AssociationImpl <em>Association</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see projectMdd.impl.AssociationImpl
		 * @see projectMdd.impl.ProjectMddPackageImpl#getAssociation()
		 * @generated
		 */
		EClass ASSOCIATION = eINSTANCE.getAssociation();

		/**
		 * The meta object literal for the '<em><b>Entities</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__ENTITIES = eINSTANCE.getAssociation_Entities();

		/**
		 * The meta object literal for the '{@link projectMdd.impl.BackendImpl <em>Backend</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see projectMdd.impl.BackendImpl
		 * @see projectMdd.impl.ProjectMddPackageImpl#getBackend()
		 * @generated
		 */
		EClass BACKEND = eINSTANCE.getBackend();

		/**
		 * The meta object literal for the '<em><b>Entities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BACKEND__ENTITIES = eINSTANCE.getBackend_Entities();

		/**
		 * The meta object literal for the '<em><b>Associations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BACKEND__ASSOCIATIONS = eINSTANCE.getBackend_Associations();

	}

} //ProjectMddPackage
