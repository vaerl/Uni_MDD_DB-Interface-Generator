/**
 */
package projectMdd;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Persistable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Persistable#isTransient <em>Transient</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getPersistable()
 * @model
 * @generated
 */
public interface Persistable extends EObject {
	/**
	 * Returns the value of the '<em><b>Transient</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transient</em>' attribute.
	 * @see #setTransient(boolean)
	 * @see projectMdd.ProjectMddPackage#getPersistable_Transient()
	 * @model default="false"
	 * @generated
	 */
	boolean isTransient();

	/**
	 * Sets the value of the '{@link projectMdd.Persistable#isTransient <em>Transient</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transient</em>' attribute.
	 * @see #isTransient()
	 * @generated
	 */
	void setTransient(boolean value);

} // Persistable
