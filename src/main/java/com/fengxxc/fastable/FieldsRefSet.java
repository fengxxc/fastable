package com.fengxxc.fastable;

import java.util.BitSet;

/**
 * com.fengxxc.fastable.FieldsRefSet
 */
public class FieldsRefSet {

    private int primaryKeyRef;
    private BitSet fieldRefSet;
    
    public FieldsRefSet(int primaryKeyRef) {
        this.primaryKeyRef = primaryKeyRef;
        this.fieldRefSet = new BitSet();
    }

    public FieldsRefSet(int primaryKeyRef, int... fieldRefs) {
        this.primaryKeyRef = primaryKeyRef;
        BitSet fieldRefSet = new BitSet();
        for (int i = 0; i < fieldRefs.length; i++) {
            fieldRefSet.set(fieldRefs[i]);
        }
        this.fieldRefSet = fieldRefSet;
    }

    /**
     * @return the primaryKeyRef
     */
    public int getPrimaryKeyRef() {
        return primaryKeyRef;
    }
    /**
     * @param primaryKeyRef the primaryKeyRef to set
     */
    public FieldsRefSet setPrimaryKeyRef(int primaryKeyRef) {
        this.primaryKeyRef = primaryKeyRef;
        return this;
    }
    /**
     * @return the fieldRefSet
     */
    public BitSet getFieldRefSet() {
        return fieldRefSet;
    }
    /**
     * @param fieldRefSet the fieldRefSet to set
     */
    public FieldsRefSet setFieldRefSet(BitSet fieldRefSet) {
        this.fieldRefSet = fieldRefSet;
        return this;
    }

    public FieldsRefSet addFieldRefs(int... fieldRefs) {
        for (int i = 0; i < fieldRefs.length; i++)
            this.fieldRefSet.set(fieldRefs[i]);
        return this;
    }

    public BitSet getAllRefs() {
        BitSet r = (BitSet) getFieldRefSet().clone();
        r.set(getPrimaryKeyRef());
        return r;
    }

    @Override
    public String toString() {
        return "{" + getPrimaryKeyRef() + ": " + getFieldRefSet().toString() + "}";
    }
}