/**
 */
package projectMdd.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import projectMdd.OutwardEntity;
import projectMdd.ProjectMddPackage;
import projectMdd.Relation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Outward Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.impl.OutwardEntityImpl#getOutwardRelations <em>Outward Relations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OutwardEntityImpl extends MinimalEObjectImpl.Container implements OutwardEntity {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutwardEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProjectMddPackage.Literals.OUTWARD_ENTITY;
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
					ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS, ProjectMddPackage.RELATION__START);
		}
		return outwardRelations;
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
			return ((InternalEList<?>) getOutwardRelations()).basicRemove(otherEnd, msgs);
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
			return getOutwardRelations();
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
			getOutwardRelations().clear();
			getOutwardRelations().addAll((Collection<? extends Relation>) newValue);
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
			getOutwardRelations().clear();
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
		case ProjectMddPackage.OUTWARD_ENTITY__OUTWARD_RELATIONS:
			return outwardRelations != null && !outwardRelations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //OutwardEntityImpl
