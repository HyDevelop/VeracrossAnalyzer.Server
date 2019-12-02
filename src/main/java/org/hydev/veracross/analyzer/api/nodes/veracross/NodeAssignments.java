package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.io.IOException;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_ASSIGNMENTS_ID;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

/**
 * This api node obtains the assignment information from Veracross and
 * returns it to the requester.
 * <p>
 * Class created by the HyDEV Team on 2019-08-16!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-08-16 15:09
 */
public class NodeAssignments extends JsonApiNode<NodeAssignments.Model>
{
    @Override
    public String path()
    {
        return "/api/assignments";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws IOException
    {
        // Create http client
        VeracrossHttpClient client = new VeracrossHttpClient();

        // Unwrap cookies
        new CookieData(data.token).store(client);

        // Get it
        return client.getAssignments(data.assignmentsId);
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("token", LENGTH_TOKEN)
                .key("assignmentsId", LENGTH_ASSIGNMENTS_ID);
    }

    @Override
    protected Class<Model> model()
    {
        return Model.class;
    }

    protected static class Model
    {
        String token;
        long assignmentsId;
    }
}
