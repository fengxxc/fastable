package main;

public class RawDataEntry {
    private static final String INDEX = "_index_";
    private Entry<String, Object> entry;
    private boolean index;

    public RawDataEntry(Object indexVal) {
        this.entry = new Entry<String, Object>(INDEX, indexVal);
        this.index = true;
    }

    public RawDataEntry(String key, Object val) {
        this(key, val, false);
    }

    public RawDataEntry(String key, Object val, boolean index) {
        this.entry = new Entry<String, Object>(key, val);
        this.index = false;
    }

    public String getKey() {
        return this.entry.getKey();
    }

    public Object getVal() {
        return this.entry.getVal();
    }

    public boolean index() {
        return this.index;
    }

    @Override
    public String toString() {
        return this.entry.toString();
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
        RawDataEntry e = (RawDataEntry) obj;
        if (e.getVal() == null) {
            return e.getKey().equals(getKey());
        }
        return e.getKey().equals(getKey()) && e.getVal().equals(getVal());
    }

    class Entry<K, V> {
        private K k;
        private V v;
        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }
		public K getKey() {
            return k;
        }
        public V getVal() {
            return v;
        }
        
        @Override
        public String toString() {
            return "" + getKey().toString() + "_" + getVal().toString() + "";
        }
    }
}