/**
 */
package projectMdd;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.TypeAttribute#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getTypeAttribute()
 * @model
 * @generated
 */
public interface TypeAttribute extends Attribute {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link projectMdd.DataType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see projectMdd.DataType
	 * @see #setType(DataType)
	 * @see projectMdd.ProjectMddPackage#getTypeAttribute_Type()
	 * @model
	 * @generated
	 */
	DataType getType();

	/**
	 * Sets the value of the '{@link projectMdd.TypeAttribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see projectMdd.DataType
	 * @see #getType()
	 * @generated
	 */
	void setType(DataType value);

} // TypeAttribute
