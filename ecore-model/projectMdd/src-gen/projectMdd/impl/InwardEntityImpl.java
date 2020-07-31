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

import projectMdd.InwardEntity;
import projectMdd.ProjectMddPackage;
import projectMdd.Relation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inward Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link projectMdd.impl.InwardEntityImpl#getInwardRelations <em>Inward Relations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InwardEntityImpl extends MinimalEObjectImpl.Container implements InwardEntity {
	/**
	 * The cached value of the '{@link #getInwardRelations() <em>Inward Relations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInwardRelations()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> inwardRelations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InwardEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProjectMddPackage.Literals.INWARD_ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Relation> getInwardRelations() {
		if (inwardRelations == null) {
			inwardRelations = new EObjectWithInverseResolvingEList<Relation>(Relation.class, this,
					ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS, ProjectMddPackage.RELATION__END);
		}
		return inwardRelations;
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getInwardRelations()).basicAdd(otherEnd, msgs);
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			return ((InternalEList<?>) getInwardRelations()).basicRemove(otherEnd, msgs);
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			return getInwardRelations();
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			getInwardRelations().clear();
			getInwardRelations().addAll((Collection<? extends Relation>) newValue);
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			getInwardRelations().clear();
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
		case ProjectMddPackage.INWARD_ENTITY__INWARD_RELATIONS:
			return inwardRelations != null && !inwardRelations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InwardEntityImpl
