package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Finder
 */
public class Finder<T> {

    private Fastable<T> fstb;
    private Set<Integer> tempFindIds;

    public Finder(Fastable<T> fstb) {
        this.fstb = fstb;
    }
    
    public Finder(Fastable<T> fstb, String fProperty, Object fValue) {
        this(fstb);
        this.and(fProperty, fValue);
    }

    public Finder<T> and(String property, Object value) {
        Set<Integer> findIds = findLinkedIds(property, value);
        if (findIds.size() == 0) {
            this.tempFindIds = null;
            return this;
        }
        if (this.tempFindIds != null) 
            this.tempFindIds.retainAll(findIds);
        else 
            this.tempFindIds = findIds;
        return this;
    }

    private Set<Integer> findLinkedIds(String property, Object value) {
        if (Fastable.BEAN.equals(fstb.getRawDataType())) {
            property = Utils.fristChartoLower(property);
        }
        RawDataEntry qEntry = new RawDataEntry(property, value);
        Set<Integer> findIds = new HashSet<Integer>();
        LinkedIdSet linkedIdSet = fstb.getGraphMap().get(qEntry);
        if (linkedIdSet == null) {
            return findIds;
        }
        if (fstb.getUniqueProperty().equals(property)) {
            // 根据唯一列的查找
            findIds.add(linkedIdSet.getId());
        } else {
            // 根据非唯一列的查找
            findIds = linkedIdSet.getLinkedIds();
        }
        return findIds;
    }

    public Finder<T> or(String property, Object value) {
        Set<Integer> findIds = findLinkedIds(property, value);
        if (findIds.size() == 0) {
            return this;
        }
        if (this.tempFindIds != null) 
            this.tempFindIds.addAll(findIds);
        else 
            this.tempFindIds = findIds;
        return this;
    }

    public List<T> fetch() {
        List<T> res = new ArrayList<>();
        if (this.tempFindIds == null) {
            return res;
        }
        for (Integer indexId : this.tempFindIds) {
            RawDataEntry iEntry = fstb.getDataEntrys().get(indexId);
            Set<Integer> dEntryIds = fstb.getGraphMap().get(iEntry).getAllIds();
            T resObj = null;
            try {
                resObj = fstb.generateObj(dEntryIds);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return res;
            }
            res.add(resObj);
        }
        this.tempFindIds = null;
        return res;
    }
    
    public List<T> fetchQuery(String property, Object value) {
        return this.and(property, value).fetch();
    }
}