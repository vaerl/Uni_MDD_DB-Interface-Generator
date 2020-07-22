/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Entity#getAssociation <em>Association</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getEntity()
 * @model
 * @generated
 */
public interface Entity extends EObject {
	/**
	 * Returns the value of the '<em><b>Association</b></em>' reference list.
	 * The list contents are of type {@link projectMdd.Association}.
	 * It is bidirectional and its opposite is '{@link projectMdd.Association#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Association</em>' reference list.
	 * @see projectMdd.ProjectMddPackage#getEntity_Association()
	 * @see projectMdd.Association#getEntities
	 * @model opposite="entities"
	 * @generated
	 */
	EList<Association> getAssociation();

} // Entity
