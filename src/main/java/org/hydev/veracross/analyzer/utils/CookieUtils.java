package org.hydev.veracross.analyzer.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
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
     * Unwrap cookies
     *
     * @param wrap Wrapped cookies
     * @return Cookie data
     */
    public static CookieData unwrap(String wrap)
    {
        // Decode json
        String jsonString = Base64Utils.decodeBase64CStr(wrap);
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();

        // Deserialize cookies
        long id = json.get("id").getAsLong();
        int personPk = json.get("personPk").getAsInt();
        String username = json.get("username").getAsString();
        List<Cookie> cookies = GSON.fromJson(json.get("cookies"), PARSABLE_COOKIES_TYPE);
        String csrf = json.get("csrf").getAsString();

        // Return it
        return new CookieData(id, personPk, username, cookies, csrf);
    }

    /**
     * Unwrap cookies and store them back to the http client.
     *
     * @param client Http client
     * @param wrap Wrapped cookies
     * @return CSRF token
     */
    public static CookieData unwrap(GeneralHttpClient client, String wrap)
    {
        CookieData data = unwrap(wrap);
        client.restoreCookies(data.cookies);
        return data;
    }

    /**
     * This class contains data for cookies
     */
    @Data
    @AllArgsConstructor
    public static class CookieData
    {
        /**
         * Constructor
         *
         * @param id User ID
         * @param personPk User school pk
         * @param username Username
         * @param client Http client
         * @param csrf CSRF token
         */
        public CookieData(Long id, Integer personPk, String username, GeneralHttpClient client, String csrf)
        {
            this(id, personPk, username, client.getCookies().getCookies(), csrf);
        }

        private Long id;
        private Integer personPk;
        private String username;
        private List<Cookie> cookies;
        private String csrf;

        /**
         * Wrap cookies into base64 string.
         *
         * @param cookieData Cookie data
         * @return Wrapped cookies
         */
        public String wrap(CookieData cookieData)
        {
            String json = GSON.toJson(cookieData);
            return Base64Utils.encodeBase64C(json.getBytes());
        }
    }
}
