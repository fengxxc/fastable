package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * IndexSet
 */
public class LinkedIdSet {

    private int id;
    private Set<Integer> linkdIds;
    

    public LinkedIdSet(int id, Set<Integer> linkdIds) {
        this.id = id;
        this.linkdIds = linkdIds;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public LinkedIdSet setId(int id) {
        this.id = id;
        return this;
    }
    /**
     * @return the indexs
     */
    public Set<Integer> getLinkedIds() {
        return linkdIds;
    }
    /**
     * @param linkdIds the linkdIds to set
     */
    public LinkedIdSet setLinkdIds(Set<Integer> linkdIds) {
        this.linkdIds = linkdIds;
        return this;
    }

    public void addLinkedIds(Integer index) {
        this.linkdIds.add(index);
    }

    public Set<Integer> getAllIds() {
        Set<Integer> r = new HashSet<>(getLinkedIds());
        r.add(getId());
        return r;
    }

    @Override
    public String toString() {
        return "{" + getId() + ": " + Arrays.toString(getLinkedIds().toArray()) + "}";
    }
}