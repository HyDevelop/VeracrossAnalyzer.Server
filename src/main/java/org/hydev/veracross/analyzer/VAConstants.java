package org.hydev.veracross.analyzer;

import com.google.gson.Gson;

import static org.hydev.veracross.analyzer.utils.ResourceReader.read;

/**
 * This class stores the constants for this trash sorter program.
 * <p>
 * Class created by the HyDEV Team on 2019-07-02!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-02 12:00
 */
public class VAConstants
{
    public static final String VERSION = read("/version.entry".split(";")[0]);

    public static final int API_SERVER_PORT;

    public static boolean DEBUG = false;
    public static Gson GSON = new Gson();

    // Param key lengths limits
    public static final int LENGTH_USERNAME = 30;
    public static final int LENGTH_PASSWORD = 30;
    public static final int LENGTH_TOKEN = 1400;
    public static final int LENGTH_ASSIGNMENT_ID = 15;


    static
    {
        // API server port
        API_SERVER_PORT = Integer.parseInt(read("/api-server-port.entry").split(";")[0]);
    }
}
