package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.*;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraLoginInfo;

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
        // Validate data
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
        User user = User.get(loginInfo.personPk());

        // Null case
        if (user == null) throw new RuntimeException("User not registered. Something is wrong...");

        // Create rating
        CourseInfoRating rating = data.rating.toCourseInfoRating()
            .id_user(loginInfo.personPk())
            .username(user.username)
            .userFullName(user.firstName + user.lastName);

        // Save rating
        rating.save();

        return "Success!";
    }
}
