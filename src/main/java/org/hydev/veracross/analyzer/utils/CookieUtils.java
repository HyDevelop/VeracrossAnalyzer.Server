package org.hydev.veracross.analyzer.utils;

import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.hydev.veracross.sdk.GeneralHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private static final Type PARSABLE_COOKIES_TYPE = new TypeToken<ArrayList<BasicClientCookie>>(){}.getType();

    /**
     * Wrap cookies into base64 string.
     *
     * @param client Http client
     * @param csrf CSRF token
     * @return Wrapped cookies
     */
    public static String wrap(GeneralHttpClient client, String csrf)
    {
        return wrap(new CookieData(client.getCookies().getCookies(), csrf));
    }

    /**
     * Wrap cookies into base64 string.
     *
     * @param cookieData Cookie data
     * @return Wrapped cookies
     */
    public static String wrap(CookieData cookieData)
    {
        String json = GSON.toJson(cookieData);
        return Base64Utils.encodeBase64C(json.getBytes());
    }

    /**
     * Unwrap cookies
     *
     * @param wrap Wrapped cookies
     * @return Cookie data
     */
    public static CookieData unwrap(String wrap)
    {
        String json = Base64Utils.decodeBase64CStr(wrap);
        return GSON.fromJson(json, CookieData.class);
    }

    /**
     * Unwrap cookies and store them back to the http client.
     *
     * @param client Http client
     * @param wrap Wrapped cookies
     * @return CSRF token
     */
    public static String unwrap(GeneralHttpClient client, String wrap)
    {
        CookieData data = unwrap(wrap);
        client.restoreCookies(new ArrayList<>(data.cookies));
        return data.csrf;
    }

    /**
     * This class contains data for cookies
     */
    @AllArgsConstructor
    public static class CookieData
    {
        private List<BasicClientCookie> cookies;
        private String csrf;
    }
}
