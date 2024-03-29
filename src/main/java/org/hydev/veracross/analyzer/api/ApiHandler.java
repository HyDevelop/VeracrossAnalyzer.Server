package org.hydev.veracross.analyzer.api;

import lombok.Getter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hydev.logger.HyLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static org.hydev.veracross.analyzer.utils.ResourceReader.read;

/**
 * This class handles api accesses and directs them to corresponding
 * api node implementations.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 19:18
 */
@Getter
public class ApiHandler extends AbstractHandler
{
    private static final String HELP_USAGE = read("/usage-help.txt");
    private static final String ERROR_WRONG_METHOD = read("/error-wrong-method.txt");

    private final VAApiServer apiServer;
    private final ApiNodeManager manager;
    private final HyLogger logger = new HyLogger("ApiHandler");

    //private final TSDConnection db = new TSDConnection();

    public ApiHandler(VAApiServer server)
    {
        this.apiServer = server;
        this.manager = new ApiNodeManager();

        logger.log("Api Handler Initialized!");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    {
        baseRequest.setHandled(true);
        if (!"get".equalsIgnoreCase(request.getMethod()) && !"post".equalsIgnoreCase(request.getMethod()))
        {
            writeResponse(response, ERROR_WRONG_METHOD);
            return;
        }

        try
        {
            // Map Headers.
            Map<String, String> headers = mapHeaders(request);

            // Verify node.
            if (target == null || target.isEmpty())
            {
                writeResponse(response, HELP_USAGE, "text/html");
                return;
            }

            // Find node.
            ApiNode node = manager.getNode(target);
            if (node == null)
            {
                writeResponse(response, HELP_USAGE, "text/html");
                return;
            }

            // Obtain Content
            String content = request.getReader().lines().collect(joining(lineSeparator()));

            // Debug output
            logger.debug("Request received: {} : {}", node, content);

            // Write response.
            writeResponse(response, node.process(new ApiAccess(request, headers, content)));
        }
        catch (Throwable e)
        {
            // Write error.
            logger.error(e);
            /*db.create().insertInto(API_SERVER_ERRORS)
                    .set(API_SERVER_ERRORS.DESCRIPTION, "Unexpected Error")
                    .set(API_SERVER_ERRORS.STACKTRACE, ExceptionUtils.getStackTrace(e))
                    .set(API_SERVER_ERRORS.TIME, DSL.currentTimestamp())
                    .executeAsync();*/

            // Make a response
            writeResponse(response, "Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Log on destroy
     */
    @Override
    public void destroy()
    {
        logger.log("Api Handler Destroyed!");
    }

    /**
     * Create a map for the headers.
     *
     * @param request Http request
     * @return Headers map
     */
    private Map<String, String> mapHeaders(HttpServletRequest request)
    {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> keys = request.getHeaderNames();

        while (keys.hasMoreElements())
        {
            String key = keys.nextElement();
            headers.put(key, request.getHeader(key));
        }
        return headers;
    }

    /**
     * Write content to a http response.
     *
     * @param response Response.
     * @param content Content.
     */
    private static void writeResponse(HttpServletResponse response, String content)
    {
        writeResponse(response, content, "application/json; charset=utf-8");
    }

    /**
     * Write content to a http response.
     *
     * @param response Response.
     * @param content Content.
     * @param contentType Content type.
     */
    private static void writeResponse(HttpServletResponse response, String content, String contentType)
    {
        try
        {
            // Declare response encoding and types
            response.setContentType(contentType);

            // Allow CORS
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // Declare response status code
            response.setStatus(HttpServletResponse.SC_OK);

            // Write back response
            response.getWriter().println(content);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Response failed to write", e);
        }
    }
}
