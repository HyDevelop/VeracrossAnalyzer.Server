package org.hydev.veracross.analyzer.api;

/**
 * An implementation of ApiNode represents one node of the http api.
 * Each node occupies a distinct url path, and serves a distinct function.
 * <p>
 * Class created by the HyDEV Team on 2019-07-04!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-07-04 18:47
 */
public interface ApiNode
{
    /**
     * Path of the api node.
     * Eg. The path for http://localhost/api/one is "/api/one"
     *
     * @return Name of the api node.
     */
    String path();

    /**
     * Process an api request.
     *
     * @param access Api access.
     * @return Result.
     */
    String process(ApiAccess access);
}
