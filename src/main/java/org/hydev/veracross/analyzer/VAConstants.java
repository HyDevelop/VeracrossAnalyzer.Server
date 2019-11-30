package org.hydev.veracross.analyzer;

import com.google.gson.Gson;

import static java.lang.Integer.parseInt;
import static org.hydev.veracross.analyzer.utils.L$.last;
import static org.hydev.veracross.analyzer.utils.ResourceReader.readSplit;

/**
 * This class stores the constants.
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
    // Version
    public static final String VERSION = readSplit("/version.entry")[0];
    public static final int VERSION_BUILD = parseInt(last(VERSION.split("\\.")));

    // Port
    public static final int API_SERVER_PORT = parseInt(readSplit("/api-server-port.entry")[0]);

    // Other settings
    public static boolean DEBUG = false;
    public static Gson GSON = new Gson();

    // Param key lengths limits
    public static final int LENGTH_USERNAME = 30;
    public static final int LENGTH_PASSWORD = 30;
    public static final int LENGTH_TOKEN = 1500;
    public static final int LENGTH_ASSIGNMENTS_ID = 15;
    public static final int LENGTH_SCORE_ID = 25;
    public static final int LENGTH_CSRF = 90;

    // School Email suffix
    public static final String EMAIL_SUFFIX = "@stjohnsprep.org";
}
