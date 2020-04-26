package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.ObtainedRating;

/**
 * Call this api node to set a rating by a user for a course.
 * <p>
 * Class created by the HyDEV Team on 2020-04-26!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-26 12:10
 */
public class NodeCourseInfoSetRating extends JsonApiNode<NodeCourseInfoSetRating.Model>
{
    @Override
    public String path()
    {
        return "/api/course-info/rating/set";
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class)
            .key("token", LENGTH_TOKEN)
            .key("rating", 5500);
    }

    protected static class Model
    {
        String token;
        ObtainedRating rating;
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        return null;
    }
}
