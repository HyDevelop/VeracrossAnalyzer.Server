package org.hydev.veracross.analyzer.utils;

import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.database.model.CourseInfo;
import org.hydev.veracross.sdk.model.VeraCourse;

import java.util.Calendar;

import static org.hydev.veracross.analyzer.api.VAApiServer.logger;

/**
 * TODO: Write a description for this class!
 * <p>
 * Class created by the HyDEV Team on 2020-05-25!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-05-25 20:37
 */
public class CourseUtils
{
    public static final String AP = "AP";
    public static final String Honors = "H";
    public static final String Accelerated = "A";
    public static final String CP = "CP";
    public static final String SPORT = "Sport";
    public static final String Club = "Club";

    /**
     * Save course info if not already saved
     *
     * @param veraCourse Course info
     */
    public static Course storeCourse(VeraCourse veraCourse)
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

        logger.log("[Course] Create - {}", course.name());
        return course;
    }

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
