package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.utils.CookieUtils;
import org.hydev.veracross.analyzer.utils.CookieUtils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.StJohnsCourse;
import org.hydev.veracross.sdk.model.VeracrossCourse;

import java.util.ArrayList;
import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.database.VADatabase.query;
import static org.hydev.veracross.analyzer.database.VADatabase.transaction;

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
        // Create http client
        VeracrossHttpClient veracross = new VeracrossHttpClient();

        // Unwrap cookies
        CookieData cookie = CookieUtils.unwrap(veracross, data.token);

        // Get courses
        List<VeracrossCourse> courses = veracross.getCourses();

        // Convert to St. John's courses
        List<StJohnsCourse> result = new ArrayList<>();
        courses.forEach(course -> result.add(new StJohnsCourse(course)));

        // Throw an access log
        VADatabase.accessLog(cookie.getUsername(), "Access Courses API", "Success");

        // Save course info
        result.forEach(NodeCourses::storeCourse);

        // Return it
        return result;
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig().key("token", LENGTH_TOKEN);
    }

    @Override
    protected Class model()
    {
        return Model.class;
    }

    protected static class Model
    {
        String token;
    }

    /**
     * Save course info if not already saved
     *
     * @param course Course info
     */
    private static void storeCourse(VeracrossCourse course)
    {
        // Create one if it does not exist
        if (query(s -> s.createNamedQuery("byId", Course.class).setParameter("id", course.getId()).list()).size() == 0)
        {
            Course saved = new Course(course.getId(), course.getAssignmentsId(),
                    course.getName(), course.getTeacherName());
            transaction(s -> s.save(saved));
        }
    }
}
