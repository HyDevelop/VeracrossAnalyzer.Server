package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.ApiNode;

import static org.hydev.veracross.analyzer.utils.ResourceReader.read;

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
public class NodeCourses implements ApiNode
{
    @Override
    public String path()
    {
        return "/api/courses";
    }

    @Override
    public String process(ApiAccess access)
    {
        return read("/2019-05-30-courses-backup.json");
    }
}
