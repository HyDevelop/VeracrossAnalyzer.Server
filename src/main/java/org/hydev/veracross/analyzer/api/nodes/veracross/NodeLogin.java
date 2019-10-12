package org.hydev.veracross.analyzer.api.nodes.veracross;

import com.google.gson.JsonObject;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_PASSWORD;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_USERNAME;

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
public class NodeLogin extends JsonApiNode
{
    @Override
    public String path()
    {
        return "/api/login";
    }

    @Override
    protected Object processJson(ApiAccess access, JsonObject data) throws Exception
    {
        // Get username and password
        String username = data.get("username").getAsString();
        String password = data.get("password").getAsString();

        // Check username (Always in "flast00" format)
        if (!username.matches("[A-Za-z]+[0-9]+")) throw new Exception("Invalid username");

        // Throw an access log
        VADatabase.transaction((s, t) ->
        {
            s.save(new AccessLog(username, "Access Login API", "Before Login"));
        });

        // Login to St. John's
        StJohnsHttpClient stJohns = new StJohnsHttpClient();
        stJohns.login(username, password);

        // Login to Veracross
        VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

        // Check database
        User user = VADatabase.query(s -> s.createNamedQuery("byUsername", User.class)
                .setParameter("username", username).getSingleResult());

        // Return cookies
        return CookieUtils.wrap(veracross);
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("password", LENGTH_PASSWORD);
    }
}
