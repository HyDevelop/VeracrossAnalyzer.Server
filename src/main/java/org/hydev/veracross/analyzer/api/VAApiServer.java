package org.hydev.veracross.analyzer.api;

import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.LoggerInstanceManager;
import cc.moecraft.logger.environments.ConsoleColoredEnv;
import cc.moecraft.logger.environments.FileEnv;
import lombok.Getter;
import org.eclipse.jetty.server.Server;
import org.hydev.veracross.analyzer.VAConstants;
import org.hydev.veracross.analyzer.api.nodes.NodeSystemMeta;
import org.hydev.veracross.analyzer.api.nodes.NodeTest;
import org.hydev.veracross.analyzer.api.nodes.NodeVersion;
import org.hydev.veracross.analyzer.api.nodes.veracross.*;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfo;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfoGetRating;
import org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo.NodeCourseInfoSetRating;
import org.hydev.veracross.analyzer.database.VADatabase;

import static cc.moecraft.logger.environments.ColorSupportLevel.FORCED;

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
    private final LoggerInstanceManager lim;
    public static HyLogger logger;

    public VAApiServer()
    {
        // Setup logger
        lim = new LoggerInstanceManager();
        lim.addEnvironment(new ConsoleColoredEnv(FORCED), new FileEnv("./logs", "veracross-analyzer.log"));
        logger = lim.getLoggerInstance("VA-Server", VAConstants.DEBUG);
    }

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
        new VAApiServer().start();
    }
}
