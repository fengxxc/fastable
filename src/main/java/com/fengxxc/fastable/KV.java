package com.fengxxc.fastable;

public class KV<K, V> {
    private boolean index;
    private K k;
    private V v;

    public KV(K key, V val) {
        this(key, val, false);
    }

    public KV(K key, V val, boolean index) {
        this.k = key;
        this.v = val;
        this.index = index;
    }

    public K getKey() {
        return k;
    }

    public V getVal() {
        return v;
    }

    public boolean index() {
        return this.index;
    }

    @Override
    public String toString() {
        String v = getVal() != null? getVal().toString() : "";
        return "" + getKey().toString() + "_" + v + "";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + getKey().hashCode();
        if (getVal() != null)
            hash = hash * 31 + getVal().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        KV e = (KV) obj;
        if (e.getVal() == null) {
            return e.getKey().equals(getKey());
        }
        return e.getKey().equals(getKey()) && e.getVal().equals(getVal());
    }
}