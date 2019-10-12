package org.hydev.veracross.analyzer.api.nodes.veracross;

import cc.moecraft.utils.ArrayUtils;
import com.google.gson.JsonObject;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.HibernateUtils;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.exceptions.VeracrossException;

import java.io.IOException;
import java.util.ArrayList;

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
        if (!username.matches("[A-Za-z]+[0-9]+")) throw new Exception("Username format");

        // Throw an access log
        VADatabase.start((s, t) ->
        {
            s.save(new AccessLog(username, "Access Login API", "Before Login"));
        });

        // Login to St. John's
        StJohnsHttpClient stJohns = new StJohnsHttpClient();
        stJohns.login(username, password);

        // Login to Veracross
        VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

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
