package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.utils.CookieData;
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
        data.username = data.username.toLowerCase();
        if (!data.username.matches("[a-z]+[0-9]+")) throw new Exception("Invalid username");

        // Login to St. John's
        StJohnsHttpClient stJohns = new StJohnsHttpClient();
        stJohns.login(data.username, data.password);

        // Login to Veracross
        VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

        // Throw access log
        AccessLog.record(data.username, "Access Login API", "Before Login");

        // Get pk
        long pk = veracross.getCourses().getPersonPk();

        // Return cookies
        return NodeLoginToken.afterLogin(veracross,
                new CookieData(null, pk, data.username, veracross, veracross.getCsrfToken()));
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("password", LENGTH_PASSWORD);
    }

    @Override
    protected Class model()
    {
        return Model.class;
    }

    public static class Model
    {
        String username;
        String password;
    }
}
