/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inward Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.InwardEntity#getInwardRelations <em>Inward Relations</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getInwardEntity()
 * @model
 * @generated
 */
public interface InwardEntity extends EObject {
	/**
	 * Returns the value of the '<em><b>Inward Relations</b></em>' reference list.
	 * The list contents are of type {@link projectMdd.Relation}.
	 * It is bidirectional and its opposite is '{@link projectMdd.Relation#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inward Relations</em>' reference list.
	 * @see projectMdd.ProjectMddPackage#getInwardEntity_InwardRelations()
	 * @see projectMdd.Relation#getEnd
	 * @model opposite="end"
	 * @generated
	 */
	EList<Relation> getInwardRelations();

} // InwardEntity
