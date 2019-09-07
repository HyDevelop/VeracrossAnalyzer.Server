package org.hydev.veracross.analyzer.utils;

import org.hydev.veracross.sdk.GeneralHttpClient;

import static org.hydev.veracross.analyzer.VAConstants.GSON;

/**
 * This class is an utility class for cookies.
 * <p>
 * Class created by the HyDEV Team on 2019-09-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-07 11:27
 */
public class CookieUtils
{
    /**
     * Wrap cookies into base64 string.
     *
     * @param client Http client
     * @return Wrapped cookies
     */
    public static String wrap(GeneralHttpClient client)
    {
        String json = GSON.toJson(client.getCookies().getCookies());
        return Base64Utils.encodeBase64C(json.getBytes());
    }
}
