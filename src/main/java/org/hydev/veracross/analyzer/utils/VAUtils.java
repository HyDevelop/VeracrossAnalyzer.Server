package org.hydev.veracross.analyzer.utils;

import org.hydev.veracross.sdk.model.VeracrossStudent;

import java.util.HashMap;

/**
 * Veracross specific utilities
 * <p>
 * Class created by the HyDEV Team on 2019-11-28!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-11-28 22:24
 */
public class VAUtils
{
    private static HashMap<VeracrossStudent, String> usernameCache = new HashMap<>();

    /**
     * Get username from the name. Format: flast21
     * 
     * @param student The student
     * @return Username
     */
    public static String getUsername(VeracrossStudent student)
    {
        // Check cache
        if (usernameCache.containsKey(student)) return usernameCache.get(student);

        // Generate username
        String username = student.getFirstName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").charAt(0) +
                student.getLastName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") +
                student.getGraduationYear().toString().substring(2);

        // Cache
        usernameCache.put(student, username);

        // Return
        return username;
    }
}
