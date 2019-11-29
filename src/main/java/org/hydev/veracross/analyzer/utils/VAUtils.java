package org.hydev.veracross.analyzer.utils;

import org.hydev.veracross.sdk.model.VeracrossStudent;

/**
 * TODO: Write a description for this class!
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
    /**
     * Get username from the name. Format: flast21
     * 
     * @param student The student
     * @return Username
     */
    public static String getUsername(VeracrossStudent student)
    {
        return student.getFirstName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").charAt(0) +
                student.getLastName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") +
                student.getGraduationYear().toString().substring(2);
    }
}
