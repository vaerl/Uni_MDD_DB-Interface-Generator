/**
 */
package projectMdd.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import projectMdd.Attribute;
import projectMdd.Displayable;
import projectMdd.Entity;
import projectMdd.OutwardEntity;
import projectMdd.Persistable;
import projectMdd.ProjectMddPackage;
import projectMdd.Relation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.impl.EntityImpl#getOutwardRelations <em>Outward Relations</em>}</li>
 *   <li>{@link projectMdd.impl.EntityImpl#isTransient <em>Transient</em>}</li>
 *   <li>{@link projectMdd.impl.EntityImpl#isDisplay <em>Display</em>}</li>
 *   <li>{@link projectMdd.impl.EntityImpl#getName <em>Name</em>}</li>
 *   <li>{@link projectMdd.impl.EntityImpl#getAttributes <em>Attributes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EntityImpl extends InwardEntityImpl implements Entity {
	/**
	 * The cached value of the '{@link #getOutwardRelations() <em>Outward Relations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutwardRelations()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> outwardRelations;

	/**
	 * The default value of the '{@link #isTransient() <em>Transient</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransient()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TRANSIENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTransient() <em>Transient</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransient()
	 * @generated
	 * @ordered
	 */
	protected boolean transient_ = TRANSIENT_EDEFAULT;

	/**
	 * The default value of the '{@link #isDisplay() <em>Display</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDisplay()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DISPLAY_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isDisplay() <em>Display</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDisplay()
	 * @generated
	 * @ordered
	 */
	protected boolean display = DISPLAY_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attributes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProjectMddPackage.Literals.ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Relation> getOutwardRelations() {
		if (outwardRelations == null) {
			outwardRelations = new EObjectWithInverseResolvingEList<Relation>(Relation.class, this,
					ProjectMddPackage.ENTITY__OUTWARD_RELATIONS, ProjectMddPackage.RELATION__START);
		}
		return outwardRelations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isTransient() {
		return transient_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransient(boolean newTransient) {
		boolean oldTransient = transient_;
		transient_ = newTransient;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProjectMddPackage.ENTITY__TRANSIENT, oldTransient,
					transient_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDisplay() {
		return display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDisplay(boolean newDisplay) {
		boolean oldDisplay = display;
		display = newDisplay;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProjectMddPackage.ENTITY__DISPLAY, oldDisplay,
					display));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProjectMddPackage.ENTITY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Attribute> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<Attribute>(Attribute.class, this,
					ProjectMddPackage.ENTITY__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOutwardRelations()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			return ((InternalEList<?>) getOutwardRelations()).basicRemove(otherEnd, msgs);
		case ProjectMddPackage.ENTITY__ATTRIBUTES:
			return ((InternalEList<?>) getAttributes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			return getOutwardRelations();
		case ProjectMddPackage.ENTITY__TRANSIENT:
			return isTransient();
		case ProjectMddPackage.ENTITY__DISPLAY:
			return isDisplay();
		case ProjectMddPackage.ENTITY__NAME:
			return getName();
		case ProjectMddPackage.ENTITY__ATTRIBUTES:
			return getAttributes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			getOutwardRelations().clear();
			getOutwardRelations().addAll((Collection<? extends Relation>) newValue);
			return;
		case ProjectMddPackage.ENTITY__TRANSIENT:
			setTransient((Boolean) newValue);
			return;
		case ProjectMddPackage.ENTITY__DISPLAY:
			setDisplay((Boolean) newValue);
			return;
		case ProjectMddPackage.ENTITY__NAME:
			setName((String) newValue);
			return;
		case ProjectMddPackage.ENTITY__ATTRIBUTES:
			getAttributes().clear();
			getAttributes().addAll((Collection<? extends Attribute>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			getOutwardRelations().clear();
			return;
		case ProjectMddPackage.ENTITY__TRANSIENT:
			setTransient(TRANSIENT_EDEFAULT);
			return;
		case ProjectMddPackage.ENTITY__DISPLAY:
			setDisplay(DISPLAY_EDEFAULT);
			return;
		case ProjectMddPackage.ENTITY__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ProjectMddPackage.ENTITY__ATTRIBUTES:
			getAttributes().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
			return outwardRelations != null && !outwardRelations.isEmpty();
		case ProjectMddPackage.ENTITY__TRANSIENT:
			return transient_ != TRANSIENT_EDEFAULT;
		case ProjectMddPackage.ENTITY__DISPLAY:
			return display != DISPLAY_EDEFAULT;
		case ProjectMddPackage.ENTITY__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case ProjectMddPackage.ENTITY__ATTRIBUTES:
			return attributes != null && !attributes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == OutwardEntity.class) {
			switch (derivedFeatureID) {
			case ProjectMddPackage.ENTITY__OUTWARD_RELATIONS:
				return ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS;
			default:
				return -1;
			}
		}
		if (baseClass == Persistable.class) {
			switch (derivedFeatureID) {
			case ProjectMddPackage.ENTITY__TRANSIENT:
				return ProjectMddPackage.PERSISTABLE__TRANSIENT;
			default:
				return -1;
			}
		}
		if (baseClass == Displayable.class) {
			switch (derivedFeatureID) {
			case ProjectMddPackage.ENTITY__DISPLAY:
				return ProjectMddPackage.DISPLAYABLE__DISPLAY;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == OutwardEntity.class) {
			switch (baseFeatureID) {
			case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
				return ProjectMddPackage.ENTITY__OUTWARD_RELATIONS;
			default:
				return -1;
			}
		}
		if (baseClass == Persistable.class) {
			switch (baseFeatureID) {
			case ProjectMddPackage.PERSISTABLE__TRANSIENT:
				return ProjectMddPackage.ENTITY__TRANSIENT;
			default:
				return -1;
			}
		}
		if (baseClass == Displayable.class) {
			switch (baseFeatureID) {
			case ProjectMddPackage.DISPLAYABLE__DISPLAY:
				return ProjectMddPackage.ENTITY__DISPLAY;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (transient: ");
		result.append(transient_);
		result.append(", display: ");
		result.append(display);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //EntityImpl
