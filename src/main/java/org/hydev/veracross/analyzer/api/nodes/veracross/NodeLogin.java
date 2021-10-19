package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.*;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;

/**
 * This api node logs in to Veracross with specified username and
 * password, and returns a token.
 * <p>
 * Class created by the HyDEV Team on 2019-09-03!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-03 08:53
 */
public class NodeLogin extends JsonApiNode<NodeLogin.Model>
{
    @Override
    public String path()
    {
        return "/api/login";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Check username (Always in "flast00" format)
        data.username = data.username.toLowerCase().replace(EMAIL_SUFFIX, "");
        if (!data.username.matches("[a-z]+[0-9]+"))
        {
            System.err.println("Error: Invalid username: " + data.username);
            throw new JsonKnownError("Invalid username! It should be in 'flast00' format, " +
                    "if you don't know it, try login with your SJP email.");
        }

        // Login to St. John's
        StJohnsHttpClient stJohns = new StJohnsHttpClient();
        stJohns.login(data.username, data.password);

        // Login to Veracross
        VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

        // Throw access log
        logger.log("[Login] New session - {} via version '{}'", data.username, veracross.getWebsiteVersion());

        // Return cookies
        return NodeLoginToken.afterLogin(veracross,
            new CookieData(null, -1L, data.username, veracross, veracross.getCsrfToken()));
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class)
                .key("username", LENGTH_USERNAME)
                .key("password", LENGTH_PASSWORD);
    }

    public static class Model
    {
        String username;
        String password;
    }
}
