package org.hydev.veracross.analyzer.database.update;

import lombok.AllArgsConstructor;

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
    /**
     * The implementations of this interface represent updates from one
     * specific version to the next.
     */
    @AllArgsConstructor
    public static class DatabaseVersion
    {
        String currentVersion;
        String lowestVersion;
    }
}
