package org.hydev.veracross.analyzer.api.nodes.veracross.courseinfo;

import org.hydev.veracross.analyzer.api.*;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating.ReturnedRating;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.getByCourse;
import static org.hydev.veracross.analyzer.database.model.CourseInfoRating.getCommentsByCourse;

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

    static class RatingData
    {
        // ratingCounts[criteria][stars] = count
        int[][] ratingCounts = new int[5][6];

        // ratingSums[criteria] = total stars
        int[] ratingSums = new int[5];
        int totalCount = 0;
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
                case "course": ratings = getByCourse(parseInt(data.value)); break;
                case "course-comments": ratings = getCommentsByCourse(parseInt(data.value)); break;
                default: return new JsonKnownError("What?");
            }

            // Parse into returned rating
            List<ReturnedRating> returnedRatings = ratings.stream().map(ReturnedRating::new).collect(toList());

            // Return comments
            if (data.condition.equals("course-comments"))
            {
                returnedRatings.removeIf(r -> r.comment() == null || r.comment().trim().isEmpty());
                return returnedRatings;
            }

            // Analyze rating data
            RatingData result = new RatingData();
            for (ReturnedRating rating : returnedRatings)
            {
                // Each criteria
                for (int i = 0; i < rating.ratings().length; i++)
                {
                    short stars = rating.ratings()[i];
                    result.ratingCounts[i][stars]++;
                    result.ratingSums[i] += stars;
                }
                result.totalCount++;
            }

            // Log
            logger.log("[Rating Get] {}: {} - {}", data.condition, cookie.username, data.value);

            return result;
        }
        catch (NumberFormatException e)
        {
            return new JsonKnownError("Number format error");
        }
    }
}
