package org.hydev.veracross.analyzer.utils;

import java.util.Scanner;

/**
 * Helper class to read resource files.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 19:07
 */
public class ResourceReader
{
    /**
     * Read text file to string
     *
     * @param name File name (Include "/" at the beginning)
     * @return String in the file.
     */
    public static String read(String name)
    {
        try
        {
            if (!name.startsWith("/"))
            {
                // name = name.substring(1);
                name = "/" + name;
            }
            return new Scanner(ResourceReader.class.getResourceAsStream(name), "UTF-8").useDelimiter("\\A").next();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to read resource information", e);
        }
    }

    /**
     * Read resource, split by ';'
     *
     * @param name File name
     * @return String in the file.
     */
    public static String[] readSplit(String name)
    {
        return read(name).split(";");
    }
}
