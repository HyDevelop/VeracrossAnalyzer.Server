package org.hydev.veracross.analyzer.api.nodes.veracross;

import org.hydev.veracross.analyzer.api.ApiAccess;
import org.hydev.veracross.analyzer.api.JsonApiConfig;
import org.hydev.veracross.analyzer.api.JsonApiNode;
import org.hydev.veracross.analyzer.database.model.AccessLog;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.database.model.CourseInfo;
import org.hydev.veracross.analyzer.utils.CookieData;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeraCourse;
import org.hydev.veracross.sdk.model.VeraCourses;

import java.util.Calendar;

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
    protected JsonApiConfig<Model> config()
    {
        return new JsonApiConfig<>(Model.class).key("token", LENGTH_TOKEN);
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
        if (Course.get((int) course.getId()) == null)
        {
            String level = detectLevel(course.getName());
            int infoId = -1;
            if (level != null && !level.equals(SPORT) && !level.equals(Club))
            {
                // Get info
                CourseInfo info = CourseInfo.getOrCreate(getSchoolYear(), course.getName(), course.getTeacherName(), level);

                // Add course id
                if (!info.courseIds().contains("" + course.getId()))
                {
                    info.addCourseId((int) course.getId());
                }

                // Save and get info id
                infoId = info.save().id_ci();
            }
            new Course((int) course.getId(), course.getName(), course.getTeacherName(), detectLevel(course.getName()), infoId).insert();
        }
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
        name = name.trim();

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
