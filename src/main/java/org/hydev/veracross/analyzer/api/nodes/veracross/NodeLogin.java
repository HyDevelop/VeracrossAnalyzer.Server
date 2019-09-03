package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;

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
        return null;
    }
}
