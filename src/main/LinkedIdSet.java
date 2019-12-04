package main;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * IndexSet
 */
public class LinkedIdSet {

    private int id;
    private BitSet linkdIds;
    

    public LinkedIdSet(int id, BitSet linkdIds) {
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
    public BitSet getLinkedIds() {
        return linkdIds;
    }
    /**
     * @param linkdIds the linkdIds to set
     */
    public LinkedIdSet setLinkdIds(BitSet linkdIds) {
        this.linkdIds = linkdIds;
        return this;
    }

    public void addLinkedIds(Integer index) {
        this.linkdIds.set(index);
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