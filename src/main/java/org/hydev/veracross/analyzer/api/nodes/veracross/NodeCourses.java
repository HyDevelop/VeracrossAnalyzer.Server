package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonApiNode.GeneralReturnData;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.GSON;

/**
 * This api node obtains the courses information from Veracross and
 * returns it to the requester.
 * <p>
 * Class created by the HyDEV Team on 2019-08-19!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-08-19 15:15
 */
public class NodeCourses extends JsonApiNode<NodeCourses.SubmitData, GeneralReturnData>
{
    @Override
    public String path()
    {
        return "/api/courses";
    }

    @Override
    protected GeneralReturnData processJson(ApiAccess access, SubmitData data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.token);

        // Get it
        return new GeneralReturnData(true, GSON.toJson(veracross.getCourses()));
    }

    /**
     * The JSON model for the data submitted from the client.
     */
    @Data
    public class SubmitData
    {
        String token;
    }
}
