package org.hydev.veracross.analyzer.utils;

import lombok.Getter;

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
public class L$<T>
{
    @Getter
    private List<T> list;

    private L$(List<T> list)
    {
        this.list = list;
    }

    /**
     * Constructor but looks better with static import
     *
     * @param list List
     * @param <T> Type
     * @return Syntax sugar object
     */
    public static <T> L$<T> l$(List<T> list)
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
        return new L$<T>(new ArrayList<T>(Arrays.asList(list)));
    }

    /**
     * Find a value
     *
     * @param operator Operator
     * @return value if found, null if not found
     */
    public T find(FindOperator<T> operator)
    {
        for (T thisOne : list) if (operator.isIt(thisOne)) return thisOne;
        return null;
    }

    /**
     * Lambda operator for find()
     *
     * @param <T> Type
     */
    @FunctionalInterface
    public interface FindOperator<T>
    {
        boolean isIt(T thisOne);
    }
}
