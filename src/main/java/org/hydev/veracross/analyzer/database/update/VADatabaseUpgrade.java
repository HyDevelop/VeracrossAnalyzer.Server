package org.hydev.veracross.analyzer.database.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.database.model.system.SystemMeta;
import org.hydev.veracross.analyzer.utils.L$;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeracrossStudent;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.hydev.veracross.analyzer.VAConstants.VERSION_BUILD;
import static org.hydev.veracross.analyzer.database.model.system.SystemMeta.ID_VERSION_BUILD;
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
    private static L$<VersionUpdate> updates = l$
    (
            // First time initialization
            new VersionUpdate(-1, 66, veracross ->
            {
                // Create meta
                SystemMeta.setBuildVersion(VERSION_BUILD);
            }),

            // TODO: Use actual release version number
            new VersionUpdate(66, 999, veracross ->
            {
                // Update: Users database

                // List all existing users
                List<User> users = VADatabase.query(s -> s.createQuery("from User").list());

                // Get students' information
                L$<VeracrossStudent> students = l$(veracross.getDirectoryStudents());

                // Give them individual updates
                users.forEach(user ->
                {
                    // Update: First login was actually last login
                    user.setFirstLogin(user.getLastLogin());

                    // Find student
                    VeracrossStudent student = students.find(s ->
                            s.getEmail().equalsIgnoreCase(user.getUsername() + "@stjohnsprep.org"));

                    // Update: All the other fields
                    user.setFirstName(student.getFirstName());
                    user.setLastName(student.getLastName());
                    user.setNickname(student.getFullName());
                    user.setGraduationYear(student.getGraduationYear());
                    user.setGroups("Student");
                    user.setEmails(student.getEmail());
                    user.setClasses(student.getAllClasses());
                    user.setAvatarUrl(student.getPhotoUrl());
                    user.setToken("Unassigned");

                    // Save user
                    VADatabase.saveOrUpdate(user);
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
        String currentVersionString = SystemMeta.get(ID_VERSION_BUILD);
        int currentVersion = currentVersionString == null ? -1 : parseInt(currentVersionString);

        // Sort
        updates.sortInt(u -> u.lowestVersion);

        // Update everything
        updates.forEach(update ->
        {
            // Has update
            if (currentVersion <= update.lowestVersion)
            {
                // TODO: Remove debug output
                System.out.println(update.lowestVersion);
                
                try
                {
                    // Update
                    update.operator.run(veracross);
                }
                catch (IOException e)
                {
                    // Set database version to previous version
                    SystemMeta.setBuildVersion(update.lowestVersion);

                    // Throw exception
                    throw new RuntimeException("Unable to update " + update, e);
                }
            }
        });
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
