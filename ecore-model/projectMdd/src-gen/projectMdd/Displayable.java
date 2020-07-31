/**
 */
package projectMdd;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Displayable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Displayable#isDisplay <em>Display</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getDisplayable()
 * @model
 * @generated
 */
public interface Displayable extends EObject {
	/**
	 * Returns the value of the '<em><b>Display</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display</em>' attribute.
	 * @see #setDisplay(boolean)
	 * @see projectMdd.ProjectMddPackage#getDisplayable_Display()
	 * @model default="true"
	 * @generated
	 */
	boolean isDisplay();

	/**
	 * Sets the value of the '{@link projectMdd.Displayable#isDisplay <em>Display</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display</em>' attribute.
	 * @see #isDisplay()
	 * @generated
	 */
	void setDisplay(boolean value);

} // Displayable
