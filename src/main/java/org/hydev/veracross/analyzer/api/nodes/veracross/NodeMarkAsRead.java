package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;

import static org.hydev.veracross.analyzer.VAConstants.*;

/**
 * This api node marks unread assignment as read
 * <p>
 * Class created by the HyDEV Team on 2019-10-13!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-13 12:51
 */
public class NodeMarkAsRead extends JsonApiNode
{
    @Override
    public String path()
    {
        return "/api/mark-as-read";
    }

    @Override
    protected Object processJson(ApiAccess access, Object data) throws Exception
    {
        throw new JsonKnownError("Not implemented yet");
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("token", LENGTH_TOKEN)
                .key("csrf", LENGTH_CSRF);
    }

    @Override
    protected Class model()
    {
        return NodeCourses.Model.class;
    }

    protected static class Model
    {
        String username;
        String token;
        String csrf;
    }
}
