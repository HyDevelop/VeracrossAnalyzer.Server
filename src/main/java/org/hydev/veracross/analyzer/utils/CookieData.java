package org.hydev.veracross.analyzer.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.hydev.veracross.sdk.GeneralHttpClient;
import org.hydev.veracross.sdk.VeracrossHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hydev.veracross.analyzer.VAConstants.GSON;
import static org.hydev.veracross.analyzer.utils.J$.null$;

/**
 * This class is a utility class for cookies.
 * <p>
 * Class created by the HyDEV Team on 2019-09-07!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2019-09-07 11:27
 */
@Data
@AllArgsConstructor
public class CookieData
{
    private static final Type PARSABLE_COOKIES_TYPE = new TypeToken<ArrayList<BasicClientCookie>>(){}.getType();

    public Long id;
    public Long personPk;
    public String username;
    public List<Cookie> cookies;
    public String csrf;

    /**
     * Constructor
     *
     * @param id User ID
     * @param personPk User school pk
     * @param username Username
     * @param client Http client
     * @param csrf CSRF token
     */
    public CookieData(Long id, Long personPk, String username, GeneralHttpClient client, String csrf)
    {
        this(id, personPk, username, client.getCookies().getCookies(), csrf);
    }

    /**
     * Unwrap cookies
     *
     * @param wrap Wrapped cookies
     */
    public CookieData(String wrap)
    {
        // Decode json
        String jsonString = Base64Utils.decodeBase64C(wrap);
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        // Deserialize cookies
        id = null$(json.get("id"), JsonElement::getAsLong);
        personPk = null$(json.get("personPk"), JsonElement::getAsLong);
        username = json.get("username").getAsString();
        cookies = GSON.fromJson(json.get("cookies"), PARSABLE_COOKIES_TYPE);
        csrf = json.get("csrf").getAsString();
    }

    /**
     * Restore cookies to http client
     *
     * @param client Http client
     * @return self
     */
    public CookieData store(GeneralHttpClient client)
    {
        client.restoreCookies(cookies);
        return this;
    }

    /**
     * Wrap cookies into base64 string.
     *
     * @return Wrapped cookies
     */
    public String wrap()
    {
        return Base64Utils.encodeBase64C(GSON.toJson(this));
    }

    /**
     * Restore a veracross http client
     *
     * @param wrap Wrapped cookies
     * @return Veracross http client
     */
    public static VeracrossHttpClient restore(String wrap)
    {
        VeracrossHttpClient client = new VeracrossHttpClient();
        new CookieData(wrap).store(client);
        return client;
    }
}
