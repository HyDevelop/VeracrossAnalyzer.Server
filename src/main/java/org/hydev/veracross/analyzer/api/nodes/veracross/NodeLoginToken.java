package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.User;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig().key("token", LENGTH_TOKEN);
    }

    @Override
    protected Class model()
    {
        return Model.class;
    }

    public static class Model
    {
        User user;
    }
}
