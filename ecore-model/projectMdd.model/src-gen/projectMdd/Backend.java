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
 *   <li>{@link projectMdd.Backend#getAssociations <em>Associations</em>}</li>
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
	 * Returns the value of the '<em><b>Associations</b></em>' containment reference list.
	 * The list contents are of type {@link projectMdd.Association}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Associations</em>' containment reference list.
	 * @see projectMdd.ProjectMddPackage#getBackend_Associations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Association> getAssociations();

} // Backend
