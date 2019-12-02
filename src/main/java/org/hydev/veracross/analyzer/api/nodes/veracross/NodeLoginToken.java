package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.database.model.system.SystemMeta;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.io.IOException;
import java.util.Date;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.database.model.system.SystemMeta.ID_MAINTENANCE;

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
            return "{\"success\":false,\"user\":\"logout\"}";
        }

        // Update token TODO: Check if actually online
        cookie.setCsrf(client.getCsrfToken());

        // After login
        return afterLogin(client, cookie);
    }

    /**
     * Process after login
     *
     * @param client Veracross http client
     * @param cookie Cookie data
     * @return ReturnModel data
     */
    public static ReturnModel afterLogin(VeracrossHttpClient client, CookieData cookie) throws IOException
    {
        // Get user from database
        User user = User.get(cookie.getUsername());

        // If user doesn't exist, register.
        if (user == null) user = User.register(cookie.getUsername(), client);

        // Update last login and token, and then save
        user.lastLogin(new Date()).token(cookie.wrap()).save();

        // Return
        return new ReturnModel(user, SystemMeta.get(ID_MAINTENANCE));
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig().key("token", LENGTH_TOKEN);
    }

    @Override
    protected Class<Model> model()
    {
        return Model.class;
    }

    public static class Model
    {
        String token;
    }

    @AllArgsConstructor
    public static class ReturnModel
    {
        User user;
        String maintenance;
    }
}
