package org.hydev.veracross.analyzer.api.nodes;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;
import org.hydev.veracross.analyzer.database.model.system.SystemMeta;

import static org.hydev.veracross.analyzer.VAConstants.VERSION;

/**
 * This node returns meta information about the system
 * <p>
 * Class created by the HyDEV Team on 2019-12-02!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-12-02 16:48
 */
public class NodeSystemMeta implements ApiNode
{
    @Override
    public String path()
    {
        return "/api/system";
    }

    /**
     * Process an api request.
     *
     * @param access Api access.
     * @return Result.
     */
    @Override
    public String process(ApiAccess access)
    {
        switch (access.getContent())
        {
            case "version": return VERSION;
            case "maintenance": return SystemMeta.getMaintenance();
        }

        return null;
    }
}
