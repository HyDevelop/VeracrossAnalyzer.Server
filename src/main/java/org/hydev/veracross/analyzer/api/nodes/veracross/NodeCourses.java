package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.database.model.CourseInfo;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating;
import org.hydev.veracross.analyzer.database.model.CourseInfoRating.ReturnedRating;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraCourse;
import org.hydev.veracross.sdk.model.VeraCourses;

import java.util.*;
import java.util.stream.Collectors;

import static org.hydev.veracross.analyzer.VAConstants.LENGTH_TOKEN;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;

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
        VeraCourses veraCourses = veracross.getCourses();

        // Throw access log
        logger.log("User {} accessed courses api.", cookie.username);

        // Find courses
        List<Course> courses = Course.get(veraCourses.stream().mapToLong(VeraCourse::getId)
            .boxed().collect(Collectors.toList()));
        Map<Long, Course> courseMap = new HashMap<>();

        // Save course info async
        for (VeraCourse veraCourse : veraCourses)
        {
            // If there are no course exist for this id
            if (courses.stream().noneMatch(c -> c.id() == veraCourse.getId()))
            {
                // Put a new course
                courses.add(storeCourse(veraCourse));
            }
        }
        courses.forEach(c -> courseMap.put(c.id(), c));

        // Find ratings
        List<CourseInfoRating> ratings = CourseInfoRating.getByPersonPk(veraCourses.getPersonPk());
        Map<Integer, CourseInfoRating> ratingsMap = new HashMap<>();
        ratings.forEach(r -> ratingsMap.put(r.id_ci(), r));

        // Return it
        return veraCourses.stream().map(v -> new CombinedCourse(v, courseMap.get(v.getId()),
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

    protected static class CombinedCourse extends VeraCourse
    {
        public String level;
        public Integer id_ci;
        public ReturnedRating rating;

        public CombinedCourse(VeraCourse other, Course course, CourseInfoRating rating)
        {
            super(other);

            if (course != null)
            {
                this.level = course.level();
                this.id_ci = course.id_ci();
            }

            this.rating = rating == null ? null : new ReturnedRating(rating);
        }
    }

    /**
     * Save course info if not already saved
     *
     * @param veraCourse Course info
     */
    private static Course storeCourse(VeraCourse veraCourse)
    {
        String level = detectLevel(veraCourse.getName());
        int infoId = -1;
        if (level != null && !level.equals(SPORT) /*&& !level.equals(Club)*/)
        {
            // Get info
            CourseInfo info = CourseInfo.getOrCreate(getSchoolYear(), veraCourse.getName(),
                veraCourse.getTeacherName(), level);

            // Add course id
            if (!info.courseIds().contains("" + veraCourse.getId()))
            {
                info.addCourseId(veraCourse.getId());
            }

            // Save and get info id
            infoId = info.save().id_ci();
        }

        // Create one if it does not exist
        Course course = new Course((int) veraCourse.getId(), veraCourse.getName(),
            veraCourse.getTeacherName(), level, infoId).insert();

        logger.log("Course {} created!", course.name());
        return course;
    }

    public static final String AP = "AP";
    public static final String Honors = "H";
    public static final String Accelerated = "A";
    public static final String CP = "CP";
    public static final String SPORT = "Sport";
    public static final String Club = "Club";

    /**
     * TODO: Optimize detectLevel
     */
    public static String detectLevel(String name)
    {
        name = name.replaceAll("\\([^()]*\\)", "").trim();

        // Common ones
        if (name.startsWith("AP")) return AP;
        if (name.endsWith(" H")) return Honors;
        if (name.endsWith(" A")) return Accelerated;
        if (name.endsWith(" CP")) return CP;
        if (name.startsWith("HS ")) return Club;
        if (name.startsWith("MS ")) return Club;
        if (name.endsWith("-BS")) return SPORT;
        if (name.endsWith("-DS")) return SPORT;
        if (name.endsWith(" AS")) return SPORT;
        if (name.endsWith(" BS")) return SPORT;

        // Uncommon ones
        String lower = name.toLowerCase();

        if (name.startsWith("Pre-AP")) return AP;
        if (lower.endsWith(" acc")) return Accelerated;
        if (name.endsWith("H")) return Honors;
        if (name.endsWith("A")) return Accelerated;
        if (name.endsWith("CP")) return CP;

        // Even more uncommon
        if (lower.contains("honors")) return Honors;
        if (lower.contains("accelerated")) return Accelerated;
        if (name.contains("Advanced")) return Accelerated;
        if (name.startsWith("Varsity ")) return SPORT;
        if (name.startsWith("JV ")) return SPORT;
        if (name.startsWith("Freshman ")) return SPORT;

        // Specific
        if (name.contains("Strength")) return SPORT;
        if (name.contains("Wellness")) return SPORT;
        if (name.contains("Team")) return SPORT;
        if (name.contains("Cross Fit")) return SPORT;
        if (name.contains("Crossfit")) return SPORT;
        if (name.contains("Judo")) return SPORT;
        if (name.contains("Jiu Jitsu")) return SPORT;
        if (name.contains("Introduction to Algorithmic Thinking")) return Accelerated;
        if (name.equals("Ceramics 1")) return CP;
        if (name.equals("Cafeteria")) return "None";
        if (name.equals("Campus Ministry")) return "None";
        if (name.equals("CLAS Homeroom")) return "None";

        // Really unknown
        return null;
    }

    /**
     * Get current school year
     *
     * @return School year
     */
    public static int getSchoolYear()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        // Convert current year to current school year: +1 if it's after August
        if (cal.get(Calendar.MONTH) > Calendar.AUGUST) year ++;

        return year;
    }
}
