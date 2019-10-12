package org.hydev.veracross.analyzer.api.nodes.veracross;

import com.google.gson.JsonObject;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_ASSIGNMENT_ID;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2019-10-01!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-01 17:59
 */
public class NodeGrading extends JsonApiNode
{
    @Override
    public String path()
    {
        return "/api/grading";
    }

    @Override
    protected Object processJson(ApiAccess access, JsonObject data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.get("token").getAsString());

        // Get course info
        long assignmentId = data.get("assignment_id").getAsLong();

        // Return it
        return veracross.getGrading(assignmentId);
    }

    @Override
    protected JsonApiConfig config()
    {
        return  new JsonApiConfig()
                .maxBodyLength(1425)
                .key("token", LENGTH_TOKEN)
                .key("id", LENGTH_ASSIGNMENT_ID);
    }
}
