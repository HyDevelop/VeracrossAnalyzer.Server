package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.util.Date;
import java.util.List;

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

        // Throw an access log
        VADatabase.accessLog(data.username, "Access Login API", "Before Login");

        // Check database
        List<User> users = VADatabase.query(s -> s.createNamedQuery("byUsername", User.class)
                .setParameter("username", data.username).list());
        User user;

        // No user -> Create user
        if (users.size() == 0) user = new User(data.username, new Date());
        else user = users.get(0);

        // Update last login
        user.setLastLogin(new Date());

        // Save user
        User finalUser = user;
        VADatabase.transaction(s -> s.save(finalUser));

        // Return cookies
        return CookieUtils.wrap(data.username, veracross, veracross.getCsrfToken());
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
