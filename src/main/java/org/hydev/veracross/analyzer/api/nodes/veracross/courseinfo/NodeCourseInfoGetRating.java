package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.getByCourse;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.getByUser;

/**
 * This api node obtains the rating data for a course / a user
 * <p>
 * Class created by the HyDEV Team on 2020-04-21!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-04-21 18:27
 */
public class NodeCourseInfoGetRating extends JsonApiNode<NodeCourseInfoGetRating.Model>
{
    @Override
    public String path()
    {
        return "/api/course-info/rating/get";
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class)
            .key("token", LENGTH_TOKEN)
            .key("condition", 15)
            .key("value", 20);
    }

    protected static class Model
    {
        String token;
        String condition;
        String value;
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Validate login
        if (!veracross.validateLogin()) throw new JsonKnownError("Login expired!");

        try
        {
            List<CourseInfoRating> ratings;

            // Condition
            switch (data.condition)
            {
                case "user": ratings = getByUser(parseLong(data.value)); break;
                case "course": ratings = getByCourse(parseLong(data.value)); break;
                default: return new JsonKnownError("What?");
            }

            // Parse to ReturnedRating
            return ratings.stream().map(CourseInfoRating.ReturnedRating::new).collect(Collectors.toList());
        }
        catch (NumberFormatException e)
        {
            return new JsonKnownError("Number format error");
        }
    }
}
