package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieData;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_ASSIGNMENTS_ID;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

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
public class NodeGradingTerm extends JsonApiNode<NodeGradingTerm.Model>
{
    @Override
    public String path()
    {
        return "/api/grading/term";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Return grading
        return CookieData.restore(data.token).getGrading(data.assignmentsId, data.term);
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class)
                .key("token", LENGTH_TOKEN)
                .key("assignmentsId", LENGTH_ASSIGNMENTS_ID)
                .key("term", 4);
    }

    protected static class Model
    {
        String token;
        long assignmentsId;
        int term;
    }
}