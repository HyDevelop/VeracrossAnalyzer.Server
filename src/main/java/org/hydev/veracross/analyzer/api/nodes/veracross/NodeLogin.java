package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;
import org.hydev.veracross.analyzer.utils.Base64Utils;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.exceptions.VeracrossException;

import java.io.IOException;

import static org.hydev.veracross.analyzer.VAConstants.GSON;

/**
 * This api node logs in to Veracross with specified username and
 * password, and returns a token.
 * <p>
 * Class created by the HyDEV Team on 2019-09-03!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-03 08:53
 */
public class NodeLogin implements ApiNode
{
    @Override
    public String path()
    {
        return "/api/login";
    }

    @Override
    public String process(ApiAccess access)
    {
        try
        {
            // Validate body
            String body = access.getContent();
            if (body == null || body.isEmpty() || body.length() > 80)
            {
                return GSON.toJson(new ReturnData(false, "Bad request"));
            }

            // Parse body
            SubmitData info = GSON.fromJson(access.getContent(), SubmitData.class);

            // Login to St. John's
            StJohnsHttpClient stJohns = new StJohnsHttpClient();
            stJohns.login(info.username, info.password);

            // Login to Veracross
            VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

            // Convert to token form
            String json = GSON.toJson(veracross.getCookies().getCookies());
            String base64 = Base64Utils.encodeBase64C(json.getBytes());

            // Return cookies
            return GSON.toJson(new ReturnData(true, base64));
        }
        catch (IOException | VeracrossException e)
        {
            e.printStackTrace();

            // TODO: Log errors
            return GSON.toJson(new ReturnData(false, e.getMessage()));
        }
    }

    /**
     * The JSON model for the data submitted from the client.
     */
    @Data
    private class SubmitData
    {
        String username;
        String password;
    }

    /**
     * The JSON model for the data returned to the client.
     */
    @Data @AllArgsConstructor
    private class ReturnData
    {
        boolean success;
        String token;
    }
}
