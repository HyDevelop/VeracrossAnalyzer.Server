package org.hydev.veracross.analyzer.utils;

import com.google.gson.reflect.TypeToken;
import org.apache.http.cookie.Cookie;
import org.hydev.veracross.sdk.GeneralHttpClient;

import java.util.List;

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

    /**
     * Unwrap cookies
     *
     * @param wrap Wrapped cookies
     * @return List of cookies
     */
    public static List<Cookie> unwrap(String wrap)
    {
        String json = Base64Utils.decodeBase64CStr(wrap);
        return GSON.fromJson(json, new TypeToken<List<Cookie>>(){}.getType());
    }
}
