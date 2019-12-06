package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraCourse;
import org.hydev.veracross.sdk.model.VeraCourses;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;

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
        CookieData cookie = new CookieData(data.token).store(veracross);

        // Get courses
        VeraCourses courses = veracross.getCourses();

        // Throw access log
        AccessLog.record(cookie.getUsername(), "Access Courses API", "Success");

        // Save course info
        courses.forEach(NodeCourses::storeCourse);

        // Return it
        return courses;
    }

    @Override
    protected JsonApiConfig config()
    {
        return new JsonApiConfig().key("token", LENGTH_TOKEN);
    }

    @Override
    protected Class<Model> model()
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
    private static void storeCourse(VeraCourse course)
    {
        // Create one if it does not exist
        if (Course.get(course.getId()) == null)
        {
            new Course(course.getId(), course.getName(), course.getTeacherName()).insert();
        }
    }
}
