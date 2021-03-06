package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.analyzer.utils.CourseUtils.CombinedCourse;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraCourses;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;
import static org.hydev.veracross.analyzer.api.nodes.veracross.NodeCourses.processCourses;

/**
 * Login with token
 * <p>
 * Class created by the HyDEV Team on 2019-11-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-07 19:14
 */
public class NodeLoginToken extends JsonApiNode<NodeLoginToken.Model>
{
    @Override
    public String path()
    {
        return "/api/login/token";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client with token
        VeracrossHttpClient client = new VeracrossHttpClient();
        CookieData cookie = new CookieData(data.token).store(client);

        // Verify login
        if (!client.validateLogin())
        {
            // Throw access log
            logger.log("[Login] Expire - {}", cookie.username);
            throw new JsonKnownError("Login expired");
        }

        // Update token
        cookie.setCsrf(client.getCsrfToken());
        cookie.setCookies(client.getCookies().getCookies());

        // Throw access log
        logger.log("[Login] Token - {} via version '{}'", cookie.username, client.getWebsiteVersion());

        // After login
        return afterLogin(client, cookie);
    }

    /**
     * Process after login
     *
     * @param veracross Veracross http client
     * @return ReturnModel data
     */
    public static ReturnModel afterLogin(VeracrossHttpClient veracross, CookieData cookieData) throws IOException
    {
        // Get courses
        VeraCourses veraCourses = veracross.getCourses();

        // Get user from database
        User user = User.get(veraCourses.getUsername());

        // If user doesn't exist, register.
        if (user == null) user = User.register(veraCourses.getUsername(), veracross);

        // Update last login and token, and then save
        user.lastLogin(new Date()).save();

        // Return
        return new ReturnModel(user, cookieData.wrap(), processCourses(veraCourses));
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class).key("token", LENGTH_TOKEN);
    }

    public static class Model
    {
        String token;
    }

    @AllArgsConstructor
    public static class ReturnModel
    {
        User user;
        String token;
        List<CombinedCourse> courses;
    }
}
