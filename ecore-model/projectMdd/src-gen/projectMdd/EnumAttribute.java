/**
 */
package projectMdd;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.EnumAttribute#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getEnumAttribute()
 * @model
 * @generated
 */
public interface EnumAttribute extends Attribute {
	/**
	 * Returns the value of the '<em><b>Values</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' attribute list.
	 * @see projectMdd.ProjectMddPackage#getEnumAttribute_Values()
	 * @model
	 * @generated
	 */
	EList<String> getValues();

} // EnumAttribute
