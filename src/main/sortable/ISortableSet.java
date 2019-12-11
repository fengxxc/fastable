package main.sortable;

/**
 * ISortableSet
 * @param <V>
 */
public interface ISortableSet<V> {
    final static char BREAK = 'b';

    void forEach(IteratorFun<V> iteratorFun);

    /**
     * 
     * @param iteratorFun
     * @param start item start at
     * @param end item end at
     * @param includeStart
     * @param includeEnd
     */
    void forEach(IteratorFun<V> iteratorFun, Integer start, Integer end, boolean includeStart, boolean includeEnd);

    @FunctionalInterface
    public interface IteratorFun<V> {
        /**
         * 
         * @param val
         * @param index always start of 0
         * @param context
         * @return true=> continue; false => break
         */
        boolean accept(V val, int index);
    }

    /**
     * == BitSet.set(i)
     * @param i
     */
	void add(V i);

    /**
     * == BitSet.cardinality()
     * @return
     */
    int count();
}
