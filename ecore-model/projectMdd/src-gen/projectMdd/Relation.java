/**
 */
package projectMdd;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.Relation#getStart <em>Start</em>}</li>
 *   <li>{@link projectMdd.Relation#getName <em>Name</em>}</li>
 *   <li>{@link projectMdd.Relation#getEnd <em>End</em>}</li>
 *   <li>{@link projectMdd.Relation#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see projectMdd.ProjectMddPackage#getRelation()
 * @model
 * @generated
 */
public interface Relation extends Persistable, Displayable {
	/**
	 * Returns the value of the '<em><b>Start</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link projectMdd.OutwardEntity#getOutwardRelations <em>Outward Relations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' reference.
	 * @see #setStart(Entity)
	 * @see projectMdd.ProjectMddPackage#getRelation_Start()
	 * @see projectMdd.OutwardEntity#getOutwardRelations
	 * @model opposite="outwardRelations" required="true"
	 * @generated
	 */
	Entity getStart();

	/**
	 * Sets the value of the '{@link projectMdd.Relation#getStart <em>Start</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' reference.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(Entity value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see projectMdd.ProjectMddPackage#getRelation_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link projectMdd.Relation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link projectMdd.InwardEntity#getInwardRelations <em>Inward Relations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' reference.
	 * @see #setEnd(Entity)
	 * @see projectMdd.ProjectMddPackage#getRelation_End()
	 * @see projectMdd.InwardEntity#getInwardRelations
	 * @model opposite="inwardRelations" required="true"
	 * @generated
	 */
	Entity getEnd();

	/**
	 * Sets the value of the '{@link projectMdd.Relation#getEnd <em>End</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' reference.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(Entity value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link projectMdd.RelationType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see projectMdd.RelationType
	 * @see #setType(RelationType)
	 * @see projectMdd.ProjectMddPackage#getRelation_Type()
	 * @model
	 * @generated
	 */
	RelationType getType();

	/**
	 * Sets the value of the '{@link projectMdd.Relation#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see projectMdd.RelationType
	 * @see #getType()
	 * @generated
	 */
	void setType(RelationType value);

} // Relation
