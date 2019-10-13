package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.*;

/**
 * This api node obtains the courses grading information from Veracross
 * and returns it to the requester.
 * <p>
 * Class created by the HyDEV Team on 2019-10-01!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-01 17:59
 */
public class NodeGrading extends JsonApiNode<NodeGrading.Model>
{
    @Override
    public String path()
    {
        return "/api/grading";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Parse values

        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.token);

        // Return grading
        return veracross.getGrading(data.assignmentId);
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("token", LENGTH_TOKEN)
                .key("assignmentsId", LENGTH_ASSIGNMENT_ID);
    }

    @Override
    protected Class model()
    {
        return Model.class;
    }

    protected static class Model
    {
        String username;
        String token;
        long assignmentId;
    }
}
