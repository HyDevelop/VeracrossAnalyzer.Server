package org.hydev.veracross.analyzer.database.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hydev.veracross.analyzer.database.VADatabase;
import org.hydev.veracross.analyzer.database.model.User;
import org.hydev.veracross.analyzer.utils.L$;
import org.hydev.veracross.sdk.VeracrossHttpClient;
import org.hydev.veracross.sdk.model.VeracrossStudent;

import java.io.IOException;
import java.util.List;

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
        // TODO: Use actual release version number
        new VersionUpdate(999, 66, veracross ->
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
     * The implementations of this interface represent updates from one
     * specific version to the next.
     */
    @Data
    @AllArgsConstructor
    private static class VersionUpdate
    {
        final int currentVersion; // Those are build versions
        final int lowestVersion;

        final UpdateOperator operator;
    }

    @FunctionalInterface
    private interface UpdateOperator
    {
        void run(VeracrossHttpClient veracross) throws IOException;
    }
}
