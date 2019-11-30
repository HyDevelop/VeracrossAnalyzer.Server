package org.hydev.veracross.analyzer.utils;

import java.util.function.Function;

/**
 * Java $weetener: General syntax sugar.
 * <p>
 * Class created by the HyDEV Team on 2019-11-27!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-27 22:42
 */
public class J$
{
    /**
     * Print
     *
     * @param o Object
     */
    public static void print(Object o)
    {
        System.out.print(o.toString());
    }

    /**
     * Print line
     *
     * @param o Object
     */
    public static void println(Object o)
    {
        System.out.println(o.toString());
    }

    /**
     * To string.
     * This is useful when o is something like int.
     * Eg. You can't just call intVar.toString();
     *
     * @param o Object
     * @return String
     */
    public static String str(Object o)
    {
        return o.toString();
    }

    /**
     * Null safe operation
     *
     * @param operation Operation
     * @param <T> Type
     * @return Result or null
     */
    public static <T, R> R null$(T value, Function<T, R> operation)
    {
        try
        {
            return value == null ? null : operation.apply(value);
        }
        catch (NullPointerException ignored)
        {
            return null;
        }
    }
}
