package com.fengxxc.fastable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * com.fengxxc.fastable.LinkedIdSet
 */
public class LinkedIdSet {

    private int id;
    private BitSet linkedIds;
    
    public LinkedIdSet(int id) {
        this.id = id;
        this.linkedIds = new BitSet();
    }

    public LinkedIdSet(int id, int... linkdIds) {
        this.id = id;
        BitSet linkedIds = new BitSet();
        for (int i = 0; i < linkdIds.length; i++) {
            linkedIds.set(linkdIds[i]);
        }
        this.linkedIds = linkedIds;
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
    public BitSet getLinkedIds() {
        return linkedIds;
    }
    /**
     * @param linkdIds the linkdIds to set
     */
    public LinkedIdSet setLinkdIds(BitSet linkdIds) {
        this.linkedIds = linkdIds;
        return this;
    }

    public LinkedIdSet addLinkedIds(int... linkedIds) {
        for (int i = 0; i < linkedIds.length; i++)
            this.linkedIds.set(linkedIds[i]);
        return this;
    }

    public BitSet getAllIds() {
        BitSet r = (BitSet) getLinkedIds().clone();
        r.set(getId());
        return r;
    }

    @Override
    public String toString() {
        return "{" + getId() + ": " + getLinkedIds().toString() + "}";
    }

    public static void main(String[] args) {
        BitSet bs1 = new BitSet();
        bs1.set(2);
        bs1.set(5);
        bs1.set(7);

        BitSet bs2 = new BitSet();
        bs2.set(1);
        bs2.set(5);
        bs2.set(8);
        bs2.set(832);
        bs2.set(1528);
        
        List<Integer> list = new ArrayList<Integer>();
        int i = bs1.nextSetBit(0);
        if (i != -1) {
            list.add((Integer) i);
            for (i = bs1.nextSetBit(i + 1); i >= 0; i = bs1.nextSetBit(i + 1)) {
                int endOfRun = bs1.nextClearBit(i);
                do {
                    list.add(i);
                } while (++i < endOfRun);
            }
        }
        System.out.println("bs1: " + list.toString());

        System.out.println("bs2: " + bs2.toString());
        System.out.println("bs2.cardinality: " + bs2.cardinality());

        ((BitSet) bs1.clone()).and(bs2);
        System.out.println("bs1: " + bs1.toString());
    }
}