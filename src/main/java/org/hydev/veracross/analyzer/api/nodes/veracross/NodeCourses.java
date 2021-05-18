package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.analyzer.utils.CourseUtils.CombinedCourse;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.CourseListV3;
import org.hydev.veracross.sdk.model.CourseV3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;
import static org.hydev.veracross.analyzer.utils.CourseUtils.storeCourse;

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
        CourseListV3 courses = veracross.getCourses();

        // Throw access log
        logger.log("[Course] Load - {}", courses.getUsername());

        return processCourses(courses);
    }

    /**
     * Process courses after user is verified
     *
     * @param veraCourses Courses
     * @return Courses with combined information
     */
    public static List<CombinedCourse> processCourses(CourseListV3 veraCourses)
    {
        if (veraCourses.getCourses().size() == 0) return new ArrayList<>();

        // Find courses
        List<Course> courses = Course.get(veraCourses.getCourses().stream().mapToLong(CourseV3::getId)
            .boxed().collect(Collectors.toList()));
        Map<Long, Course> courseMap = new HashMap<>();

        // Save course info async
        for (CourseV3 course : veraCourses.getCourses())
        {
            // If there are no course exist for this id
            if (courses.stream().noneMatch(c -> c.id() == course.getId()))
            {
                // Put a new course
                courses.add(storeCourse(course));
            }
        }
        courses.forEach(c -> courseMap.put(c.id(), c));

        // Find ratings
        List<CourseInfoRating> ratings = CourseInfoRating.getByPersonPk(veraCourses.getPersonPk());
        Map<Integer, CourseInfoRating> ratingsMap = new HashMap<>();
        ratings.forEach(r -> ratingsMap.put(r.id_ci(), r));

        // Return it
        return veraCourses.getCourses().stream().map(v -> new CombinedCourse(v, courseMap.get(v.getId()),
            ratingsMap.getOrDefault(courseMap.get(v.getId()).id_ci(), null))).collect(Collectors.toList());
    }

    @Override
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class).key("token", LENGTH_TOKEN);
    }

    protected static class Model
    {
        String token;
    }
}
