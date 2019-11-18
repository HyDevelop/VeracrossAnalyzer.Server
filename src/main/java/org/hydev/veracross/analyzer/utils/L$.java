package org.hydev.veracross.analyzer.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Find the last entry in the list
     *
     * @return Last entry
     */
    public T last()
    {
        return last(null);
    }
}
