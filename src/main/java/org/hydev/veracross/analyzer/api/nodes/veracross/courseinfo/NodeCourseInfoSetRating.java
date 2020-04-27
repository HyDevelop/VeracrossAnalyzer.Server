package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.api.JsonKnownError;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraLoginInfo;

import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
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
        // Validate data
        if (data.rating.ratings().length != 5) throw new JsonKnownError("Invalid ratings length");
        for (Short rating : data.rating.ratings())
        {
            // 0 1 2 3 4 5 (0 = unset)
            if (rating < 0 || rating > 5) return "No you can't.";
        }
        if (data.rating.comment().length() > 4998) return "Comment too long.";

        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Validate login
        VeraLoginInfo loginInfo = veracross.getLoginInfo();
        if (loginInfo == null) throw new JsonKnownError("Login expired!");

        // Get user
        User user = User.getByVeracrossPersonPk((int) loginInfo.personPk());

        // Null case
        if (user == null) throw new RuntimeException("User not registered. Something is wrong...");

        // See if rating already exists.
        List<CourseInfoRating> existingRating = getByUserAndCourse(user.id, data.rating.id_ci());
        CourseInfoRating rating = existingRating.size() == 0 ? new CourseInfoRating() : existingRating.get(0);

        // Override rating
        data.rating.toCourseInfoRating(rating)
            .id_user(user.id)
            .username(user.username)
            .userFullName(user.firstName + user.lastName);

        // Save rating
        rating.save();

        return "Success!";
    }
}
