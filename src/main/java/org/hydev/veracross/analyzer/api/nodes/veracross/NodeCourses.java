package org.hydev.veracross.analyzer.api.nodes.veracross;

import com.google.gson.JsonObject;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.StJohnsCourse;
import org.hydev.veracross.sdk.model.VeracrossCourse;

import java.util.ArrayList;
import java.util.List;

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
public class NodeCourses extends JsonApiNode
{
    @Override
    public String path()
    {
        return "/api/courses";
    }

    @Override
    protected Object processJson(ApiAccess access, JsonObject data) throws Exception
    {
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.get("token").getAsString());

        // Get courses
        List<VeracrossCourse> courses = veracross.getCoursesWithGrading();

        // Convert to St. John's courses
        List<StJohnsCourse> result = new ArrayList<>();
        courses.forEach(course -> result.add(new StJohnsCourse(course)));

        // Return it
        return result;
    }
}
