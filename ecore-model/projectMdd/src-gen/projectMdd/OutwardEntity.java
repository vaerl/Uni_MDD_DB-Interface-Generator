/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Outward Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.OutwardEntity#getOutwardRelations <em>Outward Relations</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getOutwardEntity()
 * @model
 * @generated
 */
public interface OutwardEntity extends EObject {
	/**
	 * Returns the value of the '<em><b>Outward Relations</b></em>' reference list.
	 * The list contents are of type {@link projectMdd.Relation}.
	 * It is bidirectional and its opposite is '{@link projectMdd.Relation#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outward Relations</em>' reference list.
	 * @see projectMdd.ProjectMddPackage#getOutwardEntity_OutwardRelations()
	 * @see projectMdd.Relation#getStart
	 * @model opposite="start"
	 * @generated
	 */
	EList<Relation> getOutwardRelations();

} // OutwardEntity
