package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

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
public class NodeMarkAsRead extends JsonApiNode<NodeMarkAsRead.Model>
{
    @Override
    public String path()
    {
        return "/api/mark-as-read";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.token);

        // Mark as read
        boolean success = veracross.markAssignmentAsRead(data.csrf, data.scoreId);

        // Not success
        if (!success) throw new RuntimeException("Unable to mark as read");

        // Return success
        return "Marked as read";
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("token", LENGTH_TOKEN)
                .key("csrf", LENGTH_CSRF)
                .key("scoreId", LENGTH_SCORE_ID);
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
        long scoreId;
    }
}
