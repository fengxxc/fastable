package com.fengxxc.fastable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.fengxxc.fastable.sortable.ISortableSet;

/**
 * com.fengxxc.fastable.Finder
 */
public class Finder<T> {

    private Fastable<T> fstb;
    private BitSet tempFindFieldsRef;

    public Finder(Fastable<T> fstb) {
        this.fstb = fstb;
    }
    
    public Finder(Fastable<T> fstb, String fProperty, Object fValue) {
        this(fstb);
        this.and(fProperty, fValue);
    }

    private BitSet findFieldsRef(String property, Object value) {
        if (Fastable.BEAN.equals(fstb.getRawDataType())) {
            property = Utils.FristChartoLower(property);
        }
        BitSet findIds = new BitSet();
        FieldsRefSet fieldsRefSet = fstb.getPv2FieldsRefMap().getFieldsRef(property, value);
        if (fieldsRefSet == null) {
            return findIds;
        }
        if (fstb.getUniqueProperty().equals(property)) {
            // 根据唯一列的查找
            findIds = new BitSet(1);
            findIds.set(fieldsRefSet.getPrimaryKeyRef());
        } else {
            // 根据非唯一列的查找
            findIds = fieldsRefSet.getFieldRefSet();
        }
        return findIds;
    }

    public Finder<T> and(String property, Object value) {
        BitSet findIds = findFieldsRef(property, value);
        if (findIds.isEmpty()) {
            this.tempFindFieldsRef = null;
            return this;
        }
        if (this.tempFindFieldsRef != null)
            this.tempFindFieldsRef.and(findIds);
        else 
            this.tempFindFieldsRef = findIds;
        return this;
    }

    public Finder<T> or(String property, Object value) {
        BitSet findIds = findFieldsRef(property, value);
        if (findIds.isEmpty()) {
            return this;
        }
        if (this.tempFindFieldsRef != null)
            this.tempFindFieldsRef.or(findIds);
        else 
            this.tempFindFieldsRef = findIds;
        return this;
    }

    public Finder<T> not(String property, Object value) {
        BitSet findIds = findFieldsRef(property, value);
        if (findIds.isEmpty()) {
            return this;
        }
        if (this.tempFindFieldsRef != null)
            Utils.BitSet_not(this.tempFindFieldsRef, findIds);
        return this;
    }

    public Finder<T> andRange(String sortableProperty, int start , int end , boolean includeStart, boolean includeEnd) {
        BitSet fieldsRef = findFieldsRefRange(sortableProperty, start, end, includeStart, includeEnd);
        if (fieldsRef.isEmpty()) {
            this.tempFindFieldsRef = null;
            return this;
        }
        if (this.tempFindFieldsRef != null)
            this.tempFindFieldsRef.and(fieldsRef);
        else
            this.tempFindFieldsRef = fieldsRef;
        return this;
    }

    public Finder<T> orRange(String sortableProperty, int start , int end , boolean includeStart, boolean includeEnd) {
        BitSet fieldsRef = findFieldsRefRange(sortableProperty, start, end, includeStart, includeEnd);
        if (fieldsRef.isEmpty()) {
            return this;
        }
        if (this.tempFindFieldsRef != null)
            this.tempFindFieldsRef.or(fieldsRef);
        else
            this.tempFindFieldsRef = fieldsRef;
        return this;
    }

    private BitSet findFieldsRefRange(String sortableProperty, int start, int end, boolean includeStart, boolean includeEnd) {
        ISortableSet<Integer> rangeVal = fstb.getProp2IntValIndexer().range(sortableProperty, start, end, includeStart, includeEnd);
        BitSet findIds = new BitSet();
        rangeVal.forEach((v, i) -> {
            findIds.or(findFieldsRef(sortableProperty, v));
            return true;
        });
        return findIds;
    }

    public List<T> fetch() {
        List<T> res = new ArrayList<>();
        if (this.tempFindFieldsRef == null) {
            return res;
        }
        try {
            int i = this.tempFindFieldsRef.nextSetBit(0);
            if (i != -1) {
                wrapObj(res, i);
                for (i = this.tempFindFieldsRef.nextSetBit(i + 1); i >= 0; i = this.tempFindFieldsRef.nextSetBit(i + 1)) {
                    int endOfRun = this.tempFindFieldsRef.nextClearBit(i);
                    do {
                        wrapObj(res, i);
                    } while (++i < endOfRun);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            return res;
        }
        
        this.tempFindFieldsRef = null;
        return res;
    }

    private void wrapObj(List<T> res, Integer indexId) throws InstantiationException, IllegalAccessException {
        KV<String, Object> iEntry = fstb.getPv2FieldsRefMap().getPV(indexId);
        BitSet pvEntryIds = fstb.getPv2FieldsRefMap().getFieldsRef(iEntry).getAllRefs();
        T resObj = fstb.generateObj(Utils.BitSetToIntegerArray(pvEntryIds));
        res.add(resObj);
    }
    
    public List<T> fetchQuery(String property, Object value) {
        return this.and(property, value).fetch();
    }
}