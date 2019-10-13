package org.hydev.veracross.analyzer.api;

/**
 * This class represents errors that are meant to be shown to the users,
 * instead of logged to the console as a runtime error.
 * <p>
 * Class created by the HyDEV Team on 2019-10-13!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-10-13 12:07
 */
public class JsonKnownError extends RuntimeException
{
    public JsonKnownError(String message)
    {
        super(message);
    }
}
