package org.hydev.veracross.analyzer.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * List $weetener
 * A fake array syntax sugar class. It's 2019 already why doesn't java
 * have array.find(), array.map(), array.filter(), etc...
 * <p>
 * Class created by the HyDEV Team on 2019-11-14!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-14 22:29
 */
public class L$<T> extends ArrayList<T>
{
    /**
     * Construct the list with a list
     *
     * @param list List
     */
    private L$(List<? extends T> list)
    {
        super(list);
    }

    /**
     * Constructor but looks better with static import
     *
     * @param list List
     * @param <T> Type
     * @return Syntax sugar object
     */
    public static <T> L$<T> l$(List<? extends T> list)
    {
        return new L$<>(list);
    }

    /**
     * Constructor but looks better with static import
     *
     * @param list List
     * @param <T> Type
     * @return Syntax sugar object
     */
    @SafeVarargs
    public static <T> L$<T> l$(T... list)
    {
        return new L$<>(Arrays.asList(list));
    }

    /**
     * Find a value
     *
     * @param operator Operator
     * @return value if found, null if not found
     */
    public T find(FindOperator<T> operator)
    {
        for (T thisOne : this) if (operator.isIt(thisOne)) return thisOne;
        return null;
    }

    /**
     * Find a value
     *
     * @param operator Operator
     * @param def Default
     * @return value if found, null if not found
     */
    public T find(FindOperator<T> operator, T def)
    {
        T val = find(operator);
        return val == null ? def : val;
    }

    /**
     * Lambda operator for the find() method
     *
     * @param <T> Type
     */
    @FunctionalInterface
    public interface FindOperator<T>
    {
        boolean isIt(T thisOne);
    }

    /**
     * For each with currentValue and index
     * https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/forEach
     *
     * @param operator Operator
     */
    public void forEach(ForEachOperator<T> operator)
    {
        for (int i = 0; i < this.size(); i++)
        {
            operator.run(get(i), i);
        }
    }

    /**
     * Lambda operator for the forEach() method
     *
     * @param <T> Type
     */
    @FunctionalInterface
    public interface ForEachOperator<T>
    {
        void run(T value, int index);
    }

    /**
     * For each with currentValue, index, and array
     * https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/forEach
     *
     * @param operator Operator
     */
    public void forEach(ForEachOperator2<T> operator)
    {
        for (int i = 0; i < this.size(); i++)
        {
            operator.run(get(i), i, this);
        }
    }

    /**
     * Lambda operator for the forEach() method
     *
     * @param <T> Type
     */
    @FunctionalInterface
    public interface ForEachOperator2<T>
    {
        void run(T value, int index, L$<T> list);
    }

    /**
     * Find the last entry in the list
     *
     * @param def Default value
     * @return Last entry
     */
    public T last(T def)
    {
        return isEmpty() ? def : get(size() - 1);
    }

    /**
     * Find the last entry in the list or return null
     *
     * @return Last entry
     */
    public T last()
    {
        return last((T) null);
    }

    /**
     * Find the last entry in array
     *
     * @param arr Array
     * @param def Default value
     * @param <T> Type
     * @return Last entry
     */
    public static <T> T last(T[] arr, T def)
    {
        return arr.length == 0 ? def : arr[arr.length - 1];
    }

    /**
     * Find the last entry in array or return null
     *
     * @param arr Array
     * @param <T> Type
     * @return Last entry
     */
    public static <T> T last(T[] arr)
    {
        return last(arr, null);
    }

    /**
     * Find the first entry in array
     *
     * @param def Default
     * @return First entry
     */
    public T first(T def)
    {
        return isEmpty() ? def : get(0);
    }
    /**
     * Sort array. Note: This method changes the array!
     * Returning the array is just for convenience.
     *
     * @param comparator Comparator
     * @return Self
     */
    public L$ sort$(Comparator<? super T> comparator)
    {
        sort(comparator);
        return this;
    }

    /**
     * Sort array. Note: This method changes the array!
     * Returning the array is just for convenience.
     *
     * @param comparator Comparator
     * @return Self
     */
    public L$ sortInt(ToIntFunction<? super T> comparator)
    {
        sort(Comparator.comparingInt(comparator));
        return this;
    }

    /**
     * Sort array. Note: This method changes the array!
     * Returning the array is just for convenience.
     *
     * @param comparator Comparator
     * @return Self
     */
    public L$ sortLong(ToLongFunction<? super T> comparator)
    {
        sort(Comparator.comparingLong(comparator));
        return this;
    }

    /**
     * Sort array. Note: This method changes the array!
     * Returning the array is just for convenience.
     *
     * @param comparator Comparator
     * @return Self
     */
    public L$ sortDouble(ToDoubleFunction<? super T> comparator)
    {
        sort(Comparator.comparingDouble(comparator));
        return this;
    }

    /**
     * Sort array. Note: This method changes the array!
     * Returning the array is just for convenience.
     *
     * @param comparator Comparator
     * @return Self
     */
    public L$ sort(Function<? super T, ? extends Comparable> comparator)
    {
        sort(Comparator.comparing(comparator));
        return this;
    }
}
