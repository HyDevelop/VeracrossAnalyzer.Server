package org.hydev.veracross.analyzer.api.nodes.veracross;

import com.google.gson.JsonObject;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.io.IOException;

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
public class NodeAssignments extends JsonApiNode
{
    @Override
    public String path()
    {
        return "/api/assignments";
    }

    @Override
    protected Object processJson(ApiAccess access, JsonObject data) throws IOException
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.get("token").getAsString());

        // Get it
        return veracross.getAssignments(data.get("id").getAsLong());
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .maxBodyLength(1425)
                .key("token", 1400)
                .key("id", 15);
    }
}
