package main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import main.sortable.ISortableSet;

/**
 * Finder
 */
public class Finder<T> {

    private Fastable<T> fstb;
    private BitSet tempFindIds;

    public Finder(Fastable<T> fstb) {
        this.fstb = fstb;
    }
    
    public Finder(Fastable<T> fstb, String fProperty, Object fValue) {
        this(fstb);
        this.and(fProperty, fValue);
    }

    private BitSet findLinkedIds(String property, Object value) {
        if (Fastable.BEAN.equals(fstb.getRawDataType())) {
            property = Utils.FristChartoLower(property);
        }
        BitSet findIds = new BitSet();
        LinkedIdSet linkedIdSet = fstb.getPv2linkedMap().get(property, value);
        if (linkedIdSet == null) {
            return findIds;
        }
        if (fstb.getUniqueProperty().equals(property)) {
            // 根据唯一列的查找
            findIds = new BitSet(1);
            findIds.set(linkedIdSet.getId());
        } else {
            // 根据非唯一列的查找
            findIds = linkedIdSet.getLinkedIds();
        }
        return findIds;
    }

    public Finder<T> and(String property, Object value) {
        BitSet findIds = findLinkedIds(property, value);
        if (findIds.isEmpty()) {
            this.tempFindIds = null;
            return this;
        }
        if (this.tempFindIds != null) 
            this.tempFindIds.and(findIds);
        else 
            this.tempFindIds = findIds;
        return this;
    }

    public Finder<T> or(String property, Object value) {
        BitSet findIds = findLinkedIds(property, value);
        if (findIds.isEmpty()) {
            return this;
        }
        if (this.tempFindIds != null) 
            this.tempFindIds.or(findIds);
        else 
            this.tempFindIds = findIds;
        return this;
    }

    public Finder<T> not(String property, Object value) {
        BitSet findIds = findLinkedIds(property, value);
        if (findIds.isEmpty()) {
            return this;
        }
        if (this.tempFindIds != null) 
            Utils.BitSet_not(this.tempFindIds, findIds);
        return this;
    }

    public Finder<T> andRange(String sortableProperty, int start , int end , boolean includeStart, boolean includeEnd) {
        BitSet findIds = findLinkedIdsRange(sortableProperty, start, end, includeStart, includeEnd);
        if (findIds.isEmpty()) {
            this.tempFindIds = null;
            return this;
        }
        if (this.tempFindIds != null)
            this.tempFindIds.and(findIds);
        else
            this.tempFindIds = findIds;
        return this;
    }

    public Finder<T> orRange(String sortableProperty, int start , int end , boolean includeStart, boolean includeEnd) {
        BitSet findIds = findLinkedIdsRange(sortableProperty, start, end, includeStart, includeEnd);
        if (findIds.isEmpty()) {
            return this;
        }
        if (this.tempFindIds != null)
            this.tempFindIds.or(findIds);
        else
            this.tempFindIds = findIds;
        return this;
    }

    private BitSet findLinkedIdsRange(String sortableProperty, int start, int end, boolean includeStart, boolean includeEnd) {
        ISortableSet<Integer> rangeVal = fstb.getProp2IntValIndexer().range(sortableProperty, start, end, includeStart, includeEnd);
        BitSet findIds = new BitSet();

        rangeVal.forEach((v, i) -> {
            System.out.println("~~~~~" + v);
            findIds.or(findLinkedIds(sortableProperty, v));
            return true;
        });
        return findIds;
    }

    public List<T> fetch() {
        List<T> res = new ArrayList<>();
        if (this.tempFindIds == null) {
            return res;
        }
        try {
            int i = this.tempFindIds.nextSetBit(0);
            if (i != -1) {
                wrapObj(res, i);
                for (i = this.tempFindIds.nextSetBit(i + 1); i >= 0; i = this.tempFindIds.nextSetBit(i + 1)) {
                    int endOfRun = this.tempFindIds.nextClearBit(i);
                    do {
                        wrapObj(res, i);
                    } while (++i < endOfRun);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            return res;
        }
        
        this.tempFindIds = null;
        return res;
    }

    private void wrapObj(List<T> res, Integer indexId) throws InstantiationException, IllegalAccessException {
        PVEntry iEntry = fstb.getPVEntrys().get(indexId);
        BitSet pvEntryIds = fstb.getPv2linkedMap().get(iEntry).getAllIds();
        T resObj = null;
        resObj = fstb.generateObj(Utils.BitSetToIntegerArray(pvEntryIds));
        res.add(resObj);
    }
    
    public List<T> fetchQuery(String property, Object value) {
        return this.and(property, value).fetch();
    }
}