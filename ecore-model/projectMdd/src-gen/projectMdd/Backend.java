/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Backend</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Backend#getEntities <em>Entities</em>}</li>
 *   <li>{@link projectMdd.Backend#getRelation <em>Relation</em>}</li>
 *   <li>{@link projectMdd.Backend#getDatabase <em>Database</em>}</li>
 *   <li>{@link projectMdd.Backend#getProjectName <em>Project Name</em>}</li>
 *   <li>{@link projectMdd.Backend#getProjectDescription <em>Project Description</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getBackend()
 * @model
 * @generated
 */
public interface Backend extends EObject {

	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link projectMdd.Entity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see projectMdd.ProjectMddPackage#getBackend_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getEntities();

	/**
	 * Returns the value of the '<em><b>Relation</b></em>' containment reference list.
	 * The list contents are of type {@link projectMdd.Relation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relation</em>' containment reference list.
	 * @see projectMdd.ProjectMddPackage#getBackend_Relation()
	 * @model containment="true"
	 * @generated
	 */
	EList<Relation> getRelation();

	/**
	 * Returns the value of the '<em><b>Database</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Database</em>' containment reference.
	 * @see #setDatabase(Database)
	 * @see projectMdd.ProjectMddPackage#getBackend_Database()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Database getDatabase();

	/**
	 * Sets the value of the '{@link projectMdd.Backend#getDatabase <em>Database</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database</em>' containment reference.
	 * @see #getDatabase()
	 * @generated
	 */
	void setDatabase(Database value);

	/**
	 * Returns the value of the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project Name</em>' attribute.
	 * @see #setProjectName(String)
	 * @see projectMdd.ProjectMddPackage#getBackend_ProjectName()
	 * @model required="true"
	 * @generated
	 */
	String getProjectName();

	/**
	 * Sets the value of the '{@link projectMdd.Backend#getProjectName <em>Project Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project Name</em>' attribute.
	 * @see #getProjectName()
	 * @generated
	 */
	void setProjectName(String value);

	/**
	 * Returns the value of the '<em><b>Project Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project Description</em>' attribute.
	 * @see #setProjectDescription(String)
	 * @see projectMdd.ProjectMddPackage#getBackend_ProjectDescription()
	 * @model required="true"
	 * @generated
	 */
	String getProjectDescription();

	/**
	 * Sets the value of the '{@link projectMdd.Backend#getProjectDescription <em>Project Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project Description</em>' attribute.
	 * @see #getProjectDescription()
	 * @generated
	 */
	void setProjectDescription(String value);
} // Backend
