package main.sortable;

/**
 * ISortableSet
 * @param <T>
 * @param <V>
 */
public interface ISortableSet<T, V> {
    final static char BREAK = 'b';

    void forEach(IteratorFun<T, V> iteratorFun);

    /**
     * 
     * @param iteratorFun
     * @param start item start at
     * @param end item end at
     * @param includeStart
     * @param includeEnd
     */
    void forEach(IteratorFun<T, V> iteratorFun, int start, int end, boolean includeStart, boolean includeEnd);

    @FunctionalInterface
    public interface IteratorFun<T, V> {
        /**
         * 
         * @param val
         * @param index always start of 0
         * @param context
         * @return true=> continue; false => break
         */
        boolean accept(V val, int index, T context);
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
    
    /**
     * 交集
     * @param other
     * @return new Instance of T
     */
    T toAnd(T other);

    /**
     * 并集
     * @param other
     * @return new Instance of T
     */
    T toOr(T other);

    /**
     * 差集
     * @param other
     * @return new Instance of T
     */
    T toNot(T other);
}
