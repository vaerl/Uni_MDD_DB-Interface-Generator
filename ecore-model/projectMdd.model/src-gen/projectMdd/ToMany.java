/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>To Many</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.ToMany#getEntities <em>Entities</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getToMany()
 * @model
 * @generated
 */
public interface ToMany extends Relation {
	/**
	 * Returns the value of the '<em><b>Entities</b></em>' reference list.
	 * The list contents are of type {@link projectMdd.Entity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' reference list.
	 * @see projectMdd.ProjectMddPackage#getToMany_Entities()
	 * @model
	 * @generated
	 */
	EList<Entity> getEntities();

} // ToMany
