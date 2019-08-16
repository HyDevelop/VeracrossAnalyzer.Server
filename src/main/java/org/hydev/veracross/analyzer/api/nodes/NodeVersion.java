package org.hydev.veracross.analyzer.api.nodes;

import org.hydev.veracross.analyzer.VAConstants;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;

/**
 * Api node to obtain the current version of the server.
 * <p>
 * Class created by the HyDEV Team on 2019-07-31!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-31 20:17
 */
public class NodeVersion implements ApiNode
{
    @Override
    public String path()
    {
        return "/version";
    }

    @Override
    public String process(ApiAccess access)
    {
        return VAConstants.VERSION;
    }
}
