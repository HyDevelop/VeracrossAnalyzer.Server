package org.hydev.veracross.analyzer.api.nodes.veracross;

import lombok.AllArgsConstructor;
import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.StJohnsCourse;
import org.hydev.veracross.sdk.model.VeracrossCourse;

import java.util.ArrayList;
import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.VAConstants.LENGTH_USERNAME;

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
public class NodeCourses extends JsonApiNode<NodeCourses.Model>
{
    @Override
    public String path()
    {
        return "/api/courses";
    }

    @Override
    protected Object processJson(ApiAccess access, Model data) throws Exception
    {
        // Throw an access log
        VADatabase.accessLog(data.username, "Access Courses API", "Begin");

        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieUtils.unwrap(veracross, data.token);

        // Get courses
        List<VeracrossCourse> courses = veracross.getCourses();

        // Convert to St. John's courses
        List<StJohnsCourse> result = new ArrayList<>();
        courses.forEach(course -> result.add(new StJohnsCourse(course)));

        // Throw an access log
        VADatabase.accessLog(data.username, "Access Courses API", "Success");

        // Save course info
        result.forEach(NodeCourses::storeCourse);

        // Return it
        return result;
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig()
                .key("username", LENGTH_USERNAME)
                .key("token", LENGTH_TOKEN);
    }

    protected static class Model
    {
        String username;
        String token;
    }

    @AllArgsConstructor
    protected static class ReturnModel
    {
        List<StJohnsCourse> courses;
        String csrf;
    }

    /**
     * Save course info if not already saved
     *
     * @param course Course info
     */
    private static void storeCourse(VeracrossCourse course)
    {
        // Check if already exists
        Course existing = VADatabase.query(s -> s.createNamedQuery("byId", Course.class).getSingleResult());

        // Create one if it does not exist
        if (existing == null)
        {
            Course saved = new Course(course.getId(), course.getAssignmentsId(),
                    course.getName(), course.getTeacherName());
            VADatabase.transaction(s -> s.save(saved));
        }
    }
}
