/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Association</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Association#getEntities <em>Entities</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getAssociation()
 * @model
 * @generated
 */
public interface Association extends EObject {
	/**
	 * Returns the value of the '<em><b>Entities</b></em>' reference list.
	 * The list contents are of type {@link projectMdd.Entity}.
	 * It is bidirectional and its opposite is '{@link projectMdd.Entity#getAssociation <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' reference list.
	 * @see projectMdd.ProjectMddPackage#getAssociation_Entities()
	 * @see projectMdd.Entity#getAssociation
	 * @model opposite="association" required="true"
	 * @generated
	 */
	EList<Entity> getEntities();

} // Association
