package main;

public class RawDataEntry {
    private static final String INDEX = "_index_";
    private Entry entry;
    private boolean index;

    public RawDataEntry(Object indexVal) {
        this.entry = new Entry(INDEX, index);
        this.index = true;
    }

    public RawDataEntry(String key, Object val) {
        this(key, val, false);
    }

    public RawDataEntry(String key, Object val, boolean index) {
        this.entry = new Entry(key, val);
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
        return e.getKey().equals(getKey()) && e.getVal().equals(getVal());
    }

    class Entry {
        private String k;
        private Object v;
        public Entry(String k, Object v) {
            this.k = k;
            this.v = v;
        }
        public String getKey() {
            return k;
        }
        public Object getVal() {
            return v;
        }
        
        @Override
        public String toString() {
            return "#" + this.hashCode() + "#" + getKey() + "_" + getVal().toString() + "";
        }
    }
}