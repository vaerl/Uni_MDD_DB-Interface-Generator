/**
 */
package projectMdd;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>To One</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.ToOne#getEntity <em>Entity</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getToOne()
 * @model
 * @generated
 */
public interface ToOne extends Relation {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(Entity)
	 * @see projectMdd.ProjectMddPackage#getToOne_Entity()
	 * @model
	 * @generated
	 */
	Entity getEntity();

	/**
	 * Sets the value of the '{@link projectMdd.ToOne#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(Entity value);

} // ToOne
