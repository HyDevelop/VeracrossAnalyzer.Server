package org.hydev.veracross.analyzer.database.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.database.model.Course;
import org.hydev.veracross.analyzer.database.model.CourseInfo;
import org.hydev.veracross.analyzer.database.model.system.SystemMeta;
import org.hydev.veracross.analyzer.utils.L$;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hydev.veracross.analyzer.VAConstants.VERSION_BUILD;
import static org.hydev.veracross.analyzer.api.VAApiServer.logger;
import static org.hydev.veracross.analyzer.database.VADatabase.transaction;
import static org.hydev.veracross.analyzer.utils.CourseUtils.detectLevel;
import static org.hydev.veracross.analyzer.utils.CourseUtils.getSchoolYear;
import static org.hydev.veracross.analyzer.utils.L$.l$;

/**
 * This class is a utility class to upgrade the database.
 * <p>
 * Class created by the HyDEV Team on 2019-11-17!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-17 18:02
 */
public class VADatabaseUpgrade
{
    private static final L$<VersionUpdate> updates = l$
    (
        // First time initialization
        new VersionUpdate(-1, 66, veracross ->
        {
            // Create meta
            SystemMeta.setBuildVersion(VERSION_BUILD);
        }),

        // v66 to v381
        new VersionUpdate(66, 381, veracross ->
        {
            // Update: Users database - remove all existing users
            transaction(session -> session.createSQLQuery("TRUNCATE TABLE va_users;").executeUpdate());
        }),

        // v381 to v466
        new VersionUpdate(381, 466, veracross ->
        {
            // Update: Added maintenance message field
            SystemMeta.setMaintenance("");
        }),

        // v478 to latest
        new VersionUpdate(466, VERSION_BUILD, veracross ->
        {
            // Update: Add course info
            List<Course> courses = Course.getAll();
            int schoolYear = getSchoolYear();

            Map<String, CourseInfo> map = new HashMap<>();
            AtomicInteger index = new AtomicInteger();

            transaction(s ->
            {
                for (Course c : courses)
                {
                    c.level(detectLevel(c.name()));

                    // Ignore sports and nones
                    if (c.level() == null || c.level().equals("None") || c.level().equals("Sport"))
                    {
                        s.update(c);
                        continue;
                    }

                    String key = c.name() + c.teacher() + c.level();

                    if (!map.containsKey(key))
                    {
                        map.put(key, new CourseInfo(index.get(), schoolYear, c.name(), c.teacher(), c.level(), ""));
                        s.save(map.get(key));
                        index.getAndIncrement();
                    }
                    map.get(key).addCourseId(c.id());
                    c.id_ci(map.get(key).id_ci());

                    s.update(c);
                }
            });
        })
    );

    /**
     * Check for updates
     *
     * @param veracross Veracross HTTP Client
     */
    public static void checkUpdates(VeracrossHttpClient veracross)
    {
        // Get current database version from database
        int current = SystemMeta.getBuildVersion();

        // Sort
        updates.sortInt(u -> u.lowestVersion);

        // Update everything
        updates.forEach(update ->
        {
            // Has update
            if (current <= update.lowestVersion)
            {
                try
                {
                    logger.getTiming().reset();
                    logger.log("Updating from {} to {}", current, update.lowestVersion);

                    // Update
                    update.operator.run(veracross);

                    logger.log("Update success, taking {} ms", logger.getTiming().getElapsed());
                }
                catch (IOException e)
                {
                    logger.error("Error: Unable to update " + update + ", reverting...", e);

                    // Set database version to previous version
                    SystemMeta.setBuildVersion(update.lowestVersion);

                    // Throw exception
                    throw new RuntimeException("Unable to update " + update, e);
                }
            }
        });

        // Set version number
        SystemMeta.setBuildVersion(VERSION_BUILD);
    }

    /**
     * The implementations of this interface represent updates from one
     * specific version to the next.
     */
    @Data
    @AllArgsConstructor
    private static class VersionUpdate
    {
        final int lowestVersion;
        final int currentVersion; // Those are build versions

        final UpdateOperator operator;

        public String toString()
        {
            return "from " + lowestVersion + " to " + currentVersion;
        }
    }

    @FunctionalInterface
    private interface UpdateOperator
    {
        void run(VeracrossHttpClient veracross) throws IOException;
    }
}
