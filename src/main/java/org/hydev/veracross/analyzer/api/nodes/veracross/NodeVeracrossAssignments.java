package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;

import static org.hydev.veracross.analyzer.utils.ResourceReader.read;

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
public class NodeVeracrossAssignments implements ApiNode
{
    @Override
    public String path()
    {
        return "/api/veracross/assignments";
    }

    @Override
    public String process(ApiAccess access)
    {
        return read("/2019-05-30-assignments-backup.json");
    }
}
