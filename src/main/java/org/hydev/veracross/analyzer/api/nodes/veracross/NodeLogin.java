package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.Data;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.StJohnsHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.exceptions.VeracrossException;

import java.io.IOException;

import static org.hydev.veracross.analyzer.api.JsonApiNode.GeneralReturnData;

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
public class NodeLogin extends JsonApiNode<NodeLogin.SubmitData, GeneralReturnData>
{
    @Override
    public String path()
    {
        return "/api/login";
    }

    @Override
    protected GeneralReturnData processJson(ApiAccess access, SubmitData data)
    {
        try
        {
            // Login to St. John's
            StJohnsHttpClient stJohns = new StJohnsHttpClient();
            stJohns.login(data.username, data.password);

            // Login to Veracross
            VeracrossHttpClient veracross = stJohns.veracrossLoginSSO();

            // Return cookies
            return new GeneralReturnData(true, CookieUtils.wrap(veracross));
        }
        catch (IOException | VeracrossException e)
        {
            e.printStackTrace();

            // TODO: Log errors
            return new GeneralReturnData(false, e.getMessage());
        }
    }

    /**
     * The JSON model for the data submitted from the client.
     */
    @Data
    protected class SubmitData
    {
        String username;
        String password;
    }
}
