package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_SCORE_ID;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

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
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Mark as read
        boolean success = veracross.markAssignmentAsRead(cookie.getCsrf(), data.scoreId);

        // Not success
        if (!success)
        {
            System.err.println("Unable to mark as read");
            throw new JsonKnownError("Unable to mark as read");
        }

        // Return success
        return "Marked as read";
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class)
                .key("token", LENGTH_TOKEN)
                .key("scoreId", LENGTH_SCORE_ID);
    }

    protected static class Model
    {
        String token;
        long scoreId;
    }
}
