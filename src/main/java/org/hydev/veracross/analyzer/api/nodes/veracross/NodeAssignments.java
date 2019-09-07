package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonApiNode.GeneralReturnData;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.io.IOException;

import static org.hydev.veracross.analyzer.VAConstants.GSON;

/**
 * This api node obtains the assignments information from Veracross and
 * returns it to the requester.
 * <p>
 * Class created by the HyDEV Team on 2019-08-16!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-08-16 15:09
 */
public class NodeAssignments extends JsonApiNode<NodeAssignments.SubmitData, GeneralReturnData>
{
    @Override
    public String path()
    {
        return "/api/assignments";
    }

    @Override
    protected GeneralReturnData processJson(ApiAccess access, SubmitData data) throws IOException
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.token);

        // Get it
        return new GeneralReturnData(true, GSON.toJson(veracross.getAssignments(data.id)));
    }

    /**
     * The JSON model for the data submitted from the client.
     */
    @Data
    public class SubmitData
    {
        String token;
        long id;
    }
}
