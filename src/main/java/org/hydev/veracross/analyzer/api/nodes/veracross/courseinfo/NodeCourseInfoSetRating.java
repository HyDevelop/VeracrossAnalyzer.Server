package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.CourseListV3;

import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.ObtainedRating;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.getByUserAndCourse;

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
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Validate data
        if (data.rating.ratings().length != 5) throw new JsonKnownError("Invalid ratings length");
        for (Short rating : data.rating.ratings())
        {
            // 1 2 3 4 5
            if (rating < 1 || rating > 5)
            {
                logger.error("[Rating Submit] {} - Invalid numeric rating", cookie.username);
                return "No you can't.";
            }
        }
        if (data.rating.comment().length() > 4998)
        {
            logger.error("[Rating Submit] {} - Comment too long", cookie.username);
            return "Comment too long.";
        }

        // Validate login
        CourseListV3 loginInfo;
        try
        {
            loginInfo = veracross.getCourses();
        }
        catch (Exception e)
        {
            logger.error("[Rating Submit] Unable to verify {}", cookie.username);
            throw new JsonKnownError("Unable to verify: " + e.getMessage());
        }

        // Get user
        User user = User.getByVeracrossPersonPk((int) loginInfo.getPersonPk());

        // Null case
        if (user == null) throw new RuntimeException("User not registered. Something is wrong...");

        // TODO: Case where a hacker might rate a course that isn't enrolled in, check id_ci

        // See if rating already exists.
        List<CourseInfoRating> existingRating = getByUserAndCourse(user.id, data.rating.id_ci());
        CourseInfoRating rating = existingRating.size() == 0 ? new CourseInfoRating() : existingRating.get(0);

        // Override rating
        data.rating.toCourseInfoRating(rating)
            .personPk(loginInfo.getPersonPk())
            .id_user(user.id)
            .username(user.username)
            .userFullName(user.firstName + "]=[" + user.lastName);

        // Save rating
        rating.save();

        // Log access (in case something wrong)
        logger.log("[Rating Submit] {} - {}", user.username, data.rating.id_ci());

        return "Success!";
    }
}
