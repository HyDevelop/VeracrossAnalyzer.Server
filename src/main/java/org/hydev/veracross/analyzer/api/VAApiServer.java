package org.hydev.veracross.analyzer.api;

import lombok.Getter;
import org.eclipse.jetty.server.Server;
import org.hydev.logger.HyLogger;
import org.hydev.logger.HyLoggerConfig;
import org.hydev.logger.appenders.FileAppender;
import org.hydev.veracross.analyzer.VAConstants;
import org.hydev.veracross.analyzer.api.nodes.NodeSystemMeta;
import org.hydev.veracross.analyzer.api.nodes.NodeTest;
import org.hydev.veracross.analyzer.api.nodes.NodeVersion;
import org.hydev.veracross.analyzer.api.nodes.veracross.*;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfo;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfoGetRating;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfoSetRating;
import org.hydev.veracross.analyzer.database.VADatabase;

/**
 * API server for the veracross analyzer.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 18:36
 */
@Getter
public class VAApiServer
{
    public static HyLogger logger = new HyLogger("VA-Server");

    /**
     * Start the http server
     */
    public void start()
    {
        // Log init
        logger.log("API Server Starting...");

        // Create API Handler
        ApiHandler handler = new ApiHandler(this);
        handler.getManager().register(
            // Meta nodes
            new NodeTest(),
            new NodeVersion(),
            new NodeSystemMeta(),

            // Login
            new NodeLogin(),
            new NodeLoginToken(),

            // Veracross
            new NodeAssignments(),
            new NodeCourses(),
            new NodeGradingTerm(),
            new NodeMarkAsRead(),

            // Database
            new NodeCourseInfo(),
            new NodeCourseInfoGetRating(),
            new NodeCourseInfoSetRating()
        );

        // Load Hibernate
        VADatabase.init();
        logger.log("Hibernate Initialized!");

        // Create Jetty server
        Server server = new Server(VAConstants.API_SERVER_PORT);
        server.setHandler(handler);
        logger.log("Jetty Server Created!");

        // Start Jetty server
        try
        {
            server.start();
            logger.log("Jetty Server Started! Joining...");
            server.join();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Server start error.", e);
        }
    }

    /**
     * Start the server
     *
     * @param args Nothing
     */
    public static void main(String[] args)
    {
        // Logger
        HyLoggerConfig.INSTANCE.getAppenders().add(new FileAppender("./logs", "veracross-analyzer.log"));
        HyLoggerConfig.INSTANCE.installSysOut();

        new VAApiServer().start();
    }
}
