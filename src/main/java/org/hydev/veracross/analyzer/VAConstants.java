package org.hydev.veracross.analyzer;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

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

    public static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    public static boolean DEBUG = false;

    public static final int API_SERVER_PORT;

    public static final String DATABASE_URL;
    public static final String DATABASE_USER;
    public static final String DATABASE_PASS;

    static
    {
        // Get profile
        String profile = System.getProperty("database.profile", "release");

        // Read database information.
        // - database.entry example: "jdbc:mysql://localhost:3306/database;root;password"
        String[] dbInfo = read("/" + profile + "/database.entry").split(";");

        DATABASE_URL = dbInfo[0];
        DATABASE_USER = dbInfo[1];
        DATABASE_PASS = dbInfo[2];

        // API server port
        API_SERVER_PORT = Integer.parseInt(read("/api-server-port.entry").split(";")[0]);
    }
}
